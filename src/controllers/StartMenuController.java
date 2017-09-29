package controllers;

import java.awt.Desktop;
import java.util.Calendar;
import java.util.Date;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ScoringList;

public class StartMenuController extends SuperController {


	@FXML 
	private Button nameListButton;
	@FXML
	private Button lastYearListButton;
	
	private ScoringList scoringList;
	
	FileChooser fileChooser = new FileChooser();

	/**
	 * Creates the startMenuController object.
	 */
	public StartMenuController() {
		super();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		scoringList = new ScoringList(year);
	}

	/**
	 * Initializes the controller class. 
	 * The method is automatically called after the fxml file has been loaded."
	 */
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void importNameList() {
		// TODO: get nameList
		scoringList.createFromNameList();
		
		showCandidatesList();
	}
	
	@FXML
	private void importLastYearList() {
		// TODO: get previous list
		scoringList.createFromPreviousList();
		
		showCandidatesList();
	}

	/**
	 * Shows view for when the user wants to create a new list.
	 */
	@FXML
	private void showCandidatesList() {
		mainApp.setScoringList(scoringList);
		viewController.showCandidatesListView();
		
	}
	
	/**
	 * Load the selected file
	 */
	@FXML
	private void fileChooser() {

		FileChooser fileChooser = new FileChooser();
		Desktop desktop = Desktop.getDesktop();

		Stage stage = super.mainApp.getStage();
		fileChooser.showOpenDialog(stage);
	}

}
