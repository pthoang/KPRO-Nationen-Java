package controllers;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import Main.MainApp;
import model.ScoringList;

public class RootController {

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
	private void showAddSources() {
		FileChooser fileChooser = new FileChooser();
		
		Stage stage = mainApp.getStage();
		File file = fileChooser.showOpenDialog(stage);
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
	private void showNewAndEmpty() {
		mainApp.newList();
	}

	@FXML
	private void showAbout() {
		// TODO
	}

	@FXML
	private void showSettings() {
		// TODO
	}	
	
}
