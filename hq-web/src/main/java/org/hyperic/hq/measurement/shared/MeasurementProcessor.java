/*
 * Generated by XDoclet - Do not edit!
 */
package org.hyperic.hq.measurement.shared;

import java.util.Collection;
import java.util.List;

import org.hyperic.hq.appdef.Agent;
import org.hyperic.hq.appdef.shared.AppdefEntityID;
import org.hyperic.hq.authz.server.session.Resource;
import org.hyperic.hq.authz.shared.PermissionException;
import org.hyperic.hq.measurement.MeasurementUnscheduleException;
import org.hyperic.hq.measurement.monitor.MonitorAgentException;

/**
 * Local interface for MeasurementProcessor.
 */
public interface MeasurementProcessor {
    /**
     * Ping the agent to make sure it's up
     */
    public boolean ping(Agent a) throws PermissionException;
    
    /**
     * Schedules enabled measurements for the entire ResourceEdge hierarchy
     * based on the "containment" relationship.  These metrics are scheduled
     * after the transaction is committed.
     * 
     */
    public void scheduleHierarchyAfterCommit(Collection<Resource> resources);
    
    /**
     * Schedules enabled measurements for the entire ResourceEdge hierarchy
     * based on the "containment" relationship.  These metrics are scheduled
     * after the transaction is committed.
     * 
     */
    public void scheduleHierarchyAfterCommit(Resource resource);

    public void scheduleSynchronous(List<AppdefEntityID> aeids);

    public void scheduleEnabled(Agent agent, List<AppdefEntityID> eids) throws MonitorAgentException;

    /**
     * Unschedule metrics of multiple appdef entities
     * @param agentToken the entity whose agent will be contacted for the
     *        unschedule
     * @param entIds the entity IDs whose metrics should be unscheduled
     * @throws MeasurementUnscheduleException if an error occurs
     */
    public void unschedule(String agentToken, AppdefEntityID[] entIds) throws MeasurementUnscheduleException;

    /**
     * Unschedule metrics of multiple appdef entities
     * @param agentEnt the entity whose agent will be contacted for the
     *        unschedule
     * @param entIds the entity IDs whose metrics should be unscheduled
     * @throws MeasurementUnscheduleException if an error occurs
     */
    public void unschedule(AppdefEntityID agentEnt, AppdefEntityID[] entIds) throws MeasurementUnscheduleException;

    /**
     * Unschedule measurements
     * @param aeids List of {@link AppdefEntityID}
     * @throws MeasurementUnscheduleException if an error occurs
     */
    public void unschedule(List<AppdefEntityID> aeids) throws MeasurementUnscheduleException;

}
