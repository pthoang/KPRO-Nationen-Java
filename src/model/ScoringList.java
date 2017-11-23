package model;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedReader;
import java.util.Comparator;

import controllers.CandidateController;
import controllers.ScoringListController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.google.gson.*;


public class ScoringList {

	private static ScoringList instance = null;

	private final SimpleIntegerProperty year;
	private String aboutTheScoring;
	private ObservableList<Candidate> candidates;


	public ScoringList() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		this.year = new SimpleIntegerProperty(year);
		candidates = FXCollections.observableArrayList();
	}

	public static ScoringList getOrCreateInstance() {
		if (instance == null) {
			instance = new ScoringList();
		}
		return instance;
	}

	public int getLength() {
		return candidates.size();
	}

	public void createFromNameList(File file) {
		InputStream stream = Utility.convertFileToStream(file);
		readNameList(stream);
	}

	public void createFromPreviousList(File file) {
		String content = "";
		try {
			content = readFile(file.getPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Error: could not read the json-file");
		}

		JsonParser parser = new JsonParser();
		JsonElement data = parser.parse(content);

		JsonArray people = (JsonArray)data.getAsJsonObject().get("people");

		ScoringList.getOrCreateInstance().totalEmpty();
		int rank = 1;

		// Loops trough the list and creates basic information for candidates
		for (JsonElement jsonCandidate : people) {
			String name = jsonCandidate.getAsJsonObject().get("fullName").toString();
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
				if(!temp.isEmpty()) {
					newCandidate.getBucketImageURL();
					newCandidate.setImageIsInBucket(true);
				}

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

			try {
				newCandidate.setYearOfBirth(jsonCandidate.getAsJsonObject().get("birthYear").getAsString());
			}
			catch (Exception e){
				System.out.println("No birthyear");
			}

			//Title

			try {
				newCandidate.setTitle(jsonCandidate.getAsJsonObject().get("title").getAsString());
			}
			catch (Exception e){
				System.out.println("No title");
			}

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
				if(temp.equals("null")){
					temp = "";
				}
				else {
					temp = temp.substring(1, temp.length() - 1);
				}
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

	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public void addCandidate(Candidate candidate) {
		candidates.add(candidate);
	}

	public ObservableList<Candidate> getCandidates() {
		return candidates;
	}

	public void deleteCandidate(Candidate candidate) {
		candidates.remove(candidate);
	}

	public void setAboutTheScoring(String aboutTheScoring) {
		this.aboutTheScoring = aboutTheScoring;
	}

	public String getAboutTheScoring() {
		return aboutTheScoring;
	}

	private void readNameList(InputStream stream) {
		candidates.clear();
		final AtomicInteger rank = new AtomicInteger(1);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			String name;
			while ((name = br.readLine()) != null) {
				Candidate candidate = new Candidate(name, rank.get());
				candidates.add(candidate);
				rank.incrementAndGet();
			}
		} catch (IOException e) {
			System.out.println("Could not read list of names from file: " + e);
		}
	}

	public void empty() {
        candidates = FXCollections.observableArrayList();
        candidates.add(new Candidate("", 1));
        CandidateController.getOrCreateInstance().setCandidate(candidates.get(0));
        Jury.getOrCreateInstance().empty();
    }
	public void totalEmpty() {
		candidates = FXCollections.observableArrayList();
		Jury.getOrCreateInstance().empty();
	}

	public void updateRanksWhenDeletedCandidate() {
		sortListByRank();
		compactifyRanks();
	}

	public void updateRankWhenChangedRank(Candidate candidate, int oldRank, int newRank) {

        if (newRank < oldRank) {
            changeRankForCandidates(newRank, oldRank, true);
            candidate.setRank(new SimpleIntegerProperty(newRank));
        } else if (newRank > oldRank) {
            changeRankForCandidates(oldRank, newRank + 1, false);
            candidate.setRank(new SimpleIntegerProperty(newRank));
        }

		sortListByRank();
		compactifyRanks();
	}

	private void changeRankForCandidates(int fromRank, int toRank, boolean increase) {
	    sortListByRank();
	    for (Candidate c : candidates) {
	        int rank = c.getRank();
	        if (rank > toRank) {
	            break;
            } else if(rank >= fromRank && rank < toRank) {
	            if (increase) {
                    c.setRank(new SimpleIntegerProperty(rank + 1));
                } else {
                    c.setRank(new SimpleIntegerProperty(rank - 1));
                }
            }
        }
    }

	private void sortListByRank() {
		Comparator<Candidate> comparator = Comparator.comparingInt(Candidate:: getRank);
		FXCollections.sort(candidates, comparator);
	}

	private void compactifyRanks() {
		int nextFreeRank = 1;
		for (Candidate candidate : candidates) {
			if (candidate.getRank() < nextFreeRank) {
				nextFreeRank += 1;
				candidate.setRank(new SimpleIntegerProperty(nextFreeRank));
			} else if (candidate.getRank() > nextFreeRank) {
				candidate.setRank(new SimpleIntegerProperty(nextFreeRank));
				nextFreeRank += 1;
			} else {
				nextFreeRank += 1;
			}
		}
	}
}
