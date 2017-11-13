package controllers;

import Main.MainApp;
import interfaces.DataSourceInterface;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import model.*;

import java.io.*;
import java.util.List;

public class SettingsController {

	@FXML
	private GridPane gpSettings;

	@FXML
	private TextField numCandidatesField = new TextField();
	@FXML
	private TextField numConnectionsField = new TextField();
	@FXML
	private TextField bucketAccessKeyField = new TextField();
	@FXML
	private TextField bucketSecretKeyField = new TextField();
	@FXML
	private TextField bucketNameField = new TextField();
	@FXML
	private TextField folderNameField = new TextField();
	@FXML
	private TextArea aboutScoringField = new TextArea();
	
	private MainApp mainApp;
	private Settings settings;

	public SettingsController() {
		mainApp = MainApp.getInstance();
		settings = Settings.getOrCreateInstance();

	}

	@FXML
	private void initialize() {
		setDefaultSettings();

	}

	@FXML
	private void handleSave() {
		int numCandidates = Integer.parseInt(numCandidatesField.getText());
		settings.setNumCandidates(numCandidates);
		int numConnections = Integer.parseInt(numConnectionsField.getText());
		settings.setNumConnections(numConnections);
		CandidateController.getOrCreateInstance().updateNumConnections();

		settings.setBucketAccessKey(bucketAccessKeyField.getText());
		settings.setBucketSecretKey(bucketSecretKeyField.getText());
		settings.setBucketName(bucketNameField.getText());
		settings.setFolderName(folderNameField.getText());


		String aboutScoringText = aboutScoringField.getText();
		ScoringList.getOrCreateInstance().setAboutTheScoring(aboutScoringText);
		updateAmazonBucketUploader();
		mainApp.showEditListView();
	}
	
	@FXML
	private void handleCancel() {
		mainApp.showEditListView();
	}


	public void refreshRegisterSelectors(List<DataSourceInterface> dsList) {
		// Loops trough all DataSources. not quite sure why this needs to start at 1, but it looks a whole lot prettier
		int currentRow = 1;
		for (DataSourceInterface ds : dsList) {

			// Loops trough all required inputs
			for (DataSourceFile dsf : ds.getRequiredFiles()) {
				// Creates a description for the input
				Label dsfName = new Label(dsf.getName());

				//creates a textfield

				TextField dsfFilePath = new TextField();
				dsfFilePath.textProperty().addListener((observable, oldValue, newValue) -> {
					dsf.setFilepath(newValue);
				});

				// Creates the button for the file chooser
				Button dsfFileChooserButton = new Button("Velg fil");
				// Sets the path of the chosen file to the textfield
				dsfFileChooserButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
					@Override
					public void handle(javafx.event.ActionEvent event) {
						// Opens file chooser
						File file = mainApp.chooseAndGetFile();
						// Makes sure that we didn't hit cancel
						if (file != null) {
							// Sets the path of the chosen file to the text input field
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

		String numConnections = Integer.toString(settings.getNumConnections());
		numConnectionsField.setText(numConnections);

		bucketAccessKeyField.setText(settings.getBucketAccessKey());
		bucketSecretKeyField.setText(settings.getBucketSecretKey());
		bucketNameField.setText(settings.getBucketName());
		folderNameField.setText(settings.getFolderName());
	}

	private void updateAmazonBucketUploader() {
		AmazonBucketUploader bucketUploader = AmazonBucketUploader.getOrCreateInstance();
		bucketUploader.setBucketName(settings.getBucketName());
		bucketUploader.setFolderName(settings.getFolderName());
		bucketUploader.setKeys(settings.getBucketAccessKey(), settings.getBucketSecretKey());
		if (!bucketUploader.isAccessible()) {
			String headerText = "Nøklene er ikke gyldig.";
			String contentText = "Dette betyr du ikke vil få lastet bildene opp i bucketen. Vennligst skriv inn rett nøkler.";
			Utility.newAlertError(headerText, contentText);
		}
	}

}
