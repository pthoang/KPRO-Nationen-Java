package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.text.TextFlow;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import model.AmazonBucketUploader;

import java.awt.*;
import java.io.*;

public class AboutController {

    public static AboutController instance = null;

    @FXML
    public TextFlow textField = new TextFlow();

    public AboutController() {
        instance = this;
    }

    @FXML
    public void initialize() {
        Text aboutText = readTextFromFile();
        aboutText.setStyle("-fx-font-size: 14; -fx-fill: #d9d9d9;");
        ObservableList textList = textField.getChildren();
        textList.addAll(aboutText);
    }

    private Text readTextFromFile() {
        String about = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/About.txt"));

            while (br.readLine() != null) {
                about += br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("About.txt not found: " + e);
        } catch (Exception e) {
            System.out.println("Exception when reading About.txt");
        }

        return new Text(about);
    }

    @FXML
    public void handleDownloadReport() {
        //AmazonBucketUploader.getOrCreateInstance().getFileFromBucket("Report.pdf");
        if (Desktop.isDesktopSupported()) {
            try {
                File report = new File("resources/Report.pdf");
                Desktop.getDesktop().open(report);
            } catch (IOException e) {
                System.out.println("Can't open the report: " + e);
            }
        }
    }

    @FXML
    public void handleBack() {
        MainApp.getInstance().showEditListView();
    }
}
