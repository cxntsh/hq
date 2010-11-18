package org.hyperic.hq.plugin.domain;

import java.util.Set;

import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hyperic.hq.inventory.domain.Resource;
import org.hyperic.hq.reference.RelationshipTypes;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser;
import org.springframework.datastore.annotation.Indexed;
import org.springframework.datastore.graph.annotation.GraphProperty;
import org.springframework.datastore.graph.annotation.NodeEntity;
import org.springframework.datastore.graph.annotation.RelatedTo;
import org.springframework.datastore.graph.api.Direction;
import org.springframework.datastore.graph.neo4j.fieldaccess.PartialNodeEntityStateAccessors;
import org.springframework.datastore.graph.neo4j.finder.FinderFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@NodeEntity(partial=true)
@RooToString
@RooJavaBean
@RooEntity
public class ResourceType {
	
    @NotNull
    @Indexed
    @GraphProperty
    @Transient
    private String name;
    
    //TODO was using IS_A to relate ResourceTypes to ResourceTypes also, but somehow those related ResourceTypes were ending up in this Set
    //causing ClassCastExceptions.  Switched to using EXTENDS for ResourceTypes, but still strange issue similar to Resource.findRelationships
    @RelatedTo(type = RelationshipTypes.IS_A, direction = Direction.INCOMING, elementClass = org.hyperic.hq.inventory.domain.Resource.class)
    @OneToMany
    @Transient
    private Set<Resource> resources;
    
    @RelatedTo(type = RelationshipTypes.CONTAINS, direction = Direction.OUTGOING, elementClass = PropertyType.class)
    @OneToMany
    @Transient
    private Set<PropertyType> propertyTypes;
    
    @javax.annotation.Resource
    private transient FinderFactory finderFactory2;

	public ResourceTypeRelation relateTo(ResourceType resourceType, String relationName) {
        return (ResourceTypeRelation) this.relateTo(resourceType, ResourceTypeRelation.class,
            relationName);
    }

    public boolean isRelatedTo(ResourceType resourceType, String relationName) {
        Traverser relationTraverser = getUnderlyingState().traverse(Traverser.Order.BREADTH_FIRST,
            new StopEvaluator() {

                @Override
                public boolean isStopNode(TraversalPosition currentPos) {
                    return currentPos.depth() >= 1;
                }
            }, ReturnableEvaluator.ALL_BUT_START_NODE,
            DynamicRelationshipType.withName(relationName), org.neo4j.graphdb.Direction.OUTGOING);
        for (Node related : relationTraverser) {
        	long nodeId = related.getId();
        	//TODO Is this still needed?
        	//if(related.hasProperty(PartialNodeEntityStateAccessors.FOREIGN_ID)) {
        		//nodeId = (Long)related.getProperty(PartialNodeEntityStateAccessors.FOREIGN_ID);
        	//}
            if (nodeId == resourceType.getId()) {
                return true;
            }
        }
        return false;
    }

    public static ResourceType findResourceTypeByName(String name) {
    	//Can't do JPA-style queries on property values that are only in graph
       ResourceType type = new ResourceType().finderFactory2.getFinderForClass(ResourceType.class)
            .findByPropertyValue("name", name);
       if(type != null) {
    	   type.getId();
       }
       return type;
    }
    
    
    
    public PropertyType getPropertyType(String name) {
        for(PropertyType propertyType: propertyTypes) {
            if(name.equals(propertyType.getName())) {
                return propertyType;
            }
        }
        return null;
    }


	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
        getId();
    }
}