package gui.controllers;


import gui.components.FieldFormatter;
import gui.components.PopupHandlers;
import gui.controllers.validation.UmlValidation;
import gui.models.AttributeModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import umldiagram.graphical.status.EditingStatus;
import umldiagram.logical.Attribute;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.UmlClass;
import umldiagram.logical.enums.AttributeType;

import java.util.LinkedList;


/**
 * Controller class for the new class dialog.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class EditClassController extends GridPane {
    private UmlClass umlClass;

    @FXML private TextField className;
    @FXML private CheckBox isAbstract;

    @FXML private TableView attrTable;
    @FXML private TableColumn<AttributeModel, String> attrNameCol;
    @FXML private TableColumn<AttributeModel, String> attrTypeCol;
    @FXML private TableColumn<AttributeModel, Boolean> attrOrdCol;
    @FXML private TableColumn<AttributeModel, Boolean> attrUnqCol;

    private final ObservableList<AttributeModel> attrData  = FXCollections.observableArrayList();;





    /**
     * Method that initializes the frame with all the required information.
     */
    @FXML
    private void initialize() {
        // Get the instances
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);
        EditingStatus editStatus = EditingStatus.getInstance(false);

        // Get the class object
        umlClass = (!editStatus.isAssociationClass())
                ? umlDiagram.getClasses(editStatus.getClassName())
                : umlDiagram.getAssociationClasses(editStatus.getClassName()).getUmlClass();


        // Prepare the class field
        className.setTextFormatter(FieldFormatter.getMixedFormatter(30));
        className.setText(umlClass.getName());

        // Check if abstract
        isAbstract.setSelected(umlClass.isAbstract());


        // Prepare the table
        attrTable.setEditable(true);
        attrTable.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

        // Load the information
        for(Attribute a: umlClass.getAttributes()) {
            attrData.add(new AttributeModel(a.getName(), a.getType().getName(),
                    a.isOrdered(), a.isUnique()
            ));
        }
        attrTable.setItems(attrData);

        // Prepare the table columns
        attrNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        attrNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        attrNameCol.prefWidthProperty().bind(attrTable.widthProperty().divide(4));
        attrNameCol.setOnEditCommit(
                t -> { t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
        });

        attrTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        attrTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn( AttributeType.getNamesValues() ));
        attrTypeCol.prefWidthProperty().bind(attrTable.widthProperty().divide(4));
        attrTypeCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(t.getTablePosition().getRow())).setType(t.getNewValue())
        );

        attrOrdCol.setCellValueFactory(new PropertyValueFactory<>("ordered"));
        attrOrdCol.prefWidthProperty().bind(attrTable.widthProperty().divide(4));
        attrOrdCol.setCellFactory(p -> new CheckBoxTableCell<>());
        attrOrdCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(t.getTablePosition().getRow())).setOrdered(t.getNewValue())
        );

        attrUnqCol.setCellValueFactory(new PropertyValueFactory<>("unique"));
        attrUnqCol.prefWidthProperty().bind(attrTable.widthProperty().divide(4));
        attrUnqCol.setCellFactory(p -> new CheckBoxTableCell<>());
        attrUnqCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnique(t.getNewValue())
        );
    }





    /**
     * Method that adds a new row to the table with the attribtues.
     * It is the add button action listener.
     */
    @FXML
    private void addNewAttribute() {
        attrData.add(new AttributeModel());
    }


    /**
     * Method that removes all selected rows from the table.
     * It is the remove button action listener.
     */
    @FXML
    private void removeAttributes() {
        attrData.removeAll(attrTable.getSelectionModel().getSelectedItems());
    }


    /**
     * Method that closes the current frame.
     * It is the action listener of the cancel button.
     */
    @FXML
    private void cancelCreation() {
        ((Stage) attrTable.getScene().getWindow()).close();
    }




    /**
     * Method that validates the input and if corresponds,
     * creates a new UML class.
     */
    @FXML
    private void editClass() {


        // Clean information
        className.setStyle("-fx-border-color: transparent;");

        // Temporal flags
        boolean classOk = UmlValidation.validateEditedClass(className, umlClass.getName());
        String errors = UmlValidation.validateAttributes(attrData);

        // If there are errors
        if(!errors.isEmpty()) {
            PopupHandlers.showDialog("Incorrect Data",
                    "Incorrect data found on attributes.",
                    "The following errors have been found on the attributes:\n" + errors, Alert.AlertType.INFORMATION);
        }
        else if(classOk) {
            // Prepare the editing status
            EditingStatus eStatus = EditingStatus.getInstance(false);
            eStatus.setEditedClass(true);
            eStatus.setEditedClassName(className.getText());

            // Now edit the class
            umlClass.setAbstract(isAbstract.isSelected());
            umlClass.setName(className.getText());

            // Remove the attributes
            umlClass.setList(new LinkedList<>());
            for(AttributeModel am: attrData) {
                // Create attribute and add it
                umlClass.addAttribute(new Attribute(am.getName(), AttributeType.getAttributeName(am.getType()),
                        UMLDiagram.getInstance(false).newClassId(), am.isOrdered(), am.isUnique()));
            }

            // Close the window
            cancelCreation();
        }
    }


















}
