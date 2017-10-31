package controllers;

import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import model.Candidate;
import model.Connection;

public class ConnectionController {
	
	@FXML
	private TextField nameField;
	@FXML
	private TextField descriptionField;

	private Connection connection;
	private Candidate candidate;
	private MainApp mainApp;
	

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	@FXML
	public void handleDelete() {
		candidate.deleteConnection(connection);		
	}
	
	@FXML
	public void handleSave() {
		connection.setName(nameField.getText());
		connection.setDescription(descriptionField.getText());
		//connection.setImageURL(imageURLField.getText());
	}
	
	@FXML
	public void handleAddImage() {
		File file = mainApp.choseFileAndGetFile();
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			Image newImage = SwingFXUtils.toFXImage(bufferedImage, null);
			//imageView.setImage(newImage);
		} catch (IOException ex) {
			System.out.println("Error when loading image: " + ex);
		}
	}
	
	@FXML
	public void handleCancel() {
		
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	private void setFields() {
		nameField.setText(connection.getName());
		descriptionField.setText(connection.getDescription());
	}
	
	
}

