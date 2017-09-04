package umldiagram.graphical;


import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import umldiagram.logical.AssociationClass;
import umldiagram.logical.Relationship;
import umldiagram.logical.UmlClass;
import umldiagram.logical.enums.RelationshipType;

import java.util.LinkedList;



public class DrawingDiagram extends Pane {
    private LinkedList<Node> nodes;
    private LinkedList<Arrow> arrows;
    private LinkedList<ComplexNode> complexNodes;

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
        arrows = new LinkedList<>();
        complexNodes = new LinkedList<>();

        // Clean the children
        getChildren().clear();
    }



    /**
     * Method that creates a new model, by cleaning up
     * the current canvas.
     */
    public void newModel() {
        // Prepare the lists
        nodes.clear();
        arrows.clear();
        complexNodes.clear();

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
        //node.setOnMousePressed(this::pressedClass);
        //node.setOnMouseDragged(this::draggedClass);
        node.setCursor(Cursor.CROSSHAIR);

        // Add the nodes
        nodes.add(node);
        getChildren().add(node);
    }




    /**
     * Method to create a new generalization relationship,
     * with the information received as a parameter.
     * @param rel The new relationship to create.
     */
    public void addNewRel(Relationship rel) {
        // Prepare the nodes
        Node origin = null;
        Node ending = null;

        // Get the nodes
        for(Node n: nodes) {
            if(n.getName().equals(rel.getOrigin().getClassOf().getName())) origin = n;
            if(n.getName().equals(rel.getEnd().getClassOf().getName())) ending = n;

            if(origin!=null && ending!=null) break;
        }


        // If origin and end are different
        if(!origin.equals(ending)) {
            // Get the ending points
            Point2D[] points = origin.fromTo(ending);
            char fromSide = origin.getSide(points[0]);
            char toSide = ending.getSide(points[1]);

            // Now we will create a line
            Arrow line = new Arrow(rel.getName(), points[0], points[1], rel.getType(),
                    rel.getOrigin().getName() + "\n" + rel.getOrigin().getCardinality(),
                    rel.getEnd().getName() + "\n" + rel.getEnd().getCardinality(),
                    fromSide, toSide
            );

            // Add it
            arrows.add(line);
            getChildren().add(line);
        }
        // If they are the same
        else {
            // Prepare the two points
            Point2D fromPoint = new Point2D(
                    origin.getLayoutX() + origin.getWidth(),
                    origin.getLayoutY() + Math.random() *
                            ((origin.getLayoutY() + origin.getHeight()) - origin.getLayoutY())
            );
            Point2D toPoint = new Point2D(
                    origin.getLayoutX() + Math.random() *
                            ((origin.getLayoutX() + origin.getWidth()) - origin.getLayoutX()),
                    origin.getLayoutY()
            );

            // Create the loop arrow
            Arrow loopLine = new Arrow(fromPoint, toPoint, rel);

            // Add the loop
            arrows.add(loopLine);
            getChildren().add(loopLine);
        }




    }






    public void addNewAssociationClass(AssociationClass assocClass) {
        // Prepare the nodes
        Node origin = null;
        Node ending = null;

        // Get the nodes
        for(Node n: nodes) {
            if(n.getName().equals(assocClass.getRelationship().getOrigin().getClassOf().getName()))
                origin = n;
            else if(n.getName().equals(assocClass.getRelationship().getEnd().getClassOf().getName()))
                ending = n;

            if(origin!=null && ending!=null) break;
        }


        // Get the ending points
        Point2D[] points = origin.fromTo(ending);
        char fromSide = origin.getSide(points[0]);
        char toSide = ending.getSide(points[1]);


        // TODO Create the complex node




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
