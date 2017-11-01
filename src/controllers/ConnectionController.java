package controllers;

import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

import Main.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import model.Candidate;
import model.Connection;
import model.ScoringList;

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

	private ScoringListController parent;
	

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	public void setParent(ScoringListController scoringListController) {
		this.parent = scoringListController;
	}

	@FXML
	public void handleDelete() {
		System.out.println("Deleting conneciton");
		candidate.deleteConnection(connection);
	}
	
	@FXML
	public void handleSave() {
		System.out.println("Saving connection");
		connection.setName(nameField.getText());
		connection.setDescription(descriptionField.getText());
		//connection.setImageURL(imageURLField.getText());
	}
	
	@FXML
	public void handleAddImage() {
		System.out.println("Adding image to connection");
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
		parent.closeDialog();
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

