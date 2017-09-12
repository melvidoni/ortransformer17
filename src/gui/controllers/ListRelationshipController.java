package gui.controllers;


import gui.components.PopupHandlers;
import gui.models.RelationshipModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import umldiagram.graphical.status.EditingStatus;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.enums.RelationshipType;
import java.io.IOException;





/**
 * Controller for the interface that lists relationships and
 * allows operations on them.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class ListRelationshipController {
    private final ObservableList<RelationshipModel> relsData = FXCollections.observableArrayList();

    @FXML private TableView relsTable;
    @FXML private TableColumn<RelationshipModel, String> colRelName;
    @FXML private TableColumn<RelationshipModel, String> colRelType;




    /**
     * Method that initializes the interface with the
     * basic data of the relationships.
     */
    @FXML
    private void initialize() {
        // Configure the table
        relsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        relsTable.setEditable(false);

        // Now load the data
        for(String[] r : UMLDiagram.getInstance(false).getRelationshipsOf(
                EditingStatus.getInstance(false).getClassName())) {
            // Add the information
            relsData.add(new RelationshipModel(r[0], r[1]));
        }
        relsTable.setItems(relsData);

        // Set the columns
        colRelName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRelName.prefWidthProperty().bind(relsTable.widthProperty().divide(2));
        colRelType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRelType.prefWidthProperty().bind(relsTable.widthProperty().divide(2));
    }




    /**
     * Method that closes the current frame.
     * It is the action listener of the cancel button.
     */
    @FXML
    private void cancelListing() {
        ((Stage) relsTable.getScene().getWindow()).close();
    }





    /**
     * Method that stores all the relationships to be deleted
     * and closes the frame.
     */
    @FXML
    private void deleteRelationships() {
        // If nothing is selected
        if(relsTable.getSelectionModel().getSelectedIndices().isEmpty()) {
            PopupHandlers.showWarningDialog("Incorrect Selection",
                    "Incorrect selection to remove relationships.",
                    "At least one relationship must be selected to be deleted.");
        }
        else {
            // Ask for confirmation
            boolean delete = PopupHandlers.showConfirmation("Deleting Relationships",
                    "The selected relationships will be deleted.",
                    "Please confirm that you want to proceed. This action cannot be undone.");

            // If we need to delete
            if(delete) {
                // Update the status
                EditingStatus eStatus = EditingStatus.getInstance(false);
                eStatus.setDeletedRels(true);
                eStatus.setDelRelationships(relsTable.getSelectionModel().getSelectedItems());

                // Close the window
                cancelListing();
            }
            // If not, do nothing
        }
    }






    /**
     * Method that validates the relationship selection, and opens the
     * new frame with the information to edit a relationship.
     */
    @FXML
    private void editRelationship() {
        // Get the selection count
        int selectionCount = relsTable.getSelectionModel().getSelectedIndices().size();

        // Check for selection errors
        String errors = "";
        // If there is no selection, put an error
        if(selectionCount == 0)
            errors = "One relationship must be selected to be edited.";
            // If there is more than one selection, show another message
        else if(selectionCount > 1)
            errors = "Only one relationship can be edited at a time";
            // And if there is a generalization, show another message
        else if(((RelationshipModel) relsTable.getSelectionModel().getSelectedItem())
                .getType().equals(RelationshipType.GENERALIZATION.getName())) {
            errors = "Generalization relationships cannot be edited";
        }

        // If everything is OK...
        if(errors.isEmpty()) {
            // Get the item
            RelationshipModel rm = (RelationshipModel) relsTable.getSelectionModel().getSelectedItem();

            // Get the status and save the info
            EditingStatus eStatus = EditingStatus.getInstance(false);
            eStatus.setRelName(rm.getName());
            eStatus.setAssociationClass(rm.getType().equals("Association Class"));
            eStatus.setOpenEditing(true);

            // Close the frame
            cancelListing();
        }
        // If there are errors...
        else {
            // Show a message
            PopupHandlers.showWarningDialog("Incorrect Selection",
                    "Incorrect selection to edit a relationship.", errors);
        }
    }




}
