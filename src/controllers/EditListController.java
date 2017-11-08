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
/*
		scoringListViewController = ScoringListController.getOrCreateInstance();
		candidateViewController = CandidateController.getOrCreateInstance();

		if (candidates.size() > 0) {
			scoringListViewController.fillTable();
		}
*/
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

	// TODO: can be made more complex
    /*
	public boolean nameExistInList(String name) {
		for (int i = 0; i < candidates.size(); i++) {
			Candidate c = candidates.get(i);
			String candidateName = c.getName();
			if (name.equals(candidateName) && candidate != c) {
				return true;
			}
		}
		return false;
	}
	*/

}