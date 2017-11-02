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
import javafx.scene.image.Image;
import model.Candidate;
import model.Connection;
import model.Person;

public class ConnectionController {
	
	@FXML
	private TextField nameField;
	@FXML
	private TextField descriptionField;
	@FXML
	private ImageView imageView = new ImageView();

	private Connection connection;
	private Candidate candidate;
	private MainApp mainApp;
	private CandidateController parent;

	private Image newImage;
	private String imageURL = "resources/standard.png";

	public ConnectionController() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void setParent(CandidateController candidateController) {
		this.parent = candidateController;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	@FXML
	public void handleDelete() {
		candidate.deleteConnection(connection);
		parent.closeDialog();
	}

	@FXML
	public void handleSave() {
		if (connection == null) {
			// TODO: imagePath
			Person person = new Person(nameField.getText(), imageURL);
			candidate.addConnection(person, descriptionField.getText());
		} else {
			connection.getPerson().setName(nameField.getText());
			connection.setDescription(descriptionField.getText());
			connection.getPerson().setImageURL(imageURL);
		}
		saveImageToFile();
		System.out.println("Handle save connection");
		parent.closeDialog();
	}

	@FXML
	public void handleAddImage() {
		System.out.println("Adding image to connection");
		String imageName = nameField.getText();
		imageName = imageName.replace(" ",  "");

		imageURL = "images/" + imageName + ".png";

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
		parent.chooseConnection();
	}

	private void saveImageToFile() {
		// TODO: set as ID instead
		String imageName = nameField.getText();
		imageName = imageName.replace(" ",  "");

		imageURL = "images/" + imageName + ".png";
		File outputFile = new File(imageURL);
		BufferedImage bImage = SwingFXUtils.fromFXImage(newImage,  null);
		try {
			ImageIO.write(bImage,  "png", outputFile);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@FXML
	public void handleCancel() {
		parent.closeDialog();
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
		if (connection != null) {
			setFields();
		}
	}

	public void setImageField() {
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
		System.out.println("Image url when setting: " + connection.getPerson().getImageURL());
		File file = new File(connection.getPerson().getImageURL());
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(image);
		} catch (IOException ex) {
			System.out.println("Error when loading image: " + ex);
		}

	}
	
	
}

