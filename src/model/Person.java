package model;

import javafx.beans.property.SimpleStringProperty;

import java.util.regex.Pattern;

public class Person {
	
	protected SimpleStringProperty name;
	private SimpleStringProperty imageName = new SimpleStringProperty("src/resources/style/standard.png");
	
	public Person(String name, String imageName) {
		this.name = new SimpleStringProperty(name);
		if (imageName != null) {
			this.imageName = new SimpleStringProperty(imageName);
		}
	}

	public SimpleStringProperty getNameProperty() {
		return name;
	}
	
	public String getName() {
		return name.get();
	}

	/**
	 * Returns the local imageName
	 */
	public String getImageName() {
		if (! imageName.get().equals("src/resources/style/standard.png")) {
			return "images/" + imageName.get();
		}
		return imageName.get();
	}
	
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public void setImageName(String imageName) {
		this.imageName = new SimpleStringProperty(imageName);
	}

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

	// TODO: must be used when writing JSON
	public String getBucketImageURL() {
		String bucketPath = AmazonBucketUploader.getOrCreateInstance().getBucketPath();
		// TODO: not sure
		return bucketPath + "/" + imageName.get() + ".png";
	}

}
