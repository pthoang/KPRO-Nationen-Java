package controllers;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




import Main.MainApp;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.Candidate;
import model.PoliticInformation;
import model.ShareholderInformation;

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





	//Leaving this here for now, not sure where to put this


	private Map<String, String>
	extractPoliticInformation(List<Candidate> candidates) throws Exception {
		HashMap<String, PoliticInformation> candidate_politicInformation = new HashMap<>();

		for (Candidate candidate	:
			 	candidates) {
			candidate_politicInformation.put(candidate.getName().toLowerCase(), null);
		}


		Gson gson = new Gson();

		String representativeUrl = "https://data.stortinget.no/eksport/dagensrepresentanter?format=json";
		String representativeJson = readUrl(representativeUrl);

		RepResponse repResponse = gson.fromJson(representativeJson, RepResponse.class);

		for (Representative rep :
				repResponse.dagensrepresentanter_liste) {
			String repFullName = (rep.fornavn + " " + rep.etternavn).toLowerCase();
			if(candidate_politicInformation.containsKey(repFullName)) {
				List<String> committees = rep.komiteer_liste.stream()
						.map(committee -> committee.navn).collect(Collectors.toList());
				PoliticInformation politicInformation =
						new PoliticInformation(null, rep.parti.navn, committees, null);
				String json = gson.toJson(politicInformation);
				candidate_politicInformation.put(repFullName, politicInformation);
			}
		}

		String governmentUrl = "https://data.stortinget.no/eksport/regjering?format=json";
		String governmentJson = readUrl(governmentUrl);

		GovResponse govResponse = gson.fromJson(governmentJson, GovResponse.class);

		for (GovMember govMember :
				govResponse.regjeringsmedlemmer_liste) {
			String govMemberFullName = (govMember.fornavn + " " + govMember.etternavn).toLowerCase();
			if(candidate_politicInformation.containsKey(govMemberFullName)) {
				PoliticInformation politicInformation = candidate_politicInformation.get(govMemberFullName);
				if(politicInformation != null) {
					politicInformation.setTitle(govMember.tittel);
					politicInformation.setMinistry(govMember.departement);
				} else {
					politicInformation = new PoliticInformation(govMember.tittel, govMember.parti.navn,
							null, govMember.departement);
					candidate_politicInformation.put(govMemberFullName, politicInformation);
				}
			}
		}

		return candidate_politicInformation.entrySet().stream()
				.collect(Collectors.toMap(p->p.getKey(), p->gson.toJson(p.getValue())));
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;

		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1) {
				buffer.append(chars, 0, read);

			}
			return buffer.toString();
		} finally {
			if(reader != null) {
				reader.close();
			}
		}

	}

	static class RepResponse {
		List<Representative> dagensrepresentanter_liste;
	}

	static class GovResponse {
		List<GovMember> regjeringsmedlemmer_liste;
	}

	static class Representative {
		String etternavn;
		String fornavn;
		String id;
		int kjoenn;
		Municipality fylke;
		PoliticalParty parti;
		List<Committee> komiteer_liste;
	}

	static class GovMember {
		String etternavn;
		String fornavn;
		String id;
		int kjoenn;
		String departement;
		PoliticalParty parti;
		String tittel;
	}

	static class PoliticalParty {
		String id;
		String navn;
	}

	static class Municipality {
		String id;
		String navn;
	}

	static class Committee {
		String id;
		String navn;
	}

	//Just for testing, remove before merging with dev
	public static void main(String[] args) throws Exception{
		List<Candidate> candidates = new ArrayList<>();
		AddSourcesController cont = new AddSourcesController();

		candidates.add(new Candidate("Erlend Larsen", null, null, 1));
		candidates.add(new Candidate("Siv Jensen", null, null, 1));
		candidates.add(new Candidate("Phi Thien Hoang", null, null, 1));
		candidates.add(new Candidate("Jon Georg Dale", null, null, 1));

		System.out.println(cont.extractPoliticInformation(candidates));



	}

}


