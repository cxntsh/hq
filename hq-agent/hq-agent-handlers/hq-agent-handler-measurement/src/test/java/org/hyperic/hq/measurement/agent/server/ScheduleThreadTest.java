/**
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 *  "derived work".
 *
 *  Copyright (C) [2010], VMware, Inc.
 *  This file is part of HQ.
 *
 *  HQ is free software; you can redistribute it and/or modify
 *  it under the terms version 2 of the GNU General Public License as
 *  published by the Free Software Foundation. This program is distributed
 *  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *
 */

package org.hyperic.hq.measurement.agent.server;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.hyperic.hq.appdef.shared.AppdefEntityID;
import org.hyperic.hq.measurement.MeasurementConstants;
import org.hyperic.hq.measurement.agent.ScheduledMeasurement;
import org.hyperic.hq.product.MeasurementValueGetter;
import org.hyperic.hq.product.Metric;
import org.hyperic.hq.product.MetricNotFoundException;
import org.hyperic.hq.product.MetricUnreachableException;
import org.hyperic.hq.product.MetricValue;
import org.hyperic.hq.product.PluginException;

public class ScheduleThreadTest extends TestCase {

    private static final String DSN_PLATFORM_LOAD  = "sigar:Type=LoadAverage:1";
    private static final String DSN_PLATFORM_AVAIL = "system.avail:Type=Platform:Availability";

    private static boolean loggingSetup = false;
    protected void setUp() throws Exception {
        if (!loggingSetup) {
            // Uncomment me for full logging.
            // BasicConfigurator.configure();
            loggingSetup = true;
        }
    }

    private int derivedId = 0;
    private int dsnId = 0;
    private int entityId = 0;
    private ScheduledMeasurement createMeasurement(String dsn, long interval) {
        return new ScheduledMeasurement(dsn, interval, ++derivedId, ++dsnId,
                                        new AppdefEntityID(1, ++entityId),
                                        MeasurementConstants.CAT_PERFORMANCE);

    }

    public void testSimpleStartKill() throws Exception {

        ScheduleThread st = new ScheduleThread(new SimpleSender(), new SimpleValueGetter());
        Thread t = new Thread(st);
        t.start();

        // No measurement schedule, the thing should just sit there
        assertTrue(t.isAlive());
        assertFalse(t.isInterrupted());
        long startWait = System.currentTimeMillis();
        long waitTime = 100;
        try {
            t.join(waitTime);
        } catch (InterruptedException ie) {
            fail("Thread should not have been interrupted");
        }

        assertTrue(System.currentTimeMillis() - startWait >= waitTime);
        assertEquals("Wrong number of scheduled measurements",
                     0.0, st.getNumMetricsScheduled());

        st.die();
        try {
            t.join();
        } catch (InterruptedException ie) {
            fail("Thread should not be interrupted");
        }
    }

    public void testNullCollection() throws Exception {

        ScheduleThread st = new ScheduleThread(new SimpleSender(), new NullValueGetter());

        ScheduledMeasurement m = createMeasurement(DSN_PLATFORM_LOAD, 100);
        st.scheduleMeasurement(m);

        Thread t = new Thread(st);
        t.start();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Verify against ScheduleThread statistics
        assertEquals("Wrong number of scheduled measurements",
                     1.0, st.getNumMetricsScheduled());
        assertEquals("Wrong number of failed collections",
                     0.0, st.getNumMetricsFailed());
        assertEquals("Wrong number of metric collections",
                     0.0, st.getNumMetricsFetched());
        assertTrue("Max fetch time is zero", st.getMaxFetchTime() > 0);
        assertTrue("Min fetch time is zero", st.getMinFetchTime() > 0);
        assertTrue("Tot fetch time is zero", st.getTotFetchTime() > 0);

        st.die();
        try {
            t.join();
        } catch (InterruptedException ie) {
            fail("Thread should not be interrupted");
        }
    }

    public void testSimpleCollection() throws Exception {

        ScheduleThread st = new ScheduleThread(new SimpleSender(), new SimpleValueGetter());

        ScheduledMeasurement m = createMeasurement(DSN_PLATFORM_LOAD, 100);
        st.scheduleMeasurement(m);

        Thread t = new Thread(st);
        t.start();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Verify against ScheduleThread statistics
        assertEquals("Wrong number of scheduled measurements",
                     1.0, st.getNumMetricsScheduled());

        // TODO: explain this better or fix ScheduleThread
        // Some funny math here explained:
        // 1st collection @ 0
        // 2nd collection @ 0-100 (Scheduler voodoo's up to next 100)
        // 3rd collection @ 100-200
        // 4th collection @ 200-300
        // 5th collection @ 300-400
        assertEquals("Wrong number of metric collections",
                     5.0, st.getNumMetricsFetched());
        assertTrue("Max fetch time is zero", st.getMaxFetchTime() > 0);
        assertTrue("Min fetch time is zero", st.getMinFetchTime() > 0);
        assertTrue("Tot fetch time is zero", st.getTotFetchTime() > 0);

        st.die();
        try {
            t.join();
        } catch (InterruptedException ie) {
            fail("Thread should not be interrupted");
        }
    }

    public void testCollectionMultiDomain() throws Exception {

        ScheduleThread st = new ScheduleThread(new SimpleSender(), new SimpleValueGetter());

        st.scheduleMeasurement(createMeasurement(DSN_PLATFORM_LOAD, 100));
        st.scheduleMeasurement(createMeasurement(DSN_PLATFORM_AVAIL, 100));

        Thread t = new Thread(st);
        t.start();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Verify against ScheduleThread statistics
        assertEquals("Wrong number of scheduled measurements",
                     2.0, st.getNumMetricsScheduled());
        assertEquals("Wrong number of metric collections",
                     10.0, st.getNumMetricsFetched());
        assertTrue("Max fetch time is zero", st.getMaxFetchTime() > 0);
        assertTrue("Min fetch time is zero", st.getMinFetchTime() > 0);
        assertTrue("Tot fetch time is zero", st.getTotFetchTime() > 0);

        st.die();
        try {
            t.join();
        } catch (InterruptedException ie) {
            fail("Thread should not be interrupted");
        }
    }

    public static class SimpleSender implements org.hyperic.hq.measurement.agent.server.Sender {

        public void processData(int dsnId, MetricValue data, int derivedID) {
        }
    }

    public static class SimpleValueGetter implements MeasurementValueGetter {

        public MetricValue getValue(String name, Metric metric)
                throws PluginException, MetricNotFoundException, MetricUnreachableException {

            // introduce some latency to simulate contacting the managed resource
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                // Ignore
            }
            return new MetricValue(42);
        }
    }

    public static class NullValueGetter implements MeasurementValueGetter {

        public MetricValue getValue(String name, Metric metric)
                throws PluginException, MetricNotFoundException, MetricUnreachableException {

            // introduce some latency to simulate contacting the managed resource
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                // Ignore
            }
            return null; // Should not be allowed
        }
    }
}