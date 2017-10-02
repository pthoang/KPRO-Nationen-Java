package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Candidate {

	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty imageURL;
	private SimpleStringProperty description;
	private SimpleIntegerProperty rank;

	// TODO: add missing values as adress and so on

	/**
	 * Constructor for Candidate
	 * @param name
	 * @param imageName
	 * @param despcription
	 */
	public Candidate(String name, String imageURL, String despcription, int rank) {
		splitUpName(name);
		this.imageURL = new SimpleStringProperty(imageURL);
		// Error
		this.description = new SimpleStringProperty();
		this.rank = new SimpleIntegerProperty(rank);
	}

	/**
	 * Get firstName as string
	 * @return String The firstName
	 */
	public String getFirstName() {
		return firstName.get();
	}

	/**
	 * Get firstNameProperty
	 * @return SimpleStringProperty The firstName
	 */
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}

	/**
	 * Set firstName
	 * @param firstName
	 */
	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get lastName as string
	 * @return String The lastName
	 */
	public String getLastName() {
		return lastName.get();
	}

	/**
	 * Get lastName
	 * @return SimpleStringProperty The lastName
	 */
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}

	/**
	 * Set lastName
	 * @param lastName
	 */
	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get imageName as String
	 * @return String The imageName
	 */
	public String getImageURL() {
		return imageURL.get();
	}

	/**
	 * Set imageName
	 * @return SimpleStringProperty The imageName 
	 */
	public SimpleStringProperty imageNavnProperty() {
		return imageURL;
	}

	/**
	 * Set imageName
	 * @param imageName
	 */
	public void setImageURLProperty(SimpleStringProperty imageURL) {
		this.imageURL = imageURL;
	}

	/**
	 * Get description as string
	 * @return String The description
	 */
	public String getDescription() {
		return description.get();
	}

	/**
	 * Get the description
	 * @return SimpleStringProperty The description
	 */
	public SimpleStringProperty descriptionProperty() {
		return description;
	}

	/**
	 * Set the description
	 * @param description
	 */
	public void setDescription(SimpleStringProperty description) {
		this.description = description;
	}

	/**
	 * Get the range
	 * @return int The range
	 */
	public int getRank() {
		return rank.get();
	}

	/**
	 * Get the range
	 * @return IntegerProperty The range
	 */
	public IntegerProperty rankProperty() {
		return rank;
	}

	/**
	 * Set the range
	 * @param range
	 */
	public void setRank(SimpleIntegerProperty rank) {
		this.rank = rank;
	}

	/**
	 * Split up the name based on the string name
	 * @param name
	 */
	private void splitUpName(String name) {
		if (name.contains(" ")) {
			String[] names = name.split(" ");
			System.out.println("Names: " + names[0] + " " + names[1]);
			handleMiddleNames(names);	
		}
	}

	/**
	 * Take all the names except the last one and merge them to the first name
	 * @param names
	 */
	private void handleMiddleNames(String[] names) {
		int numberOfNames = names.length;
		if (numberOfNames > 2) {
			lastName = new SimpleStringProperty(names[numberOfNames -1]);
			String firstNameString = names[0];
			for (int i = 1; i < numberOfNames-1; i++) {
				firstNameString += " " + names[i];
			}
			firstName = new SimpleStringProperty(firstNameString);
		} else if (numberOfNames == 2) {
			firstName = new SimpleStringProperty(names[0]);
			lastName = new SimpleStringProperty(names[1]);
		}
	}

}
