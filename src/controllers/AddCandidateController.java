package controllers;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Candidate;

public class AddCandidateController extends SuperController {

	@FXML
	private TextField nameField;
	@FXML
	private Button saveCandidateButton;
	@FXML
	private TextArea descriptionField;
	
	@FXML
	private TableView<Candidate> candidateTable;
	@FXML
	private TableColumn<Candidate, String> firstNameColumn;
	@FXML
	private TableColumn<Candidate, String> lastNameColumn;
	
	@FXML
	private Button cancelButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button nextButton;
	
	/**
	 * Creates the AddCandidateController object.
	 */
	public AddCandidateController() {
		super();
	}
	
	/**
	 * Called when pushed the saveCandidate button in view.
	 * Saves the candidate if input is valid.
	 */
	@FXML
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
	 * Set the mainApp, then get the list of candidates and set them in the table.
	 * @param mainApp
	 */
	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		
		ObservableList<Candidate> candidates = mainApp.getCandidates();
		candidateTable.setItems(candidates);
	}
	
	/**
	 * Initialize the view.
	 */
	@FXML
	private void initialize() {
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
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
