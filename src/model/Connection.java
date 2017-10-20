package model;

import javafx.beans.property.SimpleStringProperty;

public class Connection {
	
	private Candidate candidate;
	private Person person;
	
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	
	public Connection(Candidate candidate, Person person, String description) {
		this.candidate = candidate;
		this.person = person;
		this.name = person.getNameProperty();
		this.description = new SimpleStringProperty(description);
		
		System.out.println("Person name in connection: " + name.get() + " or " + person.getNameProperty().get());
		
	}
	public SimpleStringProperty nameProperty() {
		return name;
	}
	
	public SimpleStringProperty descriptionProperty() {
		return description;
	}
	
	public String getName() {
		System.out.println("Get name: " + name.get());
		return name.get();
	}
	
	public String getDescription() {
		return description.get();
	}
	
}
