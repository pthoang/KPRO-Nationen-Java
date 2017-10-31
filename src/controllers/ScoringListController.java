package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import model.AmazonBucketUploader;
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

	@FXML
	private Button markAsDoneButton;

	// Related to table view
	@FXML
	private TableView<Candidate> candidateTable;
	@FXML
	private TableColumn<Candidate, Integer> rankColumn;
	@FXML
	private TableColumn<Candidate, String> nameColumn;

	private ScoringList scoringList;
	private static ObservableList<Candidate> candidates;

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
	private ChoiceBox<String> genderChoiceBox = new ChoiceBox<String>(FXCollections.observableArrayList("", "Kvinne", "Mann", "Annet"));
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
	
	private Candidate candidate;

	private Image newImage;

	private final String IMAGE_PATH = "images/";
	private String errorMessage;

	private MainApp mainApp;

	private HashMap<String, Integer> candidateColor = new HashMap<>();

	private AmazonBucketUploader bucketUploader;

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
	
	public void setBucketUploader(AmazonBucketUploader bucketUploader) {
		this.bucketUploader = bucketUploader;
	}

	public void fillTable() {
		candidateTable.setItems(candidates);

		Candidate firstCandidate = candidates.get(0);
		setCandidate(firstCandidate);

		for(Candidate candidate : candidates){
			if(!candidateColor.containsKey(candidate.getName())){
				candidateColor.put(candidate.getName(), 0);
			}
		}
	}

	public Candidate getCandidateByName(String name){
		for(Candidate candidate : candidates){
			if(candidate.getName().equals(name)){
				return candidate;
			}
		}
		return null;
	}

	@FXML 
	private void initialize() {
		candidateTable.setEditable(true);

		nameColumn.setCellFactory(new CellFactory());

		rankColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("rank"));
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		nameColumn.setOnEditCommit(cell -> {
			int row = cell.getTablePosition().getRow();
			candidateTable.getItems().set(row, getCandidateByName(cell.getNewValue()));
		});

		candidateTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> setCandidate(newValue));

			
		networkNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		networkDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
		
		networkTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> connectionDialog(newValue));


		/**
		 * Adding listeners to the textfields for feedback handling
		 */
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);

		});

		previousYearRankField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		rankField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		municipalityField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		animalsPGField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		hiredHelpPGField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		farmingPGField.textProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
			markAsDoneButton.setDisable(false);
			saveCandidateButton.setDisable(false);
		});

		genderChoiceBox.getItems().addAll(FXCollections.observableArrayList("", "Kvinne", "Mann", "Annet"));
		
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
		networkTable.setItems(candidate.getConnections());		
		networkTable.refresh();
	}
	
	// Related to candidate view

	/**
	 * Updates the candidates based on the changes in the fields.
	 * Then refresh the table.
	 */
	@FXML
	public void handleSaveChangesToCandidate() {
		municipalityField.setStyle("");
		networkTable.setStyle("");

		candidate.setStatus("");
		int fieldsMissing = 0;
		saveCandidateButton.setDisable(true);

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
		candidate.setMunicipality(new SimpleStringProperty(newMunicipality));

		// Gender
		String gender = getSelectedGender();
		// validateGender(gender);
		candidate.setGender(gender);
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

		//Field handling only needed with persons
		if(candidate.getIsPerson()) {
			//Checks if network table is empty, if so give a warning
			if(networkTable.getItems().size() < 1){
				fieldsMissing++;
				networkTable.setStyle("-fx-border-color: #ffff65");
			}


			if(newMunicipality == null || newMunicipality.equals("")){
				fieldsMissing++;
				municipalityField.setStyle("-fx-border-color: #ffff65");
			}

			//Checks if there are any fields empty, if so set the candidate to "unfinished"
			if(fieldsMissing > 0){
				candidate.setStatus("unfinished");
			} else {
				candidate.setStatus("allFields");
			}
		}
		candidateTable.refresh();
		// Network
		// TODO
		handleErrorMessage(); 
		uploadToBucket();

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

	private void validateGender(String gender) {
		if (gender == "") {
			errorMessage += "\n Du må velge et kjønn. Velg 'Annet' om kandidaten ikke er et menneske.";
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
			candidate.setStatus("invalidFields");
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


	@FXML
	public void markAsDone(){
		if(markAsDoneButton.getText().equals("Marker komplett")){
			if(!nameField.getText().isEmpty()) {
				markAsDoneButton.setText("Marker ukomplett");
				getCandidateByName(nameField.getText()).setStatus("finished");
			}
		}else{
			if(!nameField.getText().isEmpty()){
				markAsDoneButton.setText("Marker komplett");
				getCandidateByName(nameField.getText()).setStatus("");
			}
		}
		candidateTable.refresh();
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
		networkTable.setStyle("");
		municipalityField.setStyle("");
		File file = new File(candidate.getImageURL());
		setImageField(file);

		nameField.setText(candidate.getName());
		municipalityField.setText(candidate.getMunicipality());
		rankField.setText(Integer.toString(candidate.getRank()));
		previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
		genderChoiceBox.getSelectionModel().select(setGenderChoice(candidate));;
		descriptionField.setText(candidate.getDescription());
		animalsPGField.setText(Integer.toString(candidate.getAnimalsPG()));
		hiredHelpPGField.setText(Integer.toString(candidate.getHiredHelpPG()));
		farmingPGField.setText(Integer.toString(candidate.getFarmingPG()));

		if(candidate.getStatus().equals("finished")){
			markAsDoneButton.setText("Marker ukomplett");
		}else{
			markAsDoneButton.setText("Marker komplett");
		}


		if(candidate.getIsPerson()) {
			if (municipalityField.getText() == null) {
				municipalityField.setStyle("-fx-border-color: #ffff65");
			}

			networkTable.setItems(candidate.getConnections());
			if (networkTable.getItems().size() < 1) {
				networkTable.setStyle("-fx-border-color: #ffff65");
			}
		}
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
		connectionDialog(null);
	}
	
	private void connectionDialog(Connection connection) {
		saveCandidateButton.setDisable(false);
		// Create the custom dialog.
		Dialog<Pair<Person, String>> dialog = new Dialog<>();
		dialog.setTitle("Nettverks kobling");
		dialog.setHeaderText("Legg inn navn og en kort beskrivelse for koblingen, samt link til bilde");

		// Set the button types.
		ButtonType addButtonType = new ButtonType("Legg til", ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Avslutt", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(50);
		grid.setVgap(20);
		
		TextField name = new TextField();
		TextField description = new TextField();
		TextField imageURL = new TextField();
		
		if (connection == null) {
			name.setPromptText("");
			description.setPromptText("");
			imageURL.setPromptText("");
			
			// Request focus on the name field by default.
			Platform.runLater(() -> name.requestFocus());
			
		} else {
			name.setText(connection.getName());
			description.setText(connection.getDescription());
			// TODO
			imageURL.setText(connection.getImageURL());
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

		dialog.showAndWait();

		if (connection == null) {
			Person person = new Person(name.getText(), imageURL.getText());
		
			candidate.addConnection(person, description.getText());
		} else {
			connection.setName(name.getText());
			connection.setDescription(description.getText());
			connection.setImageURL(imageURL.getText());
		}

		updateNetworkList();
	}
	public static class CellFactory implements Callback<TableColumn<Candidate, String>, TableCell<Candidate, String>> {

		private int editingIndex = -1 ;

		@Override
		public TableCell<Candidate, String> call(TableColumn<Candidate, String> param) {
			return new TableCell<Candidate, String>() {

				@Override
				protected void updateItem(String item, boolean empty)
				{
					super.updateItem(item, empty);
					setText(item);

					if(this.getIndex() > -1 && this.getIndex()<55){

						String status = candidates.get(this.getIndex()).getStatus();

						if(status.equals("finished")){
							getTableRow().setStyle("-fx-background-color: rgb(53,109,48);");
						} else if (status.equals("unfinished")){
							getTableRow().setStyle("-fx-background-color: rgb(156,156,59);");
						} else if (status.equals("invalidFields")){
							getTableRow().setStyle("-fx-background-color: rgb(157,57,68);");
						} else if (status.equals("allFields")) {
							getTableRow().setStyle("-fx-background-color: rgb(108,139,68);");
						} else {
							getTableRow().setStyle("");
						}
					}
				}

				@Override
				public void startEdit() {
					editingIndex = getIndex();
					super.startEdit();
				}

				@Override
				public void commitEdit(String newValue) {
					editingIndex = -1 ;
					super.commitEdit(newValue);
				}

				@Override
				public void cancelEdit() {
					editingIndex = -1 ;
					super.cancelEdit();
				}

			};
		}
	}
	
	public void uploadToBucket() {
		String imagePath = candidate.getImageURL();
		File image = new File(imagePath);
		String fileName = image.getName();
		bucketUploader.uploadFile(image, fileName);
	}
	
	private String getSelectedGender() {
		String gender = genderChoiceBox.getValue();
		if (gender.equals("Kvinne")) {
			return "F";
		} else if (gender.equals("Mann")) {
			return "M";
		} else if (gender.equals("Annet")) {
			return "O";
		}
		return "";
	}
	
	private int setGenderChoice(Candidate candidate) {
		String gender = candidate.getGender();
		if (gender.equals("F")) {
			return 1;
		} else if (gender.equals("M")) {
			return 2;
		} else if (gender.equals("O")) {
			return 3;
		} 
		return 0;
	}
}