package gui.components;



import app.Main;
import gui.controllers.MainWindowController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umldiagram.graphical.DrawingDiagram;

import java.io.IOException;








public class DrawingCanvas extends Pane {
    private DrawingDiagram drawingDiagram;


    /**
     * Default constructor of the class, that initializes
     * the diagram with the corresponding values and listeners.
     */
    public DrawingCanvas() {
        super();

        // Create a new model
        newModel();

        // Add the listeners
        this.setOnMouseClicked(this::clickAction);
    }


    /**
     * Method that creates a new model, and cleans
     * the current drawing pane.
     */
    public void newModel() {
        // Start the diagram
        drawingDiagram = new DrawingDiagram();

        // Clean the pane
        getChildren().clear();
    }







    private void clickAction(MouseEvent me) {
        System.out.println("CLICK ACTION");

        System.out.println("BOUND? " );

        try {
            /*
            LEFT MOUSE BUTTON
            */
            if(me.getButton() == MouseButton.PRIMARY) {
                System.out.println("PRIMARY BUTTON -> ");

                System.out.println("NEW CLASS");


                FXMLLoader dloader = new FXMLLoader(Main.class.getResource("/gui/views/NewClassDialog.fxml"));

                Stage dialogStage = new Stage();
                dialogStage.setResizable(false);
                dialogStage.setTitle("Create New Class");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(getScene().getWindow());

                Scene scene = new Scene(dloader.load());
                dialogStage.setScene(scene);

                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();

            }

        }
        catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }





    }

}
