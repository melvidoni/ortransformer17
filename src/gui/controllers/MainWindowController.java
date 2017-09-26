package gui.controllers;


import gui.components.PopupHandlers;
import gui.components.ScriptTab;
import gui.models.RelationshipModel;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import gui.components.TreeBrowser;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import transformations.ort.TransformationStatus;
import transformations.ort.UMLtoXML;
import transformations.open.OpenDiagram;
import umldiagram.graphical.DrawingDiagram;
import umldiagram.graphical.status.DrawingStatus;
import umldiagram.graphical.status.EditingStatus;
import umldiagram.logical.AssociationClass;
import umldiagram.logical.Relationship;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.UmlClass;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;




/**
 * Controller class of the main interface, with all the logic for
 * the menu options and the toolbar options.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class MainWindowController {
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuTransform;
    @FXML private MenuItem menuPng;

    @FXML private ToolBar toolBar;
    @FXML private ToggleButton toggleNewClass;
    @FXML private ToggleButton toggleNewGen;
    @FXML private ToggleButton toggleNewAssoc;
    @FXML private ToggleButton toggleNewAgg;
    @FXML private ToggleButton toggleNewComp;
    @FXML private ToggleButton toggleNewAC;

    @FXML private TabPane tabPane;
    @FXML private ScriptTab scriptTab;

    @FXML private TreeBrowser treePane;
    @FXML private DrawingDiagram drawingCanvas;

    private ContextMenu classContextMenu = new ContextMenu();
    private ContextMenu acContextMenu = new ContextMenu();

    private DrawingStatus drawingStatus;
    private EditingStatus editingStatus;
    private TransformationStatus transfStatus;

    private Line drawingLine;







    /**
     * Method that initializes the window on construction.
     */
    @FXML
    private void initialize() {
        // Block menus until activated
        menuSave.setDisable(true);
        menuTransform.setDisable(true);
        menuPng.setDisable(true);

        // Hide elements
        toolBar.setManaged(false);
        tabPane.setManaged(false);
        tabPane.getTabs().remove(scriptTab);

        // Coordinates
        drawingLine = null;
        drawingStatus = DrawingStatus.getInstance(true);
        drawingStatus.bindProperties(toggleNewClass.selectedProperty(),
                toggleNewGen.selectedProperty(), toggleNewAssoc.selectedProperty(),
                toggleNewAgg.selectedProperty(), toggleNewComp.selectedProperty(),
                toggleNewAC.selectedProperty());

        // Prepare other statuses
        editingStatus = EditingStatus.getInstance(true);
        transfStatus = TransformationStatus.getInstance(true);


        // Prepare the context menu
        MenuItem editMenu = new MenuItem("Edit");
        editMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_edit.png")));
        editMenu.setOnAction((ActionEvent e) -> editClass());

        MenuItem delMenu = new MenuItem("Delete");
        delMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_delete.png")));
        delMenu.setOnAction((ActionEvent e) -> deleteClass());

        MenuItem relMenu = new MenuItem("Relationship");
        relMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_rel.png")));
        relMenu.setOnAction((ActionEvent e) -> listRelationships() );

        MenuItem editACMenu = new MenuItem("Edit");
        editACMenu.setGraphic(new ImageView(new Image("/gui/views/img/context_edit.png")));
        editACMenu.setOnAction((ActionEvent e) -> editClass());



        // Put the items on the context menu
        classContextMenu.getItems().addAll(editMenu, delMenu, relMenu);
        acContextMenu.getItems().add(editACMenu);
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
        menuPng.setDisable(false);

        // Show the hidden elements
        toolBar.setManaged(true);
        tabPane.setManaged(true);
        scriptTab.initialize();
        tabPane.getTabs().remove(scriptTab);

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

        // Clean the statuses
        editingStatus = EditingStatus.getInstance(true);
        transfStatus = TransformationStatus.getInstance(true);
    }






    /**
     * Action listener for the button regular clicks on the
     * drawing canvas side of the system.
     * @param me The mouse even that triggered the listener.
     */
    @FXML
    private void canvasClicked(MouseEvent me) {
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);
        classContextMenu.hide();

        try {
            if(me.getButton() == MouseButton.PRIMARY) {
                // If the class is activated
                if(toggleNewClass.isSelected()) {
                    // If the point is not occupied
                    if(!drawingCanvas.pointOccupiedByClass(me.getX(), me.getY())) {
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
                && drawingCanvas.pointOccupiedByClass(me.getX(), me.getY())
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

            // Update the size
            drawingCanvas.updateSize(Math.max(drawingLine.getStartX(), drawingLine.getEndX()),
                    Math.max(drawingLine.getStartY(), drawingLine.getEndY()));
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
            if(drawingLine!=null && drawingCanvas.pointOccupiedByClass(me.getX(), me.getY()) ){

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
                        PopupHandlers.showDialog("Incorrect Relationship",
                                "Incorrect endpoints for the generalization.",
                                "The following errors have been found:" + errors, Alert.AlertType.INFORMATION);
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
                        PopupHandlers.showDialog("Incorrect Relationship",
                                "Incorrect endpoints for the relationship.",
                                "The following errors have been found:" + errors, Alert.AlertType.INFORMATION);
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
                        PopupHandlers.showDialog("Incorrect Association Class",
                                "Incorrect endpoints for the association class.",
                                "The following errors have been found:" + errors, Alert.AlertType.INFORMATION);
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




    /**
     * Method to show the corresponding context menu, when requested by
     * the user, if it corresponds.
     * @param e The listened context menu event .
     */
    @FXML
    private void contextMenuRequested(ContextMenuEvent e) {
        // If we are not drawing
        if(!drawingStatus.isDrawing()) {
            // Hide the menus
            classContextMenu.hide();
            acContextMenu.hide();

            // Store the class name
            editingStatus.setClassName(drawingCanvas.getClassAt(e.getX(), e.getY()));

            // If the point is occupied by a regular class
            if(drawingCanvas.pointOccupiedByClass(e.getX(), e.getY())) {
                // Show the regular class menu
                classContextMenu.show(drawingCanvas, e.getScreenX(), e.getScreenY());
            }
            // If the point is occupied by an association class
            else if(drawingCanvas.pointOccupiedByAssocClass(e.getX(), e.getY())) {
                // Show the association class menu
                acContextMenu.show(drawingCanvas, e.getScreenX(), e.getScreenY());
                editingStatus.setAssociationClass(true);
            }
        }
    }





    /**
     * Method that deletes the class upon which the context menu
     * was called. It also deletes the related relationships.
     */
    private void deleteClass() {
        // Show a confirmation message
        boolean delete = PopupHandlers.showConfirmation(
                "Delete Class " + editingStatus.getClassName(),
                "Please confirm you want to delete the selected class.",
                "Be aware that besides the class, all relationships and " +
                        "association classes ending or originating on this class" +
                        " will also be deleted."
        );

        // If they requested delete
        if(delete) {
            // Get the diagram
            UMLDiagram diagram = UMLDiagram.getInstance(false);

            // Get the relationships
            LinkedList<String[]> relationshipsOf = diagram.getRelationshipsOf(editingStatus.getClassName());

            // Now, delete the class
            diagram.deleteClass(editingStatus.getClassName());
            treePane.update(diagram);

            // Now delete the visuals
            drawingCanvas.deleteClass(editingStatus.getClassName(), relationshipsOf);
        }
        // In any case, clean the name
        editingStatus.setClassName("");
        editingStatus.setAssociationClass(false);
    }




    /**
     * Method that edits a class or association class, in which the
     * context menu was called. It also updates the tree and drawing.
     */
    private void editClass() {
        try {
            // First, lets call the interface
            PopupHandlers.showPopup("/gui/views/EditClassDialog.fxml",
                    "Edit " + ( editingStatus.isAssociationClass() ? "Association " : "" ) + "Class",
                    drawingCanvas.getScene());

            // Now if there is something to edit
            if(editingStatus.isEditedClass()) {
                // Get the diagram
                UMLDiagram umlDiagram = UMLDiagram.getInstance(false);

                // Update the information
                treePane.update(umlDiagram);
                drawingCanvas.editClass();

                // Clean the data
                editingStatus = EditingStatus.getInstance(true);
            }
        }
        catch(IOException ioe) {
            // TODO COMPLETE EXCEPTION
            ioe.printStackTrace();
        }

    }




    /**
     * Action listener to handle the action on listing the relationships
     * associated to a particular class.
     */
    private void listRelationships() {
        try {
            // First, lets call the interface
            PopupHandlers.showPopup("/gui/views/ListRelationshipsDialog.fxml",
                    "Relationships of Class " + StringUtils.capitalize(editingStatus.getClassName()),
                    drawingCanvas.getScene());

            // Get the diagram
            UMLDiagram diagram = UMLDiagram.getInstance(false);

            // Check if there are deleted relationships
            if(editingStatus.hasDeletedRels()) {
                // Go through all the relationships
                for(RelationshipModel rm : editingStatus.getDelRelationships()) {
                    // If this is an association class, remove it
                    if(rm.getType().equals("Association Class")) {
                        // Update the diagram and canvas
                        diagram.deleteAssociationClass(rm.getName());
                        drawingCanvas.deleteAssociationClass(rm.getName());
                    }
                    // Otherwise, it is a normal relationship
                    else {
                        // Update the diagram and canvas
                        diagram.deleteRelationship(rm.getName());
                        drawingCanvas.deleteRelationship(rm.getName());
                    }
                }
                // Update the pane
                treePane.update(diagram);
            }

            // If we need to open the editing
            else if(editingStatus.needsOpenEditing()) {
                // Open the new dialog
                PopupHandlers.showPopup("/gui/views/EditRelationshipDialog.fxml",
                        "Editing Relationship " + StringUtils.capitalize(editingStatus.getRelName()),
                        drawingCanvas.getScene());

                // If something was edited
                if(editingStatus.hasEditedRel()) {
                    // Update logical part
                    treePane.update(diagram);

                    // Update the diagram
                    drawingCanvas.editRelationship();
                }
            }


            // Clean the status
            editingStatus = EditingStatus.getInstance(true);
        }
        catch(IOException ioe) {
            // TODO COMPLETE EXCEPTION
            ioe.printStackTrace();
        }
    }






    /**
     * Method to export the generated diagram as a PNG image,
     * on a location slected by the user.
     */
    @FXML
    private void exportToPNGMenu() {
        try {
            // Get the selected file
            File file = PopupHandlers.showSaveFileChooser("Export Diagram as Image",
                    "PNG Files", "*.png", (Stage) drawingCanvas.getScene().getWindow());

            // If a file was selected
            if (file != null) {
                // Get the snapshop and write the image
                ImageIO.write(SwingFXUtils.fromFXImage(
                        drawingCanvas.snapshot(new SnapshotParameters(), null),
                        null), "png", file);
            }
        }
        catch (IOException ex) {
            // TODO COMPLETE THIS MESSAGE
            ex.printStackTrace();
        }
    }





    /**
     * Method that saves te current diagram on the file selected
     * by the user. It shows a filechooser to pick the file up.
     */
    @FXML
    private void saveDiagramMenu() {
        try {
            // Get the selected file
            File file = PopupHandlers.showSaveFileChooser("Save Diagram",
                    "ORT Files", "*.ort", (Stage) drawingCanvas.getScene().getWindow());

            // If a file was selected, save the diagram
            if(file != null)
                UMLtoXML.transformToXML(file, false, drawingCanvas.getNodes());
        }
        catch (Exception ex) {
            // TODO COMPLETE THIS MESSAGE
            ex.printStackTrace();
        }
    }




    /**
     * Method that opens a diagram from an *.ort file selected by
     * the user, using a FileChooser to pick the file up.
     */
    @FXML
    private void openDiagramMenu() {
       try {
            // Get the selected file
            File file = PopupHandlers.showOpenFileChooser("Open Diagram",
                    "ORT Files", "*.ort", (Stage) drawingCanvas.getScene().getWindow());

            // If a file was selected, save the diagram
            if(file != null) {
                // Create a new model
                newModelMenu();

                // Now store get the information
                OpenDiagram.open(file, drawingCanvas);

                // Resize if it goes outside
                drawingCanvas.updateSize(drawingCanvas.getBoundsInLocal().getWidth(),
                        drawingCanvas.getBoundsInLocal().getHeight());

                // Update the tree
                treePane.update(UMLDiagram.getInstance(false));
            }
        }
        catch (Exception ex) {
            // TODO COMPLETE THIS MESSAGE
            ex.printStackTrace();
        }
    }





    /**
     * Method that exits the application without asking for
     * any type of input.
     */
    @FXML
    private void exitMenu() {
        // Show confirmation
        Platform.exit();
    }




    @FXML
    private void transformDiagram() {
        try {
            // Start showing the transformation information
            PopupHandlers.showPopup(
                    "/gui/views/TransformDialog.fxml",
                    "Configure Transformation", drawingCanvas.getScene()
            );

            // If the transformation was configured
            if(transfStatus.needsTransformation()) {
                // Show the progress
                PopupHandlers.showPopup("/gui/views/ProgressDialog.fxml",
                        "Transformation in Progress", drawingCanvas.getScene());

                // If it was transformed
                if(transfStatus.wasTransformed()) {
                    // Set the information on the script tab and show it
                    scriptTab.showGeneratedScripts();
                    tabPane.getTabs().add(scriptTab);
                }
            }
        }
        catch (IOException e) {
            // TODO CORRECT THIS CATCH
            e.printStackTrace();
        }
    }




}