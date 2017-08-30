package gui.controllers;


import gui.components.DrawingCanvas;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import gui.components.TreeBrowser;


/**
 * Controller class of the main interface, with all the logic for
 * the menu options and the toolbar options.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class MainWindowController {
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuTransform;

    @FXML private ToolBar toolBar;
    @FXML private SplitPane splitPane;

    @FXML private TreeBrowser treePane;
    @FXML private DrawingCanvas drawingCanvas;




    /**
     * Method that initializes the window on construction.
     */
    @FXML
    private void initialize() {
        // Block menus until activated
        menuSave.setDisable(true);
        menuTransform.setDisable(true);

        // Hide elements
        toolBar.setManaged(false);
        splitPane.setManaged(false);

        // Start the elements
        drawingCanvas = new DrawingCanvas();
    }




    /**
     * Shows a dialog message about the creators of the tool.
     */
    @FXML
    private void showAboutMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set text information
        alert.setTitle("About OR-Transformer");
        alert.setHeaderText("Object-Relational Transforming Tool");
        alert.setContentText("This tool was created at the Institute of Design and Development" +
                " (INGAR CONICET-UTN) by Dr. Aldo Vecchietti, Dr. Fernanda Golobysky, and Dr. Melina Vidoni.");

        // Change the image
        Image image = new Image("/gui/views/img/icon_about.png");
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        // Show the dialog
        alert.showAndWait();
    }




    /**
     * Action listener for the menu item of new model.
     */
    @FXML
    private void newModelMenu() {
        // Enable the menus
        menuSave.setDisable(false);
        menuTransform.setDisable(false);

        // Show the hidden elements
        toolBar.setManaged(true);
        splitPane.setManaged(true);

        // Create a new model
        treePane.newModel();
        drawingCanvas = new DrawingCanvas();
    }












    @FXML
    private void newClassTool() {
        drawingCanvas.drawNewClass();
    }

}