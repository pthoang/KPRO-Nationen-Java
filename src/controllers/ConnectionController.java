package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

import Main.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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
	private String imageURL = "resources/standard.png";


	public ConnectionController() {
		instance = this;
		mainApp = MainApp.getInstance();
		parent = CandidateController.getOrCreateInstance();
	}

	/*
	public static ConnectionController getOrCreateInstance() {
		if (instance == null) {
			instance = new ConnectionController();
		}
		return instance;
	}
	*/

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
		if (connection != null) {
			setFields();
		} else {
			setImageField(imageURL);
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
	public void handleAddImage() {
		File file = mainApp.chooseAndGetFile();
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			newImage = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(newImage);
		} catch (IOException ex) {
			System.out.println("Error when loading image: " + ex);
		}
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
			saveImageToFile(imageName);
			AmazonBucketUploader.getOrCreateInstance().uploadFile(new File(imageURL), imageName);
		} else {
			connection.getPerson().setName(nameField.getText());
			connection.setDescription(descriptionField.getText());
			connection.getPerson().setImageName(imageURL);
		}
		parent.closeDialog();
	}

	@FXML
	public void handleCancel() {
		parent.closeDialog();
	}

	private void setImageField(String imageURL) {
	    System.out.println("Set image field in connectionC: " + imageURL);
	    if (! imageURL.endsWith(".png")) {
	        imageURL += ".png";
        }
		File file = new File(imageURL);
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			newImage = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(newImage);
		} catch (IOException ex) {
			System.out.println("Error when loading image: " + ex);
		}
	}

	private void setFields() {
		nameField.setText(connection.getPerson().getName());
		descriptionField.setText(connection.getDescription());
		setImageField(connection.getPerson().getImageName());

		deleteButton.setDisable(false);
		if (isChosen()) {
			chooseButton.setDisable(true);
		}
        saveButton.setDisable(true);
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

	private void saveImageToFile(String imageName) {
        File outputFile = new File("images/" + imageName + ".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(newImage, null);

        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

