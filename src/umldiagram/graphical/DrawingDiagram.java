package umldiagram.graphical;


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





    public void addNewNode(double x, double y, UmlClass c) {
        // Draw the basic rectangle
        Node node = new Node(x, y, c);

        // Add the listeners for dragging
        node.setOnMousePressed(this::pressedClass);
        node.setOnMouseDragged(this::draggedClass);

        // Add the nodes
        nodes.add(node);
        getChildren().add(node);
    }









    private void pressedClass(MouseEvent me) {
        orgSceneX = me.getSceneX();
        orgSceneY = me.getSceneY();
        orgTranslateX = ((Node)(me.getSource())).getTranslateX();
        orgTranslateY = ((Node)(me.getSource())).getTranslateY();
    }



    private void draggedClass(MouseEvent me) {
        double offsetX = me.getSceneX() - orgSceneX;
        double offsetY = me.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        ((Node)(me.getSource())).setTranslateX(newTranslateX);
        ((Node)(me.getSource())).setTranslateY(newTranslateY);
    }


}
