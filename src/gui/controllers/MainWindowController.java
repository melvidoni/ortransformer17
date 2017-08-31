package gui.controllers;


import app.Main;
import gui.components.PopupHandlers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import gui.components.TreeBrowser;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umldiagram.graphical.DrawingDiagram;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.UmlClass;

import java.io.IOException;


/**
 * Controller class of the main interface, with all the logic for
 * the menu options and the toolbar options.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class MainWindowController {
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuTransform;

    @FXML private ToolBar toolBar;
    @FXML private ToggleButton toggleNewClass;



    @FXML private SplitPane splitPane;
    @FXML private TreeBrowser treePane;
    @FXML private DrawingDiagram drawingCanvas;






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
        drawingCanvas.newModel();
    }




    /**
     * Action listener for the button regular clicks on the
     * drawing canvas side of the system.
     * @param me The mouse even that triggered the listener.
     */
    @FXML
    private void canvasClicked(MouseEvent me) {
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);

        try {
            /*
            LEFT CLICK
            */
            if(me.getButton() == MouseButton.PRIMARY) {


                // If the class is activated
                if(toggleNewClass.isSelected()) {

                    // If the point is not occupied
                    if(!drawingCanvas.pointOccupied(me.getX(), me.getY())) {
                        // Show the window
                        PopupHandlers.showPopup("/gui/views/NewClassDialog.fxml",
                                "Create New Class", drawingCanvas.getScene());



                        // If a class was created
                        UmlClass c = umlDiagram.hasUndrawnClass();
                        if(c != null) {
                            // Update the tree
                            treePane.update(umlDiagram);

                            // Now draw the class
                            drawingCanvas.addNewNode(me.getX(), me.getY(), c);
                            umlDiagram.setUndrawnClass(false);
                        }

                        // Deselect after creating
                        toggleNewClass.setSelected(false);
                    }


                }

            }
        }
        catch(IOException ioe) {
            // TODO REMOVE STACK TRACE
            ioe.printStackTrace();
        }
    }









}