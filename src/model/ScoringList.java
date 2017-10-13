package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.google.gson.*;


public class ScoringList {

	private final SimpleIntegerProperty year;
	private ObservableList<Candidate> candidates;
	// TOOD: make as a model?
	private SimpleStringProperty juryDescription;

	final private int MAX_LENGTH = 100;

	/** 
	 * Create the ScoringList object.
	 * @param id
	 * @param year
	 */
	public ScoringList(int year) {
		this.year = new SimpleIntegerProperty(year);
		candidates = FXCollections.observableArrayList();
	}

	/**
	 * Get year
	 * @return int The year
	 */
	public int getYear() {
		return year.get();
	}

	/** 
	 * Get year
	 * @return SimpleIntegerProperty The year as a property
	 */
	public SimpleIntegerProperty yearProperty() {
		return year;
	}

	/**
	 * Add candidate
	 * @param candidate
	 */
	public void addCandidate(Candidate candidate) {
		candidates.add(candidate);
	}

	/**
	 * Get the length of the list with candidates
	 * @return int The size of the list
	 */
	public int getLength() {
		return candidates.size();
	}

	/**
	 * Get if the list is full based on MAX_LENGTH
	 * @return boolean If the length is MAX_LENGTH
	 */
	public boolean isFull() {
		return getLength() == MAX_LENGTH;
	}

	public SimpleStringProperty numberOfCandidatesProperty() {
		return new SimpleStringProperty(getLength() + "/" + MAX_LENGTH);
	}

	public void createFromNameList(String filePath) {
		// TODO: missing validation of file
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			readNameList(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void createFromPreviousList(String filePath) {
		// TODO
		try {
			readJson(filePath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveList() {
		// TODO: Save and download
	}
	
	public ObservableList<Candidate> getCandidates() {
		return candidates;
	}
	
	public void deleteCandidate(Candidate candidate) {
		candidates.remove(candidate);
	}
	
	// To  help during development
	public void printCandidates() {
		for (int i = 0; i < candidates.size(); i++) {
			System.out.println(candidates.get(i).getName());
		}
	}
	
	private void readNameList(Stream<String> stream) throws IOException {
		final AtomicInteger rank = new AtomicInteger(1);
		stream.forEach((name) -> {
			Candidate candidate = new Candidate(name, rank.get(), 0);
			candidates.add(candidate);
			rank.incrementAndGet();		
		});
	}

	private void readJson(String filepath) throws IOException {
		//loads the whole file into memory
		String content = new String(Files.readAllBytes(Paths.get(filepath)));

		//starts the parserto create json objects we can work with
		JsonParser parser = new JsonParser();
		JsonElement data = parser.parse(content);

		//this gets the list of people from the
		JsonArray people = (JsonArray)data.getAsJsonObject().get("people");

		int rank = 1;

		//loops trough the list and creates basic information for candidates
		for (JsonElement jsonCandidate : people) {
			//System.out.println(jsonCandidate.getAsJsonObject().get("firstName"));
			String name = jsonCandidate.getAsJsonObject().get("firstName").toString();
			//System.out.println(jsonCandidate.getAsJsonObject().get("lastYear"));

			//this is just a quickfix. needs error handling when they dont have a value from before
			int lastYear = 0; //Integer.parseInt(jsonCandidate.getAsJsonObject().get("lastYear").toString());

			//creates and add the candidate
			Candidate newCandidate = new Candidate(name, rank, lastYear);
			candidates.add(newCandidate);

			rank++;
			System.out.println(jsonCandidate.getAsJsonObject().get("firstName"));
		}

	}
}
