package umldiagram.graphical;



public class DrawingStatus {
    private static DrawingStatus instance = new DrawingStatus();

    private boolean drawingClass;
    private boolean drawingGen;
    private boolean drawingAssoc;




    /**
     * Private constructor that initializes the instance.
     */
    private DrawingStatus() {
        drawingClass = false;
        drawingGen = false;
        drawingAssoc = false;
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
     * Setter to change if the status is drawing a class.
     * @param d true if it is drawing, false otherwise.
     */
    public void setDrawingClass(boolean d) {
        drawingClass = d;
    }


    /**
     * Setter to change if the status is drawing a generalization.
     * @param d true if it is drawing, false otherwise.
     */
    public void setDrawingGen(boolean d) {
        drawingGen = d;
    }



    /**
     * Setter to change if the status is drawing an association.
     * @param d true if it is drawing, false otherwise.
     */
    public void setDrawingAssoc(boolean d) {
        drawingGen = d;
    }






    /**
     * Method to check if anything is being drawn.
     * @return true if something is being drawn, false otherwise.
     */
    public boolean isDrawing() {
        // TODO COMPLETE THIS PART
        return drawingClass || drawingGen || drawingAssoc;
    }


    /**
     * Method that returns if a line is being drawn.
     * @return true if it is being drawn, false otherwise.
     */
    public boolean isDrawingLine() {
        // TODO ADD OTHER RELATIONSHIPS
        return drawingAssoc || drawingGen;
    }

}
