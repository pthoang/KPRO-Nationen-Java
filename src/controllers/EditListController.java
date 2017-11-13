package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.AmazonBucketUploader;
import model.Candidate;
import model.ScoringList;

import Main.MainApp;

public class EditListController {


	public static EditListController instance = null;


	@FXML
	private CandidateController candidateViewController;
	@FXML
	private ScoringListController scoringListViewController;

	private Candidate candidate;
	private MainApp mainApp;

	private static ObservableList<Candidate> candidates;

	public EditListController() {
		instance = this;
		mainApp = MainApp.getInstance();
	}

	public static EditListController getOrCreateInstance() {
		if (instance == null) {
			instance = new EditListController();
		}
		return instance;
	}

	public Candidate getCandidate() {
		return candidate;
	}

}