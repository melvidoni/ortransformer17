package gui.components;


import app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;




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
        FXMLLoader dloader = new FXMLLoader(Main.class.getResource(resource));

        Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(scene.getWindow());

        dialogStage.setScene(new Scene(dloader.load()));

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }
}
