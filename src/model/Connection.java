package model;

import javafx.beans.property.SimpleStringProperty;

public class Connection {

	private Person person;
	private SimpleStringProperty description;
	
	public Connection(Person person, String description) {
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

    public String getImageURL() {
        return this.person.getImageName();
    }

	public String getName() {
		return this.person.getName();
	}

	public void setName(String name) {
		this.person.setName(name);
	}

	public void setImageURL(String imageURL) {
		this.person.setImageName(imageURL);
	}
}
