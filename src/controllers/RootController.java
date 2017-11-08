package controllers;

import javafx.fxml.FXML;

import java.io.File;

import Main.MainApp;
import model.ScoringList;

public class RootController {

	private MainApp mainApp;


	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}	

	@FXML
	private void showLoadList() {
		File file = mainApp.chooseAndGetFile();
		createScoringListBasedOnFileType(file);

		EditListController.getOrCreateInstance().fillTable();
	}

	private void createScoringListBasedOnFileType(File file) {
		String filePath = file.getAbsolutePath();
		ScoringList scoringList = ScoringList.getOrCreateInstance();

		if (filePath.toLowerCase().endsWith(".json")) {
			scoringList.createFromPreviousList(filePath);
		} else if (filePath.toLowerCase().endsWith(".txt")) {
			scoringList.createFromNameList(filePath);
		} else {
			// TODO
			System.out.println("Error: invalid file");
		}
	}
	
	@FXML
	private void showLoadSources() {
		mainApp.showLoadSourcesView();
	}

	@FXML
	private void showNewAndEmpty() {
		mainApp.newList();
	}

	@FXML
	private void showAbout() {
		// TODO
	}

	@FXML
	private void showSettings() {
		mainApp.showSettingsView();
	}	
	
	@FXML
	private void showUserManual() {
		// TODO
	}

}
