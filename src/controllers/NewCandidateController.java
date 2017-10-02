package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Candidate;
import model.ScoringList;

public class NewCandidateController extends SuperController {

	@FXML
	private TextField nameField;
	@FXML
	private TextArea descriptionField;
	@FXML
	private TextField rankField;
	@FXML
	private Button saveCanditateButton;
	
	ScoringList scoringList;

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
	private void handleSaveCandidate() {
		saveCandidate();
		
		
	}
	
	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		
		scoringList = super.mainApp.getScoringList();
	}

	private void saveCandidate() {
		if (isInputValid()) {
			String name = nameField.getText();
			String description = descriptionField.getText();
			String rank = rankField.getText();
			int rankNr = Integer.parseInt(rank);

			// TODO: automatically save it to this years list

			Candidate candidate = new Candidate(name, null, description, rankNr);

			scoringList.addCandidate(candidate);

			cleanFields();
		}
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