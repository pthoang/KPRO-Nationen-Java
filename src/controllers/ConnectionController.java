package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

import Main.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import model.Candidate;
import model.Connection;
import model.Person;

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
		mainApp = MainApp.getInstance();
		parent = CandidateController.getOrCreateInstance();
	}

	public static ConnectionController getOrCreateInstance() {
		if (instance == null) {
			instance = new ConnectionController();
		}
		return instance;
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

		errorMessage += validateName(nameField.getText());
		errorMessage += validateDescription(descriptionField.getText());

		handleErrorMessage(errorMessage);
	}

	@FXML
	public void handleAddImage() {
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
		if (connection == null) {
			connection = candidate.getConnections().get(candidate.getConnections().size()-1);
		}
		parent.chooseConnection(connection);
	}

	// TODO: Is also in Person. Should find a way to reuse it
	private String validateName(String name) {
		Pattern pattern = Pattern.compile("^[A-ZÆØÅa-zæøå. \\-]++$");
		String errorMessage = "";

		if (name.length() <= 2) {
			errorMessage += "\n Navn må være lengre enn 2 bokstaver.";
		}
		if (!pattern.matcher(name).matches()) {
			errorMessage += "\n Navnet inneholder ugyldige bokstaver. Tillatt er: a-å, ., og -";
		}

		return errorMessage;
	}

	// Is also in Candidate. Should find a way to reuse it
	private String validateDescription(String description) {
		if (description.length() <= 5 || description.equals(null)) {
			return "\n Beskrivelse mangler";
		}
		return "";
	}

	// Is also in CandidateController. Should find a way to reuse it
	private void handleErrorMessage(String errorMessage) {
		if (errorMessage.length() != 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Feilmeldinger");
			alert.setHeaderText("Felter til koblingen er ikke korrekt utfylt.");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			candidate.setStatus("invalidFields");
		} else {
			saveConnection();
		}
	}

	private void saveConnection() {
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
		parent.closeDialog();
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
        } else {
            setImageField(imageURL);
            saveButton.setDisable(true);
        }
    }

	public void setImageField(String imageURL) {
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
		setImageField(connection.getPerson().getImageURL());

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
}

