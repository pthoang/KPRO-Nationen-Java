package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings {
	
	private int numCandidates = 100;
	private String bucketAccessKey;
	private String bucketSecretKey;
	private String bucketName = "tunmedia";
	private String folderName = "maktkaring_" + Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
	
	public Settings() {
		setDefaultKeys();
	}
	
	public int getNumCandidates() {
		return numCandidates;
	}
	
	public void setNumCandidates(int numCandidates) {
		this.numCandidates = numCandidates;
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
	
	// Just under testing
	private void setDefaultKeys() {
		try (Stream<String> stream = Files.lines(Paths.get("/home/doraoline/Downloads/rootkey.csv"))) {
			List<String> keys = stream.collect(Collectors.toList());
			bucketAccessKey = keys.get(0).split("=")[1];
			bucketSecretKey = keys.get(1).split("=")[1];				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
