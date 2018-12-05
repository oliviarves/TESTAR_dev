package nl.ou.testar.StateModel.Persistence.OrientDB.Entity;

import com.orientechnologies.orient.core.metadata.schema.OType;

import java.util.HashMap;
import java.util.Map;


public class EntityClassFactory {

    public enum EntityClassName {AbstractState, AbstractAction, AbstractStateModel}

    // a repo for generated classes, so we don't execute the same generation code over and over if not needed
    private static Map<EntityClassName, EntityClass> entityClasses = new HashMap<>();

    /**
     * This method generates an EntityClass.
     * @param className
     * @return
     */
    public static EntityClass createEntityClass(EntityClassName className) {
        //@todo this class will need refactoring as the number of vertex types grows
        switch (className) {
            case AbstractState:
                return entityClasses.containsKey(EntityClassName.AbstractState) ? entityClasses.get(EntityClassName.AbstractState)
                            : createAbstractStateClass();

            case AbstractAction:
                return entityClasses.containsKey(EntityClassName.AbstractAction) ? entityClasses.get(EntityClassName.AbstractAction)
                            : createAbstractActionClass();

            case AbstractStateModel:
                return entityClasses.containsKey(EntityClassName.AbstractStateModel) ? entityClasses.get(EntityClassName.AbstractStateModel)
                            : createAbstractStateModelClass();

            default:
                return null;
        }
    }

    private static EntityClass createAbstractStateClass() {
        EntityClass abstractStateClass = new EntityClass("AbstractState", EntityClass.EntityType.Vertex);
        Property uniqueId = new Property("uid", OType.STRING);
        uniqueId.setMandatory(true);
        uniqueId.setNullable(false);
        uniqueId.setIdentifier(true);
        abstractStateClass.addProperty(uniqueId);
        Property stateId = new Property("stateId", OType.STRING);
        stateId.setMandatory(true);
        stateId.setNullable(false);
        abstractStateClass.addProperty(stateId);
        Property abstractionLevelIdentifier = new Property("abstractionLevelIdentifier", OType.STRING);
        abstractionLevelIdentifier.setMandatory(true);
        abstractionLevelIdentifier.setNullable(false);
        abstractStateClass.addProperty(abstractionLevelIdentifier);
        Property isInitial = new Property("isInitial", OType.BOOLEAN);
        isInitial.setMandatory(true);
        isInitial.setNullable(false);
        abstractStateClass.addProperty(isInitial);
        Property unvisitedActions = new Property("unvisitedActions", OType.EMBEDDEDSET, OType.STRING);
        unvisitedActions.setMandatory(false);
        unvisitedActions.setNullable(true);
        abstractStateClass.addProperty(unvisitedActions);
        Property concreteStateIds = new Property("concreteStateIds", OType.EMBEDDEDSET, OType.STRING);
        concreteStateIds.setMandatory(false);
        concreteStateIds.setNullable(false);
        abstractStateClass.addProperty(concreteStateIds);
        entityClasses.put(EntityClassName.AbstractState, abstractStateClass);
        return abstractStateClass;
    }

    private static EntityClass createAbstractActionClass() {
        EntityClass abstractActionClass = new EntityClass("AbstractAction", EntityClass.EntityType.Edge);
        Property actionId = new Property("actionId", OType.STRING);
        actionId.setMandatory(true);
        actionId.setNullable(false);
        actionId.setIdentifier(true);
        abstractActionClass.addProperty(actionId);
        Property unvisitedActions = new Property("concreteActionIds", OType.EMBEDDEDSET, OType.STRING);
        unvisitedActions.setMandatory(false);
        unvisitedActions.setNullable(false);
        abstractActionClass.addProperty(unvisitedActions);
        entityClasses.put(EntityClassName.AbstractAction, abstractActionClass);
        return abstractActionClass;
    }

    private static EntityClass createAbstractStateModelClass() {
        EntityClass abstractStateModelClass = new EntityClass("AbstractStateModel", EntityClass.EntityType.Vertex);
        Property id = new Property("abstractionLevelIdentifier", OType.STRING);
        id.setMandatory(true);
        id.setNullable(false);
        id.setIdentifier(true);
        abstractStateModelClass.addProperty(id);
        Property abstractionAttributes = new Property("abstractionAttributes", OType.EMBEDDEDSET, OType.STRING);
        abstractionAttributes.setMandatory(true);
        abstractionAttributes.setNullable(false);
        abstractStateModelClass.addProperty(abstractionAttributes);
        entityClasses.put(EntityClassName.AbstractStateModel, abstractStateModelClass);
        return abstractStateModelClass;
    }



}