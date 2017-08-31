package umldiagram.graphical;


import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import umldiagram.logical.UmlClass;

import java.util.LinkedList;



public class DrawingDiagram extends Pane {
    private LinkedList<Node> nodes;

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;




    /**
     * Default constructor of the class, that initializes a new
     * drawing diagram and cleans all the lists.
     */
    public DrawingDiagram() {
        super();

        // Prepare the lists
        nodes = new LinkedList<>();

        // Clean the children
        getChildren().clear();
    }



    /**
     * Method that creates a new model, by cleaning up
     * the current canvas.
     */
    public void newModel() {
        // Prepare the lists
        nodes = new LinkedList<>();

        // Clean the children
        getChildren().clear();
    }


    /**
     * Method that checks if the point received belongs to another
     * node, or not. If it is contained in at least one, it returns.
     * @param x Coordinate x of the point.
     * @param y Coordinate y of the point.
     * @return true if the point belongs to a rectangle, false otherwise.
     */
    public boolean pointOccupied(double x, double y) {
        // Check through each node
        for(Node n: nodes) {
            if(n.contains(x, y)) return true;
        }
        return false;
    }


    /**
     * Method to create a new graphical node, with the info
     * received as a parameter.
     * @param x The origin coordinates x.
     * @param y The origin coordinate y.
     * @param c The class with the logical data.
     */
    public void addNewNode(double x, double y, UmlClass c) {
        // Draw the basic rectangle
        Node node = new Node(x, y, c);

        // Add the listeners for dragging
        node.setOnMousePressed(this::pressedClass);
        node.setOnMouseDragged(this::draggedClass);
        node.setCursor(Cursor.CROSSHAIR);

        // Add the nodes
        nodes.add(node);
        getChildren().add(node);
    }





    /**
     * Given a point, it evaluates all the classes in order to return
     * the name of the class that contains that point.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The class name where it was clicked, or an empty string if
     * the point does not belong to any class.
     */
    public String getClassAt(double x, double y) {
        for(Node n: nodes) {
            if(n.contains(x, y)) return n.getName();
        }
        return "";
    }

















    private void pressedClass(MouseEvent me) {
        // If it is not drawing anything else
        if(!DrawingStatus.getInstance(false).isDrawing()) {
            orgSceneX = me.getSceneX();
            orgSceneY = me.getSceneY();
            orgTranslateX = ((Node)(me.getSource())).getTranslateX();
            orgTranslateY = ((Node)(me.getSource())).getTranslateY();
        }
    }




    private void draggedClass(MouseEvent me) {
        // If it is not drawing anything else
        if(!DrawingStatus.getInstance(false).isDrawing()) {
            double offsetX = me.getSceneX() - orgSceneX;
            double offsetY = me.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            ((Node)(me.getSource())).setTranslateX(newTranslateX);
            ((Node)(me.getSource())).setTranslateY(newTranslateY);
        }
    }


}
