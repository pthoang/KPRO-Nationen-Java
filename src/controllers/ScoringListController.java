package controllers;

import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.Candidate;
import model.Connection;
import model.Person;
import model.ScoringList;

import Main.MainApp;

public class ScoringListController {

	@FXML
	private Button backButton;
	@FXML
	private Button saveButton;

	// Related to table view
	@FXML
	private TableView<Candidate> candidateTable;
	@FXML
	private TableColumn<Candidate, Integer> rankColumn;
	@FXML
	private TableColumn<Candidate, String> nameColumn;

	private ScoringList scoringList;
	private ObservableList<Candidate> candidates;

	// Related to candidate view 
	@FXML
	private Button deleteButton;
	@FXML
	private Button saveCandidateButton;
	@FXML
	private Button saveListButton;
	@FXML
	private Button changeImageButton;

	@FXML
	private ImageView imageView = new ImageView();
	@FXML
	private TextField nameField = new TextField();
	@FXML
	private TextField previousYearRankField  = new TextField();
	@FXML
	private TextField rankField  = new TextField();
	@FXML
	private TextArea descriptionField  = new TextArea();
	@FXML
	private TextField municipalityField  = new TextField();
	@FXML
	private TextField animalsPGField = new TextField();
	@FXML
	private TextField hiredHelpPGField = new TextField();
	@FXML
	private TextField farmingPGField = new TextField();
	@FXML
	private TableView<Connection> networkTable;
	@FXML
	private TableColumn<Connection, String> networkNameColumn;
	@FXML
	private TableColumn<Connection, String> networkDescriptionColumn;;

	private ObservableList<Connection> connections =  FXCollections.observableArrayList();
	
	private Candidate candidate;

	private Image newImage;

	private final String IMAGE_PATH = "images/";
	private String errorMessage;

	private MainApp mainApp;

	public ScoringListController() {
	}

	/**
	 * Set mainApp in super, then gets the candidates and shows them in the table.
	 * @params mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		updateLists();

		if (candidates.size() > 0) {
			fillTable();
		}
	}

	public void fillTable() {
		candidateTable.setItems(candidates);

		Candidate firstCandidate = candidates.get(0);
		setCandidate(firstCandidate);
	}

	@FXML 
	private void initialize() {
		rankColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("rank"));
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		candidateTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> setCandidate(newValue));
			
		networkNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		networkDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
		
		//networkTable.getSelectionModel().selectedItemProperty().addListener(
		//		(observable, oldValue, newValue) -> connectionDialog(newValue));
		
	}

	public void refreshTable() {
		updateLists();
		candidateTable.refresh();
	}

	public void updateLists() {
		scoringList = mainApp.getScoringList();
		candidates = scoringList.getCandidates();
	}
	
	public void updateNetworkList() {
		System.out.println("Updating networkTable");
		networkTable.setItems(connections);
		
		System.out.println(connections);
		System.out.println(connections.get(0).getName());
		networkTable.refresh();
	}
	
	private ObservableList<HashMap> generateDataInMap() {
		// Max number of network
        int max = 15;
        ObservableList<HashMap> networkMap = FXCollections.observableArrayList();
        for (int i = 1; i < max; i++) {
            HashMap<Person, String> connection = candidate.getNetwork();
 
            networkMap.add(connection);
        }
        return networkMap;
	}



	// Related to candidate view

	/**
	 * Updates the candidates based on the changes in the fields.
	 * Then refresh the table.
	 */
	@FXML
	public void handleSaveChangesToCandidate() {
		if (candidate == null) {
			createAndAddEmptyCandidate();
		}

		errorMessage = "";

		// Name
		String newName = nameField.getText();
		// validateName(newName);

		// Image
		if (newImage != null) {
			saveImageToFile();
		}

		// PreviousYearRank
		String newPreviousYearRank = previousYearRankField.getText();
		// validatePreviousYearRank(newPreviousYearRank);

		// Rank
		String newRank = rankField.getText();
		// validateRank(newRank);

		// Municipality
		String newMunicipality = municipalityField.getText();
		candidate.setMunicipality(new SimpleStringProperty(newMunicipality));;

		// Description
		String description = descriptionField.getText();
		// validateDescription(description);

		// ProductionGrants
		try {
			int animalsPG = Integer.parseInt(animalsPGField.getText());
			candidate.setAnimalsPG(new SimpleIntegerProperty(animalsPG));
		} catch (NumberFormatException e) {
			System.out.println("Candidate don't have a animalsPG");
		}

		try {
			int hiredHelpPG = Integer.parseInt(hiredHelpPGField.getText());
			candidate.setHiredHelpPG(new SimpleIntegerProperty(hiredHelpPG));
		} catch (NumberFormatException e) {
			System.out.println("Candidate don't have a hiredHelpPG");
		}

		try {
			int farmingPG = Integer.parseInt(farmingPGField.getText());
			candidate.setFarmingPG(new SimpleIntegerProperty(farmingPG));
		} catch (NumberFormatException e) {
			System.out.println("Candidate don't have a farmingPG");
		}

		// Network
		// TODO	

		handleErrorMessage(); 
	}

	private void createAndAddEmptyCandidate() {
		int nextCandidateRank = candidates.size() + 1;

		rankField.setText(Integer.toString(nextCandidateRank));
		candidate = new Candidate("", nextCandidateRank, 0);

		scoringList.addCandidate(candidate);
	}

	private void validateName(String name) {
		Pattern pattern = Pattern.compile("^[A-ZÆØÅa-zæøå. \\-]++$");

		if (name.length() <= 2) {
			errorMessage += "\n Navn må være lengre enn 2 bokstaver.";
		} 
		if (!pattern.matcher(name).matches()) {
			errorMessage += "\n Navnet inneholder ugyldige bokstaver. Tillatt er: a-å, ., og -";
		} 
		if (nameExistInList(name)) {
			errorMessage += "\n Det eksisterer allerede noen med det for- og etternavnet";
		}
	}

	private void validateRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				errorMessage += "\n Plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			errorMessage += "\n Plasseringen er ikke et tall";
		}
	}

	private void validatePreviousYearRank(String rankString) {
		try {
			int rank = Integer.parseInt(rankString);
			if (rank < 1 || rank > 100) {
				errorMessage += "\n FJorårets plasseringen må være mellom 1 og 100";
			}
		} catch (NumberFormatException e) {
			errorMessage += "\n Fjorårets plasseringen er ikke et tall";
		}
	}


	private void validateDescription(String description) {
		if (description.length() <= 5) {
			errorMessage += "\n Beskrivelse mangler:";
		}
	}

	private void saveCandidate() {
		String newName = nameField.getText();
		candidate.setName(new SimpleStringProperty(newName));

		String description = descriptionField.getText();
		candidate.setDescription(new SimpleStringProperty(description));

		try {
			int rank = Integer.parseInt(rankField.getText());
			candidate.setRank(new SimpleIntegerProperty(rank));
		} catch (NumberFormatException e) {
			System.out.println("Candidate don't have a rank");
		}


		try {
			int previousYearRank = Integer.parseInt(previousYearRankField.getText());
			candidate.setPreviousYearRank(new SimpleIntegerProperty(previousYearRank));
		} catch (NumberFormatException e) {
			System.out.println("Candidate don't have a previousYear rank");
		}
	}

	private void handleErrorMessage() {
		if (errorMessage.length() != 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Feilmeldinger");
			alert.setHeaderText("Felter til kandidaten er ikke korrekt utfylt.");
			alert.setContentText(errorMessage);
			alert.showAndWait();
		} else {
			saveCandidate();
			refreshTable();
		}
	}

	@FXML
	private void handleChangeImage() {
		File file = mainApp.choseFileAndGetFile();
		setImageField(file);
	}

	@FXML
	public void handleNewCandidate() {
		candidate = null;
		cleanFields();
	}

	/**
	 * Saves the list to a file locally.
	 */
	@FXML
	public void handleSaveList() {
		// TODO: copy the list and save it as temporaryList?
	}

	/**
	 * Called when the delete-button is clicked. Deletes a candidate from the list.
	 */
	@FXML
	public void handleDelete() {
		scoringList.deleteCandidate(candidate);
		cleanFields();
	}

	/**
	 * Get the candidate to be set in the fields, and then fill inn the fields.
	 * @param candidate
	 */
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
		setFields();		
	}

	/**
	 * Sets all the fields to the candidate.
	 */
	public void setFields() {
		File file = new File(candidate.getImageURL());
		setImageField(file);

		nameField.setText(candidate.getName());
		municipalityField.setText(candidate.getMunicipality());
		rankField.setText(Integer.toString(candidate.getRank()));
		previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
		descriptionField.setText(candidate.getDescription());
		animalsPGField.setText(Integer.toString(candidate.getAnimalsPG()));
		hiredHelpPGField.setText(Integer.toString(candidate.getHiredHelpPG()));
		farmingPGField.setText(Integer.toString(candidate.getFarmingPG()));
		
		networkTable.setItems(connections);
	}

	public void cleanFields() {
		File file = new File("images/standard.png");
		setImageField(file);

		nameField.setText("");
		municipalityField.setText("");
		rankField.setText("");
		previousYearRankField.setText("");
		descriptionField.setText("");
		animalsPGField.setText("");
		hiredHelpPGField.setText("");
		farmingPGField.setText("");
	}

	private void saveImageToFile() {
		// TODO: set as ID instead
		String imageName = candidate.getName();
		imageName = imageName.replace(" ",  "");

		File outputFile = new File(IMAGE_PATH + imageName + ".png");
		BufferedImage bImage = SwingFXUtils.fromFXImage(newImage,  null);
		try {
			ImageIO.write(bImage,  "png", outputFile);
			candidate.setImageURLProperty(new SimpleStringProperty("images/" + imageName + ".png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void setImageField(File file) {
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			newImage = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(newImage);
		} catch (IOException ex) {
			System.out.println("Error when loading image: " + ex);
		}
	}

	// TODO: can be made more complex
	private boolean nameExistInList(String name) {
		String[] names = name.split(" ");
		int numberOfNames = names.length;
		for (int i = 0; i < candidates.size(); i++) {
			Candidate c = candidates.get(i);
			String candidateName = c.getName();
			
			if (name.equals(candidateName)) {
				return true;
			}
		}
		return false;
	}
	
	
	@FXML
	public void handleAddConnection() {
		
		System.out.println("Handle Add Connection");
		connectionDialog(null);
		
		updateNetworkList();
		
	}
	
	private void connectionDialog(Connection connection) {
		// Create the custom dialog.
		Dialog<Pair<Person, String>> dialog = new Dialog<>();
		dialog.setTitle("Nettverks kobling");
		dialog.setHeaderText("Legg inn navn og en kort beskrivelse for koblingen, samt link til bilde");

		// Set the button types.
		ButtonType addButtonType = new ButtonType("Legg til", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(50);
		grid.setVgap(20);
		//grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField name = new TextField();
		TextField description = new TextField();
		TextField imageURL = new TextField();

		
		if (connection == null) {
			name.setPromptText("");
			description.setPromptText("");
			imageURL.setPromptText("");
		} else {
			
			name.setPromptText(connection.getName());
			description.setPromptText(connection.getDescription());
			// TODO
			imageURL.setPromptText("");
		}
		
		grid.add(new Label("Navn:"), 0, 0);
		grid.add(name, 1, 0);
		grid.add(new Label("Beskrivelse:"), 0, 1);
		grid.add(description, 1, 1);
		grid.add(new Label("Link til bilde:"), 0, 2);
		grid.add(imageURL, 1, 2);

		// Enable/Disable login button depending on whether a username was entered.
		Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
		addButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		name.textProperty().addListener((observable, oldValue, newValue) -> {
		    addButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the name field by default.
		Platform.runLater(() -> name.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		/**
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == addButtonType) {
		        return new Pair<>(name.getText(), description.getText());
		    }
		    return null;
		});

		
		result.ifPresent(nameDescription -> {
		    System.out.println("name=" + nameDescription.getKey() + ", description=" + nameDescription.getValue());
		});
		**/
		Optional<Pair<Person, String>> result = dialog.showAndWait();

		String nameString = name.getText();
		System.out.println("Name: " + nameString);
		// Save the person
		Person person = new Person(name.getText(), imageURL.getText());
		//candidate.addToNetwork(person, description.getText());
		Connection newConnection = new Connection(candidate, person, description.getText());
		//System.out.println(connection.getName() + " + " + connection.getDescription());
		connections.add(newConnection);
		
		updateNetworkList();
	}
}
