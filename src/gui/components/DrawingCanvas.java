package gui.components;



import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import umldiagram.graphical.DrawingDiagram;



public class DrawingCanvas extends Canvas {
    private DrawingDiagram drawingDiagram;

    private boolean newClassFlag;
    private boolean newAssocRelFlag;
    private boolean newAggregRelFlag;
    private boolean newCompRelFlag;
    private boolean newGenRelFlag;
    private boolean newAssocClassFlag;



    public DrawingCanvas() {
        // Start the diagram
        drawingDiagram = new DrawingDiagram();

        // Prepare the flags
        newClassFlag = false;
        newAssocRelFlag = false;
        newAggregRelFlag = false;
        newCompRelFlag = false;
        newGenRelFlag = false;
        newAssocClassFlag = false;

        // Add the listeners
        setOnMouseClicked( me -> {
            System.out.println("ADDING LISTENER");
            clickAction(me);
        } );
    }



    /**
     * Sets all the flags for drawing a new class.
     */
    public void drawNewClass() {
        // Set this one to be drawn
        newClassFlag = true;

        // Set the others as false
        newAssocRelFlag = false;
        newAggregRelFlag = false;
        newCompRelFlag = false;
        newGenRelFlag = false;
        newAssocClassFlag = false;
    }












    private void clickAction(MouseEvent me) {
        if(me.getButton() == MouseButton.PRIMARY) {
            System.out.println("LEFT");
        }
        else if(me.getButton() == MouseButton.SECONDARY) {
            System.out.println("RIGHT");
        }




    }
}
