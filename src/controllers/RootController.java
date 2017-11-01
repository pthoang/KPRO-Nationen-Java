package controllers;

import javafx.fxml.FXML;

import java.io.File;

import Main.MainApp;
import model.ScoringList;

public class RootController {
	private SettingsController settingsController;

	private MainApp mainApp;

	/**
	 * Create the RootController object.
	 */
	public RootController() {
		super();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}	

	@FXML
	private void showLoadList() {
		File file = mainApp.choseFileAndGetFile();
		String filePath = file.getAbsolutePath();

		ScoringList scoringList = mainApp.getScoringList();

		if (filePath.toLowerCase().endsWith(".json")) {
			scoringList.createFromPreviousList(filePath);
		} else if (filePath.toLowerCase().endsWith(".txt")) {
			scoringList.createFromNameList(filePath);
		} else {
			System.out.println("Error: invalid file");
		}

		mainApp.setScoringList(scoringList);

		mainApp.updateView();
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
		// TOOD
	}

}
