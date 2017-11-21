package model;

import javafx.beans.property.SimpleStringProperty;

public class Person {
	
	protected SimpleStringProperty name;
	private boolean imageIsInBucket = false;

	public Person(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public SimpleStringProperty getNameProperty() {
		return name;
	}
	
	public String getName() {
		return name.get();
	}

	public String getImageName() {
		return name.get().replace(" ", "") + ".png";
	}
	
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public String getBucketImageURL() {
		String imagePath = "";
		String bucketPath = AmazonBucketUploader.getOrCreateInstance().getBucketPath();
		if (imageIsInBucket) {
			imagePath += bucketPath + "/images/" + getImageName();
		} else {
			imagePath += bucketPath + "/standard.png";
		}
		return imagePath;
	}

	public void setImageIsInBucket(boolean bool){
		imageIsInBucket = bool;
	}

	public boolean getImageIsInBucket() {
		return imageIsInBucket;
	}
}
