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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import javax.imageio.ImageIO;


public class AddJuryController {

    @FXML
    private Button chooseImageButton;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField nameField;
    @FXML
    private javafx.scene.control.TextArea descriptionField;
    @FXML
    private ImageView imageView = new ImageView();

    private MainApp mainApp;
    private Jury jury;
    private javafx.scene.image.Image newImage;
    private String imagePath;
    private String imageName;
    private File imageFile;

    public AddJuryController(){

        mainApp = MainApp.getInstance();
        setImageField(new File("resources/standard.png"));
        jury = Jury.getOrCreateInstance();
    }

    @FXML
    public void initialize() {
        setImageField(new File("resources/standard.png"));
    }

    @FXML
    private void fileChooser() {
        System.out.println("Filechooser");
        imageFile = mainApp.chooseAndGetFile();
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            newImage = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(newImage);
            imageName = imageFile.getName();
            imagePath = imageFile.getPath();
        } catch (IOException ex) {
            System.out.println("Error when loading image: " + ex);
        }

        setImageField(imageFile);
    }

    @FXML
    public void handleBack() {
            mainApp.showEditListView();
    }

    @FXML
    public void handleNext() {
        mainApp.showEditListView();
    }

    @FXML
    public void addMember() {
        //need to include amazon image upload
        System.out.println(imageFile+" "+imageName);
        AmazonBucketUploader.getOrCreateInstance().uploadFile(imageFile, imageName);
        //upload image to bucket
        JuryMember member = new JuryMember(nameField.getText(),descriptionField.getText(),imageName);
        System.out.println("Jury: " + jury);
        System.out.println("Member: " + member);
        this.jury.addMemberToJury(member);
        cleanFields();
    }

    private void cleanFields() {
        imagePath = "resources/standard.png";
        nameField.setText("");
        descriptionField.setText("");

    }

    @FXML
    public void handleAddDescriptioin() {
        
    }

    private void setImageField(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            newImage = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(newImage);
        } catch (IOException ex) {
            System.out.println("Error when loading image: " + ex);
        }
    }

}