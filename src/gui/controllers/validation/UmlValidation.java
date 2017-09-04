package gui.controllers.validation;


import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import umldiagram.logical.UMLDiagram;




/**
 * Static class that contains validations used on the GUI controllers.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class UmlValidation {



    /**
     * Method that evaluates a new relationship to decide if the values are correct.
     * @param relNameField Name of the relationship.
     * @param endRole End role name.
     * @param originRole Origin role name.
     * @param endMinCard End min cardinality.
     * @param endMaxCard End max cardinality.
     * @param originMinCard Origin min cardinality.
     * @param originMaxCard Origin max cardinality.
     * @return true if everything is okay, false otherwise.
     */
    public static boolean validateNewRelationship(TextField relNameField,
                                                  TextField endRole, TextField originRole,
                                                  TextField endMinCard, TextField endMaxCard,
                                                  TextField originMinCard, TextField originMaxCard) {
        // Get the diagram instance
        UMLDiagram diagram = UMLDiagram.getInstance(false);

        // Temporal flags
        boolean relNameOk = false;
        boolean endpointsOk = validateEndpoints(endRole, originRole, endMinCard,
                endMaxCard, originMinCard, originMaxCard);

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


        // Now return
        return relNameOk && endpointsOk;
    }







    /**
     * Method that valida a new class name, to decide if the values are correct.
     * @param className The textfield with the class name.
     * @return True if it is correct, false otherwise.
     */
    public static boolean validateNewClass(TextField className) {
        // Temporal flags
        boolean classOk = false;

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

        // Return the value
        return classOk;
    }






    /**
     * Method that validates the endpoints of a relationship.
     * @param endRole End role name.
     * @param originRole Origin role name.
     * @param endMinCard End min cardinality.
     * @param endMaxCard End max cardinality.
     * @param originMinCard Origin min cardinality.
     * @param originMaxCard Origin max cardinality.
     * @return true if everything is ok, false if there is at least one error.
     */
    private static boolean validateEndpoints(TextField endRole, TextField originRole,
                                             TextField endMinCard, TextField endMaxCard,
                                             TextField originMinCard, TextField originMaxCard) {
        // Temporal flags
        boolean rolesOk = false;
        boolean oCardOk = false;
        boolean eCardOk = false;

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



        // Return the value
        return eCardOk && oCardOk && rolesOk;
    }






    /**
     * Checks if two values of a cardinality are correct.
     * @param min Min value of the cardinality.
     * @param max Max value of the cardinality.
     * @return true if the max is a star, or if the max is greater than or
     * equal to the min. False in the other case.
     */
    private static boolean cardinalityAccepted(String min, String max) {
        // If the max is a star, then it is ok
        // If not, the minimum must be less than the maximum
        return max.contains("*") || (Integer.valueOf(min) <= Integer.valueOf(max));
    }








}
