package gui.controllers;


import gui.components.PopupHandlers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import transformations.ort.TranslationTask;




/**
 * Controller class for the interface that shows a progress bar when
 * the transformation task is being executed.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
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
        tTask.setOnSucceeded(ce -> progressListener());

        tTask.exceptionProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue != null) {
                Exception ex = (Exception) newValue;

                PopupHandlers.showDialog("Error", "Something bad happened",
                        ex.getMessage(), Alert.AlertType.ERROR);
            }
        });



        // Bind the elements
        progressBar.progressProperty().bind(tTask.progressProperty());
        progressLabel.textProperty().bind(tTask.messageProperty());


        // Start the thread
        Thread th = new Thread(tTask);
        th.setDaemon(true);
        th.start();
    }






    /**
     * Method that launches when the task is successfully
     * completed, closing the progress dialog.
     */
    private void progressListener() {
        ((Stage) progressBar.getScene().getWindow()).close();
    }




}