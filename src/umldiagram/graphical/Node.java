package umldiagram.graphical;


import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import umldiagram.logical.Attribute;
import umldiagram.logical.UmlClass;




/**
 * Class that graphically represents a node (regular class) in the UML diagram.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
class Node extends Label {
    private String name;



    /**
     * Initializes the node with the elements received as parameters.
     * @param x The coordinate x.
     * @param y The coordinate y.
     * @param umlClass The UML class in object format.
     */
    Node(double x, double y, UmlClass umlClass) {
        super();
        name = umlClass.getName();

        // Get the attribute
        String attr = umlClass.getLongestAttribute();


        // Prepare the text
        String text = name.toUpperCase();
        for(Attribute a: umlClass.getAttributes()) {
            text += "\n" + a.toString();
        }


        // Get a label for the text
        setText(text);
        setWrapText(true);
        setStyle("-fx-background-color: #bbe1ff; " +
                "-fx-border-color: #76B6FF; " +
                "-fx-text-fill: #20283d");


        setPadding(new Insets(10,10,10,10));

        setLayoutX(x);
        setLayoutY(y);
    }




    /**
     * Method to override the parent one, and check if a point
     * is contained inside this label.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if it is contained, false otherwise.
     */
    @Override
    public boolean contains(double x, double y) {
        return getLayoutX() <= x && getLayoutY()<=y
            && (getLayoutX() + getWidth())>=x && (getLayoutY()+getHeight())>=y;
    }


    /**
     * Returns the name of the node.
     * @return the name of the node.
     */
    public String getName() {
        return name;
    }









    /**
     * Method that compares two nodes, and returns the mid point between
     * the sides that are closer to eachother.
     * @param targetNode The node to be compared with.
     * @return Two points tuple (origin, ending).
     */
    public Point2D[] fromTo(Node targetNode) {
        // Create the array
        Point2D[] vector = new Point2D[2];

        // If the origin is under
        if(getLayoutY() >= (targetNode.getLayoutY() + targetNode.getHeight())){
            vector[0] = new Point2D(getLayoutX() + getWidth() / 2, getLayoutY());
            vector[1] = new Point2D(targetNode.getLayoutX() + targetNode.getWidth() / 2,
                    targetNode.getLayoutY() + targetNode.getHeight() - 1);
        }
        // If not, it is up
        else if( (getLayoutY() + getHeight()) <= targetNode.getLayoutY()){
            vector[0] = new Point2D(getLayoutX() + getWidth() / 2,
                    getLayoutY() + getHeight() - 1);
            vector[1] = new Point2D(targetNode.getLayoutX() + targetNode.getWidth() / 2,
                    targetNode.getLayoutY());
        }
        // If it is at the parent's left.
        else if( (getLayoutX() + getWidth()) <= targetNode.getLayoutX()){
            vector[0] = new Point2D(getScaleX() + getWidth(),
                    getLayoutY() + getHeight() / 2);
            vector[1] = new Point2D(getLayoutX(), getLayoutY() + targetNode.getHeight() / 2);
        }
        // If it is at the parent's right.
        else if ( getLayoutX() >= (targetNode.getLayoutX() + targetNode.getWidth()) ){
            vector[0] = new Point2D(getLayoutX(), getLayoutY() + getHeight() / 2);
            vector[1] = new Point2D(targetNode.getLayoutX() + targetNode.getWidth(),
                    targetNode.getLayoutY() + targetNode.getHeight() / 2);
        }
        return vector;
    }







}
