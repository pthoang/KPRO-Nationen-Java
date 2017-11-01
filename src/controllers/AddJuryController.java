package controllers;


import java.awt.*;

import java.awt.image.BufferedImage;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;
import model.Jury;
import model.ScoringList;
import model.Organization;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;


public class AddJuryController {

    @FXML
    private Button chooseImageButton;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private ImageView juryImageView = new ImageView();

    private MainApp mainApp;
    private Jury jury;
    private Image newImage;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void fileChooser() {
        File file = mainApp.choseFileAndGetFile();
        setImageField(file);
    }

    //TODO - lage en generell metode?
    private void setImageField(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            newImage = SwingFXUtils.toFXImage(bufferedImage, null);
            juryImageView.setImage(newImage);
        } catch (IOException ex) {
            System.out.println("Error when loading image: " + ex);
        }
    }

    public void handleBack() {
        System.out.println("Trying to go back, but the past is behind you");
    }

    public void handleNext() {System.out.println("Trying to go back, but the past is behind you");}

    public void addMember() {System.out.println("Trying to go back, but the past is behind you");}

}
