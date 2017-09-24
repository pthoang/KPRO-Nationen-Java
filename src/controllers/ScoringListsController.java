package controllers;

import java.util.Date;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ScoringList;

public class ScoringListsController extends SuperController {
	
	@FXML
	private TableView<ScoringList> scoringListsTable;
	@FXML
	private TableColumn<ScoringList, Date> createdColumn;
	@FXML
	private TableColumn<ScoringList, Date> lastChangedColumn;
	@FXML
	private TableColumn<ScoringList, Integer> yearColumn;
	@FXML
	private TableColumn<ScoringList, String> numberOfCandidatesColumn;
	
	/**
	 * Creates the ScoringListsController.
	 */
	public ScoringListsController() {
		super();
	}
	
	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		
		ObservableList<ScoringList> scoringLists = mainApp.getScoringLists();
		scoringListsTable.setItems(scoringLists);
	}
	
	@FXML
	private void initialize() {
		createdColumn.setCellValueFactory(cellData -> cellData.getValue().createdProperty());
		lastChangedColumn.setCellValueFactory(cellData -> cellData.getValue().lastChangedProperty());
		yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
		numberOfCandidatesColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfCandidatesProperty());
	}
}
