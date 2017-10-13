package controllers;

import java.io.File;
import java.util.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ScoringList;

public class StartMenuController extends SuperController {


	@FXML 
	private Button nameListButton;
	@FXML
	private Button lastYearListButton;

	@FXML
	private ImageView imageView;
	
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
		FileChooser fileChooser = new FileChooser();
		
		Stage stage = super.mainApp.getStage();
		File file = fileChooser.showOpenDialog(stage);
		String filePath = file.getAbsolutePath();
	
		scoringList.createFromNameList(filePath);
		
		super.mainApp.setScoringList(scoringList);
		
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
}
