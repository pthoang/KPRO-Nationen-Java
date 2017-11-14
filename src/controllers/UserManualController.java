package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import javafx.scene.text.TextFlow;

import javafx.collections.ObservableList;
import model.Utility;

public class UserManualController {

    private static UserManualController instance = null;

    @FXML
    public TextFlow userManualField = new TextFlow();

    public static UserManualController getInstance() {
        if (instance == null) {
            instance = new UserManualController();
        }
        return instance;
    }

    @FXML
    public void initialize() {
        Text userManualText = readTextFromFile();
        userManualText.setStyle("-fx-font-size: 14; -fx-fill: #d9d9d9;");
        ObservableList textList = userManualField.getChildren();
        textList.add(userManualText);
    }

    private Text readTextFromFile() {
        String about = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Utility.getResourceAsStream("src/resources/texts/UserManual.txt")));

            while (br.readLine() != null) {
                about += br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("About.txt not found: " + e);
        } catch (Exception e) {
            System.out.println("Exception when reading UserManual.txt");
        }

        return new Text(about);
    }

    @FXML
    public void handleBack() {
        MainApp.getInstance().showEditListView();
    }

}
