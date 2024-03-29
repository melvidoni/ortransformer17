package umldiagram.graphical;


import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import umldiagram.logical.AssociationClass;
import umldiagram.logical.Relationship;
import umldiagram.logical.UmlClass;
import umldiagram.logical.enums.RelationshipType;


/**
 * Class that graphically represents and association class.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
class ComplexNode extends Group {
    private String className;
    private String relName;


    private Arrow arrow;
    private Node node;
    private Line line;
    private char side;

    private String originNode;
    private String endNode;




    /**
     * Constructor to create a new complex node with the information received.
     * @param ac The association class in object format.
     * @param startPoint The starting point of the relationship.
     * @param endPoint The ending point of the relationship.
     * @param fromSide Side where the relationship starts.
     * @param toSide Side where the relationship ends.
     */
    ComplexNode(AssociationClass ac, Point2D startPoint, Point2D endPoint, char fromSide, char toSide) {
        // Initialize the basic values
        super();
        className = ac.getUmlClass().getName();
        relName = ac.getRelationship().getName();

        // Update the nodes
        originNode = ac.getRelationship().getOrigin().getClassOf().getName();
        endNode = ac.getRelationship().getEnd().getClassOf().getName();

        // Create the basic arrow
        arrow = new Arrow(relName, startPoint, endPoint, ac.getRelationship().getType(),
                ac.getRelationship().getOrigin(), ac.getRelationship().getEnd(),
                fromSide, toSide
        );

        // Add it to the group
        getChildren().add(arrow);

        // Create the midline and the node
        Point2D finalMidPoint = drawMidLine(startPoint, endPoint);
        drawNode(finalMidPoint, ac.getUmlClass());
    }


    /**
     * Method that draws the node of the association class, using the point
     * where the mid line ends.
     * @param finalMidPoint Point where the mid line ends.
     * @param umlClass The UML Class of the Association Class.
     */
    private void drawNode(Point2D finalMidPoint, UmlClass umlClass) {
        // Get the point for the association class
        Point2D classStartPoint = getNodeStartPoint(finalMidPoint, umlClass);

        // Draw the node
        node = new Node(classStartPoint.getX(), classStartPoint.getY(), umlClass, true);

        // Add it
        getChildren().add(node);
    }




    /**
     * Method that draws mid line for the association class.
     * @param startPoint The starting point of the relationship.
     * @param endPoint The ending point of the relationship.
     * @return the mid point of the regular relationship line, as a Point2D.
     */
    private Point2D drawMidLine(Point2D startPoint, Point2D endPoint) {
        // For the midline, get the points
        Point2D startingMidPoint = new Point2D(
                (startPoint.getX() + endPoint.getX()) / 2,
                (endPoint.getY() + startPoint.getY()) / 2
        );
        Point2D finalMidPoint = getEndingPoint(startingMidPoint, startPoint, endPoint);

        // Draw the line
        line = new Line(startingMidPoint.getX(), startingMidPoint.getY(),
                finalMidPoint.getX(), finalMidPoint.getY());
        line.setStyle("-fx-stroke: #565656;");
        getChildren().add(line);

        // Return the mid point
        return finalMidPoint;
    }




    /**
     * Method that updates the values of the association class, changing
     * the names and attributes on the regular node.
     * @param umlClass The information in object format to update the node.
     */
    void updateValues(UmlClass umlClass) {
        // Lets update the node
        node.updateValues(umlClass.getName(), umlClass.getAttributes());
        layout();

        // Relocate the class
        Point2D newStartingPoint =  (side == 'V')
                ? (new Point2D(line.getEndX(), line.getEndY() - node.getHeight() / 2))
                : (new Point2D(line.getEndX() - node.getWidth() / 2, line.getEndY()));
        node.relocate(newStartingPoint.getX(), newStartingPoint.getY());
        layout();
    }






    /**
     * Method that updates the values of the relationship, changing the
     * corresponding data, according to the one received as parameters.
     * @param relationship The relationship with the information to be used.
     * @param startPoint The starting point of the relationship.
     * @param endPoint The ending point of the relationship.
     * @param fromSide Side where the relationship starts.
     * @param toSide Side where the relationship ends.
     */
    void updateArrow(Relationship relationship, Point2D startPoint, Point2D endPoint,
                     char fromSide, char toSide) {
        // Call the arrow to update it
        arrow.updateValues(relationship, startPoint, endPoint, fromSide, toSide);
        layout();
    }






    /**
     * Method to obtain the starting drawing point for a complex node,
     * based on the received parameters.
     * @param endPoint The point where the line ends.
     * @param uClass The UML class in object format.
     * @return the point where the class should be drawn.
     */
    private Point2D getNodeStartPoint(Point2D endPoint, UmlClass uClass) {
        // Prepare the string for the faux label
        HBox root = new HBox();
        Node fauxNode = new Node(0,0, uClass, false);
        root.getChildren().add(fauxNode);
        new Scene(root);
        root.applyCss();
        root.layout();

        // Depending on the side
        switch(side) {
            case 'V': // Vertical line
                      return new Point2D(endPoint.getX(),
                              endPoint.getY() - fauxNode.getHeight() / 2);

            case 'H': // Horizontal line
                      return new Point2D(endPoint.getX() - fauxNode.getWidth() / 2,
                                 endPoint.getY());
        }

        return null;
    }





    /**
     * Method that returns the ending point for the line towards the
     * association class in a ComplexLine.
     * @param midPoint Mid point of the line.
     * @param originPoint Origin point of the line.
     * @param endPoint Ending point of the line.
     * @return the point where the line ends in object format.
     */
    private Point2D getEndingPoint(Point2D midPoint, Point2D originPoint, Point2D endPoint) {
        // Obtain the slope of the line
        double slope = (originPoint.getY() - endPoint.getY())  /
                (endPoint.getX() - originPoint.getX());

        // If it is an horizontal line
        if(endPoint.getX() == originPoint.getX() || (slope <= 1 && slope >= -1)){
            side = 'H';
            return new Point2D(midPoint.getX(), midPoint.getY() + 50);

        }
        // If this is a vertical line
        else {
            side = 'V';
            return new Point2D(midPoint.getX() + 50, midPoint.getY());

        }
    }




    /**
     * Method that evaluates if a point is inside the node
     * included on this group.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if it is contained, false otherwise.
     */
    @Override
    public boolean contains(double x, double y) {
        return node.contains(x, y);
    }


    /**
     * Method to obtain the class name of this complex node.
     * @return The class name of the complex node;
     */
    String getClassName() {
        return className;
    }


    /**
     * Method to obtain the relationship name of this complex node.
     * @return The class name of the complex node;
     */
    String getRelName() {
        return relName;
    }




    /**
     * Method that checks if this arrow starts on a specific node. This is done
     * by comparing the name of a class to the one saved.
     * @param originClass The class name to be compared.
     * @return true if it starts there, false otherwise.
     */
    boolean startsOn(String originClass) {
        return originNode.equals(originClass);
    }




    /**
     * Method that checks if this arrow ends on a specific node. This is done
     * by comparing the name of a class to the one saved.
     * @param endClass The class name to be compared.
     * @return true if it ends there, false otherwise.
     */
    boolean endsOn(String endClass) {
        return endNode.equals(endClass);
    }






    void updateDrawing(Arrow newArrow, RelationshipType type, UmlClass umlClass) {
        // Update the arrow
        arrow.updateDrawing(newArrow, type);

        // Remove the other bits
        getChildren().removeAll(line, node);

        // Update the line
        Line mainLine = newArrow.getMainLine();
        Point2D finalMidPoint = drawMidLine(new Point2D(mainLine.getStartX(), mainLine.getStartY()),
                                            new Point2D(mainLine.getEndX(), mainLine.getEndY()));

        // Update the node
        drawNode(finalMidPoint, umlClass);
    }
}
