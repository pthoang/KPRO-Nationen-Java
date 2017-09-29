package controllers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
		
		BufferedReader br = null;
		String filePath = "src/NameListTest.txt";
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			readNameList(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		scoringList.createFromNameList();
		
		showCandidatesList();
	}
	
	private void readNameList(Stream<String> stream) throws IOException {
		final AtomicInteger rank = new AtomicInteger();
		stream.forEach((name) -> {
			System.out.println("Name: " + name);
			Candidate candidate = new Candidate(name, null, null, rank.get());
			scoringList.addCandidate(candidate);
			rank.incrementAndGet();
			System.out.println("ScoringList in streamreader: " + scoringList);
		});
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
