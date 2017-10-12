package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Candidate {

	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty municipality = new SimpleStringProperty();
	private SimpleStringProperty imageURL = new SimpleStringProperty("images/standard.png");;
	private SimpleStringProperty description = new SimpleStringProperty();
	private SimpleIntegerProperty rank;
	private SimpleIntegerProperty previousYearRank;

	// PG stands for ProductionGrants
	private SimpleIntegerProperty animalsPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty hiredHelpPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty farmingPG = new SimpleIntegerProperty(0);

	// TODO: missing network

	/**
	 * Constructor for the Candidate object. Used when creating a new candidate
	 * based on list from jury.
	 * 
	 * @param name
	 * @param rank
	 * @param previousYearRank
	 */
	public Candidate(String name, int rank, int previousYearRank) {
		splitUpAndSaveName(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
	}

	/**
	 * Constructor for the Candidate object. Used when creating manually a new
	 * candidate.
	 * 
	 * @param name
	 * @param rank
	 * @param previousYearRank
	 * @param imageName
	 * @param despcription
	 */
	public Candidate(String name, String imageURL, String despcription, int rank) {
		splitUpAndSaveName(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);

		this.imageURL = new SimpleStringProperty(imageURL);
		this.description.setValue(despcription);
	}

	/**
	 * Get firstName as string
	 * 
	 * @return String The firstName
	 */
	public String getFirstName() {
		return firstName.get();
	}

	/**
	 * Get firstNameProperty
	 * 
	 * @return SimpleStringProperty The firstName
	 */
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}

	/**
	 * Set firstName
	 * 
	 * @param firstName
	 */
	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get lastName as string
	 * 
	 * @return String The lastName
	 */
	public String getLastName() {
		return lastName.get();
	}

	/**
	 * Get lastName
	 * 
	 * @return SimpleStringProperty The lastName
	 */
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}

	/**
	 * Set lastName
	 * 
	 * @param lastName
	 */
	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
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
		System.out.println("Image PATH in Candidate: " + imageURL.get());
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
	 * @param imageName
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

	/**
	 * Split up the name based on the string name
	 * 
	 * @param name
	 */
	public void splitUpAndSaveName(String name) {
		if (name.contains(" ")) {
			String[] names = name.split(" ");
			handleMiddleNames(names);
		} else {
			firstName = new SimpleStringProperty(name);
			lastName = new SimpleStringProperty("");
		}
	}

	/**
	 * Take all the names except the last one and merge them to the first name
	 * 
	 * @param names
	 */
	private void handleMiddleNames(String[] names) {
		int numberOfNames = names.length;
		if (numberOfNames > 2) {
			lastName = new SimpleStringProperty(names[numberOfNames - 1]);
			String firstNameString = names[0];
			for (int i = 1; i < numberOfNames - 1; i++) {
				firstNameString += " " + names[i];
			}
			firstName = new SimpleStringProperty(firstNameString);
		} else if (numberOfNames == 2) {
			firstName = new SimpleStringProperty(names[0]);
			lastName = new SimpleStringProperty(names[1]);
		}
	}

	// TODO
	// Missing functions to validate rank etc
}
