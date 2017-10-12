package controllers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;
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
		// TODO: should this be moved to its own class?
		
		/* TODO commented out in production
		FileChooser fileChooser = new FileChooser();
		
		Stage stage = super.mainApp.getStage();
		File file = fileChooser.showOpenDialog(stage);
		String filePath = file.getAbsolutePath();
	
		scoringList.createFromNameList(filePath);
		*/
		scoringList.createFromNameList("src/NameListTest.txt");
		super.mainApp.setScoringList(scoringList);
		
		//showCandidatesList();
		viewController.showScoringListView();
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
}
