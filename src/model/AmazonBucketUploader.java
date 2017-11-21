package model;

import java.io.File;
import java.awt.image.BufferedImage;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AmazonBucketUploader {

	public static AmazonBucketUploader instance = null;
	
	private String bucketName;
	private String folderName;
	private String accessKey;
	private String secretKey;
	private boolean isAccessible = false;
	
	private AmazonS3 s3Client;

	public static AmazonBucketUploader getOrCreateInstance() {
		if (instance == null) {
			instance = new AmazonBucketUploader();
		}
		return instance;
	}

	protected AmazonBucketUploader() {
		Settings settings = Settings.getOrCreateInstance();
		bucketName = settings.getBucketName();
		folderName = settings.getFolderName();
		accessKey = settings.getBucketAccessKey();
		secretKey = settings.getBucketSecretKey();

		getClient();
		createOrGetBucket();
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
		if (s3Client != null) {
			createOrGetBucket();
		}
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public void setKeys(String accessKey, String secretKey) {
		validateKeys(accessKey, secretKey);
		if (isAccessible) {
			this.accessKey = accessKey;
			this.secretKey = secretKey;

			getClient();
		}
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
		PutObjectRequest por = getPor(file, fileName);
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

	private PutObjectRequest getPor(File file, String fileName) {
		String path = bucketName + "/" + folderName;
		if (fileName.endsWith(".png")) {
			path += "/images";
		}
		PutObjectRequest por = new PutObjectRequest(path, fileName, file);
		por.setCannedAcl(CannedAccessControlList.PublicRead);

		return por;
	}

	private void validateKeys(String accessKey, String secretKey) {
		BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 news3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-2").build();
        BufferedImage bfImage = Utility.getResourceAsImage("/resources/style/standard.png");
		File file = Utility.convertBufferedImageToFile(bfImage);
		PutObjectRequest por = getPor(file, "standard.png");
		try {
			news3Client.putObject(por);
			isAccessible = true;
		} catch (AmazonS3Exception e) {
			System.out.println("Exception when validating Amazon bucket keys: " + e);
			isAccessible = false;
		}
	}

	public boolean isAccessible() {
		return isAccessible;
	}

    public BufferedImage getImageFromBucket(String imageName) {
        String imageKey = folderName + "/" + imageName;
        System.out.println("ImageKey: " + imageKey);
        GetObjectRequest getObjReq = new GetObjectRequest(bucketName, imageKey);
        File file = new File("tempFiles/" + imageKey);
        s3Client.getObject(getObjReq, file);
        BufferedImage bfImage = Utility.convertFileToImage(file);
        return bfImage;
    }

    public String getBucketPath() {
	    return
				"https://s3.us-east-2.amazonaws.com/" + bucketName + "/" + folderName;
    }
}