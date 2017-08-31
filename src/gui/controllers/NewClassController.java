package gui.controllers;


import gui.components.FieldFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;


public class NewClassController extends GridPane {
    @FXML private TextField className;
    @FXML private CheckBox isAbstract;
    @FXML private TableView attrTable;

    private boolean acceptClicked;



    @FXML
    private void initialize() {
        className.setTextFormatter(FieldFormatter.getMixedFormatter(30));
    }




    @FXML
    private void addNewAttribute() {

    }


    @FXML
    private void removeAttributes() {

    }


    @FXML
    private void cancelCreation() {

    }


    @FXML
    private void createNewClass() {
        // Lets validate the data
        if(className.getText().isEmpty()) {
            System.out.println("Empty class name");
        }

    }
}
