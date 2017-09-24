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
	
	
	public AddCandidateController() {
		super();
	}
	
	
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
	 * Verify the input
	 */
	private boolean isInputValid() {
		// TODO
		return true;
	}
	
	/**
	 * Clean fields
	 */
	private void cleanFields() {
		nameField.setText("");
		descriptionField.setText("");
	}
	
	
	
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		
		ObservableList<Candidate> candidates = mainApp.getCandidates();
		System.out.println("Candidates: " + candidates);
		candidateTable.setItems(candidates);
	}
	
	@FXML
	private void initialize() {
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
	}
	
	
	
	
	@FXML
	private void cancel() {
		super.viewController.showStartMenu();
		
	}
	
	@FXML
	private void save() {
		// TODO: save list
	}
	
	@FXML
	private void next() {
		super.viewController.showAddDatabaseView();
	}
	
	
}
