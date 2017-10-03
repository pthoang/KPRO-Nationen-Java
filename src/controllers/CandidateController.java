package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Candidate;

public class CandidateController extends SuperController {

	@FXML
	private Button deleteButton;
	@FXML
	private Button saveCandidateButton;
	@FXML
	private Button saveListButton;
	
	@FXML
	private TextField nameField = new TextField();
	@FXML
	private TextField previousYearRankField  = new TextField();
	@FXML
	private TextField rankField  = new TextField();
	@FXML
	private TextArea descriptionField  = new TextArea();
	@FXML
	private TextField municipalityField  = new TextField();
	
	private Candidate candidate;
	
	private ScoringListController scoringListController;
	
	@FXML
	public void initialize() {
	}
	
	public void setScoringListController(ScoringListController scoringListController) {
		this.scoringListController = scoringListController;
	}
	
	@FXML
	public void handleSaveChangesToCandidate() {
		// Name
		String newName = nameField.getText();
		candidate.splitUpAndSaveName(newName);
		System.out.println("Changed name");
		
		// PreviousYearRank
		int newPreviousYearRank = Integer.parseInt(previousYearRankField.getText());
		candidate.setPreviousYearRank(new SimpleIntegerProperty(newPreviousYearRank));

		// Rank
		int rank = Integer.parseInt(previousYearRankField.getText());
		candidate.setRank(new SimpleIntegerProperty(rank));
		
		// Municipality
		String newMunicipality = municipalityField.getText();
		candidate.setMunicipality(new SimpleStringProperty(newMunicipality));;

		System.out.println("Asking to update the table");
		scoringListController.refreshTable();
	}
	
	@FXML
	public void handleSaveList() {
		// TODO: copy the list and save it as temporaryList?
	}
	
	@FXML
	public void handleDelete() {
		super.mainApp.getScoringList().deleteCandidate(candidate);
	}
	
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
		setFields();		
	}
	
	public void setFields() {
		nameField.setText(candidate.getFirstName() + " " + candidate.getLastName());
		municipalityField.setText(candidate.getMunicipality());
		rankField.setText(Integer.toString(candidate.getRank()));
		previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
		descriptionField.setText(candidate.getDescription());
		
	}
}
