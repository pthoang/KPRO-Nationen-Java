package controllers;

import java.io.File;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;

public class CandidateController extends SuperController {

	@FXML
	private Button deleteButton;
	@FXML
	private Button saveCandidateButton;
	@FXML
	private Button saveListButton;
	
	@FXML
	private ImageView image;
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
	
	private Candidate candidate;
	
	private ScoringListController scoringListController;
	
	private String newImagePath;
	private String errorMessage;
	
	@FXML
	public void initialize() {
	}
	
	/**
	 * Sets the scoringListController.
	 * @param scoringListController
	 */
	public void setScoringListController(ScoringListController scoringListController) {
		this.scoringListController = scoringListController;
	}
	
	/**
	 * Updates the candidates based on the changes in the fields.
	 * Then refresh the table.
	 */
	@FXML
	public void handleSaveChangesToCandidate() {
		errorMessage = "";
		
		// Name
		String newName = nameField.getText();
		validateName(newName);
		
		// Image
		if (newImagePath != "") {
			candidate.setImageURLProperty(new SimpleStringProperty(newImagePath));
		}
		
		// PreviousYearRank
		int newPreviousYearRank = Integer.parseInt(previousYearRankField.getText());
		candidate.setPreviousYearRank(new SimpleIntegerProperty(newPreviousYearRank));

		// Rank
		int rank = Integer.parseInt(rankField.getText());
		candidate.setRank(new SimpleIntegerProperty(rank));
		
		// Municipality
		String newMunicipality = municipalityField.getText();
		candidate.setMunicipality(new SimpleStringProperty(newMunicipality));;
		
		// Description
		String description = descriptionField.getText();
		validateDescription(description);

		// ProductionGrants
		int animalsPG = Integer.parseInt(animalsPGField.getText());
		candidate.setAnimalsPG(new SimpleIntegerProperty(animalsPG));
		
		int hiredHelpPG = Integer.parseInt(hiredHelpPGField.getText());
		candidate.setHiredHelpPG(new SimpleIntegerProperty(hiredHelpPG));
		
		int fargminPG = Integer.parseInt(farmingPGField.getText());
		candidate.setFarmingPG(new SimpleIntegerProperty(fargminPG));
		
		// Network
		// TODO	
		
		handleErrorMessage(); 
	}
	
	private void validateName(String name) {
		Pattern pattern = Pattern.compile("^[A-ZÆØÅa-zæøå.- ]++$");
		
		if (name.length() <= 2) {
			errorMessage += "\n Navn må være lengre enn 2 bokstaver.";
		} 
		if (!pattern.matcher(name).matches()) {
			errorMessage += "\n Navnet inneholder ugyldige bokstaver. Tillatt er: a-å, ., og -";
		}
	}
	
	private void validateDescription(String description) {
		if (description.length() <= 5) {
			errorMessage += "\n Beskrivelse mangler:";
		}
	}
	
	private void saveCandidate() {
		String newName = nameField.getText();
		candidate.splitUpAndSaveName(newName);
		
		String description = descriptionField.getText();
		candidate.setDescription(new SimpleStringProperty(description));
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
			scoringListController.refreshTable();
		}
	}
	
	@FXML
	private void handleChangeImage() {
		FileChooser fileChooser = new FileChooser();
		
		System.out.println("MainApp: " + super.mainApp);
		Stage stage = super.mainApp.getStage();
		File file = fileChooser.showOpenDialog(stage);
		
		newImagePath = file.getAbsolutePath();
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
		super.mainApp.getScoringList().deleteCandidate(candidate);
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
		//image.setImage(new Image("/home/doraoline/Koding/KPRO-Nationen-Java/person_icon.png"));
		nameField.setText(candidate.getFirstName() + " " + candidate.getLastName());
		municipalityField.setText(candidate.getMunicipality());
		rankField.setText(Integer.toString(candidate.getRank()));
		previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
		descriptionField.setText(candidate.getDescription());
		animalsPGField.setText(Integer.toString(candidate.getAnimalsPG()));
		hiredHelpPGField.setText(Integer.toString(candidate.getHiredHelpPG()));
		farmingPGField.setText(Integer.toString(candidate.getFarmingPG()));
	}
}
