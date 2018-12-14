package umldiagram.graphical;


import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import umldiagram.graphical.status.DrawingStatus;
import umldiagram.graphical.status.EditingStatus;
import umldiagram.logical.AssociationClass;
import umldiagram.logical.Relationship;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.UmlClass;
import java.util.LinkedList;
import java.util.stream.Collectors;


/**
 * Class that represents the drawing space of the system, in which
 * the user clicks, creates the elements and visualizes a UML diagram.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class DrawingDiagram extends Pane {
    private LinkedList<Node> nodes;
    private LinkedList<Arrow> arrows;
    private LinkedList<ComplexNode> complexNodes;

    Point2D orgScene = null;
    Point2D orgTranslate = null;
    private LinkedList<Arrow> draggedArrows;
    private LinkedList<ComplexNode> draggedCN;



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
        draggedArrows = new LinkedList<>();
        draggedCN = new LinkedList<>();

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
    public boolean pointOccupiedByClass(double x, double y) {
        // Check through each node
        for(Node n: nodes) {
            if(n.contains(x, y)) return true;
        }
        return false;
    }




    /**
     * Method that checks if the point received belongs to a
     * complex node, or not. If it is contained in at least one, it returns.
     * @param x Coordinate x of the point.
     * @param y Coordinate y of the point.
     * @return true if the point belongs to a complex node, false otherwise.
     */
    public boolean pointOccupiedByAssocClass(double x, double y) {
        // Check through each node
        for(ComplexNode n: complexNodes) {
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
        Node node = new Node(x, y, c, false);

        // Add the listeners for dragging
        node.setOnMouseDragged(this::draggedClass);
        //node.setOnMouseReleased(this::releasedClass);

        node.setOnDragOver(this::releasedClass);


        // Add the nodes
        nodes.add(node);
        getChildren().add(node);

        // Update the size
        updateSize(getBoundsInLocal().getWidth(), getBoundsInLocal().getHeight());
    }






    /**
     * Method to create a new relationship,
     * with the information received as a parameter.
     * @param rel The new relationship to create.
     */
    public void addNewRel(Relationship rel) {
        // Get the arrow
        Arrow arrow = createNewArrow(getOriginEnding(rel), rel);

        // Add the loop
        arrows.add(arrow);
        getChildren().add(arrow);
    }



    /**
     * Method to obtain the origin and ending nodes of a relationship.
     * @param rel The relationship to be analyzed.
     * @return An array with two nodes. Position 0 is origin, and position 1 is ending.
     */
    private Node[] getOriginEnding(Relationship rel) {
        // Prepare the nodes
        Node origin = null;
        Node ending = null;

        // Get the nodes
        for(Node n: nodes) {
            if(n.getName().equals(rel.getOrigin().getClassOf().getName())) origin = n;
            if(n.getName().equals(rel.getEnd().getClassOf().getName())) ending = n;

            if(origin!=null && ending!=null) break;
        }

        // Return the pair
        return new Node[]{origin, ending};
    }




    /**
     * Method that creates a new arrow, using the information required for it.
     * @param originEnding A origin-ending tuple of nodes.
     * @param rel The relationship that matches this arrow.
     * @return A graphical arrows initialized.
     */
    private Arrow createNewArrow(Node[] originEnding, Relationship rel) {
        Node origin = originEnding[0];
        Node ending = originEnding[1];

        // If origin and end are different
        if(!origin.equals(ending)) {
            // Get the ending points
            Point2D[] points = origin.fromTo(ending);
            char fromSide = origin.getSide(points[0]);
            char toSide = ending.getSide(points[1]);

            // Now we will create a line
            return new Arrow(rel.getName(), points[0], points[1], rel.getType(),
                    rel.getOrigin(),rel.getEnd(), fromSide, toSide
            );
        }
        // If they are the same
        else {
            // Prepare the two points
            Point2D fromPoint = new Point2D(
                    origin.getTranslateX() + origin.getWidth(),
                    origin.getTranslateY() + Math.random() *
                            ((origin.getTranslateY() + origin.getHeight()) - origin.getTranslateY())
            );
            Point2D toPoint = new Point2D(
                    origin.getTranslateX() + Math.random() *
                            ((origin.getTranslateX() + origin.getWidth()) - origin.getTranslateX()),
                    origin.getTranslateY()
            );

            // Create the loop arrow
            return new Arrow(fromPoint, toPoint, rel);
        }
    }





    /**
     * Method to create a new association class relationship,
     * with the information received as a parameter.
     * @param assocClass The new association class to create.
     */
    public void addNewAssociationClass(AssociationClass assocClass) {
        // Get the nodes
        Node[] oeNodes = getOriginEnding(assocClass.getRelationship());

        // Get the ending points
        Point2D[] points = oeNodes[0].fromTo(oeNodes[1]);

        // Create the complex node
        ComplexNode complexNode = new ComplexNode(assocClass, points[0],
                points[1], oeNodes[0].getSide(points[0]), oeNodes[1].getSide(points[1]));

        // Add it to the diagram
        complexNodes.addFirst(complexNode);
        getChildren().add(complexNode);

        // Update the size
        updateSize(getBoundsInLocal().getWidth(), getBoundsInLocal().getHeight());
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
        for(ComplexNode cn : complexNodes) {
            if(cn.contains(x, y)) return cn.getClassName();
        }
        return "";
    }







    /**
     * Method that removes the class node associated to the one which name
     * was received as a parameter, as well as all the related relationships.
     * @param className The class node to delete.
     * @param relationshipsOf The relationships to be removed.
     */
    public void deleteClass(String className, LinkedList<String[]> relationshipsOf) {
        // Look for the node and delete it
        for (Node n : nodes) {
            if (n.getName().equals(className)) {
                getChildren().remove(n);
                nodes.remove(n);
                break;
            }
        }

        // Separate the association classes and relationships
        for(String[] rof : relationshipsOf) {
            // If it is association class, delete it
            if(rof[1].equals("Association Class")) deleteAssociationClass(rof[0]);
            // Else, it is a normal relationship
            else  deleteRelationship(rof[0]);
        }

        // Update the size
        updateSize(getBoundsInLocal().getWidth(), getBoundsInLocal().getHeight());
    }



    /**
     * Method that deletes aan association class from the diagram.
     * @param acName The assoc class name to be deleted.
     */
    public void deleteAssociationClass(String acName) {
        // Go through the complex nodes
        for(ComplexNode cn: complexNodes) {
            // If the name is the same
            if(cn.getRelName().equals(acName)) {
                // Then delete it
                getChildren().remove(cn);
                complexNodes.remove(cn);
                break;
            }
        }

        // Update the size
        updateSize(getBoundsInLocal().getWidth(), getBoundsInLocal().getHeight());
    }



    /**
     * Method that deletes a relationship from the diagram.
     * @param relName The relationship to be deleted.
     */
    public void deleteRelationship(String relName) {
        // Go through the regular arrows
        for(Arrow a: arrows) {
            // If the name is the same
            if(a.getName().equals(relName)) {
                // Remove it
                getChildren().remove(a);
                arrows.remove(a);
                break;
            }
        }
    }





    /**
     * Method that edits a class in the diagram. This can be either a
     * regular class or an association class.
     */
    public void editClass() {
        // Get the information
        EditingStatus editingStatus = EditingStatus.getInstance(false);
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);


        // If it is a regular class
        if(!editingStatus.isAssociationClass()) {
            // Get the class
            UmlClass umlClass = umlDiagram.getClasses(editingStatus.getEditedClassName());

            // Go through the nodes
            for(Node n: nodes) {
                // If this matches
                if( n.getName().equals(editingStatus.getClassName()) ) {
                    // Update the node
                    n.updateValues(umlClass.getName(), umlClass.getAttributes());
                    break;
                }
            }

            // Force the class to change
            this.layoutChildren();


            /*
                EDIT THE RELATIONSHIPS
             */
            // Get the relationships
            LinkedList<String[]> relationships = umlDiagram.getRelationshipsOf(editingStatus.getEditedClassName());

            // Go through them
            for(String[] rel: relationships) {
                // If this is association class
                if(rel[1].equals("Association Class")) {
                    // Remove the existing association class
                    deleteAssociationClass(rel[0]);

                    // Add a new one
                    addNewAssociationClass(umlDiagram.getAssociationClasses(rel[0]));

                }
                // If not, edit
                else {
                    // Remove the arrow
                    deleteRelationship(rel[0]);

                    // Create a new relationship
                    addNewRel(umlDiagram.getRelationship(rel[0]));
                }
            }

        }
        // If it is an association class
        else {
            // Get the association class
            AssociationClass assocClass = umlDiagram.getAssociationClasses(editingStatus.getEditedClassName());

            // Go through the complex nodes
            for(ComplexNode cn: complexNodes) {
                // If this is it
                if(cn.getClassName().equals(editingStatus.getClassName()) ) {
                   // Update the complex node
                   cn.updateValues(assocClass.getUmlClass());
                   break;
                }
            }


        }


        // Update the size
        updateSize(getBoundsInLocal().getWidth(), getBoundsInLocal().getHeight());
    }





    /**
     * Method that edits a relationship in the diagram. It can be either
     * a regular relationship or an association class.
     */
    public void editRelationship() {
        // Get the instances
        EditingStatus eStatus = EditingStatus.getInstance(false);
        UMLDiagram diagram = UMLDiagram.getInstance(false);

        // Get the relationship
        Relationship relationship = (eStatus.isAssociationClass())
                ? (diagram.getRelAssociationClass(eStatus.getEditedRelName()).getRelationship())
                : (diagram.getRelationship(eStatus.getEditedRelName()));


        // Get the nodes
        Node[] oeNodes = getOriginEnding(relationship);

        // Get the ending points
        Point2D[] points = oeNodes[0].fromTo(oeNodes[1]);
        char fromSide = oeNodes[0].getSide(points[0]);
        char toSide = oeNodes[1].getSide(points[1]);



        // If it is an association class
        if(eStatus.isAssociationClass()) {
            // Go through the nodes
            for(ComplexNode cn: complexNodes) {
                // If this is it
                if(cn.getRelName().equals(eStatus.getRelName())) {
                    // Call the editing method
                    cn.updateArrow(relationship, points[0], points[1], fromSide, toSide);
                    break;
                }
            }
        }
        // If not, it is a regular relationship
        else {
            // Find the relationship
            for(Arrow a: arrows) {
                // If this is it
                if(a.getName().equals(eStatus.getRelName())) {
                    // Call the editing method
                    a.updateValues(relationship, points[0], points[1], fromSide, toSide);
                    break;
                }
            }
        }
        // Finish the editing
    }




    /**
     * Method that returns the list of nodes of the drawing.
     * @return The list of nodes in object format.
     */
    public LinkedList<Node> getNodes() {
        return nodes;
    }




    /**
     * Method that updates the size of the pane, in order to add
     * scrollbars, if corresponds.
     * @param width The current width.
     * @param height The current height.
     */
    public void updateSize(double width, double height) {
        // Apply the elements
        applyCss();
        layoutChildren();

        // Check each side, and update
        if (width > getMinWidth())  setMinWidth(width);
        if (height > getMinHeight()) setMinHeight(height);
    }




    /**
     * Action listener for when the mouse is released from the dragging event
     * @param me The mouse event to be used.
     */
    private void draggedClass(MouseEvent me) {
        // If it is not drawing anything else
        if(!DrawingStatus.getInstance(false).isDrawing() && me.getButton() == MouseButton.SECONDARY) {
            // Get the node
            Node node = (Node) me.getSource();

            // Save the values
            if (orgScene == null && orgTranslate == null) {
                // Store points
                orgScene = new Point2D(me.getSceneX(), me.getSceneY());
                orgTranslate = new Point2D( ((Node)me.getSource()).getTranslateX(),
                        ((Node)me.getSource()).getTranslateY());

                // Now store the arrows
                draggedArrows.addAll(
                        arrows.stream().filter(a -> a.startsOn(node.getName()) || a.endsOn(node.getName())).collect(Collectors.toList())
                );

                // Store the complex nodes
                draggedCN.addAll(
                        complexNodes.stream().filter(cn -> cn.startsOn(node.getName()) || cn.endsOn(node.getName()) ).collect(Collectors.toList())
                );
            }

            // Calculate the movement
            double newTranslateX = orgTranslate.getX() + me.getSceneX() - orgScene.getX();
            double newTranslateY = orgTranslate.getY() + me.getSceneY() - orgScene.getY();

            // Update it!
            node.setTranslateX(newTranslateX);
            node.setTranslateY(newTranslateY);

            // Update the arrows
            for(Arrow da : draggedArrows) {
                // Get the relationship
                Relationship rel = UMLDiagram.getInstance(false).getRelationship(da.getName());

                // Update with the values of a new arrow. It is simpler!
                da.updateDrawing(createNewArrow(getOriginEnding(rel), rel), rel.getType());
            }

            // Update the complex nodes
            for(ComplexNode dcn : draggedCN) {
                // Get the association class
                AssociationClass ac = UMLDiagram.getInstance(false).getRelAssociationClass(dcn.getRelName());
                Relationship rel = ac.getRelationship();

                // Update with new elements
                dcn.updateDrawing(createNewArrow(getOriginEnding(rel), rel), rel.getType(), ac.getUmlClass());
            }


        }
    }






    /**
     * Action listener for when the mouse is released from the dragging event.
     * @param me The mouse event to be used.
     */
    private void releasedClass(DragEvent me) {
        // Drag released
        System.out.println("drag released");

        // If it is not drawing anything else
        if(!DrawingStatus.getInstance(false).isDrawing() /*&& me.getButton() == MouseButton.SECONDARY*/) {
            // Back to nothing
            orgTranslate = null;
            orgScene = null;
            draggedArrows.clear();
            draggedCN.clear();
        }
    }



}
