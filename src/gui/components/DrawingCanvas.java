package gui.components;



import app.Main;
import gui.controllers.NewClassController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private boolean newClassFlag;
    private boolean newAssocRelFlag;
    private boolean newAggregRelFlag;
    private boolean newCompRelFlag;
    private boolean newGenRelFlag;
    private boolean newAssocClassFlag;


    /**
     * Default constructor of the class, that initializes
     * the diagram with the corresponding values and listeners.
     */
    public DrawingCanvas() {
        super();

        // Create a new model
        newModel();

        // Add the listeners
        this.setOnMouseClicked(me -> {this.clickAction(me);});
    }


    /**
     * Method that creates a new model, and cleans
     * the current drawing pane.
     */
    public void newModel() {
        // Start the diagram
        drawingDiagram = new DrawingDiagram();

        // Prepare the flags
        newClassFlag = false;
        newAssocRelFlag = false;
        newAggregRelFlag = false;
        newCompRelFlag = false;
        newGenRelFlag = false;
        newAssocClassFlag = false;

        // Clean the pane
        getChildren().clear();
    }



    /**
     * Sets all the flags for drawing a new class.
     */
    public void drawNewClass() {
        // Set this one to be drawn
        newClassFlag = true;

        // Set the others as false
        newAssocRelFlag = false;
        newAggregRelFlag = false;
        newCompRelFlag = false;
        newGenRelFlag = false;
        newAssocClassFlag = false;
    }









    private void clickAction(MouseEvent me) {
        System.out.println("CLICK ACTION");

        try {
            /*
            LEFT MOUSE BUTTON
            */
            if(me.getButton() == MouseButton.PRIMARY) {
                System.out.println("PRIMARY BUTTON -> " + newClassFlag);


                // If this is to draw a new class
                if(newClassFlag) {
                    System.out.println("NEW CLASS");

                    FXMLLoader dloader = FXMLLoader.load(Main.class.getResource("/gui/views/NewClassDialog.fxml"));

                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Create New Class");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner( getScene().getWindow() );

                    Scene scene = new Scene(dloader.load());
                    dialogStage.setScene(scene);

                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();
                }
            }
            else if(me.getButton() == MouseButton.SECONDARY) {
                System.out.println("RIGHT");
            }

        }
        catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }





    }


}
