package model;

import javafx.beans.property.SimpleStringProperty;

public class Person {
	
	private SimpleStringProperty name;
	private SimpleStringProperty imageURL = new SimpleStringProperty("resources/standard.png");
	
	public Person(String name, String imageURL) {
		this.name = new SimpleStringProperty(name);
		this.imageURL = new SimpleStringProperty(imageURL);
	}

	
	public SimpleStringProperty getNameProperty() {
		return name;
	}
	
	public String getName() {
		return name.get();
	}
	
	public SimpleStringProperty getImageURLProperty() {
		return imageURL;
	}
	
	public String getImageURL() {
		return imageURL.get();
	}
	
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = new SimpleStringProperty(imageURL); 
	}

	// TODO: validate name
	
	// TODO: validate imageURL
	

	

}
