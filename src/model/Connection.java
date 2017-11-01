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

	public SimpleStringProperty getNameProperty() {
		return person.getNameProperty();
	}

	public SimpleStringProperty getDescriptionProperty() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = new SimpleStringProperty(description);
	}

	public String getDescription() {
		return description.get();
	}

	public Person getPerson() {
		return person;
	}
}
