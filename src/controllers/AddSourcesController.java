package controllers;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import Main.MainApp;
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

	}

	//Leaving this here for now, not sure where to put this
	private HashMap<String, ArrayList<ShareholderInformation>>
	extractShareholderData(List<Candidate> candidates, FileReader fileReader){

		HashMap<String, ArrayList<ShareholderInformation>>candidates_shareholderInformation = new HashMap<>();


		for (Candidate candidate:
			 candidates) {
			candidates_shareholderInformation.put(candidate.getName().toLowerCase(), new ArrayList<>());

		}


		try (BufferedReader br = new BufferedReader(fileReader)) {

			String csvSplit = ";";
			String[] fields = br.readLine().replaceAll("[^a-zA-Z0-9;_]+", "").split(csvSplit);
			List<String> fieldsList = Arrays.asList(fields);

			int orgNoIndex = fieldsList.indexOf("selskap_orgnr");
			int orgNameIndex = fieldsList.indexOf("selskap_navn");
			int totalStocksIndex = fieldsList.indexOf("aksjer_totalt_selskapet");
			int stocksCandidateIndex = fieldsList.indexOf("aksjer_antall");
			int shareholderNameIndex = fieldsList.indexOf("aksjonr_navn");
			System.out.println(shareholderNameIndex);
			String line;
			while((line = br.readLine()) != null) {

				String[] information = line.split(csvSplit);
				String shareholderName = information[shareholderNameIndex].toLowerCase();

				if(candidates_shareholderInformation.containsKey(shareholderName)) {
					ShareholderInformation shareholderInformation =
							new ShareholderInformation(information[orgNoIndex], information[orgNameIndex],
													new BigInteger(information[totalStocksIndex]),
													Integer.parseInt(information[stocksCandidateIndex]));
					candidates_shareholderInformation.get(shareholderName).add(shareholderInformation);
				}
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}


		return candidates_shareholderInformation;
	}

	private Map<String, PoliticInformation>
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
				.filter(map -> map.getValue() != null).collect(Collectors.toMap(p->p.getKey(), p->p.getValue()));
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

		System.out.println(cont.extractPoliticInformation(candidates));


		System.out.println(cont.extractShareholderData(candidates,
				new FileReader("C:/Users/vicen/Downloads/AR2015.csv")));

	}

}
