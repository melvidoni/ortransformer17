package gui.controllers;


import umldiagram.logical.enums.RelationshipType;


/**
 * Abstract class that contains basic methods for the controllers
 * of relationship windows that need to set up basic information.
 */
public abstract class ARelationshipController {
    protected RelationshipType type;
    protected String originClass;
    protected String endClass;


    /**
     * Method that initializes the controller with the received
     * information about the relationship.
     * @param t Type of the relationship.
     * @param origin Name of origin class.
     * @param end Name of destination class.
     */
    public void initialize(RelationshipType t, String origin, String end) {
        type = t;
        originClass = origin;
        endClass = end;
    }

}
