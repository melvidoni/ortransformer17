package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Main class that launches the application.
 */
public class Main extends Application {



    /**
     * Method that constructs the main application.
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main window
            Parent root = FXMLLoader.load(getClass().getResource("/gui/views/MainWindow.fxml"));
            primaryStage.setTitle("OR-Transformer");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(new Image("/gui/views/img/logo.png"));




            // Prepare sizes
            primaryStage.setMinHeight(500);
            primaryStage.setMinWidth(600);
            primaryStage.setMaximized(true);

            // Show the interface
            primaryStage.show();
        }
        catch(Exception e) {
            // TODO REMOVE STACK STRACE
            e.printStackTrace();
        }
    }


    /**
     * Main method for launching the application.
     * @param args Default arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }


}
