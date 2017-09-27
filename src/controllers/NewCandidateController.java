package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Candidate;

public class NewCandidateController extends SuperController {

	@FXML
	private TextField nameField;
	@FXML
	private TextArea descriptionField;
	@FXML
	private Button saveAndAddMoreCandidatesButton;
	@FXML
	private Button saveCanditateAndGoBackButton;
	
	/**
	 * Creates the AddCandidateController object.
	 */
	public NewCandidateController() {
		super();
		System.out.println("NewCandidateController in class: " + this);
	}
	
	
	
	/**
	 * Called when pushed the saveCandidate button in view.
	 * Saves the candidate if input is valid.
	 */
	@FXML
	private void handleSaveAndAddMore() {
		saveCandidate();
	}
	
	private void saveCandidate() {
		if (isInputValid()) {
			String name = nameField.getText();
			String description = descriptionField.getText();
			
			// TODO: automatically save it to this years list
			
			Candidate candidate = new Candidate(name, null, description);
			
			super.mainApp.addCandidate(candidate);
			
			cleanFields();
		}
	}
	
	@FXML
	private void handleSaveAndGoBack() {
		saveCandidate();
		viewController.showStartMenu();
	}
	
	/**
	 * Verify the input.
	 */
	// TODO: can be improved
	private boolean isInputValid() {
		String name = nameField.getText();
		String description = descriptionField.getText();
		if (name.length() > 2 & description.length() > 20) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Clean fields
	 */
	private void cleanFields() {
		nameField.setText("");
		descriptionField.setText("");
	}
	
	
	/**
	 * Initialize the view.
	 */
	@FXML
	private void initialize() {
	}

}