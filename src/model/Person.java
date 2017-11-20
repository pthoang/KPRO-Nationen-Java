package model;

import javafx.beans.property.SimpleStringProperty;

public class Person {
	
	protected SimpleStringProperty name;
	private SimpleStringProperty imageName = new SimpleStringProperty(Utility.STANDARD_IMAGE_PATH);
	private boolean imageIsInBucket = false;


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

	public String getImageName() {
		/*
		if (! imageName.get().equals(Utility.STANDARD_IMAGE_PATH)) {
			return "images/" + imageName.get();
		}
		*/
		return name.get().replace(" ", "") + ".png";
	}
	
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public void setImageName(String imageName) {
		this.imageName = new SimpleStringProperty(imageName);
	}

	public String getBucketImageURL() {
		String bucketPath = AmazonBucketUploader.getOrCreateInstance().getBucketPath();
		return bucketPath + imageName.get();
	}

	public void setImageIsInBucket(boolean bool){
		imageIsInBucket = bool;
	}

	public boolean getImageIsInBucket() {
		return imageIsInBucket;
	}
}
