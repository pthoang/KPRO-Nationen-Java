package controllers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Main.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;
import model.StockInformation;

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

	}

	//Leaving this here for now, not sure where to put this
	private HashMap<String, ArrayList<StockInformation>> extractStockData(ObservableList<Candidate> candidates,
																		  File file){
		HashMap<String, ArrayList<StockInformation>> candidates_stockInformation =
				new HashMap<>();


		for (Candidate candidate:
			 candidates) {
			candidates_stockInformation.put(candidate.getName().toLowerCase(), new ArrayList<>());
		}


		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String csvSplit = ";";
			String[] fields = br.readLine().split(csvSplit);
			List<String> fieldsList = Arrays.asList(fields);

			int orgNoIndex = fieldsList.indexOf("selskap_orgnr");
			int orgNameIndex = fieldsList.indexOf("selskap_navn");
			int totalStocksIndex = fieldsList.indexOf("aksjer_totalt_selskapet");
			int stocksCandidateIndex = fieldsList.indexOf("aksjer_antall");
			int shareholdeNameIndex = fieldsList.indexOf("aksjonær_navn");

			String line;
			while((line = br.readLine()) != null) {

				String[] information = line.split(csvSplit);
				String shareholderName = information[shareholdeNameIndex].toLowerCase();

				if(candidates_stockInformation.containsKey(shareholderName)) {
					StockInformation stockInformation =
							new StockInformation(information[orgNoIndex], information[orgNameIndex],
													Integer.parseInt(information[totalStocksIndex]),
													Integer.parseInt(information[stocksCandidateIndex]));
					candidates_stockInformation.get(shareholderName).add(stockInformation);
				}
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}


		return candidates_stockInformation;
	}


}
