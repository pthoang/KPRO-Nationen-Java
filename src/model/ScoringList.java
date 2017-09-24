package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class ScoringList {
	
	private final SimpleIntegerProperty id;
	private Date created;
	private Date lastChanged;
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
		this.id.set(id);
		this.year.set(year);
		candidates = new HashMap<Candidate, Integer>();
		created = new Date();
		lastChanged = new Date();
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
	
	public Date createdProperty() {
		return created;
	}
	
	public Date lastChangedProperty() {
		return lastChanged;
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
