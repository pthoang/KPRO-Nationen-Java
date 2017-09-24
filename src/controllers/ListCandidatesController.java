package controllers;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Candidate;

public class ListCandidatesController extends SuperController {
	
	@FXML
	private TableView<Candidate> candidateTable;
	@FXML
	private TableColumn<Candidate, String> firstNameColumn;
	@FXML
	private TableColumn<Candidate, String> lastNameColumn;
	
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
		
		ObservableList<Candidate> candidates = mainApp.getCandidates();
		System.out.println("Candidates: " + candidates);
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
}
