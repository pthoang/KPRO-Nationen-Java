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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


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

	public void createFromNameList() {
		
	}
	
	public void createFromPreviousList() {
		
	}
	
	public void saveList() {
		// TODO: Save and download
	}
	
	public ObservableList<Candidate> getCandidates() {
		return candidates;
	}

}
