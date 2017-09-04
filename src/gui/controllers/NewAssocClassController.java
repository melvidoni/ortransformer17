package gui.controllers;


import gui.components.FieldFormatter;
import gui.controllers.validation.UmlValidation;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umldiagram.logical.*;
import umldiagram.logical.enums.RelationshipType;




public class NewAssocClassController extends ARelationshipController {
    @FXML private TextField classNameField;
    @FXML private CheckBox abstractClass;

        @FXML private TextField relNameField;

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
        // Now set the values for the other elements
        setValues();

        // Set the class values
        endClassName.setText(endClass);
        originClassName.setText(originClass);

        // Put controllers on the name fields
        classNameField.setTextFormatter(FieldFormatter.getMixedFormatter(30));
        relNameField.setTextFormatter(FieldFormatter.getMixedFormatter(20));
        endRole.setTextFormatter(FieldFormatter.getMixedFormatter(15));
        originRole.setTextFormatter(FieldFormatter.getMixedFormatter(15));

        // Put controllers on the cardinalities
        endMinCard.setTextFormatter(FieldFormatter.getNumberFormatter(2));
        originMinCard.setTextFormatter(FieldFormatter.getNumberFormatter(2));
        endMaxCard.setTextFormatter(FieldFormatter.getStarFormatter(2));
        originMaxCard.setTextFormatter(FieldFormatter.getStarFormatter(2));
    }






    /**
     * Method that closes the current frame.
     * It is the action listener of the cancel button.
     */
    @FXML
    private void cancelCreation() {
        ((Stage) classNameField.getScene().getWindow()).close();
    }








    /**
     * Validates if the input data for a relationship is correct and,
     * in that case, creates the logical relationship. Otherwise, it shows
     * error messages.
     */
    @FXML
    private void createRelationship() {
        // Get the diagram instance
        UMLDiagram diagram = UMLDiagram.getInstance(false);

        // Clean the information
        cleanFields();


        // Validate the data
        boolean classNameOk = UmlValidation.validateNewClass(classNameField);
        boolean relationshipOk = UmlValidation.validateNewRelationship(relNameField,
                endRole, originRole, endMinCard, endMaxCard, originMinCard, originMaxCard);

        /*
            IF THERE ARE NO ERRORS
         */
        if(classNameOk && relationshipOk) {
            // Create the origin endpoint
            RelationshipEndpoint epOrigin = new RelationshipEndpoint(originRole.getText(),
                    originBrowsable.isSelected(), originUnique.isSelected(), originOrdered.isSelected(),
                    originMinCard.getText() + ".." + originMaxCard.getText(),
                    diagram.getClasses(originClass), type );

            // Create the ending endpoint
            RelationshipEndpoint epEnd = new RelationshipEndpoint(endRole.getText(),
                    endBrowsable.isSelected(), endUnique.isSelected(), endOrdered.isSelected(),
                    endMinCard.getText() + ".." + endMaxCard.getText(),
                    diagram.getClasses(endClass), type);

            // Now creat the main relationship
            Relationship r = new Relationship( diagram.newRelId(), relNameField.getText(),
                    epOrigin, epEnd, type);

            // Create the association class
            AssociationClass ac = new AssociationClass(
                    new UmlClass( String.valueOf(diagram.newClassId()),
                            classNameField.getText(), abstractClass.isSelected()
                    ), r);

            // Add the association class
            diagram.addAssociationClass(ac);

            // And cancel
            cancelCreation();
        }

    }












    /**
     * Method that cleans the fields by removing the borders and the tooltips.
     */
    private void cleanFields() {
        // Change the borders on the methods
        classNameField.setStyle("-fx-border-color: transparent;");
        relNameField.setStyle("-fx-border-color: transparent;");
        endRole.setStyle("-fx-border-color: transparent;");
        endMinCard.setStyle("-fx-border-color: transparent;");
        endMaxCard.setStyle("-fx-border-color: transparent;");
        originRole.setStyle("-fx-border-color: transparent;");
        originMinCard.setStyle("-fx-border-color: transparent;");
        originMaxCard.setStyle("-fx-border-color: transparent;");

        // Change the tooltips
        classNameField.setTooltip(null);
        relNameField.setTooltip(null);
        endRole.setTooltip(null);
        endMinCard.setTooltip(null);
        endMaxCard.setTooltip(null);
        originRole.setTooltip(null);
        originMinCard.setTooltip(null);
        originMaxCard.setTooltip(null);
    }


}

