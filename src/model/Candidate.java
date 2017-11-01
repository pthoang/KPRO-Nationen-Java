package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Organization;

public class Candidate extends Person {


	private SimpleStringProperty name;
	private SimpleStringProperty municipality = new SimpleStringProperty();
	private SimpleStringProperty imageURL = new SimpleStringProperty("resources/standard.png");
	private SimpleStringProperty description = new SimpleStringProperty();
	private SimpleIntegerProperty rank;
	private SimpleIntegerProperty previousYearRank;
	private String status;
	private boolean isPerson;

	private ObservableList<Organization> organizations = FXCollections.observableArrayList();


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
		this.name = new SimpleStringProperty(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.status = "";
		this.isPerson = true;
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
		this.name = new SimpleStringProperty(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);

		this.imageURL = new SimpleStringProperty(imageURL);
		this.description.setValue(despcription);

	}

	/**
	 * Get name as string
	 *
	 * @return String The name
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * Get nameProperty
	 *
	 * @return SimpleStringProperty The name
	 */
	public SimpleStringProperty nameProperty() {
		return name;
	}

	/**
	 * Set name
	 *
	 * @param name
	 */
	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	/**
	 * Get municipality as string
	 *
	 * @return String The municipality
	 */
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
	 * Get imageName as String
	 *
	 * @return String The imageName
	 */
	public String getImageURL() {
		return imageURL.get();
	}

	/**
	 * Set imageName
	 *
	 * @return SimpleStringProperty The imageName
	 */
	public SimpleStringProperty imageNavnProperty() {
		return imageURL;
	}

	/**
	 * Set imageName
	 *
	 */
	public void setImageURLProperty(SimpleStringProperty imageURL) {

		this.imageURL = imageURL;
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
	public ObservableList<Organization> getOrganizations() {
		return organizations;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setisPerson(boolean isPerson){ this.isPerson = isPerson; }

	public boolean getisPerson(){ return this.isPerson; }
}
