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

package org.hyperic.hq.measurement.server.session;

import org.hyperic.hq.zevents.Zevent;
import org.hyperic.hq.zevents.ZeventManager;
import org.hyperic.hq.zevents.ZeventPayload;
import org.hyperic.hq.zevents.ZeventSourceId;

public class MeasurementScheduleZevent extends Zevent {

    static {
        ZeventManager.getInstance()
            .registerEventClass(MeasurementScheduleZevent.class);
    }

    public static class MeasurementScheduleZeventSource
        implements ZeventSourceId
    {
        private static final long serialVersionUID = 4112168034565196596L;
        
        private final int _id;

        public MeasurementScheduleZeventSource(int id) {
            _id = id;
        }

        /**
         * Returns the id of the {@link Measurement} that the event is
         * for.
         */
        public int getId() {
            return _id;
        }

        public int hashCode() {
            int result = 17;
            result = 37*result+_id;
            result = 37*result+this.getClass().toString().hashCode();
            return result;
        }

        public boolean equals(Object o) {
            if (o== this)
                return true;

            if (o == null || !(o instanceof MeasurementScheduleZeventSource))
                return false;

            return ((MeasurementScheduleZeventSource)o).getId() == getId();
        }

        public String toString() {
            return "MeasID[" + _id + "]";
        }
    }

    public static class MeasurementScheduleZeventPayload
        implements ZeventPayload
    {
        private long _interval;

        public MeasurementScheduleZeventPayload(long interval) {
            _interval = interval;
        }

        /**
         * @return The measurement collection interval; <code>0</code> if 
         *         collection has been disabled.
         */
        public long getInterval() {
            return _interval;
        }
    }

    public MeasurementScheduleZevent(int measId, long interval) {
        super(new MeasurementScheduleZeventSource(measId),
              new MeasurementScheduleZeventPayload(interval));
    }

}
