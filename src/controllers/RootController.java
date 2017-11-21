package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import Main.MainApp;
import model.Candidate;
import model.ScoringList;
import model.Utility;

public class RootController {

	private MainApp mainApp;


	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}	

	@FXML
	private void showLoadList() {
		File file = mainApp.chooseAndGetFile();
		createScoringListBasedOnFileType(file);

		ScoringListController.getOrCreateInstance().fillTable();
	}
	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}


	@FXML
	private void createJsonList() throws IOException{
		File file = mainApp.chooseAndGetFile();
		String content = readFile(file.getPath(), StandardCharsets.UTF_8);

		JsonParser parser = new JsonParser();
		JsonElement data = parser.parse(content);

		JsonArray people = (JsonArray)data.getAsJsonObject().get("people");

		ScoringList.getOrCreateInstance().totalEmpty();
		int rank = 1;

		// Loops trough the list and creates basic information for candidates
		for (JsonElement jsonCandidate : people) {
			String name = jsonCandidate.getAsJsonObject().get("firstName").toString();
			name = name.substring(1, name.length()-1);
			SimpleIntegerProperty lastYear = new SimpleIntegerProperty(); //Integer.parseInt(jsonCandidate.getAsJsonObject().get("lastYear").toString());
			SimpleStringProperty description = new SimpleStringProperty();
			SimpleStringProperty residence = new SimpleStringProperty();
			SimpleStringProperty twitter = new SimpleStringProperty();


			// Creates and add the candidate
			Candidate newCandidate = new Candidate(name, rank);

			//description
			try {
				String temp = jsonCandidate.getAsJsonObject().get("bio").toString();
				temp = temp.substring(1, temp.length()-1);
				description.setValue(temp);
				newCandidate.setDescription(description);
			}
			catch (Exception e){
				System.out.println("No description");
			}

			//imgurl
			try {
				String temp = jsonCandidate.getAsJsonObject().get("img").toString();
				temp = temp.substring(1, temp.length()-1);
				newCandidate.setImageName(temp);
				newCandidate.getImageIsInBucket();
			}
			catch (Exception e){
				System.out.println("No img");
			}

			//lastyear
			try {

				lastYear.setValue(jsonCandidate.getAsJsonObject().get("lastYear").getAsInt());
				newCandidate.setPreviousYearRank(lastYear);

			}
			catch (Exception e){
				System.out.println("No lastyear");
			}

			//birthyear

			//todo

			//gender
			try {
				String temp = jsonCandidate.getAsJsonObject().get("gender").toString();
				temp = temp.substring(1, temp.length()-1);
				if (temp.equals("none")){
					temp = "O";
				}
				else{
					temp = temp.toUpperCase();
				}

				newCandidate.setGender(temp);
			}
			catch (Exception e){
				System.out.println("No gender");
			}

			//profession
			try {
				String temp = jsonCandidate.getAsJsonObject().get("profession").toString();
				temp = temp.substring(1, temp.length()-1);
				newCandidate.setProfession(temp);

			}
			catch (Exception e){
				System.out.println("No profession");
			}

			//residence
			try {
				String temp = jsonCandidate.getAsJsonObject().get("residence").toString();
				temp = temp.substring(1, temp.length()-1);
				residence.setValue(temp);
				newCandidate.setMunicipality(residence);

			}
			catch (Exception e){
				System.out.println("No residence");
			}

			//twitter
			try {
				String temp = jsonCandidate.getAsJsonObject().get("twitterAcnt").toString();
				temp = temp.substring(1, temp.length()-1);
				twitter.setValue(temp);
				newCandidate.setTwitter(twitter);
			}
			catch (Exception e){
				System.out.println("No twitter");
			}

			ScoringList.getOrCreateInstance().addCandidate(newCandidate);

			rank++;
		}


		ScoringListController.getOrCreateInstance().fillTable();
		ScoringListController.getOrCreateInstance().refreshTable();
	}


	private void createScoringListBasedOnFileType(File file) {
		String filePath = file.getAbsolutePath();
		ScoringList scoringList = ScoringList.getOrCreateInstance();

		if (filePath.toLowerCase().endsWith(".json")) {
			scoringList.createFromPreviousList(filePath);
		} else if (filePath.toLowerCase().endsWith(".txt")) {
			scoringList.createFromNameList(file);
		} else {
			String headerText = "Det er ikke en godkjent filtype.";
			String contentText = "Filtypen må være enten .txt eller .json";
			Utility.newAlertError(headerText, contentText);
			System.out.println("Error: invalid file");
		}
	}

	@FXML
	private void showAddJury(){
		mainApp.showJuryAdmin();
	}

	@FXML
	private void showNewAndEmpty() {
		mainApp.newList();
	}

	@FXML
	private void showSettings() {
		mainApp.showSettingsView();
	}	

}
