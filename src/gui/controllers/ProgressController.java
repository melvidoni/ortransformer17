package gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import transformations.ort.TransformationStatus;





public class ProgressController {
    @FXML private ProgressBar progressBar;
    @FXML protected Label progressLabel;




    /**
     * Method that initializes the interface by binding the
     * properties and assigning the listener to the bar.
     */
    @FXML
    private void initialize() {
        // Get the status
        TransformationStatus tStatus = TransformationStatus.getInstance(false);

        // Bind the progress bar
        progressBar.progressProperty().bindBidirectional( tStatus.getProgressProperty() );
        progressBar.progressProperty().addListener( ce -> progressListener() );

        // Bind the label
        progressLabel.textProperty().bindBidirectional( tStatus.getLabelProperty() );
    }





    /**
     * Method that acts as a listener for the progress in the bar.
     */
    private void progressListener() {
        // TODO CHANGE THIS BEHAVIOR
        if(progressBar.getProgress() == 100)
            System.out.println("COMPLETED");
    }


}