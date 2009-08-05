package org.hyperic.hq.events.server.session;

import java.util.List;

import org.hyperic.hq.events.shared.RegisteredTriggerValue;
/**
 * DAO for interacting with {@link RegisteredTrigger}s
 * @author jhickey
 *
 */
public interface TriggerDAOInterface {

    RegisteredTrigger create(RegisteredTriggerValue createInfo);

    void deleteAlertDefinition(AlertDefinition def);

    List findAll();

    List findByAlertDefinitionId(Integer id);

    RegisteredTrigger findById(Integer id);

    RegisteredTrigger get(Integer id);

    void removeTriggers(AlertDefinition def);

}
