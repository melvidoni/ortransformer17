package gui.controllers;


import gui.components.FieldFormatter;
import gui.components.PopupHandlers;
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
import umldiagram.logical.Attribute;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.UmlClass;
import umldiagram.logical.enums.AttributeType;
import java.util.stream.Collectors;




/**
 * Controller class for the new class dialog.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class NewClassController extends GridPane {
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
        // Prepare the class field
        className.setTextFormatter(FieldFormatter.getMixedFormatter(30));

        // Prepare the table
        attrTable.setEditable(true);
        attrTable.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
        attrTable.setItems(attrData);

        // Prepare the table columns
        attrNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        attrNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        attrNameCol.setOnEditCommit(
                t -> { t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
        });

        attrTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        attrTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn( AttributeType.getNamesValues() ));
        attrTypeCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(t.getTablePosition().getRow())).setType(t.getNewValue())
        );

        attrOrdCol.setCellValueFactory(new PropertyValueFactory<>("ordered"));
        attrOrdCol.setCellFactory(p -> new CheckBoxTableCell<>());

        attrUnqCol.setCellValueFactory(new PropertyValueFactory<>("unique"));
        attrUnqCol.setCellFactory(p -> new CheckBoxTableCell<>());
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
    private void createNewClass() {
        // Clean information
        className.setStyle("-fx-border-color: transparent;");

        // Temporal flags
        boolean classOk = false;
        boolean attrNameOk = true;
        boolean attrTypeOk = true;
        String errors = "";

        // If the class name is empty
        if(className.getText().isEmpty()) {
            // Add a border and tooltip
            className.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            className.setTooltip(new Tooltip("The class name cannot be empty"));
        }
        // If the class name is repeated
        else if(UMLDiagram.getInstance(false).getClassesNames().contains(className.getText().toUpperCase())) {
            // Add a border and tooltip
            className.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            className.setTooltip(new Tooltip("The class name is already in use."));
        }
        else classOk = true;

        // If there are duplicate attributes
        if(attrData.stream().collect(Collectors.groupingBy(AttributeModel::getName, Collectors.counting()))
                .entrySet().stream().anyMatch(e -> e.getValue() > 1)) {
            // Add an error message
            if(!errors.isEmpty()) errors += "\n";
            errors += "> Attribute names must be unique.";
        }
        for(AttributeModel am: attrData) {
            // Check for empty attribute names
            if(am.getName().isEmpty() && attrNameOk ) {
                // Add an error message
                if(!errors.isEmpty()) errors += "\n";
                errors += "> Attribute names cannot be empty.";

                // Change the flag
                attrNameOk = false;
            }

            // Check for empty attribute types
            if(am.getType().isEmpty() && attrTypeOk) {
                // Add an error message
                if(!errors.isEmpty()) errors += "\n";
                errors += "> Attribute types cannot be empty.";

                // Change the flag
                attrTypeOk = false;
            }

        }

        // If there are errors
        if(!errors.isEmpty()) {
            PopupHandlers.showWarningDialog("Incorrect Data",
                    "Incorrect data found on attributes.",
                    "The following errors have been found on the attributes:\n" + errors);
        }
        else if(classOk) {
            // Create the base class
            UmlClass c = new UmlClass(String.valueOf(UMLDiagram.getId()),
                    className.getText(), isAbstract.isSelected());

            // Add the attributes
            for(AttributeModel am: attrData) {
                // Create attribute and add it
                c.addAttribute(new Attribute(am.getName(), AttributeType.getAttribute(am.getType()),
                        UMLDiagram.getId(), am.isOrdered(), am.isUnique()));
            }

            // Add the class to the diagram
            UMLDiagram.getInstance(false).addClass(c);

            // Close the window
            cancelCreation();
        }
    }


















}
