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

import model.Settings;

public class SettingsController {

	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField numCandidatesField;

	@FXML
	private GridPane gpSettings;
	
	@FXML
	private TextField bucketAccessKeyField;
	@FXML
	private TextField bucketSecretKeyField;
	@FXML
	private TextField bucketNameField;
	@FXML
	private TextField folderNameField;
	
	private MainApp mainApp;
	private Settings settings;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setSettings(Settings settings) {
		this.settings = settings;
		
		setDefaultSettings();
	}
	
	@FXML
	private void handleSave() {
		int numCandidates = Integer.parseInt(numCandidatesField.getText());
		settings.setNumCandidates(numCandidates);
		settings.setBucketAccessKey(bucketAccessKeyField.getText());
		settings.setBucketSecretKey(bucketSecretKeyField.getText());
		settings.setBucketName(bucketNameField.getText());
		settings.setFolderName(folderNameField.getText());
		
		mainApp.updateAmazonBucketUploader();
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

				//creates a textfield
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
	
	private void setDefaultSettings() {
		String numCandidates = Integer.toString(settings.getNumCandidates());
		numCandidatesField.setText(numCandidates);
		bucketAccessKeyField.setText(settings.getBucketAccessKey());
		bucketSecretKeyField.setText(settings.getBucketSecretKey());
		bucketNameField.setText(settings.getBucketName());
		folderNameField.setText(settings.getFolderName());
	}
	
}
