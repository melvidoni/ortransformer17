package gui.components;


import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import umldiagram.logical.*;


/**
 * This class shows a tree view of the elements of the
 * UML diagram, and also contains a reference to the
 * logical diagram object representation,
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class TreeBrowser extends TreeView {
    private TreeItem<String> root;





    /**
     * Creates a new model on the browser.
     */
    public void newModel() {
        // Create a new diagram and update
        update(UMLDiagram.getInstance(true));
    }




    /**
     * Method that updates the tree, in order to show
     * the new information of the uml diagram.
     * @param umlDiagram Reference to the UML diagram.
     */
    private void update(UMLDiagram umlDiagram) {
        // Clear the diagram
        setRoot(null);

        // Create the root
        root = new TreeItem<>(umlDiagram.getName(),
                 new ImageView(new Image("/gui/views/img/tree_root.png")));
        root.setExpanded(true);


        // Go through all the classes
        for(UmlClass c: umlDiagram.getClasses()) {
            // Create an item
            TreeItem<String> classNode = new TreeItem<>(c.getName(),
                    new ImageView(new Image("/gui/views/img/tree_class.png")));

            // Add the attributes
            for(Attribute a: c.getAttributes()) {
                classNode.getChildren().add(new TreeItem<>(a.toString(),
                        new ImageView(new Image("/gui/views/img/tree_attribute.png"))) );
            }

            // Now add the relationships
            for(Relationship r: umlDiagram.getRelationships()) {
                // If this class is the origin
                if(r.getOrigin().getClassOf().getName().equals(c.getName())) {
                    // Then we add it to the class
                    classNode.getChildren().add(new TreeItem<>(r.toString(),
                            new ImageView(new Image("/gui/views/img/tree_rel.png"))) );
                }
            }

            // Now add the class node
            root.getChildren().add(classNode);
        }


        // Now check association classes
        for(AssociationClass ac: umlDiagram.getAssociationClasses()) {
            // Create the node
            TreeItem<String> acNode = new TreeItem<>(ac.getUmlClass().getName(),
                    new ImageView(new Image("/gui/view/img/tree_assocclass.png")));

            // Add the attributes
            for(Attribute a: ac.getUmlClass().getAttributes()) {
                acNode.getChildren().add(new TreeItem<>(a.getName(),
                        new ImageView(new Image("/gui/views/img/tree_attribute.png"))) );
            }

            // Now the relationship
            acNode.getChildren().add(new TreeItem<>(ac.getRelationship().getName(),
                    new ImageView(new Image("/gui/views/img/tree_rel.png"))) );

            // Add to the root
            root.getChildren().add(acNode);
        }



        // Add the new root to the diagram
        setRoot(root);

         // Basic configurations
        setEditable(false);
    }



}
