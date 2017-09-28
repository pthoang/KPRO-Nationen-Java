package model;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;


public class ScoringList {

	private final SimpleIntegerProperty id;
	private ObjectProperty<LocalDate> created;
	private ObjectProperty<LocalDate> lastChanged;
	private final SimpleIntegerProperty year;
	private Map<Candidate, Integer> candidates;
	// TOOD: make as a model?
	private SimpleStringProperty juryDescription;

	final private int MAX_LENGTH = 100;

	/** 
	 * Create the ScoringList object.
	 * @param id
	 * @param year
	 */
	public ScoringList(int id, int year) {
		this.id = new SimpleIntegerProperty(id);
		this.year = new SimpleIntegerProperty(year);
		candidates = new HashMap<Candidate, Integer>();
		created = new SimpleObjectProperty<LocalDate>(LocalDate.now());
		lastChanged = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	}

	/**
	 * Get id
	 * @return int The id
	 */
	public int getId() {
		return id.get();
	}


	/**
	 * Get year
	 * @return int The year
	 */
	public int getYear() {
		return year.get();
	}

	public SimpleIntegerProperty yearProperty() {
		return year;
	}

	public ObjectProperty<LocalDate> createdProperty() {
		return created;
	}

	public LocalDate getCreatedDate() {
		return created.get();
	}

	public ObjectProperty<LocalDate> lastChangedProperty() {
		return lastChanged;
	}

	public LocalDate getLastChangedDate() {
		return lastChanged.get();
	}
	/**
	 * Add candidate with rank to list
	 * @param candidate
	 * @param rank
	 */
	public void addCandidate(Candidate candidate, int rank) {
		candidates.put(candidate, rank);
	}

	/**
	 * Get the rank to the candidate if in list
	 * @param candidate
	 * @return int The rank to the candidate if in the list
	 */
	public int getRankToCandidate(Candidate candidate) {
		boolean candidateInList = candidates.containsKey(candidate);
		if (candidateInList) {
			return candidates.get(candidate);
		}
		return -1;
	}

	/**
	 * Get the candidate based on rank
	 * @param rank
	 * @return Candidate the candidate
	 */
	public Candidate getCandidateByRank(int rank) {
		// TODO
		return null;
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


}
