package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class StartMenuController extends SuperController {
	
	
	@FXML 
	private Button newListButton;
	@FXML
	private Button listCandidatesButton;
	@FXML 
	private Button previousListsButton;
	
	/**
	 * Creates the startMenuController object.
	 */
	public StartMenuController() {
		super();
	}
	
	/**
	 * Initializes the controller class. 
	 * The method is automatically called after the fxml file has been loaded."
	 */
	@FXML
	private void initialize() {
		
	}
	
	/**
	 * Showes view for when the user wants to create a new list.
	 */
	@FXML
	private void showNewList() {
		viewController.showNewListView();
	}
	
	/**
	 * Shows the view for when the user wants to see previous lists.
	 */
	@FXML
	private void showPreviousLists() {
		viewController.showPreviousListsView();
	}
	
	/**
	 * Shows the view for when the user wants to see existing candidates.
	 */
	@FXML
	private void showListCandidates() {
		viewController.showListCandidatesView();
	}
}
