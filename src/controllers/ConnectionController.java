package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import Main.MainApp;
import javafx.scene.image.WritableImage;
import model.*;


public class ConnectionController {

	public static ConnectionController instance = null;
	
	@FXML
	private TextField nameField;
	@FXML
	private TextField descriptionField;
	@FXML
	private ImageView imageView = new ImageView();
	@FXML
	private Button cancelButton;
	@FXML
	private Button addImageButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button chooseButton;

	private Connection connection;
	private Candidate candidate;
	private MainApp mainApp;
	private CandidateController parent;

	private Image newImage;
	private String imageURL = Utility.STANDARD_IMAGE_PATH;
	private BufferedImage bfImage = null;


	public ConnectionController() {
		instance = this;
		mainApp = MainApp.getInstance();
		parent = CandidateController.getOrCreateInstance();
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
		if (connection != null) {
			setFields();
		} else {
			BufferedImage bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
			setImageField(bfImage);
			saveButton.setDisable(true);
		}
	}
	@FXML
	public void initialize() {
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(false);
		});

		descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(false);
		});

		saveButton.setDisable(true);
		deleteButton.setDisable(true);
	}

	@FXML
	public void handleDelete() {
		candidate.deleteConnection(connection);
		parent.closeDialog();
	}

	@FXML
	public void handleSave() {
		String errorMessage = "";

		errorMessage += Utility.validateName(nameField.getText());
		errorMessage += Utility.validateDescription(descriptionField.getText());

		handleErrorMessage(errorMessage);
	}

    @FXML
    private void handleChangeImage() {
        File file = mainApp.chooseAndGetFile();
        bfImage = Utility.convertFileToImage(file);
        setImageField(bfImage);
    }

	@FXML
	public void handleChooseAsNetwork() {
		handleSave();
		if (connection == null) {
			connection = candidate.getConnections().get(candidate.getConnections().size()-1);
		}
		parent.chooseConnection(connection);
	}

	private void handleErrorMessage(String errorMessage) {
		if (errorMessage.length() != 0) {
		    String headerText = "Felter til koblingen er ikke korrekt utfylt.";
            Utility.newAlertError(headerText, errorMessage);
			candidate.setStatus("invalidFields");
		} else {
			saveConnection();
		}
	}

	private void saveConnection() {
		if (connection == null) {
            String imageName = nameField.getText();
            imageName = imageName.replace(" ",  "");
            Person person = new Person(nameField.getText(), imageName);
			candidate.addConnection(person, descriptionField.getText());
			uploadToBucket();
		} else {
			connection.getPerson().setName(nameField.getText());
			connection.setDescription(descriptionField.getText());
			connection.getPerson().setImageName(imageURL);
		}
		parent.closeDialog();
	}

    private void uploadToBucket() {
        String imageName = candidate.getImageName();
        File file = Utility.convertBufferedImageToFile(bfImage);
        AmazonBucketUploader.getOrCreateInstance().uploadFile(file, imageName);
        candidate.setImageIsInBucket(true);
    }

	@FXML
	public void handleCancel() {
		parent.closeDialog();
	}

	private void setImageField(BufferedImage bfImage) {
		WritableImage image = Utility.convertBufferedImageToWritable(bfImage);
		imageView.setImage(image);
	}

	private void setFields() {

		nameField.setText(connection.getPerson().getName());
		descriptionField.setText(connection.getDescription());

		getAndSetCorrectImage();

		deleteButton.setDisable(false);
		if (isChosen()) {
			chooseButton.setDisable(true);
		}
        saveButton.setDisable(true);
	}

    private void getAndSetCorrectImage() {
        BufferedImage bfImage;

        if (connection.getPerson().getImageIsInBucket()) {
            System.out.println("Getting image from bucket");
            bfImage = AmazonBucketUploader.getOrCreateInstance().getImageFromBucket(candidate.getImageName());
        } else {
            bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
            System.out.println("Getting standard image");

        }

        setImageField(bfImage);
    }

	private boolean isChosen() {
	    int num = Math.min(candidate.getConnections().size(), 10);
		for (int i = 0; i < num; i++) {
		    Connection c = candidate.getConnections().get(i);
			if (connection == c) {
                return true;
            }
		}
		return false;
	}
	/*
	private void saveImageToFile(String imageName) {
        File outputFile = Utility.getResourcesFile("images/" + imageName + ".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(newImage, null);

        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
}

