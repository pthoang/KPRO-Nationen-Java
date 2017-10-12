package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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

import Main.MainApp;

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
	
	private Candidate candidate;
	
	private Image newImage;
	
	private final String IMAGE_PATH = "images/";
	
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
		updateLists();
		
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
		updateLists();
		candidateTable.refresh();
	}
	
	public void updateLists() {
		scoringList = super.mainApp.getScoringList();		
		candidates = scoringList.getCandidates();
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
		// Name
		String newName = nameField.getText();
		candidate.splitUpAndSaveName(newName);
		
		// Image
		if (newImage != null) {
			saveImageToFile();
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
		
		refreshTable();
	}
	
	private void createAndAddEmptyCandidate() {
		candidate = new Candidate("", 0, 0);
		scoringList.addCandidate(candidate);
	}
	
	@FXML
	private void handleChangeImage() {
		// TODO: can maybe be its own static function, since it is used multiple places. Returns the path as a string
		FileChooser fileChooser = new FileChooser();

		// TODO
		//FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		//FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		//fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		
		Stage stage = super.mainApp.getStage();
		File file = fileChooser.showOpenDialog(stage);
		
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
		String imageName = candidate.getFirstName() + candidate.getLastName();
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
}
