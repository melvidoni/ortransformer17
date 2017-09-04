package gui.controllers;


import gui.components.FieldFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import umldiagram.logical.Relationship;
import umldiagram.logical.RelationshipEndpoint;
import umldiagram.logical.UMLDiagram;
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

        // Temporal flags
        boolean classNameOk = false;
        boolean relNameOk = false;
        boolean rolesOk = false;
        boolean oCardOk = false;
        boolean eCardOk = false;


        // Check for the class name
        if(classNameField.getText().isEmpty()) {
            classNameField.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            classNameField.setTooltip(new Tooltip("The class name cannot be empty."));
        }
        else if(diagram.getClassesNames().contains(classNameField.getText().toUpperCase())) {
            classNameField.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            classNameField.setTooltip(new Tooltip("The class name is already in use."));
        }
        else classNameOk = true;

        // Check if the name is empty
        if(relNameField.getText().isEmpty()) {
            relNameField.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            relNameField.setTooltip(new Tooltip("The relationship name cannot be empty."));
        }
        else if(diagram.existsRelationship(relNameField.getText())) {
            relNameField.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            relNameField.setTooltip(new Tooltip("The relationship name already exists."));
        }
        else relNameOk = true;

        // Check the roles
        if(endRole.getText().isEmpty() || originRole.getText().isEmpty()) {
            // If it is the ending
            if(endRole.getText().isEmpty()) {
                endRole.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
                endRole.setTooltip(new Tooltip("The role name cannot be empty"));
            }
            // If it is the origin
            if(originRole.getText().isEmpty()) {
                originRole.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
                originRole.setTooltip(new Tooltip("The role name cannot be empty"));
            }
        }
        else if(endRole.getText().equals(originRole.getText())) {
            endRole.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            originRole.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");

            endRole.setTooltip(new Tooltip("The name cannot be the same of the origin."));
            originRole.setTooltip(new Tooltip("The name cannot be the same of the ending."));
        }
        else rolesOk = true;

        // Check the ending cardinalities
        if(endMinCard.getText().isEmpty() || endMaxCard.getText().isEmpty()) {
            // If it is the min
            if(endMinCard.getText().isEmpty()) {
                endMinCard.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
                endMinCard.setTooltip(new Tooltip("The min cardinality cannot be empty."));
            }
            // If it is the max
            if(endMaxCard.getText().isEmpty()) {
                endMaxCard.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
                endMaxCard.setTooltip(new Tooltip("The max cardinality cannot be empty"));
            }
        }
        else if(!cardinalityAccepted(endMinCard.getText(), endMaxCard.getText())) {
            endMaxCard.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            endMaxCard.setTooltip(new Tooltip("Max cardinality must be greater or equal to the min."));
        }
        else eCardOk = true;


        // Check the origin cardinalities
        if(originMinCard.getText().isEmpty() || originMaxCard.getText().isEmpty()) {
            // If it is the min
            if(originMinCard.getText().isEmpty()) {
                originMinCard.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
                originMinCard.setTooltip(new Tooltip("The min cardinality cannot be empty."));
            }
            // If it is the max
            if(originMaxCard.getText().isEmpty()) {
                originMaxCard.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
                originMaxCard.setTooltip(new Tooltip("The max cardinality cannot be empty"));
            }
        }
        else if(!cardinalityAccepted(originMinCard.getText(), originMaxCard.getText())) {
            originMaxCard.setStyle("-fx-border-color: #f4416b ; -fx-border-width: 2px ;");
            originMaxCard.setTooltip(new Tooltip("Max cardinality must be greater or equal to the min."));
        }
        else oCardOk = true;



        /*
            IF THERE ARE NO ERRORS
         */
        if(relNameOk && rolesOk && eCardOk && oCardOk && classNameOk) {
            // Create the origin endpoint
            RelationshipEndpoint epOrigin = new RelationshipEndpoint(originRole.getText(),
                    originBrowsable.isSelected(), originUnique.isSelected(), originOrdered.isSelected(),
                    originMinCard.getText() + ".." + originMaxCard.getText(),
                    diagram.getClasses(originClass), RelationshipType.ASSOCIATION );

            // Create the ending endpoint
            RelationshipEndpoint epEnd = new RelationshipEndpoint(endRole.getText(),
                    endBrowsable.isSelected(), endUnique.isSelected(), endOrdered.isSelected(),
                    endMinCard.getText() + ".." + endMaxCard.getText(),
                    diagram.getClasses(endClass), type);

            // Now creat the main relationship
            Relationship r = new Relationship( diagram.newRelId(), relNameField.getText(),
                    epOrigin, epEnd, type);










            // Add the relationship
            //diagram.addRelationship(r);

            // And cancel
            cancelCreation();
        }

    }





    /**
     * Checks if two values of a cardinality are correct.
     * @param min Min value of the cardinality.
     * @param max Max value of the cardinality.
     * @return true if the max is a star, or if the max is greater than or
     * equal to the min. False in the other case.
     */
    private boolean cardinalityAccepted(String min, String max) {
        // If the max is a star, then it is ok
        // If not, the minimum must be less than the maximum
        return max.contains("*") || (Integer.valueOf(min) <= Integer.valueOf(max));
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

