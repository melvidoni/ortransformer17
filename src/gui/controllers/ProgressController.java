package gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import transformations.ort.TransformationStatus;
import transformations.ort.TranslationTask;


public class ProgressController {
    @FXML private ProgressBar progressBar;
    @FXML protected Label progressLabel;




    /**
     * Method that initializes the interface by binding the
     * properties and assigning the listener to the bar.
     */
    @FXML
    private void initialize() {
        // Prepare the simulation tread
        TranslationTask tTask = new TranslationTask();

        // Bind the progress bar
        progressBar.progressProperty().bind(tTask.progressProperty());
        progressBar.progressProperty().addListener( ce -> progressListener() );

        // Bind the label
        progressLabel.textProperty().bind(tTask.messageProperty());


        // Start the thread
        Thread th = new Thread(tTask);
        th.setDaemon(true);
        th.start();
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