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

public class ListCandidatesController extends SuperController {

	@FXML
	private TableView<Candidate> candidateTable;
	@FXML
	private TableColumn<Candidate, Integer> rankColumn;
	@FXML
	private TableColumn<Candidate, String> firstNameColumn;
	@FXML
	private TableColumn<Candidate, String> lastNameColumn;
	
	@FXML
	private Button nextButton;
	@FXML
	private Button backButton;

	/**
	 * Creates the ListCandidatesController object
	 */
	public ListCandidatesController() {
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

	/**
	 * Initialize the view.
	 */
	@FXML
	private void initialize() {
		rankColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("rank"));
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
	}
	
	@FXML
	private void handleNext() {
		super.viewController.showAddDatabaseView();
	}
	
	@FXML
	private void handleBack() {
		super.viewController.showStartMenu();
	}
	
	public void getAndFillTable() {
		ScoringList scoringList = super.mainApp.getScoringList();
		ObservableList<Candidate> candidates = scoringList.getCandidates();
		candidateTable.setItems(candidates);
	}
	
	
}
