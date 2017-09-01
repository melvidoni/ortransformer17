package umldiagram.graphical;


import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import umldiagram.logical.enums.RelationshipType;

public class DrawingStatus {
    private static DrawingStatus instance = new DrawingStatus();

    private SimpleBooleanProperty drawingClass;
    private SimpleBooleanProperty drawingGen;
    private SimpleBooleanProperty drawingAssoc;

    private String originClass;
    private String endClass;




    /**
     * Private constructor that initializes the instance.
     */
    private DrawingStatus() {
        drawingClass = new SimpleBooleanProperty(false);
        drawingGen = new SimpleBooleanProperty(false);
        drawingAssoc = new SimpleBooleanProperty(false);

        originClass = "";
        endClass = "";
    }


    /**
     * Method to obtain the current instance of the drawing status.
     * @param newInstance true to create a new instance, false otherwise.
     * @return the instance of the class.
     */
    public static DrawingStatus getInstance(boolean newInstance) {
        if(newInstance) instance = new DrawingStatus();
        return instance;
    }




    /**
     * Method that binds the properties of the status with the
     * ones received as parameters.
     * @param dc Toggle of drawing class property.
     * @param dg Toggle of drawing generalization property.
     * @param da Toggle of drawing association property.
     */
    public void bindProperties(BooleanProperty dc, BooleanProperty dg,
                               BooleanProperty da) {
        Bindings.bindBidirectional(drawingClass, dc);
        Bindings.bindBidirectional(drawingGen, dg);
        Bindings.bindBidirectional(drawingAssoc, da);
    }



    /**
     * Method to check if anything is being drawn.
     * @return true if something is being drawn, false otherwise.
     */
    public boolean isDrawing() {
        // TODO COMPLETE THIS PART
        return drawingClass.get() || drawingGen.get() || drawingAssoc.get();
    }


    /**
     * Method that returns if a line is being drawn.
     * @return true if it is being drawn, false otherwise.
     */
    public boolean isDrawingLine() {
        // TODO ADD OTHER RELATIONSHIPS
        return drawingAssoc.get() || drawingGen.get();
    }






    public RelationshipType getRelType() {
        // TODO FILL WITH OTHER RELATIONSHIP
        if(drawingGen.get()) return RelationshipType.GENERALIZATION;
        else if(drawingAssoc.get()) return RelationshipType.ASSOCIATION;


        return null;
    }



    /**
     * Setter to change the values of the classes being used
     * to create a new relationship.
     * @param oc Name of the origin class.
     * @param ec Name of the destination class.
     */
    public void setClasses(String oc, String ec) {
        originClass = oc;
        endClass = ec;
    }


    /**
     * Getter to obtain the value of the destination class.
     * @return The class name.
     */
    public String getEndClass() {
        return endClass;
    }


    /**
     * Getter to obtain the value of the origin class.
     * @return The class name.
     */
    public String getOriginClass() {
        return originClass;
    }
}
