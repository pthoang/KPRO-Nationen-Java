package controllers;

import java.awt.*;

import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.util.*;


import Main.MainApp;

import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;
import model.ScoringList;
import model.Organization;

public class AddSourcesController {

	@FXML
	private Button nextButton;
	@FXML
	private Button fileChooserButton;
	@FXML
	private TextField loadedFileNameField;

	FileChooser fileChooser = new FileChooser();
	private MainApp mainApp;

	private File folder = null;

	private ObservableList<Candidate> candidates;

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

		this.folder = mainApp.choseFolderAndGetFiles();

	}

	public void handleBack() {
		System.out.println("Trying to go back, but the past is behind you");
	}

	public void handleNext() {

		this.candidates = this.mainApp.getScoringList().getCandidates();




		//Adds subsidiaries to candidates if they own shares in the company
		BufferedReader bufferedReader = null;
		try {
			if (folder.isDirectory()) {
				for (File file : folder.listFiles()) {
					FileReader fileReader = new FileReader(file);
					bufferedReader = new BufferedReader(fileReader);
					String line = "";
					String cvsSplitBy = ";";
					while ((line = bufferedReader.readLine()) != null) {
						// use comma as separator
						String[] sub_organization = line.split(cvsSplitBy);
						System.out.println("Navn: " + sub_organization[1]);

						for (Candidate candidate : this.candidates) {
							java.util.List<Organization> organizations = candidate.getOrganizations();
							for (Organization organization : organizations){
								if (organization.getOrg_number() == sub_organization[0]){
									if (Integer.parseInt(sub_organization[3])>0){
										//Increase Animals
									}
									if (Integer.parseInt(sub_organization[4])>0){
										//Increase Animals
									}
									if (Integer.parseInt(sub_organization[5])>0){
										//Increase Animals
									}
									if (Integer.parseInt(sub_organization[6])>0){
										//Increase Animals
									}
									if (Integer.parseInt(sub_organization[7])>0){
										//Increase Farming
									}
									if (Integer.parseInt(sub_organization[8])>0){
										//Increase Farming
									}
									if (Integer.parseInt(sub_organization[9])>0){
										//Increase Animals
									}
									if (Integer.parseInt(sub_organization[10])>0){
										//Increase Animals
									}
									if (Integer.parseInt(sub_organization[11])>0){
										//Increase Hired help
									}

								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		//End of subsidies loading

		for (Candidate candidate : this.candidates) {
				if (candidate.getAnimalsPG()>0){
					for (Candidate candidate2 : this.candidates){
						if (candidate2.getAnimalsPG()>0 && candidate.getName() != candidate2.getName()) {
							candidate.addConnection(candidate2,candidate2.getName()+" har også");
						}
				}

				}
			}

	}







}


