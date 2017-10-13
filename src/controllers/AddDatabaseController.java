package controllers;

import java.awt.Desktop;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddDatabaseController {

	@FXML
	private Button nextButton;
	@FXML
	private Button fileChooserButton;
	@FXML
	private TextField loadedFileNameField;

	FileChooser fileChooser = new FileChooser();
	private MainApp mainApp;

	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void createOutput() {
		// TODO
	}

	/**
	 * Field updates when a file is loaded
	 */
	@FXML
	private void loadedFileNameField() {
		// TODO

	}

	/**
	 * Load the selected file
	 */
	@FXML
	private void fileChooser() {

		FileChooser fileChooser = new FileChooser();
		Desktop desktop = Desktop.getDesktop();

		Stage stage = mainApp.getStage();
		fileChooser.showOpenDialog(stage);
	}
	

}
