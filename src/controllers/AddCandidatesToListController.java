package controllers;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Candidate;

public class AddCandidatesToListController extends SuperController {

	@FXML
	private Button cancelButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button nextButton;

	@FXML
	private NewCandidateController newCandidateController;
	@FXML
	private ListCandidatesController listCandidatesController;

	/**
	 * Creates the AddCandidateController object.
	 */
	public AddCandidatesToListController() {
		super();
	}

	/**
	 * Set the mainApp, then get the list of candidates and set them in the table.
	 * @param mainApp
	 */
	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);

		System.out.println("NewCandidateController when setting MainApp: " + newCandidateController);
		newCandidateController.setMainApp(mainApp);
		listCandidatesController.setMainApp(mainApp);
	}

	@Override
	public void setViewController(ViewController viewController) {
		super.setViewController(viewController);

		System.out.println("NewCandidateController when setting ViewController: " + newCandidateController);
		newCandidateController.setViewController(viewController);
		listCandidatesController.setViewController(viewController);
	}

	/**
	 * Initialize the view.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * Called when the cancelButton is clocked. Shows the startMenu.
	 */

	@FXML
	private void cancel() {
		super.viewController.showStartMenu();	
	}

	/**
	 * Called when the saveButton is clicked. Saves the list.
	 */
	@FXML
	private void save() {
		// TODO: save list
	}

	/**
	 * Called when the nextButton is clicked. Moves on to showing databases.
	 */
	@FXML
	private void next() {
		super.viewController.showAddDatabaseView();
	}
}
