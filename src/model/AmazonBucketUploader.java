package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class AmazonBucketUploader {
	
	Bucket bucket;
	String bucketName = "tunmedia";
	String keyName = ""; 
	String filePath = ""; 
	
	private String accessKey = "";
	private String secretKey = ""; 
	
	private AmazonS3 s3Client;
	
	public AmazonBucketUploader() {
		setKeys();
		getClient();
		createOrGetBucket();
	}
	
	private void setKeys() {
		File file = new File("/home/doraoline/Downloads/rootkey.csv");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String line = reader.readLine();
				accessKey = line.split("=")[1];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				String line = reader.readLine();
				secretKey = line.split("=")[1];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("AccessKey: " + accessKey);
		System.out.println("SecretKey: " + secretKey);

	}
	
	public void getClient() {
		System.out.println("Get client");
		BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
		s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-2").build();
		System.out.println("After getting client");
		System.out.println("Owner: "+ s3Client.getS3AccountOwner());
	}
			
	public void createOrGetBucket() {
		System.out.println("Create or get bucket");
		if(!(s3Client.doesBucketExist(bucketName))) {
			System.out.println("Bucket did not exist");
			s3Client.createBucket(new CreateBucketRequest(bucketName));
			
		} else {
			System.out.println("Bucket exists");
		}
	}
	public void uploadFile(File file) {
		System.out.println("Uploading file");
		String fileName = "Testfile.png";
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
		System.out.println("After uploading file");
	}
	/*
	// Create a list of UploadPartresponse objects. 
	// You get one of these for each part upload.
	List<PartETag>partETags = new ArrayList<PartETag>();
	
	// Initialize
	InitiateMultiPartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, keyName);
	
	initiateMultipartUploadResult initRespons = s3Client.initiateMultipartUpload(initRequest);

	
	File file = new File(filePath);
	
	long contentLength = file.length();
	// Set part size to 5 MB
	long partSize = 5242880;
	
	try {
		long filePosition = 0;
		for (int i; filePosition < contentLength; i++) {
			// Last part can be less than 5 MB. Adjust part size
			partSize = Math.min(partSize,  (contentLength - filePosition));
			
			// Create a request to upload a part
			UploadPartRequest uploadRequest = new UploadPartRequest()
					.withBucketName(bucket).withKey(keyName)
					.withUploadId(initResponse.getUploadId()).withPartNumber(i)
					.withFIleOffset(filePosition)
					.withFile(file)
					.withPartSize(partSize);
			
			// Upload part and add response to our list
			partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
			
			filePosition += partSize;
			
		}
		// Complete
		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
				bucket, 
				keyName, 
				initResponse.getUploadId(),
				partETags);
				
		s3Client.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			s3Client.abortMultipartUpload
	
	*/
}