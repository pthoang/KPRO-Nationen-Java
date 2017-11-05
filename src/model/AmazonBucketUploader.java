package model;

import java.io.File;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AmazonBucketUploader {
	
	private String bucketName;
	private String folderName;
	private String accessKey;
	private String secretKey;
	
	private AmazonS3 s3Client;
	
	public AmazonBucketUploader(String bucketName, String folderName, String accessKey, String secretKey) {
		this.bucketName = bucketName;
		this.folderName = folderName;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		
		getClient();
		createOrGetBucket();
	}
	
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
		
		createOrGetBucket();
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public void setKeys(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		
		this.secretKey = secretKey;
		
		getClient();
	}
		
	private void getClient() {
		BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
		s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-2").build();
	}
			
	private void createOrGetBucket() {
		if(!(s3Client.doesBucketExist(bucketName))) {
			s3Client.createBucket(new CreateBucketRequest(bucketName));
		}
	}
	public void uploadFile(File file, String fileName) {
		String path = bucketName + "/" + folderName;
		PutObjectRequest por = new PutObjectRequest(path, fileName, file);
		por.setCannedAcl(CannedAccessControlList.PublicRead);
		try {
			s3Client.putObject(por);
		} catch (AmazonS3Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Advarsel!");
			alert.setHeaderText("Noe gikk galt");
			alert.setContentText("Access og/eller secret key til Amazon-bøtta er ikke gyldig."
					+ "	Gå til innstillinger og dobbletsjekk at disse er korrekt." 
					+ "	Bildene vil ikke lagres i bøtta før informasjonen er korrekt.");
			alert.showAndWait();
		}
	}
}