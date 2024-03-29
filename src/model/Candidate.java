package model;

import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.common.collect.ImmutableSet;
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

	/*
		The fieldStatus represent the color of the TextField for the candidate.
		This is the sequence of the fields:
		[name, lastYearsRank, Rank, Municipality, gender, birthyear, profession, twitter, description, title and network]
		The numbers represent:
		0 - not checked / correct data
		1 - missing data
		2 - wrong input
		For example, if municipality is missing fields, fieldStatus[3] will be 1
		This array is used for colourcoding
	 */
	private int[] fieldStatus = new int[11];


	private JsonObject rawData = new JsonObject();
	private JsonArray elements = new JsonArray();

	private int animalSubsidies;
	private int farmingSubsidies;
	private int hiredHelpSubsidies;


	private ObservableList<Connection> connections =  FXCollections.observableArrayList();

	public Candidate(String name, int rank) {
		super(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(-1);
		this.status = "";
	}


	public Candidate(String name, String description, int rank) {
		super(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.previousYearRank = new SimpleIntegerProperty(rank);
		this.description.setValue(description);
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
		if (twitterLink.get() == null) {
			return "";
		}
		return twitterLink.get();
	}

	public void addConnection(Connection connection) {
		connections.add(connection);
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

		if (profession == null) {
			return "";
		}
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getTitle(){
		if (title == null) {
			return "";
		}
		return title;
	}

	public void setTitle(String title) { this.title = title; }

	public String validate(String name, String rank, String previousYearRank, String gender, String description) {
		String errorMessage = "";
		errorMessage += Utility.validateName(name);

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
		fieldStatus[2] = 0;
		return "";
	}

	private String validatePreviousYearRank(String rankString) {
		if (rankString.equals("")) {
			return "";
		}

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
		fieldStatus[1] = 0;
		return "";
	}

	private String validateGender(String gender) {
		if (gender.equals("")) {
			fieldStatus[4] = 2;
			return "\n Du må velge et kjønn. Velg 'Annet' om kandidaten ikke er et menneske.";
		}
		fieldStatus[4] = 0;
		return "";
	}

	private String validateDescription(String description) {
		if (description.length() <= 5 || description.equals(null)) {
			fieldStatus[8] = 2;
			return "\n Beskrivelse mangler:";
		}
		fieldStatus[8] = 0;
		return "";
	}

	public boolean hasPreviousYearRank() {
		return previousYearRank.get() != -1;
	}

	public void addRawData(String field, JsonElement data) {
		this.rawData.add(field, data);
	}

	public void setElements(JsonArray elements) {
		this.elements = elements;
	}

	public JsonArray getElements() {
		return this.elements;
	}

	public int[] getFieldStatus() {
		return fieldStatus;
	}

	public void setFieldStatus(int field, int status) {
		fieldStatus[field] = status;
	}

	public void setAnimalSubsidies(int animalSubsidies) {
		this.animalSubsidies = animalSubsidies;
	}

	public void setFarmingSubsidies(int farmingSubsidies) {
		this.farmingSubsidies = farmingSubsidies;
	}

	public void setHiredHelpSubsidies(int hiredHelpSubsidies) {
		this.hiredHelpSubsidies = hiredHelpSubsidies;
	}

	public int getHiredHelpSubsidies() {
		return this.hiredHelpSubsidies;
	}

	public int getAnimalSubsidies() {
		return this.animalSubsidies;
	}

	public int getFarmingSubsidies() {
		return this.farmingSubsidies;
	}


}
