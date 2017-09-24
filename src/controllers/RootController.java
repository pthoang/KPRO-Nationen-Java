package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootController extends SuperController {
	
	@FXML
	private MenuItem addCandidateItem;
	@FXML
	private MenuItem existingScoringListItem;
	@FXML
	private MenuItem existingCandidatesItem;
	
	@FXML
	private MenuItem aboutItem;
	@FXML
	private MenuItem settingsItem;
	

	/**
	 * Create the RootController object.
	 */
	public RootController() {
		super();
	}
	
	@FXML
	private void showNewScoringList() {
		super.viewController.showNewListView();
	}
	
	@FXML
	private void showAddCandidate() {
		super.viewController.showNewListView();
		
	}
	
	@FXML
	private void showExistingScoringLists() {
		super.viewController.showPreviousListsView();
	}
	
	@FXML
	private void showExistingCandidates() {
		super.viewController.showListCandidatesView();
	}
	
	@FXML
	private void showAbout() {
		// TODO
	}
	
	@FXML
	private void showSettings() {
		// TODO
	}
}
