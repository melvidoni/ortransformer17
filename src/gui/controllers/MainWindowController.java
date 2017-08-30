package gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainWindowController {
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuTransform;
    @FXML private SplitPane splitPane;


    /**
     * Method that initializes the window on construction.
     */
    @FXML
    private void initialize() {
        menuSave.setDisable(true);
        menuTransform.setDisable(true);
    }


    /**
     * Shows a dialog message about the creators of the tool.
     */
    @FXML
    private void showAboutMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set text information
        alert.setTitle("About OR-Transformer");
        alert.setHeaderText("Object-Relational Transforming Tool");
        alert.setContentText("This tool was created at the Institute of Design and Development" +
                " (INGAR CONICET-UTN) by Dr. Aldo Vecchietti, Dr. Fernanda Golobysky, and Dr. Melina Vidoni.");

        // Change the image
        Image image = new Image("/gui/views/img/icon_about.png");
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        // Show the dialog
        alert.showAndWait();
    }

}