/*
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 * 
 * Copyright (C) [2004-2008], Hyperic, Inc.
 * This file is part of HQ.
 * 
 * HQ is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package org.hyperic.hq.plugin.oracle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.hyperic.hq.product.AutoServerDetector;
import org.hyperic.hq.product.PluginException;
import org.hyperic.hq.product.FileServerDetector;
import org.hyperic.hq.product.ProductPlugin;
import org.hyperic.hq.product.ServerDetector;
import org.hyperic.hq.product.ServerResource;
import org.hyperic.hq.product.ServiceResource;

import org.hyperic.util.config.ConfigResponse;
import org.hyperic.util.jdbc.DBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OracleServerDetector 
    extends ServerDetector
    implements FileServerDetector,
               AutoServerDetector {

    private transient Log log =  LogFactory.getLog("OracleServerDetector");

    private static final String PTQL_QUERY = "State.Name.eq=oracle";

    private static final String PROP_PROC_PTQL = "process.ptql";
    
    private static final String PROP_TNSNAMES = "tnsnames";

    private static final String ORATAB = "/etc/oratab";

    private static final Pattern _serviceNameEx =
        Pattern.compile("\\(\\s*service_name\\s*=", Pattern.CASE_INSENSITIVE);

    // Versions
    static final String VERSION_8i = "8i";
    static final String VERSION_9i = "9i";
    static final String VERSION_10g = "10g";
    static final String VERSION_11g = "11g";

    // User instance
    static final String USER_INSTANCE = "User Instance";
    static final String USER_QUERY =
        "SELECT UNIQUE username FROM V$SESSION WHERE username IS NOT NULL";
    static final String DBA_USER_QUERY =
        "SELECT * FROM DBA_USERS WHERE USERNAME = ";

    // Tablespace
    static final String TABLESPACE = "Tablespace";
    static final String TABLESPACE_QUERY = "SELECT * FROM DBA_TABLESPACES";

    // Table
    static final String SEGMENT = "Segment";
    static final String SEGMENT_QUERY = "select SEGMENT_NAME, TABLESPACE_NAME" +
                                        " FROM USER_SEGMENTS" +
                                        " WHERE SEGMENT_NAME not like 'BIN$%'" +
                                        " and SEGMENT_NAME not like 'SYS_%'";

    // Server custom props
    static final String VERSION_QUERY = 
        "SELECT * FROM V$VERSION";

    /**
     * Utility function to query the process table for Oracle
     */
    private List getServerProcessList() {
        ArrayList servers = new ArrayList();

        long[] pids = getPids(PTQL_QUERY);

        for (int i=0; i<pids.length; i++) {
            String exe = getProcExe(pids[i]);

            if (exe == null) {
                continue;
            }

            File binary = new File(exe.toLowerCase());

            if (!binary.isAbsolute()) {
                continue;
            }

            if (!servers.contains(binary.getAbsolutePath()))
                servers.add(binary.getAbsolutePath());
        }

        return servers;
    }

    private boolean hasExe(File bin, String name) {
        if (isWin32()) {
            return
                new File(bin, name + ".exe").exists() ||
                new File(bin, name + ".bat").exists();
        }
        else {
            return
                new File(bin, name).exists();
        }
    }

    //e.g. file = "orasql10.", matches: "orasql10.dll", "liborasql10.so", etc.
    private boolean hasFile(File dir, final String file) {
        String[] names = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().indexOf(file) != -1;
            }
        });
        if (names == null || names.length == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    //for use w/ -jar hq-pdk-shared.jar or agent.properties
    private boolean configureProperties(ConfigResponse config) {
        boolean hasCreds = false;
        String[] keys =
            getConfigSchema(getTypeInfo(), config).getOptionNames();

        for (int i=0; i<keys.length; i++) {
            String key = keys[i];
            String val = getManager().getProperty(key);
            if (val != null) {
                config.setValue(key, val);
                if (key.startsWith("jdbc")) {
                    hasCreds = true;
                }
            }
        }
        return hasCreds;
    }

    /**
     * Utility method to determine oracle version by file layout
     */
    private List getServerList (String path) 
        throws PluginException
    {
        List servers = new ArrayList();
        String version = getTypeInfo().getVersion();
        boolean found = false;

        File oracle = new File(path);
        File bin = oracle.getParentFile();
        if (!isWin32()) {
            bin = new File(oracle, "bin");
        }
        if (bin.getName().equals("bin")) {
            path = bin.getParent();
        }
        // in HHQ-3577, changed the default installpath.  Therefore we need to
        // make sure that a new oracle inst is not detected, rather it should
        // just modify the existing one.
        String aiid = oracle.getParent();

        // Make sure that oracle exists, and is a normal file
        if (oracle.exists() && bin.isDirectory()) {
            if (hasExe(bin, "adrci")) {
                found = version.equals(VERSION_11g);
            }
            else if (hasExe(bin, "trcsess") || hasFile(bin, "orasql10.")) {
                found = version.equals(VERSION_10g);
            }
            else if (hasExe(bin, "dgmgrl")) {
                found = version.equals(VERSION_9i);
            }
            else {
                //assume Oracle 8i
                found = version.equals(VERSION_8i);
            }
        }

        if (found) {
            ConfigResponse productConfig = new ConfigResponse();
            ServerResource server = createServerResource(path);
            server.setIdentifier(aiid);

            // Set custom properties
            ConfigResponse cprop = new ConfigResponse();
            cprop.setValue("version", version);
            server.setCustomProperties(cprop);
            setProductConfig(server, productConfig);
            if (configureProperties(productConfig)) {
                server.setMeasurementConfig();
            }
            servers.add(server);
        }
        
        // HHQ-3577 allow listener names in tnsnames.ora to be used in the url
        String fs = File.separator;
        String tnsDir = getTnsNamesDir(path, "network" + fs + "admin" + fs + "tnsnames.ora");
        if (log.isDebugEnabled()) log.debug("using tns dir as " + tnsDir);
        System.setProperty("oracle.net.tns_admin", tnsDir);

        return servers;
    }

    // Auto-scan.. Does process scan first, falls back to oratab
    public List getServerResources(ConfigResponse platformConfig)
        throws PluginException
    {
        List servers = new ArrayList();

        // First do process table scan
        List paths = getServerProcessList();
        for (int i = 0; i < paths.size(); i++) {
            String dir = (String)paths.get(i);
            List found = getServerList(dir);
            if (!found.isEmpty()) {
                servers.addAll(found);
            }
        }

        // If nothing found, try parsing /etc/oratab
        if (servers.size() == 0 && !isWin32())
        {
            try
            {
                String line;
                BufferedReader in = new BufferedReader(new FileReader(ORATAB));
                while ((line = in.readLine()) != null)
                {
                    // Check for empty or commented out lines
                    if (line.length() == 0 || line.startsWith("#")) {
                        continue;
                    }
                    // Ensure format
                    int x1, x2;
                    x1 = line.indexOf(':');
                    x2 = line.indexOf(':', x1+1);
                    if (x1 != -1 && x2 != -1) {
                        String oraHome = line.substring(x1+1, x2);
                        log.debug("Found ORACLE_HOME=" + oraHome);
                        servers.addAll(getServerList(oraHome));
                    }
                }
            }
            catch (FileNotFoundException e) {
                //Ok, no oracle installed.
            }
            catch (IOException e) {
                log.error("Error parsing oratab: " + e);
            }
        }

        return servers;
    }

    // File scan
    public List getServerResources (ConfigResponse platformConfig, String path)
        throws PluginException
    {
        return getServerList(path);
    }

    // Discover Oracle services
    protected List discoverServices(ConfigResponse config)
        throws PluginException
    {
        // HHQ-3577 allow listener names in tnsnames.ora to be used in the url
        String tnsDir = getTnsNamesDir(
            config.getValue(ProductPlugin.PROP_INSTALLPATH),
            config.getValue(PROP_TNSNAMES));
        if (log.isDebugEnabled()) log.debug("using tns dir as " + tnsDir);
        System.setProperty("oracle.net.tns_admin", tnsDir);
        
        String url = config.getValue(OracleMeasurementPlugin.PROP_URL);
        if (url == null) {
        	log.warn("No value for config property " + OracleMeasurementPlugin.PROP_URL +
        			", no services will be discovered.");
        	return null;
        }

        String user = config.getValue(OracleMeasurementPlugin.PROP_USER);
        if (user == null) {
        	log.info("No value for config property " + OracleMeasurementPlugin.PROP_USER);
        }

        String pass = config.getValue(OracleMeasurementPlugin.PROP_PASSWORD);
        if (pass == null) {
        	log.info("No value for config property " + OracleMeasurementPlugin.PROP_PASSWORD);
        }

        ArrayList services = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        try
        {
            String instance = url.substring(url.lastIndexOf(':') + 1);
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            services.addAll(getUserServices(stmt, instance));
            services.addAll(getTablespaceServices(stmt, instance));
            // turning this off by default.
            // There are too many table that this will discover
            // most of which the user probably won't care about.
            // Also work needs to be done by the user to enable
            // scheduled control actions to do an Anaylze per table
            // so that the system info gets updated before the
            // size is calc'd.
            //services.addAll(getSegmentServices(stmt, instance));
            services.addAll(getProcessServices(config));
            // this requires extra config on the user side to set ORACLE_HOME
            // env var, so to avoid confusion disabling
            //services.addAll(getTnsServices(config));
            setCustomProps(stmt);
        }
        catch (SQLException e)
        {
            // Try to do some investigation of what went wrong
            if (e.getMessage().indexOf("table or view does not exist") != -1)
            {
                log.error("System table does not exist, make sure that " +
                          " the Oracle user specified has the correct " +
                          " privileges.  See the HQ server configuration " +
                          " page for more information");
                return services;
            }
            // Otherwise, dump the error.
            throw new PluginException("Error querying for Oracle " +
                                      "services: " + e.getMessage(), e);
        }
        finally {
            DBUtil.closeJDBCObjects(log, conn, stmt, null);
        }
        return services;
    }

    private void setCustomProps(Statement stmt)
        throws SQLException
    {
        ResultSet rs = null;
        try
        {
            // Query for server inventory properties
            ConfigResponse props = new ConfigResponse();
            rs = stmt.executeQuery(VERSION_QUERY);
            if (rs != null && rs.next()) {
                String version = rs.getString(1);
                props.setValue("version", version);
            }
            setCustomProperties(props);
        }
        finally {
            DBUtil.closeResultSet(log, rs);
        }
    }

    private List getUserServices(Statement stmt, String instance)
        throws SQLException
    {
        // Discover the user instances, for user instances to be
        // discovered, the user must be connected to the database.
        List rtn = new ArrayList();
        ResultSet rs = null;
        try
        {
            // Set server description
            setDescription("Oracle " + instance + " database instance");

            // Discover user instances
            ArrayList users = new ArrayList();
            rs = stmt.executeQuery(USER_QUERY);
            while (rs != null && rs.next()) {
                String username = rs.getString(1);
                users.add(username);
            }
            rs.close();
            for (int i=0; i<users.size(); i++)
            {
                String username = (String)users.get(i);
                ServiceResource service = new ServiceResource();
                service.setType(this, USER_INSTANCE);
                service.setServiceName(username);
                service.setDescription("User of the " + instance + 
                                       " database instance");
                ConfigResponse productConfig = new ConfigResponse();
                ConfigResponse metricConfig = new ConfigResponse();
                productConfig.setValue(OracleMeasurementPlugin.PROP_USERNAME,
                                       username);
                service.setProductConfig(productConfig);
                service.setMeasurementConfig(metricConfig);
                // Query for service inventory properties
                rs = stmt.executeQuery(DBA_USER_QUERY + "'" + username + "'");
                if (rs != null && rs.next())
                {
                    ConfigResponse svcProps = new ConfigResponse();
                    svcProps.setValue("status",
                                      rs.getString("ACCOUNT_STATUS"));
                    svcProps.setValue("default_tablespace",
                                      rs.getString("DEFAULT_TABLESPACE"));
                    svcProps.setValue("temp_tablespace",
                                      rs.getString("TEMPORARY_TABLESPACE"));
                    service.setCustomProperties(svcProps);
                }
                rtn.add(service);
            }
        }
        finally {
            DBUtil.closeResultSet(log, rs);
        }
        return rtn;
    }

    private List getSegmentServices(Statement stmt, String instance)
        throws SQLException
    {
        List rtn = new ArrayList();
        ResultSet rs = null;
        try
        {
            // Discover tables
            rs = stmt.executeQuery(SEGMENT_QUERY);
            int segment_col = rs.findColumn("SEGMENT_NAME");
            int ts_col = rs.findColumn("TABLESPACE_NAME");
            while (rs != null && rs.next())
            {
                String segment = rs.getString(segment_col);
                String tablespace = rs.getString(ts_col);
                ServiceResource service = new ServiceResource();
                service.setType(this, SEGMENT);
                service.setServiceName(segment);
                service.setDescription("Segment in the " + instance +
                                       " database instance");
                ConfigResponse productConfig = new ConfigResponse();
                ConfigResponse metricConfig = new ConfigResponse();
                productConfig.setValue(OracleMeasurementPlugin.PROP_SEGMENT,
                                       segment);
                productConfig.setValue(OracleMeasurementPlugin.PROP_TABLESPACE,
                                       tablespace);
                service.setProductConfig(productConfig);
                service.setMeasurementConfig(metricConfig);
                service.setControlConfig();
                rtn.add(service);
            }
        }
        finally {
            DBUtil.closeResultSet(log, rs);
        }
        return rtn;
    }

    private List getTablespaceServices(Statement stmt, String instance)
        throws SQLException
    {
        List rtn = new ArrayList();
        ResultSet rs = null;
        try
        {
            // Discover tablespaces
            rs = stmt.executeQuery(TABLESPACE_QUERY);
            int ts_col = rs.findColumn("TABLESPACE_NAME");
            while (rs != null && rs.next())
            {
                String tablespace = rs.getString(ts_col);
                ServiceResource service = new ServiceResource();
                service.setType(this, TABLESPACE);
                service.setServiceName(tablespace);
                service.setDescription("Tablespace on the " + instance +
                                       " database instance");
                ConfigResponse productConfig = new ConfigResponse();
                ConfigResponse metricConfig = new ConfigResponse();
                productConfig.setValue(OracleMeasurementPlugin.PROP_TABLESPACE,
                                       tablespace);
                service.setProductConfig(productConfig);
                service.setMeasurementConfig(metricConfig);
                ConfigResponse svcProps = new ConfigResponse();
                // 9i and 10g only
                if (!getTypeInfo().getVersion().equals(VERSION_8i)) {
                    svcProps.setValue("block_size",
                                      rs.getString("BLOCK_SIZE"));
                    svcProps.setValue("allocation_type",
                                      rs.getString("ALLOCATION_TYPE"));
                    svcProps.setValue("space_management",
                                      rs.getString("SEGMENT_SPACE_MANAGEMENT"));
                }
                svcProps.setValue("contents",
                                  rs.getString("CONTENTS"));
                svcProps.setValue("logging",
                                  rs.getString("LOGGING"));
                service.setCustomProperties(svcProps);
                rtn.add(service);
            }
        }
        finally {
            DBUtil.closeResultSet(log, rs);
        }
        return rtn;
    }

    private List getProcessServices(ConfigResponse config)
    {
        List rtn = new ArrayList();
        String ptql = config.getValue(PROP_PROC_PTQL);
        if (log.isDebugEnabled())
            log.debug("using ptql, "+ptql+", to retrieve processes");
        List processes = getProcesses(ptql);
        for (Iterator i=processes.iterator(); i.hasNext(); )
        {
            String process = (String)i.next();
            if (log.isDebugEnabled())
                log.debug("adding Process Metrics "+process+" service");
            ServiceResource service = new ServiceResource();
            service.setType(this, "Process Metrics");
            service.setServiceName(process+" process");
            ConfigResponse productConfig = new ConfigResponse();
            ptql = "State.Name.eq=oracle,Args.0.sw="+process;
            productConfig.setValue("process.query", ptql);
            service.setProductConfig(productConfig);
            service.setMeasurementConfig();
            rtn.add(service);
        }
        return rtn;
    }

    private List getProcesses(String ptql)
    {
        long[] pids = getPids(ptql);

        List rtn = new ArrayList();
        for (int i=0; i<pids.length; i++)
        {
            String[] args = getProcArgs(pids[i]);
            if (args.length == 0 || args[0] == null) {
                continue;
            }
            rtn.add(args[0]);
        }
        return rtn;
    }
    
    private String getTnsNamesDir(String installpath, String tnsnames) {
       if (installpath == null || tnsnames == null) {
           return "";
        }
        String fs = File.separator;
        if (fs.equals("\\")) {
            fs = fs + fs;
        }
        String[] toks = tnsnames.split(fs);
        StringBuilder rtn = new StringBuilder();
        for (int i=0; i<toks.length-1; i++) {
            rtn.append(toks[i]).append(fs);
        }
        return installpath + fs + rtn.toString();
    }

    private List getTnsServices(ConfigResponse config)
    {
        String line;
        BufferedReader reader = null;
        String tnsnames = config.getValue(PROP_TNSNAMES),
               installpath = config.getValue(ProductPlugin.PROP_INSTALLPATH);
        List rtn = new ArrayList();
        try
        {
            String fs = File.separator;
            if (log.isDebugEnabled()) {
                log.debug("READING tnsnames.ora FILE: "+installpath+fs+tnsnames);
            }
            reader = new BufferedReader(new FileReader(installpath+fs+tnsnames));
            while (null != (line = reader.readLine()))
            {
                if (_serviceNameEx.matcher(line).find())
                {
                    String[] toks = line.split("=");
                    if (toks[1] == null)
                        continue;
                    String tnslistener =
                        toks[1].replaceAll("\\s*\\)", "").trim();
                    if (log.isDebugEnabled())
                        log.debug("Configuring TNS Listener "+tnslistener);
                    ServiceResource service = new ServiceResource();
                    service.setType(this, "TNS Ping");
                    service.setServiceName(tnslistener+" TNS Ping");
                    ConfigResponse productConfig = new ConfigResponse();
                    productConfig.setValue("tnslistener", tnslistener);
                    service.setProductConfig(productConfig);
                    service.setMeasurementConfig();
                    rtn.add(service);
                }
            }
        }
        catch (IOException e) {
            log.error("Error reading "+tnsnames);
        }
        finally {
            close(reader);
        }
        return rtn;
    }

    private void close(Reader reader)
    {
        if (reader == null)
            return;
        try {
            reader.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}