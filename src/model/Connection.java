package model;

import javafx.beans.property.SimpleStringProperty;

public class Connection {
	
	private Candidate candidate;
	private Person person;
	
	private SimpleStringProperty description;
	
	public Connection(Candidate candidate, Person person, String description) {
		this.candidate = candidate;
		this.person = person;
		this.description = new SimpleStringProperty(description);	
	}
	
	public SimpleStringProperty nameProperty() {
		return person.getNameProperty();
	}
	
	public void setName(String name) {
		person.setName(name);
	}
	
	public SimpleStringProperty descriptionProperty() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = new SimpleStringProperty(description);
	}
	
	public String getName() {
		return person.getName();
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public String getImageURL() {
		return person.getImageURL();
	}
	
	public void setImageURL(String imageURL) {
		person.setImageURL(imageURL);
	}
}
