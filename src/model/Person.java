package model;

import javafx.beans.property.SimpleStringProperty;

public class Person {
	
	private String name;
	private String imageURL;
	
	public Person(String name, String imageURL) {
		this.name = name;
		this.imageURL = imageURL;
	}

	
	public SimpleStringProperty getNameProperty() {
		return new SimpleStringProperty(name);
	}
	// TODO: validate name
	
	// TODO: validate imageURL
	

	

}
