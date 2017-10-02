package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ScoringListController extends SuperController {

	@FXML
	private Button backButton;
	@FXML
	private Button saveButton;

	public ScoringListController() {
		super();
	}
	
	@FXML
	private void handleBack() {
		super.viewController.showAddDatabaseView();
	}
	
	@FXML
	private void handleSave() {
		super.mainApp.getScoringList().saveList();
		
	}
}
