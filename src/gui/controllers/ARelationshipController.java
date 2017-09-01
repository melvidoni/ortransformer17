package gui.controllers;


import umldiagram.graphical.DrawingStatus;
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
     * Method that initializes the controller with  the information about
     * current relationships being drawn.
     */
    void setValues() {
        // Get the drawing status
        DrawingStatus status = DrawingStatus.getInstance(false);

        // Set the values
        type = status.getRelType();
        originClass = status.getOriginClass();
        endClass = status.getEndClass();
    }

}
