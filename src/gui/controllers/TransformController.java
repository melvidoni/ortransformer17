package gui.controllers;



import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import transformations.ort.TransformationStatus;
import transformations.ort.enums.DatabaseType;
import transformations.ort.enums.ImplementationType;
import java.util.Arrays;
import java.util.stream.Collectors;




/**
 * Controller for the interface that allows configuring a
 * transformation process.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class TransformController {
    @FXML private ComboBox implementationCombo;
    @FXML private ComboBox dbtypeCombo;





    /**
     * Method to initialize the current interface with the
     * corresponding information on the combo boxes.
     */
    @FXML
    private void initialize() {
        // Load the implementation combo box
        implementationCombo.getItems().setAll(Arrays.stream(ImplementationType.values())
                .map(ImplementationType::getName).collect(Collectors.toList()) );
        implementationCombo.getSelectionModel().selectFirst();

        // Load the database type
        dbtypeCombo.getItems().setAll(Arrays.stream(DatabaseType.values())
                .map(DatabaseType::getStringType).collect(Collectors.toList()));
        dbtypeCombo.getSelectionModel().selectFirst();

        // Disable this because only object-relation mapping works
        dbtypeCombo.setDisable(true);
        dbtypeCombo.setStyle("-fx-opacity: 1");
    }




    /**
     * Method that closes the current frame.
     * It is the action listener of the cancel button.
     */
    @FXML
    private void cancelTransformation() {
        ((Stage) implementationCombo.getScene().getWindow()).close();
    }




    /**
     * Method that saves the transformation configuration and
     * closes the window. It does ont prompt for any other
     * confirmation.
     */
    @FXML
    private void startTransformation() {
        // Update the status
        TransformationStatus tStatus = TransformationStatus.getInstance(false);
        tStatus.setTransform(true);
        tStatus.setImplementation(ImplementationType.getImplementacion(
                implementationCombo.getSelectionModel().getSelectedItem().toString()) );
        tStatus.setDatabase( DatabaseType.getType(
                dbtypeCombo.getSelectionModel().getSelectedItem().toString()) );

        // Close this window
        cancelTransformation();
    }











}
