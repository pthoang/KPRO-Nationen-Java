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
	private Button saveListButton;

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
		updateLists();

		scoringListViewController = ScoringListController.getOrCreateInstance();
		candidateViewController = CandidateController.getOrCreateInstance();


		/*
		scoringListViewController.setParentController(this);
		scoringListViewController.setScoringList(scoringList);
		//scoringListViewController.setMainApp(mainApp);

		candidateViewController.setParentController(this);
		//candidateViewController.setMainApp(mainApp);

		if (candidates.size() > 0) {
			scoringListViewController.fillTable();
		}
		*/


	}

	public void setScoringListViewController(ScoringListController scoringListViewController) {
		System.out.println("Setting scoringListControler in editList: " + scoringListViewController);
		this.scoringListViewController = scoringListViewController;
		//scoringListViewController.setParentController(this);
		//scoringListViewController.setScoringList(scoringList);
		//scoringListViewController.setMainApp(mainApp);
	}

	public void setCandidateViewController(CandidateController candidateViewController) {
		this.candidateViewController = candidateViewController;
		//candidateViewController.setParentController(this);
		if (candidates.size() > 0) {
			scoringListViewController.fillTable();
		}
	}

	public static EditListController getOrCreateInstance() {
		if (instance == null) {
			System.out.println("EditListController is null");
			instance = new EditListController();
		}
		return instance;
	}

	@FXML
	private void initialize() {
	}

	public void updateLists() {
		// scoringList = ScoringList.getOrCreateInstance();
		candidates = ScoringList.getOrCreateInstance().getCandidates();
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
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

	public void fillTable() {
		System.out.println("ScoringListViewController in editLIstC: " + scoringListViewController);
		scoringListViewController.fillTable();
	}

	public void refreshTable() {
		scoringListViewController.refreshTable();
	}

	/*
	public void addCandidate(Candidate candidate) {
		scoringList.addCandidate(candidate);
	}

	public void deleteCandidate(Candidate candidate) {
		scoringList.deleteCandidate(candidate);
	}
	*/
}