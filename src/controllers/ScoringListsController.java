package controllers;

import java.util.Date;

import Main.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ScoringList;
import util.DateUtil;

public class ScoringListsController extends SuperController {
	
	@FXML
	private TableView<ScoringList> scoringListsTable;
	@FXML
	private TableColumn<ScoringList, String> createdColumn;
	@FXML
	private TableColumn<ScoringList, String> lastChangedColumn;
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
		createdColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty property = new SimpleStringProperty();
			property.setValue(DateUtil.dateToString(cellData.getValue().getCreatedDate()));
			return property;
		});
		lastChangedColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty property = new SimpleStringProperty();
			property.setValue(DateUtil.dateToString(cellData.getValue().getLastChangedDate()));
			return property;
		});
		yearColumn.setCellValueFactory(new PropertyValueFactory<ScoringList, Integer>("year"));
		numberOfCandidatesColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfCandidatesProperty());
	}
}
