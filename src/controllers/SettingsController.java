package controllers;

import Main.MainApp;
import interfaces.DataSourceInterface;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.DataSourceFile;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
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

	//this is the function that is supposed to spawn a file chooser and update the filepath for required files
	@FXML private void handleLoadFile(ActionEvent event) throws IOException {
		System.out.println("event " + event);
	}

	public void refreshRegisterSelectors(List<DataSourceInterface> dsList) {
		//loops trough all DataSources. not quite sure why this needs to start at 1, but it looks a whole lot prettier
		int currentRow = 1;
		for (DataSourceInterface ds : dsList) {

			//loops trough all required inputs
			for (DataSourceFile dsf : ds.getRequiredFiles()) {
				//creates a description for the input
				Label dsfName = new Label(dsf.getName());

				//creates a textfield. todo. add update handler
				TextField dsfFilePath = new TextField();
				dsfFilePath.textProperty().addListener((observable, oldValue, newValue) -> {
					dsf.setFilepath(newValue);
				});

				//creates the button for the file chooser
				Button dsfFileChooserButton = new Button("Velg fil");
				//sets the path of the chosen file to the textfield
				dsfFileChooserButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
					@Override
					public void handle(javafx.event.ActionEvent event) {
						//opens file chooser
						File file = mainApp.choseFileAndGetFile();
						//makes sure that we didnt hit cancel
						if (file != null) {
							//sets the path of the chosen file to the text input field
							dsfFilePath.setText(file.getAbsolutePath());
						}
					}
				});

				gpSettings.addRow(currentRow, dsfName);
				gpSettings.addRow(currentRow, dsfFilePath);
				gpSettings.addRow(currentRow, dsfFileChooserButton);

				currentRow++;
			}
		}

	}
	
}
