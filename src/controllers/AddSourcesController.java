package controllers;

import java.awt.Desktop;
import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddSourcesController {

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
		File file = mainApp.choseFileAndGetFile();
		System.out.println("Trying to add source");
	}
	@FXML
	private void fileChooser_tilskudd() {
		File file = mainApp.choseFileAndGetFile();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String line = "";
			String cvsSplitBy = ";";

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] organization = line.split(cvsSplitBy);

				System.out.println("Navn: "+ organization[1]);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handleBack() {
		System.out.println("Trying to go back, but the past is behind you");
	}

	public void handleNext() {
		System.out.println("Trying to go to the Next, but the future is inpredictable");
	}





}
