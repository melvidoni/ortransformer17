package gui.controllers;


import gui.components.FieldFormatter;
import gui.controllers.validation.UmlValidation;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umldiagram.graphical.status.EditingStatus;
import umldiagram.logical.Relationship;
import umldiagram.logical.RelationshipEndpoint;
import umldiagram.logical.UMLDiagram;
import umldiagram.logical.enums.RelationshipType;


public class EditRelationshipController extends ARelationshipController {
    private Relationship relationship;

    @FXML private TextField nameField;

    @FXML private Label endClassName;
    @FXML private TextField endRole;
    @FXML private TextField endMinCard;
    @FXML private TextField endMaxCard;
    @FXML private CheckBox endBrowsable;
    @FXML private CheckBox endUnique;
    @FXML private CheckBox endOrdered;

    @FXML private Label originClassName;
    @FXML private TextField originRole;
    @FXML private TextField originMinCard;
    @FXML private TextField originMaxCard;
    @FXML private CheckBox originBrowsable;
    @FXML private CheckBox originUnique;
    @FXML private CheckBox originOrdered;




    /**
     * Method to initialize the interface by adding the
     * corresponding elements and controllers.
     */
    @FXML
    private void initialize() {
        // Get the status and diagram
        EditingStatus eStatus = EditingStatus.getInstance(false);
        UMLDiagram diagram = UMLDiagram.getInstance(false);

        // Find the relationship
        relationship = (!eStatus.isAssociationClass())
                ? diagram.getRelationship(eStatus.getRelName())
                : diagram.getRelAssociationClass(eStatus.getRelName()).getRelationship();



        // Put controllers on the name fields
        nameField.setTextFormatter(FieldFormatter.getMixedFormatter(20));
        endRole.setTextFormatter(FieldFormatter.getMixedFormatter(15));
        originRole.setTextFormatter(FieldFormatter.getMixedFormatter(15));

        // Put controllers on the cardinalities
        endMinCard.setTextFormatter(FieldFormatter.getNumberFormatter(2));
        originMinCard.setTextFormatter(FieldFormatter.getNumberFormatter(2));
        endMaxCard.setTextFormatter(FieldFormatter.getStarFormatter(2));
        originMaxCard.setTextFormatter(FieldFormatter.getStarFormatter(2));



        // Now set the default values
        type = relationship.getType();
        originClass = relationship.getOrigin().getClass().getName();
        endClass = relationship.getEnd().getClass().getName();


        // Set the values on the fields
        nameField.setText(relationship.getName());

        endClassName.setText(relationship.getEnd().getClassOf().getName());
        endRole.setText(relationship.getEnd().getName());
        String[] endCard = relationship.getEnd().getCardinality().split("\\.\\.");
        endMinCard.setText(endCard[0]);
        endMaxCard.setText(endCard[1]);
        endBrowsable.setSelected(relationship.getEnd().isBrowsable());
        endUnique.setSelected(relationship.getEnd().isUnique());
        endOrdered.setSelected(relationship.getEnd().isOrdered());

        originClassName.setText(relationship.getOrigin().getClassOf().getName());
        originRole.setText(relationship.getOrigin().getName());
        String[] originCard = relationship.getOrigin().getCardinality().split("\\.\\.");
        originMinCard.setText(originCard[0]);
        originMaxCard.setText(originCard[1]);
        originBrowsable.setSelected(relationship.getOrigin().isBrowsable());
        originUnique.setSelected(relationship.getOrigin().isUnique());
        originOrdered.setSelected(relationship.getOrigin().isOrdered());
    }










    /**
     * Method that closes the current frame.
     * It is the action listener of the cancel button.
     */
    @FXML
    private void cancelEdit() {
        ((Stage) nameField.getScene().getWindow()).close();
    }






    /**
     * Validates if the input data for a relationship is correct and,
     * in that case, edits the logical relationship. Otherwise, it shows
     * error messages.
     */
    @FXML
    private void editRelationship() {
        // Get the diagram instance
        UMLDiagram diagram = UMLDiagram.getInstance(false);
        EditingStatus eStatus = EditingStatus.getInstance(false);

        // Clean the information
        cleanFields();



        // Validate the relationship
        boolean relationshipOk = UmlValidation.validateEditedRelationship(nameField,
                eStatus.getRelName());
        boolean endpointsOk = UmlValidation.validateEndpoints(endRole, originRole,
                endMinCard, endMaxCard, originMinCard, originMaxCard);


        if(relationshipOk && endpointsOk) {
            // Edit the relationship
            relationship.setName(nameField.getText());

            // Edit the origin
            relationship.getOrigin().setName(originRole.getText());
            relationship.getOrigin().setBrowsable(originBrowsable.isSelected());
            relationship.getOrigin().setUnique(originUnique.isSelected());
            relationship.getOrigin().setOrdered(originOrdered.isSelected());
            relationship.getOrigin().setCardinality(
                    originMinCard.getText() + ".." + originMaxCard.getText()
            );

            // Edit the end
            relationship.getEnd().setName(endRole.getText());
            relationship.getEnd().setBrowsable(endBrowsable.isSelected());
            relationship.getEnd().setUnique(endUnique.isSelected());
            relationship.getEnd().setOrdered(endOrdered.isSelected());
            relationship.getEnd().setCardinality(
                    endMinCard.getText() + ".." + endMaxCard.getText()
            );

            // Update the information
            eStatus.setEditedRelName(nameField.getText());
            eStatus.setEditedRel(true);

            // And cancel
            cancelEdit();
        }

    }
















    /**
     * Method that cleans the fields by removing the borders and the tooltips.
     */
    private void cleanFields() {
        // Change the borders on the methods
        nameField.setStyle("-fx-border-color: transparent;");
        endRole.setStyle("-fx-border-color: transparent;");
        endMinCard.setStyle("-fx-border-color: transparent;");
        endMaxCard.setStyle("-fx-border-color: transparent;");
        originRole.setStyle("-fx-border-color: transparent;");
        originMinCard.setStyle("-fx-border-color: transparent;");
        originMaxCard.setStyle("-fx-border-color: transparent;");

        // Change the tooltips
        nameField.setTooltip(null);
        endRole.setTooltip(null);
        endMinCard.setTooltip(null);
        endMaxCard.setTooltip(null);
        originRole.setTooltip(null);
        originMinCard.setTooltip(null);
        originMaxCard.setTooltip(null);
    }


}

