package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import Main.MainApp;
import model.Candidate;
import model.ScoringList;
import model.Utility;
import org.apache.commons.io.FilenameUtils;

public class RootController {

	private MainApp mainApp;


	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}	

	@FXML
	private void showLoadList() {
		File file = mainApp.chooseAndGetFile();
		if (file == null) {
			return;
		}
		createScoringListBasedOnFileType(file);

		ScoringListController.getOrCreateInstance().fillTable();
	}

	private void createScoringListBasedOnFileType(File file) {
		String filePath = file.getAbsolutePath();
		ScoringList scoringList = ScoringList.getOrCreateInstance();

		if (filePath.toLowerCase().endsWith(".json")) {
			scoringList.createFromPreviousList(file);
		} else if (filePath.toLowerCase().endsWith(".txt")) {
			scoringList.createFromNameList(file);
		} else {
			String headerText = "Det er ikke en godkjent filtype.";
			String contentText = "Filtypen må være enten .txt eller .json";
			Utility.newAlertError(headerText, contentText);
			System.out.println("Error: invalid file");
		}
	}

	@FXML
	private void showAddJury(){
		mainApp.showJuryAdmin();
	}

	@FXML
	private void showNewAndEmpty() {
		mainApp.newList();
	}

	@FXML
	private void showSettings() {
		mainApp.showSettingsView();
	}	

}
