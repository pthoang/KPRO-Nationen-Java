package model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.java2d.pipe.SpanShapeRenderer;

public class Candidate extends Person {


	private SimpleStringProperty name;
	private SimpleStringProperty municipality = new SimpleStringProperty();
	private SimpleStringProperty imageURL = new SimpleStringProperty("resources/standard.png");
	private SimpleStringProperty description = new SimpleStringProperty();
	private SimpleIntegerProperty rank;
	private SimpleIntegerProperty previousYearRank;
	private SimpleStringProperty twitterLink = new SimpleStringProperty();

	private ArrayList organizations;

	private JsonObject rawData = new JsonObject();

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

	public void setTwitter(SimpleStringProperty twitter) {
		this.twitterLink = twitter;
	}

	public String getTwitter() {
		return twitterLink.get();
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


}
