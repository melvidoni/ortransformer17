package umldiagram.graphical;


import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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





    public void addNewNode(double x, double y) {
        // Get coords of mouse
        Node rectangle = new Node(x, y, 100, 100, Color.BLUEVIOLET);
        rectangle.setOnMousePressed(this::pressedClass);
        rectangle.setOnMouseDragged(this::draggedClass);

        // Add the nodes
        nodes.add(rectangle);
        getChildren().add(rectangle);
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
