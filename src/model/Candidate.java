package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Candidate extends Person {

	private SimpleStringProperty municipality = new SimpleStringProperty();
	private SimpleStringProperty description = new SimpleStringProperty();
	private SimpleIntegerProperty rank;
	private SimpleIntegerProperty previousYearRank;
	private String status;
	private String gender = "";

	private ArrayList organizations;


	// PG stands for ProductionGrants
	private SimpleIntegerProperty animalsPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty hiredHelpPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty farmingPG = new SimpleIntegerProperty(0);

	private ObservableList<Connection> connections =  FXCollections.observableArrayList();

	/**
	 * Constructor for the Candidate object. Used when creating a new candidate
	 * based on list from jury.
	 *
	 * @param name
	 * @param rank
	 * @param previousYearRank
	 */
	public Candidate(String name, int rank, int previousYearRank) {
		super(name, null);
		//this.name = new SimpleStringProperty(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.status = "";
	}

	/**
	 * Constructor for the Candidate object. Used when creating manually a new
	 * candidate.
	 *
	 * @param name
	 * @param rank
	 * @param despcription
	 */
	public Candidate(String name, String imageURL, String despcription, int rank) {
		super(name, imageURL);
		//this.name = new SimpleStringProperty(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);

		//this.imageURL = new SimpleStringProperty(imageURL);
		this.description.setValue(despcription);

	}


	public String getMunicipality() {
		return municipality.get();
	}

	/**
	 * Get municipality
	 *
	 * @return SimpleStringProperty The municipality
	 */
	public SimpleStringProperty municipalityProperty() {
		return municipality;
	}

	/**
	 * Set municipality
	 *
	 * @param municipality
	 */
	public void setMunicipality(SimpleStringProperty municipality) {
		this.municipality = municipality;
	}

	/**
	 * Get description as string
	 *
	 * @return String The description
	 */
	public String getDescription() {
		return description.get();
	}

	/**
	 * Get the description
	 *
	 * @return SimpleStringProperty The description
	 */
	public SimpleStringProperty descriptionProperty() {
		return description;
	}

	/**
	 * Set the description
	 *
	 * @param description
	 */
	public void setDescription(SimpleStringProperty description) {
		this.description = description;
	}

	/**
	 * Get the range
	 *
	 * @return integer The range
	 */
	public int getRank() {
		return rank.get();
	}

	/**
	 * Get the rank
	 *
	 * @return IntegerProperty The rank
	 */
	public IntegerProperty rankProperty() {
		return rank;
	}

	/**
	 * Set the rank
	 *
	 * @param rank
	 */
	public void setRank(SimpleIntegerProperty rank) {
		this.rank = rank;
	}

	/**
	 * Get the previousYearRank
	 *
	 * @return integer The previousYearRank
	 */
	public int getPreviousYearRank() {
		return previousYearRank.get();
	}

	/**
	 * Get the previousYearRank
	 *
	 * @return IntegerProperty The previousYearRank
	 */
	public IntegerProperty previousYearRankProperty() {
		return previousYearRank;
	}

	/**
	 * Set the previousYearRank
	 *
	 * @param previousYearRank
	 */
	public void setPreviousYearRank(SimpleIntegerProperty previousYearRank) {
		this.previousYearRank = previousYearRank;
	}

	public int getAnimalsPG() {
		return animalsPG.get();
	}

	public IntegerProperty getAnimalsPGProperty() {
		return animalsPG;
	}

	public void setAnimalsPG(SimpleIntegerProperty animalsPG) {
		this.animalsPG = animalsPG;
	}

	public int getHiredHelpPG() {
		return hiredHelpPG.get();
	}

	public IntegerProperty getHiredHelpPGProperty() {
		return hiredHelpPG;
	}

	public void setHiredHelpPG(SimpleIntegerProperty hiredHelpPG) {
		this.hiredHelpPG = hiredHelpPG;
	}

	public int getFarmingPG() {
		return farmingPG.get();
	}

	public IntegerProperty getFarmingPGProperty() {
		return farmingPG;
	}

	public void setFarmingPG(SimpleIntegerProperty farmingPG) {
		this.farmingPG = farmingPG;
	}

	// TODO
	// Missing functions to validate rank etc

	public void addConnection(Person person, String description) {
		Connection newConnection = new Connection(this, person, description);
		connections.add(newConnection);
	}

	public ObservableList<Connection> getConnections() {
		return connections;
	}
	
	public void deleteConnection(Connection connection) {
		connections.remove(connection);
		System.out.println("Delete connection");
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public boolean getIsPerson(){ 
		if (gender.equals("F") || gender.equals("M")) {
			return true;
		}
		return false;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGender() {
		return gender;
	}

	// Validation
	public String validate(String name, String rank, String previousYearRank, String gender, String description) {
		String errorMessage = "";
		errorMessage += super.validateName(name);

		errorMessage += validateRank(rank);
		errorMessage += validatePreviousYearRank(previousYearRank);
		errorMessage += validateGender(gender);
		errorMessage += validateDescription(description);

		return errorMessage;
	}

	private String validateRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				return "\n Plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			return "\n Plasseringen er ikke et tall";
		}
		return "";
	}

	private String validatePreviousYearRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				return "\n FJorårets plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			return "\n Fjorårets plasseringen er ikke et tall";
		}
		return "";
	}

	private String validateGender(String gender) {
		if (gender == "") {
			return "\n Du må velge et kjønn. Velg 'Annet' om kandidaten ikke er et menneske.";
		}
		return "";
	}

	private String validateDescription(String description) {
		if (description.length() <= 5) {
			return "\n Beskrivelse mangler:";
		}
		return "";
	}
}
