package controllers;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Candidate;
import model.ScoringList;

public class ListCandidatesController extends SuperController {

	@FXML
	private TableView<Candidate> candidateTable;
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

		ScoringList scoringList = mainApp.getScoringList();
		ObservableList<Candidate> candidates = scoringList.getCandidates();
		for (int i = 0; i < candidates.size(); i++) {
			System.out.println(candidates.get(i).getFirstName() + candidates.get(i).getLastName());

		}
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
	
	@FXML
	private void handleNext() {
		super.viewController.showAddDatabaseView();
	}
	
	@FXML
	private void handleBack() {
		super.viewController.showStartMenu();
	}
}
