package gui.components;


import app.Main;
import gui.controllers.ARelationshipController;
import gui.controllers.NewRelationshipController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umldiagram.logical.enums.RelationshipType;

import java.io.IOException;
import java.util.Optional;


public class PopupHandlers {



    /**
     * Method to show a popup, using the FXML received as parameter, and with the
     * corresponding scene and title also received.
     * @param resource The path toward the FXML file to use.
     * @param title The title of the popup to be shown.
     * @param scene The scene used as parent to link the popup.
     * @throws IOException For errors opening the FXML file.
     */
    public static void showPopup(String resource, String title, Scene scene) throws IOException {
        // Load and configure the dialog
        FXMLLoader dloader = new FXMLLoader(Main.class.getResource(resource));
        Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(scene.getWindow());

        // Show the dialog and wait until the user closes it
        dialogStage.setScene(new Scene(dloader.load()));
        dialogStage.showAndWait();
    }






    /**
     * Method that shows a warning dialog, using the information received as parameters.
     * @param title The title of the warning.
     * @param subtitle The subtitle.
     * @param message The main message content.
     */
    public static void showWarningDialog(String title, String subtitle, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set text information
        alert.setTitle(title);
        alert.setHeaderText(subtitle);
        alert.setContentText(message);

        // Change the image
        Image image = new Image("/gui/views/img/icon_warning.png");
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        // Show the dialog
        alert.showAndWait();
    }





    /**
     * Method that shows a confirmation dialog, using the information received as parameters.
     * @param title The title of the warning.
     * @param subtitle The subtitle.
     * @param message The main message content.
     * @return true if the user clicked OK, false otherwise.
     */
    public static boolean showConfirmation(String title, String subtitle, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Set the information
        alert.setTitle(title);
        alert.setHeaderText(subtitle);
        alert.setContentText(message);

        // Change the image
        Image image = new Image("/gui/views/img/icon_about.png");
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        // Get the button type
        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }



}
