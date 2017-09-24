package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Candidate {

	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty imageName;
	private SimpleStringProperty description;
	private SimpleIntegerProperty range;
	
	
	public Candidate(String name, String imageName, String despcription) {
		// TODO: split up name
		this.firstName = new SimpleStringProperty(name);
		this.imageName = new SimpleStringProperty(imageName);
		// Error
		this.description = new SimpleStringProperty();
	}
	public String getFirstName() {
		return firstName.get();
	}
	
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}
	
	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName.get();
	}
	
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}
	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
	}
	
	public String getImageName() {
		return imageName.get();
	}
	
	public SimpleStringProperty imageNavnProperty() {
		return imageName;
	}
	public void setImageNavn(SimpleStringProperty imageName) {
		this.imageName = imageName;
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public SimpleStringProperty descriptionProperty() {
		return description;
	}
	public void setDescription(SimpleStringProperty description) {
		this.description = description;
	}
	
	public int getRange() {
		return range.get();
	}
	
	public IntegerProperty rangeProperty() {
		return range;
	}
	public void setRange(SimpleIntegerProperty range) {
		this.range = range;
	}

	
}
