package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.AmazonBucketUploader;
import model.Candidate;
import model.ScoringList;

import Main.MainApp;

public class EditListController {

	@FXML
	private Button saveListButton;

	@FXML
	private CandidateController candidateViewController;
	@FXML
	private ScoringListController scoringListViewController;

	private ScoringList scoringList;
	private Candidate candidate;
	private MainApp mainApp;

	private static ObservableList<Candidate> candidates;

	/**
	 * Set mainApp in super, then gets the candidates and shows them in the table.
	 * @params mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		updateLists();

		if (candidates.size() > 0) {
			scoringListViewController.fillTable();
		}

		scoringListViewController.setParentController(this);
		scoringListViewController.setScoringList(scoringList);
		scoringListViewController.setMainApp(mainApp);

		candidateViewController.setParentController(this);
		candidateViewController.setMainApp(mainApp);
	}

	public MainApp getMainApp() {
		return mainApp;
	}
	
	@FXML
	private void initialize() {
	}

	public void updateLists() {
		scoringList = mainApp.getScoringList();
		candidates = scoringList.getCandidates();
	}

	public void setCandidate(Candidate candidate) {
		candidateViewController.setCandidate(candidate);
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public ObservableList<Candidate> getCandidates() {
		return candidates;
	}



	/**
	 * Saves the list to a file locally.
	 */
	@FXML
	public void handleSaveList() {
		// TODO: copy the list and save it as temporaryList?
	}

	// TODO: can be made more complex
	private boolean nameExistInList(String name) {
		String[] names = name.split(" ");
		int numberOfNames = names.length;
		for (int i = 0; i < candidates.size(); i++) {
			Candidate c = candidates.get(i);
			String candidateName = c.getName();
			
			if (name.equals(candidateName)) {
				return true;
			}
		}
		return false;
	}

	public void setBucketUploader(AmazonBucketUploader bucketUploader) {
		candidateViewController.setBucketUploader(bucketUploader);
	}

	public void fillTable() {
		scoringListViewController.fillTable();
	}

	public void refreshTable() {
		scoringListViewController.refreshTable();
	}

	public void addCandidate(Candidate candidate) {
		scoringList.addCandidate(candidate);
	}

	public void deleteCandidate(Candidate candidate) {
		scoringList.deleteCandidate(candidate);
	}
}