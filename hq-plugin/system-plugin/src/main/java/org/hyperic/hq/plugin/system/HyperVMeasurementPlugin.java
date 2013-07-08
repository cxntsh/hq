package org.hyperic.hq.plugin.system;


import org.hyperic.hq.product.Metric;
import org.hyperic.hq.product.MetricNotFoundException;
import org.hyperic.hq.product.MetricUnreachableException;
import org.hyperic.hq.product.MetricValue;
import org.hyperic.hq.product.PluginException;
import org.hyperic.hq.product.Win32MeasurementPlugin;
import org.hyperic.sigar.win32.Pdh;
import org.hyperic.sigar.win32.Win32Exception;

public class HyperVMeasurementPlugin extends Win32MeasurementPlugin {   
    
    @Override
    public MetricValue getValue(Metric metric) throws PluginException, MetricNotFoundException, MetricUnreachableException {

        if ( ("pdh".equals(metric.getDomainName())) || ("pdh_formatted".equals(metric.getDomainName())) ) {
            String obj = "\\" + metric.getObjectPropString() + "\\" + metric.getAttributeName();
            Double val;
            try 
            {          
                obj = obj.replaceAll("%3A", ":").toLowerCase();
                if ( ("pdh".equals(metric.getDomainName()))) {
                    val = new Pdh().getRawValue(obj);
                }
                else {
                    val = new Pdh().getFormattedValue(obj);
                }
                return new MetricValue(val);
            }catch(Win32Exception e) {
                throw new PluginException(e);
            }
        }
        else {
            return super.getValue(metric);
        }
    }
}
