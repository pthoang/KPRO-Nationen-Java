package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Candidate;

public class CandidateController extends SuperController {

	@FXML
	private Button saveButton;
	@FXML
	private Button deleteButton;
	
	@FXML
	private TextField nameField = new TextField();
	@FXML
	private TextField lastYearRankField  = new TextField();
	@FXML
	private TextField rankField  = new TextField();
	@FXML
	private TextArea descriptionField  = new TextArea();
	@FXML
	private TextField addressField  = new TextField();
	
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	public void handleSave() {
		
	}
	
	@FXML
	public void handleDelete() {
		
	}
	
	@FXML
	public void handleChangeName() {
		
	}
	
	@FXML
	public void handleChangeLastYearRank() {
		
	}
	
	@FXML
	public void handleChangeRank() {
		
	}
	
	@FXML
	public void handleChangeAddressField() {
		
	}
	
	public void setCandidate(Candidate candidate) {
	
		nameField.setText(candidate.getFirstName() + candidate.getLastName());
		rankField.setText(Integer.toString(candidate.getRank()));
		descriptionField.setText(candidate.getDescription());
		
	}
}
