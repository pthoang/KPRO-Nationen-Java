package model;

import javafx.beans.property.SimpleStringProperty;

import java.util.regex.Pattern;

public class Person {
	
	private SimpleStringProperty name;
	private SimpleStringProperty imageURL = new SimpleStringProperty("resources/standard.png");
	
	public Person(String name, String imageURL) {
		this.name = new SimpleStringProperty(name);
		if (imageURL != null) {
			this.imageURL = new SimpleStringProperty(imageURL);
		}
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
	protected String validateName(String name) {
		Pattern pattern = Pattern.compile("^[A-ZÆØÅa-zæøå. \\-]++$");
		String errorMessage = "";

		if (name.length() <= 2) {
			errorMessage += "\n Navn må være lengre enn 2 bokstaver.";
		}
		if (!pattern.matcher(name).matches()) {
			errorMessage += "\n Navnet inneholder ugyldige bokstaver. Tillatt er: a-å, ., og -";
		}

		return errorMessage;
	}
	
	// TODO: validate imageURL
	

	

}
