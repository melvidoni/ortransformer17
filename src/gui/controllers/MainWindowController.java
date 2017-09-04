package gui.controllers;


import gui.components.PopupHandlers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import gui.components.TreeBrowser;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import umldiagram.graphical.DrawingDiagram;
import umldiagram.graphical.DrawingStatus;
import umldiagram.logical.AssociationClass;
import umldiagram.logical.Relationship;
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
    @FXML private ToggleButton toggleNewGen;
    @FXML private ToggleButton toggleNewAssoc;
    @FXML private ToggleButton toggleNewAgg;
    @FXML private ToggleButton toggleNewComp;
    @FXML private ToggleButton toggleNewAC;

    @FXML private SplitPane splitPane;
    @FXML private TreeBrowser treePane;
    @FXML private DrawingDiagram drawingCanvas;


    private ContextMenu contextMenu = new ContextMenu();


    private DrawingStatus drawingStatus;
    private Line drawingLine;





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

        // Coordinates
        drawingLine = null;
        drawingStatus = DrawingStatus.getInstance(true);
        drawingStatus.bindProperties(toggleNewClass.selectedProperty(),
                toggleNewGen.selectedProperty(), toggleNewAssoc.selectedProperty(),
                toggleNewAgg.selectedProperty(), toggleNewComp.selectedProperty(),
                toggleNewAC.selectedProperty());

        // Prepare the context menu
        MenuItem editMenu = new MenuItem("Edit");
        editMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_edit.png")));
        MenuItem delMenu = new MenuItem("Delete");
        delMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_delete.png")));
        MenuItem relMenu = new MenuItem("Relationship");
        relMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_rel.png")));

        // Put the items on the context menu
        contextMenu.getItems().addAll(editMenu, delMenu, relMenu);
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

        // Clean coordinates
        drawingLine = null;
        drawingStatus = DrawingStatus.getInstance(true);
        drawingStatus.bindProperties(toggleNewClass.selectedProperty(),
                toggleNewGen.selectedProperty(), toggleNewAssoc.selectedProperty(),
                toggleNewAgg.selectedProperty(), toggleNewComp.selectedProperty(),
                toggleNewAC.selectedProperty());
    }




    /**
     * Action listener for the button regular clicks on the
     * drawing canvas side of the system.
     * @param me The mouse even that triggered the listener.
     */
    @FXML
    private void canvasClicked(MouseEvent me) {
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);
        contextMenu.hide();

        try {
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




    /**
     * Method that starts drawing a line when the canvas is
     * pressed and there is a relationshio tool toggled.
     * @param me The mouse event that triggered the listener.
     */
    @FXML
    private void canvasPressed(MouseEvent me) {
        // If this is a primary button
        if(me.isPrimaryButtonDown() && drawingLine==null
                && drawingCanvas.pointOccupied(me.getX(), me.getY())
                && drawingStatus.isDrawingLine() ) {

            // Create a new line
            drawingLine = new Line(me.getX(), me.getY(), me.getX(), me.getY());
            drawingCanvas.getChildren().add(drawingLine);
        }
    }


    /**
     * Method that draws the line while the user is dragging the
     * mouse around, trying to find the endpoint.
     * @param me The mouse event that triggered the listener.
     */
    @FXML
    private void canvasDragged(MouseEvent me) {
        // If it is primary button and a line is being drawn
        if(me.isPrimaryButtonDown() && drawingLine!=null && drawingStatus.isDrawingLine() ) {
            // Change the ending line
            drawingLine.setEndX(me.getX());
            drawingLine.setEndY(me.getY());

            // Calculate the positions
            double mx = Math.max(drawingLine.getStartX(), drawingLine.getEndX());
            double my = Math.max(drawingLine.getStartY(), drawingLine.getEndY());


            // Resize if it goes outside
            if (mx > drawingCanvas.getMinWidth())  drawingCanvas.setMinWidth(mx);
            if (my > drawingCanvas.getMinHeight()) drawingCanvas.setMinHeight(my);
        }
    }




    /**
     * Method that evaluates whether to create a relationship or not,
     * depending on where the mouse ended up its draggin. It calls the
     * corresponding constructors and deletes the temporary line. If
     * corresponds, draws the new line.
     * @param me The mouse event that triggered the listener.
     */
    @FXML
    private void canvasReleased(MouseEvent me) {
        try {

            // Check all the variables
            if(drawingLine!=null && drawingCanvas.pointOccupied(me.getX(), me.getY()) ){

                // Get the diagram
                UMLDiagram umlDiagram = UMLDiagram.getInstance(false);

                // Get both classes
                String originClass = drawingCanvas
                        .getClassAt(drawingLine.getStartX(), drawingLine.getStartY());
                String endClass = drawingCanvas.getClassAt(me.getX(), me.getY());

                /*
                 GENERALIZATION
                 */
                // If this is a generalization
                if(toggleNewGen.isSelected()) {
                    // Validate the generalization
                    String errors = umlDiagram.validGen(originClass, endClass);

                    // If there are errors
                    if(!errors.isEmpty()) {
                        // Show a message
                        PopupHandlers.showWarningDialog("Incorrect Relationship",
                                "Incorrect endpoints for the generalization.",
                                "The following errors have been found:" + errors);
                    }
                    // If there are no errors
                    else {
                        // Create logical gen
                        Relationship newGen = new Relationship( String.valueOf(umlDiagram.newRelId()) );
                        newGen.newGeneralization(umlDiagram.getClasses(originClass),
                                umlDiagram.getClasses(endClass));
                        umlDiagram.addRelationship(newGen);
                        treePane.update(umlDiagram);

                        // Draw the line
                        drawingCanvas.addNewRel(newGen);
                    }

                    // Clean the status
                    toggleNewGen.setSelected(false);
                }

                /*
                ASSOCIATION, AGGREGATION OR COMPOSITION
                */
                if(toggleNewAssoc.isSelected() || toggleNewAgg.isSelected()
                        || toggleNewComp.isSelected()) {
                    // Validate the association
                    String errors = umlDiagram.validRelationship(originClass, endClass);

                    // If there are errors
                    if(!errors.isEmpty()) {
                        // Show a message
                        PopupHandlers.showWarningDialog("Incorrect Relationship",
                                "Incorrect endpoints for the relationship.",
                                "The following errors have been found:" + errors);
                    }
                    else {
                        // Set the classes on the status
                        drawingStatus.setClasses(originClass, endClass);

                        // Call the new window, depending on the type
                        PopupHandlers.showPopup("/gui/views/NewRelationshipDialog.fxml",
                                "New " + drawingStatus.getRelType().getName() + " Relationship",
                                drawingCanvas.getScene());

                        // If there is a new association
                        Relationship relationship = umlDiagram.hasUndrawnRelationship();
                        if(relationship != null) {
                            // Update tree
                            treePane.update(umlDiagram);

                            // Draw relationship
                            drawingCanvas.addNewRel(relationship);
                            umlDiagram.setUndrawnRelationship(false);
                        }


                        // Change the status
                        drawingStatus.setClasses("", "");
                    }

                    // Clean the status
                    toggleNewAssoc.setSelected(false);
                    toggleNewAgg.setSelected(false);
                    toggleNewComp.setSelected(false);
                }

                /*
                ASSOCIATION CLASS
                 */
                if(toggleNewAC.isSelected()) {
                    // Validate the errors
                    String errors = umlDiagram.validAC(originClass, endClass);

                    // If there are errors
                    if(!errors.isEmpty()) {
                        // Show a message
                        PopupHandlers.showWarningDialog("Incorrect Association Class",
                                "Incorrect endpoints for the association class.",
                                "The following errors have been found:" + errors);
                    }
                    else {
                        // Set the classes on the status
                        drawingStatus.setClasses(originClass, endClass);

                        // Call the new window, depending on the type
                        PopupHandlers.showPopup("/gui/views/NewAssocClassDialog.fxml",
                                "New Association Class Relationship",
                                drawingCanvas.getScene());

                        // If there is a new association class
                        AssociationClass assocClass = umlDiagram.hasUndrawnAssocClass();

                        if(assocClass != null) {
                            // Update tree
                            treePane.update(umlDiagram);

                            // Draw relationship
                            drawingCanvas.addNewAssociationClass(assocClass);
                            umlDiagram.setUndrawnAssocClass(false);
                        }


                        // Change the status
                        drawingStatus.setClasses("", "");
                        toggleNewAC.setSelected(false);
                    }
                }
            }

            // Remove the line
            drawingCanvas.getChildren().remove(drawingLine);
            drawingLine = null;

        }
        catch (IOException e) {
            // TODO COMPLETE EXCEPTION
            e.printStackTrace();
        }
    }











    @FXML
    private void contextMenuRequested(ContextMenuEvent e) {
        // If the point is occupied
        if(drawingCanvas.pointOccupied(e.getX(), e.getY()) && !drawingStatus.isDrawing()) {
            // Hide
            contextMenu.hide();
            contextMenu.show(drawingCanvas, e.getScreenX(), e.getScreenY());
        }
    }








}