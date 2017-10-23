package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SettingsController {

	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField numCandidatesField;
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void handleSave() {
		int numCandidates = Integer.parseInt(numCandidatesField.getText());
		mainApp.setNumCandidates(numCandidates);
		mainApp.showScoringListView();
	}
	
	@FXML
	private void handleCancel() {
		mainApp.showScoringListView();
	}
	
}
