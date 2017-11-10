package model;

import controllers.ScoringListController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings {

	private static Settings instance = null;
	
	private int numCandidates = 100;
	private int numConnections = 10;
	private String bucketAccessKey;
	private String bucketSecretKey;
	private String bucketName = "tunmedia";
	private String folderName = "maktkaring_" + Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

	public static Settings getOrCreateInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	private Settings() {
		setDefaultKeys();
	}
	
	public int getNumCandidates() {
		return numCandidates;
	}
	
	public void setNumCandidates(int numCandidates) {
		this.numCandidates = numCandidates;
	}

	public int getNumConnections() {
		return numConnections;
	}

	public void setNumConnections(int numConnections) {
		this.numConnections = numConnections;
	}
	
	public String getBucketAccessKey() {
		return bucketAccessKey;
	}
	
	public void setBucketAccessKey(String bucketAccessKey) {
		this.bucketAccessKey = bucketAccessKey;
	}
	
	public String getBucketSecretKey() {
		return bucketSecretKey;
	}
	
	public void setBucketSecretKey(String bucketSecretKey) {
		this.bucketSecretKey = bucketSecretKey;
	}
	
	public String getBucketName() {
		return bucketName;
	}
	
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	// TODO: remove after testing
	// Just under testing
	private void setDefaultKeys() {
		try (Stream<String> stream = Files.lines(Paths.get("rootkey.csv"))) {
			List<String> keys = stream.collect(Collectors.toList());
			bucketAccessKey = keys.get(0).split("=")[1];
			bucketSecretKey = keys.get(1).split("=")[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
