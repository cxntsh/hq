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

package org.hyperic.hq.events.server.session;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;

import org.hyperic.hq.events.shared.EventTrackerLocal;
import org.hyperic.hq.events.shared.EventTrackerLocalHome;

/**
 * A mock instance of the local home interface for the event tracker EJB.
 */
public class MockEventTrackerLocalHome implements EventTrackerLocalHome {

    private EventTrackerLocal _eventTracker;
    
    public MockEventTrackerLocalHome(EventTrackerLocal eventTracker) {
        setEventTracker(eventTracker);
    }
    
    public void setEventTracker(EventTrackerLocal eventTracker) {
        _eventTracker = eventTracker;
    }
     
    /**
     * @see org.hyperic.hq.events.shared.EventTrackerLocalHome#create()
     */
    public EventTrackerLocal create() throws CreateException {
        return _eventTracker;
    }

    /**
     * @see javax.ejb.EJBLocalHome#remove(java.lang.Object)
     */
    public void remove(Object arg0) throws RemoveException, EJBException {
    }

}
