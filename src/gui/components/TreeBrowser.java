package gui.components;


import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import umldiagram.UMLDiagram;


public class TreeBrowser extends TreeView {
    private UMLDiagram umlDiagram;
    private TreeItem<String> root;



    /**
     * Creates a new model on the browser.
     */
    public void newModel() {
        // Create a new diagram
        umlDiagram = new UMLDiagram("Diagram");

        // Update with a new diagram
        update();
    }




    private void update() {
        // Clear the diagram
        setRoot(null);

        // Create the root
         root = new TreeItem<>(umlDiagram.getName(),
                 new ImageView(new Image("/gui/views/img/tree_root.png")));
         root.setExpanded(true);




         // Add the new root to the diagram
         setRoot(root);
    }



}
