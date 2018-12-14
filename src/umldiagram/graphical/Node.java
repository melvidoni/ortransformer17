package umldiagram.graphical;


import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import umldiagram.logical.Attribute;
import umldiagram.logical.UmlClass;

import java.util.List;




/**
 * Class that graphically represents a node (regular class) in the UML diagram.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class Node extends Label {
    private String name;



    /**
     * Initializes the node with the elements received as parameters.
     * @param x The coordinate x.
     * @param y The coordinate y.
     * @param umlClass The UML class in object format.
     */
    Node(double x, double y, UmlClass umlClass, boolean isAC) {
        super();
        name = umlClass.getName();


        // Prepare the text
        String text = name.toUpperCase();
        for(Attribute a: umlClass.getAttributes()) {
            text += "\n" + a.toString();
        }

        // Get a label for the text
        setText(text);
        setWrapText(true);
        if(!isAC) {
            setStyle("-fx-background-color: #bbe1ff; " +
                    "-fx-border-color: #76B6FF; -fx-text-fill: #20283d");
        }
        else {
            setStyle("-fx-background-color: #bfde9c; " +
                    "-fx-border-color: #5e991d; -fx-text-fill: #1a3d0a");
        }

        setPadding(new Insets(10,10,10,10));

        setTranslateX(x);
        setTranslateY(y);

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
        return getTranslateX() <= x && getTranslateY()<=y
            && (getTranslateX() + getWidth())>=x && (getTranslateY()+getHeight())>=y;
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
     * the sides that are closer to each other.
     * @param targetNode The node to be compared with.
     * @return Two points tuple (origin, ending).
     */
    Point2D[] fromTo(Node targetNode) {
        // Create the array
        Point2D[] vector = new Point2D[2];

        // If the origin is under
        if(getTranslateY() >= (targetNode.getTranslateY() + targetNode.getHeight())){
            vector[0] = new Point2D(getTranslateX() + getWidth() / 2, getTranslateY());
            vector[1] = new Point2D(targetNode.getTranslateX() + targetNode.getWidth() / 2,
                    targetNode.getTranslateY() + targetNode.getHeight() - 1);
        }
        // If not, it is up
        else if( (getTranslateY() + getHeight()) <= targetNode.getTranslateY()){
            vector[0] = new Point2D(getTranslateX() + getWidth() / 2,
                    getTranslateY() + getHeight() - 1);
            vector[1] = new Point2D(targetNode.getTranslateX() + targetNode.getWidth() / 2,
                    targetNode.getTranslateY());
        }
        // If it is at the the origin's right.
        else if( (getTranslateX() + getWidth()) <= targetNode.getTranslateX()){
            vector[0] = new Point2D(getTranslateX() + getWidth(),
                    getTranslateY() + getHeight() / 2);
            vector[1] = new Point2D(targetNode.getTranslateX(),
                    targetNode.getTranslateY() + targetNode.getHeight() / 2);
        }
        // If it is at the origin's left.
        else if ( getTranslateX() >= (targetNode.getTranslateX() + targetNode.getWidth()) ){
            vector[0] = new Point2D(getTranslateX(), getTranslateY() + getHeight() / 2);
            vector[1] = new Point2D(targetNode.getTranslateX() + targetNode.getWidth(),
                    targetNode.getTranslateY() + targetNode.getHeight() / 2);
        }
        return vector;
    }



    /**
     * Given a point, this method returns the side of the rectangle in which
     * it is contained, in order to estimate the cardinalities locations.
     * @param p The point to be evaluated
     * @return The return follows a character code. It is L (left), R (right),
     *         T (up) and B (down).
     */
    char getSide(Point2D p) {
        if(getTranslateX() == p.getX()){
            return 'L';
        }
        else if((getTranslateX() + getWidth()) == p.getX() ){
            return 'R';
        }
        else if(getTranslateY() == p.getY()){
            return 'T';
        }
        else
            return 'B';
    }


    /**
     * Update the values of the node, with the information received
     * as a parameters.
     * @param n The new name of the node.
     * @param attributes The list of attributes.
     */
    void updateValues(String n, List<Attribute> attributes) {
        // Prepare the text
        String text = n.toUpperCase();
        for(Attribute a: attributes) {
            text += "\n" + a.toString();
        }

        // Change the name
        name = n;

        // Chane the text
        setText(text);
        applyCss();
    }
}
