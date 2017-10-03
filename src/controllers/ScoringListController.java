package controllers;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Candidate;
import model.ScoringList;

public class ScoringListController extends SuperController {

	@FXML
	private ListCandidatesController listCandidatesController;
	@FXML
	private CandidateController candidateController;
	
	@FXML
	private Button backButton;
	@FXML
	private Button saveButton;
	
	
	@FXML
	private TableView<Candidate> candidateTable;
	@FXML
	private TableColumn<Candidate, Integer> rankColumn;
	@FXML
	private TableColumn<Candidate, String> firstNameColumn;
	@FXML
	private TableColumn<Candidate, String> lastNameColumn;
	
	
	public ScoringListController() {
		super();
	}
	
	/**
	 * Set mainApp in super, then gets the candidates and shows them in the table.
	 * @params mainApp
	 */
	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);

		getAndFillTable();
	}
	
	
	
	public void getAndFillTable() {
		ScoringList scoringList = super.mainApp.getScoringList();
		ObservableList<Candidate> candidates = scoringList.getCandidates();
		candidateTable.setItems(candidates);
	}
	
	@FXML 
	private void initialize() {
		rankColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("rank"));
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
	
		candidateTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> candidateController.setCandidate(newValue));		
	}
	
	@FXML
	private void handleBack() {
		super.viewController.showStartMenu();
	}
	
	@FXML
	private void handleSave() {
		super.mainApp.getScoringList().saveList();	
	}
}
