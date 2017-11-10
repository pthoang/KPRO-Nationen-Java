package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javafx.beans.property.SimpleIntegerProperty;
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

	public void createFromNameList(String filePath) {
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			readNameList(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void createFromPreviousList(String filePath) {
		try {
			readJson(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	private void readNameList(Stream<String> stream) throws IOException {
		final AtomicInteger rank = new AtomicInteger(1);
		stream.forEach((name) -> {
			Candidate candidate = new Candidate(name, rank.get());
			candidates.add(candidate);
			rank.incrementAndGet();		
		});
	}

	private void readJson(String filepath) throws IOException {
		// Loads the whole file into memory
		String content = new String(Files.readAllBytes(Paths.get(filepath)));

		// Starts the parser to create json objects we can work with
		JsonParser parser = new JsonParser();
		JsonElement data = parser.parse(content);

		// Gets the list of people from the
		JsonArray people = (JsonArray)data.getAsJsonObject().get("people");

		int rank = 1;

		// Loops trough the list and creates basic information for candidates
		for (JsonElement jsonCandidate : people) {
			String name = jsonCandidate.getAsJsonObject().get("firstName").toString();

			// TODO
			//this is just a quickfix. needs error handling when they don't have a value from before
			int lastYear = 0; //Integer.parseInt(jsonCandidate.getAsJsonObject().get("lastYear").toString());

			// Creates and add the candidate
			Candidate newCandidate = new Candidate(name, rank);
			// newCandidate.setLastYearRank(lastYear);
			candidates.add(newCandidate);

			rank++;
		}
	}

	public void empty() {
        candidates = null;
        Jury.getOrCreateInstance().empty();
    }
}
