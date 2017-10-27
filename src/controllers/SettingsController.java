package controllers;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Settings;

public class SettingsController {

	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField numCandidatesField;
	
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
	
	private void setDefaultSettings() {
		String numCandidates = Integer.toString(settings.getNumCandidates());
		numCandidatesField.setText(numCandidates);
		bucketAccessKeyField.setText(settings.getBucketAccessKey());
		bucketSecretKeyField.setText(settings.getBucketSecretKey());
		bucketNameField.setText(settings.getBucketName());
		folderNameField.setText(settings.getFolderName());
	}
	
}
