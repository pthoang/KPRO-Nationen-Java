package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Calendar;


public class Settings {

	private static Settings instance = null;
	
	private int numCandidates = 100;
	private int numConnections = 10;
	private String bucketAccessKey = "";
	private String bucketSecretKey = "";
	private String bucketName = "tunmedia";
	private String folderName;

	public static Settings getOrCreateInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	private Settings() {
		setDefaultKeys();
		String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		folderName = "maktkaring_" + year;
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

	private void setDefaultKeys() {
		InputStream in = Main.MainApp.class.getResourceAsStream("/resources/rootkeys.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String key;
			while ((key = br.readLine()) != null) {
				if (key.startsWith("AWSAccessKeyId")) {
					bucketAccessKey = key.split("=")[1];
				} else if (key.startsWith("AWSSecretKey")) {
					bucketSecretKey = key.split("=")[1];
				} else {
					System.out.println("Is not a key");
				}
			}
		} catch (IOException e) {
			System.out.println("Could not read the keys: " + e);
		}
	}
}
