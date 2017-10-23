package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class AmazonBucketUploader {
	
	String bucketName = "tunmedia";
	String folderName = "maktkaring_" + Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
	
	private String accessKey = "";
	private String secretKey = "";
	
	private AmazonS3 s3Client;
	
	public AmazonBucketUploader() {
		setKeys();
		getClient();
		createOrGetBucket();
	}
	
	private void setKeys() {
		try (Stream<String> stream = Files.lines(Paths.get("/home/doraoline/Downloads/rootkey.csv"))) {
			List<String> keys = stream.collect(Collectors.toList());
			accessKey = keys.get(0).split("=")[1];
			secretKey = keys.get(1).split("=")[1];				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getClient() {
		BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
		s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-2").build();
	}
			
	public void createOrGetBucket() {
		if(!(s3Client.doesBucketExist(bucketName))) {
			s3Client.createBucket(new CreateBucketRequest(bucketName));
		} else {
			System.out.println("Bucket exists");
		}
	}
	public void uploadFile(File file, String fileName) {
		String path = bucketName + "/" + folderName;
		PutObjectRequest por = new PutObjectRequest(path, fileName, file);
		por.setCannedAcl(CannedAccessControlList.PublicRead);
		s3Client.putObject(por);
	}
	
	// TODO: if image exist, change name
	
}