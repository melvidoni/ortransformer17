package umldiagram.graphical;


import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import umldiagram.graphical.status.DrawingStatus;
import umldiagram.graphical.status.EditingStatus;
import umldiagram.logical.AssociationClass;
import umldiagram.logical.Relationship;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.UmlClass;

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
        //node.setOnMousePressed(this::pressedClass);
        //node.setOnMouseDragged(this::draggedClass);
        node.setCursor(Cursor.CROSSHAIR);

        // Add the nodes
        nodes.add(node);
        getChildren().add(node);
    }




    /**
     * Method to create a new relationship,
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





    /**
     * Method to create a new association class relationship,
     * with the information received as a parameter.
     * @param assocClass The new association class to create.
     */
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

        // Create the complex node
        ComplexNode complexNode = new ComplexNode(assocClass, points[0],
                points[1], origin.getSide(points[0]), ending.getSide(points[1]));

        // Add it to the diagram
        complexNodes.addFirst(complexNode);
        getChildren().add(complexNode);
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


        // Prepare the nodes
        Node origin = null;
        Node ending = null;

        // Get the nodes
        for(Node n: nodes) {
            if(n.getName().equals(relationship.getOrigin().getClassOf().getName())) origin = n;
            if(n.getName().equals(relationship.getEnd().getClassOf().getName())) ending = n;

            if(origin!=null && ending!=null) break;
        }

        // Get the ending points
        Point2D[] points = origin.fromTo(ending);
        char fromSide = origin.getSide(points[0]);
        char toSide = ending.getSide(points[1]);



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
