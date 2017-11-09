package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.common.collect.ImmutableSet;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Candidate extends Person {

	private static final Set<String> GENDERS_IF_PERSON = ImmutableSet.of("F", "M");
	private SimpleStringProperty municipality = new SimpleStringProperty("");
	private SimpleStringProperty description = new SimpleStringProperty("");
	private SimpleIntegerProperty rank;
	private SimpleIntegerProperty previousYearRank;
	private SimpleStringProperty twitterLink = new SimpleStringProperty();
	private String gender = "";
	private String yearOfBirth;
	private String profession;
	private String title;

	// TODO: should use enum instead of strings to tell the status
	private String status;

	private ArrayList organizations;

	/*
		The fieldStatus represent the color of the TextField for the candidate.
		This is the sequence of the fields:
		[name, lastYearsRank, Rank, Municipality, gender, birthyear, profession, twitter, animals, hiredhelp, farming,
		description and network]
		The numbers represent:
		0 - not checked
		1 - missing data
		2 - contains correct data
		3 - wrong input
		For example, if municipality is missing fields, fieldStatus[3] will be 1

		This array is used for colourcoding
	 */
	private int[] fieldStatus = new int[14];


	private JsonObject rawData = new JsonObject();

	// PG stands for ProductionGrants
	private SimpleIntegerProperty animalsPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty hiredHelpPG = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty farmingPG = new SimpleIntegerProperty(0);


	private ObservableList<Connection> connections =  FXCollections.observableArrayList();

	public Candidate(String name, int rank, int previousYearRank) {
		super(name, null);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.status = "";
	}


	public Candidate(String name, String imageURL, String despcription, int rank) {
		super(name, imageURL);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.description.setValue(despcription);
	}

	//appends test organizations
	public void testOrg() {
		JsonObject testData1 = new JsonObject();
		JsonObject testData2 = new JsonObject();

		int org = 975938883;
		String name = "BJØRN RUNE KYNNINGSRUD";
		int stocks = 131;
		int value = 4;


		int org1 = 984566077;
		String name1 = "BEST MELK SAMDRIFT DA";
		int stocks1 = 149;
		int value1 = 11;

		testData1.addProperty("org", org);
		testData1.addProperty("name", name);
		testData1.addProperty("stocks", stocks);
		testData1.addProperty("value", value);

		testData2.addProperty("org", org1);
		testData2.addProperty("name", name);
		testData2.addProperty("stocks", stocks);
		testData2.addProperty("value", value);

		JsonArray returnObject = new JsonArray();
		returnObject.add(testData1);
		returnObject.add(testData2);

		this.rawData.add("stocks", returnObject);
	}

	public JsonObject getRawData() {
		return rawData;
	}

	public void addData(String name, JsonElement element) {
		rawData.add(name, element);
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

	public void setMunicipality(SimpleStringProperty municipality) {
		this.municipality = municipality;
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(SimpleStringProperty description) {
		this.description = description;
	}

	public int getRank() {
		return rank.get();
	}

	public void setRank(SimpleIntegerProperty rank) {
		this.rank = rank;
	}

	public int getPreviousYearRank() {
		return previousYearRank.get();
	}

	public void setPreviousYearRank(SimpleIntegerProperty previousYearRank) {
		this.previousYearRank = previousYearRank;
	}

	public void setTwitter(SimpleStringProperty twitterLink) {
		this.twitterLink = twitterLink;
	}

	public String getTwitter() {
		return this.twitterLink.get();
	}

	// TODO
	// Missing functions to validate rank etc

	public int getAnimalsPG() {
		return animalsPG.get();
	}

	public void setAnimalsPG(SimpleIntegerProperty animalsPG) {
		this.animalsPG = animalsPG;
	}

	public int getHiredHelpPG() {
		return hiredHelpPG.get();
	}

	public void setHiredHelpPG(SimpleIntegerProperty hiredHelpPG) {
		this.hiredHelpPG = hiredHelpPG;
	}

	public int getFarmingPG() {
		return farmingPG.get();
	}

	public void setFarmingPG(SimpleIntegerProperty farmingPG) {
		this.farmingPG = farmingPG;
	}

	public void addConnection(Person person, String description) {
		Connection newConnection = new Connection(person, description);
		connections.add(newConnection);
	}

	public ObservableList<Connection> getConnections() {
		return connections;
	}
	
	public void deleteConnection(Connection connection) {
		connections.remove(connection);
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public boolean getIsPerson(){ 
		if (GENDERS_IF_PERSON.contains(gender)) {
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

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getTitle(){ return title; }

	public void setTitle(String title) { this.title = title; }


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
				fieldStatus[2] = 2;
				return "\n Plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			fieldStatus[2] = 2;
			return "\n Plasseringen er ikke et tall";
		}
		return "";
	}

	private String validatePreviousYearRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				fieldStatus[1] = 2;
				return "\n Fjorårets plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			fieldStatus[1] = 2;
			return "\n Fjorårets plasseringen er ikke et tall";
		}
		return "";
	}

	private String validateGender(String gender) {
		if (gender.equals("")) {
			fieldStatus[1] = 2;
			return "\n Du må velge et kjønn. Velg 'Annet' om kandidaten ikke er et menneske.";
		}
		return "";
	}

	private String validateDescription(String description) {
		if (description.length() <= 5 || description.equals(null)) {
			fieldStatus[11] = 2;
			return "\n Beskrivelse mangler:";
		}
		return "";
	}

	public void addRawData(String field, JsonElement data) {
		this.rawData.add(field, data);
	}

	public int[] getFieldStatus() {
		return fieldStatus;
	}

	public void setFieldStatus(int field, int status) {
		fieldStatus[field] = status;
	}
}
