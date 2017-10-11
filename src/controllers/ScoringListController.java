package controllers;

import java.io.File;

import Main.MainApp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Candidate;
import model.ScoringList;

public class ScoringListController extends SuperController {
	
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
	private TableColumn<Candidate, String> firstNameColumn;
	@FXML
	private TableColumn<Candidate, String> lastNameColumn;
	
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
	
	private Candidate candidate;
	
	private String newImagePath;
	
	
	
	public ScoringListController() {
		super();
	}
	
	/**
	 * Set mainApp in super, then gets the candidates and shows them in the table.
	 * @params mainApp
	 */
	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);

		getAndFillTable();
	}
	
	
	
	public void getAndFillTable() {
		scoringList = super.mainApp.getScoringList();
		candidates = scoringList.getCandidates();
		
		candidateTable.setItems(candidates);
		
		Candidate firstCandidate = candidates.get(0);
		setCandidate(firstCandidate);
	}
	
	@FXML 
	private void initialize() {
		rankColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("rank"));
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
	
		candidateTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue) -> setCandidate(newValue));
	}
	
	@FXML
	private void handleBack() {
		super.viewController.showStartMenu();
	}
	
	@FXML
	private void handleSave() {
		super.mainApp.getScoringList().saveList();	
	}
	
	public void refreshTable() {
		
		scoringList = super.mainApp.getScoringList();
		
		//scoringList.printCandidates();
		
		candidates = scoringList.getCandidates();
				
		candidateTable.refresh();
	}
	
	
	
	// Related to candidate view

	/**
	 * Updates the candidates based on the changes in the fields.
	 * Then refresh the table.
	 */
	@FXML
	public void handleSaveChangesToCandidate() {
		
		if (candidate == null) {
			candidate = new Candidate("", 0, 0);
			scoringList.addCandidate(candidate);
		}
		// Name
		String newName = nameField.getText();
		candidate.splitUpAndSaveName(newName);
		System.out.println("Changed name");
		
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
		candidate.setDescription(new SimpleStringProperty(description));

		// ProductionGrants
		int animalsPG = Integer.parseInt(animalsPGField.getText());
		candidate.setAnimalsPG(new SimpleIntegerProperty(animalsPG));
		
		int hiredHelpPG = Integer.parseInt(hiredHelpPGField.getText());
		candidate.setHiredHelpPG(new SimpleIntegerProperty(hiredHelpPG));
		
		int fargminPG = Integer.parseInt(farmingPGField.getText());
		candidate.setFarmingPG(new SimpleIntegerProperty(fargminPG));
		
		// Network
		// TODO	
		refreshTable();
	}
	
	@FXML
	private void handleChangeImage() {
		FileChooser fileChooser = new FileChooser();
		
		Stage stage = super.mainApp.getStage();
		File file = fileChooser.showOpenDialog(stage);
		
		newImagePath = file.getAbsolutePath();
		
		System.out.println("Path: " + newImagePath);
		Image image = new Image(getClass().getResource(newImagePath).toExternalForm());
		
		imageView.setImage(image);
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
		Image image = new Image(getClass().getResource("../person_icon.png").toExternalForm());
		
		imageView.setImage(image);
		nameField.setText(candidate.getFirstName() + " " + candidate.getLastName());
		municipalityField.setText(candidate.getMunicipality());
		rankField.setText(Integer.toString(candidate.getRank()));
		previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
		descriptionField.setText(candidate.getDescription());
		animalsPGField.setText(Integer.toString(candidate.getAnimalsPG()));
		hiredHelpPGField.setText(Integer.toString(candidate.getHiredHelpPG()));
		farmingPGField.setText(Integer.toString(candidate.getFarmingPG()));
	}
	
	public void cleanFields() {
		imageView.setImage(new Image(getClass().getResource("../person_icon.png").toExternalForm()));
		nameField.setText("");
		municipalityField.setText("");
		rankField.setText("");
		previousYearRankField.setText("");
		descriptionField.setText("");
		animalsPGField.setText("");
		hiredHelpPGField.setText("");
		farmingPGField.setText("");
	}
}
