package controllers;

import Main.MainApp;
import interfaces.DataSourceInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import model.DataSourceFile;

import java.util.List;

public class SettingsController {

	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField numCandidatesField;

	@FXML
	private GridPane gpSettings;
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void handleSave() {
		int numCandidates = Integer.parseInt(numCandidatesField.getText());
		mainApp.setNumCandidates(numCandidates);
		mainApp.showScoringListView();
	}
	
	@FXML
	private void handleCancel() {
		mainApp.showScoringListView();
	}

	public void refreshRegisterSelectors(List<DataSourceInterface> dsList) {
		//loops trough all DataSources
		int currentRow = 0;
		for (DataSourceInterface ds : dsList) {

			//loops trough all required inputs
			for (DataSourceFile dsf : ds.getRequiredFiles()) {
				Label dsfName = new Label(dsf.getName());
				TextField dsfFilePath = new TextField();
				Button dsfFileChooserButton = new Button("Velg fil");

				gpSettings.addRow(currentRow, dsfName);
				gpSettings.addRow(currentRow, dsfFilePath);
				gpSettings.addRow(currentRow, dsfFileChooserButton);

				currentRow++;
			}

			/*

			THIS IS THE OLD VERSION, only single file support

			//gets the name of the register and creates a label with it
			Label dsName = new Label(ds.getNameOfRegister());

			//adds an input text field
			TextField dsFilepath = new TextField();

			//adds a button that will open a filechooser
			Button dsFileChooserButton = new Button("Velg fil");

			System.out.println(ds);
			System.out.println(currentRow);
			gpSettings.addRow(currentRow, dsName);
			gpSettings.addRow(currentRow, dsFilepath);
			gpSettings.addRow(currentRow, dsFileChooserButton);
			*/

			currentRow++;
		}

	}
	
}
