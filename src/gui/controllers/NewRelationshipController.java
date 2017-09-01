package gui.controllers;


import gui.components.FieldFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;




public class NewRelationshipController extends ARelationshipController {
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



    public void NewRelationshipController() {
        initialize();
    }




    /**
     * Method to initialize the interface by adding the
     * corresponding elements and controllers.
     */
    @FXML
    private void initialize() {
        // Set the class values
        endClassName.setText(endClass);
        originClassName.setText(originClass);

        // Put controllers on the name fields
        nameField.setTextFormatter(FieldFormatter.getMixedFormatter(20));
        endRole.setTextFormatter(FieldFormatter.getMixedFormatter(15));
        originRole.setTextFormatter(FieldFormatter.getMixedFormatter(15));

        // Put controllers on the cardinalities
        endMinCard.setTextFormatter(FieldFormatter.getNumberFormatter(2));
        originMinCard.setTextFormatter(FieldFormatter.getNumberFormatter(2));
        endMaxCard.setTextFormatter(FieldFormatter.getStarFormatter(2));
        originMaxCard.setTextFormatter(FieldFormatter.getStarFormatter(2));

        // Now set the values for the other elements
        setValues();
    }










    /**
     * Method that closes the current frame.
     * It is the action listener of the cancel button.
     */
    @FXML
    private void cancelCreation() {
        ((Stage) nameField.getScene().getWindow()).close();
    }




    @FXML
    private void createRelationship() {
        System.out.println("create a: " + type.getName());
    }




}

