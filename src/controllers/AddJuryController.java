package controllers;


import java.awt.*;

import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.util.*;


import Main.MainApp;

import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;
import model.Jury;
import model.ScoringList;



public class AddJuryController {

    @FXML
    private Button chooseImageButton;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField loadedFileNameField;
    private MainApp mainApp;
    private Jury jury;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void fileChooser() {

        File file = mainApp.chooseAndGetFile();
        System.out.println("Trying to add picture");
    }

    @FXML
    public void handleBack() {
            mainApp.showEditListView();
    }

    @FXML
    public void handleNext() {System.out.println("Go to List");}

    @FXML
    public void addMember() {
        //ADD JURY MEMBER
        System.out.println("legg til medlem");
    }

}