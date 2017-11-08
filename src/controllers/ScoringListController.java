package controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import model.*;
import Main.MainApp;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.stage.FileChooser;
import model.Candidate;
import model.ScoringList;

import java.io.FileWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

public class ScoringListController {
	@FXML
	private Button backButton;
	@FXML
	private Button saveButton;

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
	private Button analyzeButton;

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
	private TextField twitterField = new TextField();
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

	private AmazonBucketUploader bucketUploader;
	
	public ScoringListController() {
	}
	
	public void setBucketUploader(AmazonBucketUploader bucketUploader) {
		this.bucketUploader = bucketUploader;
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


		/*
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
		}*/

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

		String twitter = twitterField.getText();
		candidate.setTwitter(new SimpleStringProperty(twitter));

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
			Alert alert = new Alert(Alert.AlertType.ERROR);
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
		File file = mainApp.chooseAndGetFile();
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
		twitterField.setText(candidate.getTwitter());
		/*
		animalsPGField.setText(Integer.toString(candidate.getAnimalsPG()));
		hiredHelpPGField.setText(Integer.toString(candidate.getHiredHelpPG()));
		farmingPGField.setText(Integer.toString(candidate.getFarmingPG()));

		*/

		networkTable.setItems(candidate.getConnections());
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
		twitterField.setText("");
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
		// Create the custom dialog.
		Dialog<Pair<Person, String>> dialog = new Dialog<>();
		dialog.setTitle("Nettverks kobling");
		dialog.setHeaderText("Legg inn navn og en kort beskrivelse for koblingen, samt link til bilde");

		// Set the button types.
		ButtonType addButtonType = new ButtonType("Legg til", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Avslutt", ButtonBar.ButtonData.CANCEL_CLOSE);
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
	
	public void uploadToBucket() {
		String imagePath = candidate.getImageURL();
		File image = new File(imagePath);
		String fileName = image.getName();
		bucketUploader.uploadFile(image, fileName);
	}

	@FXML
	public void handleAnalyzeAll() {
		mainApp.generateAll();
	}
    @FXML
    private TableView<Candidate> candidateTable;
    @FXML
    private TableColumn<Candidate, Integer> rankColumn;
    @FXML
    private TableColumn<Candidate, String> nameColumn;
    @FXML
    private Label countLabel = new Label();

    private EditListController parentController;

    private ScoringList scoringList;
    private MainApp mainApp;
    private static ObservableList<Candidate> candidates;
    private HashMap<String, Integer> candidateColor = new HashMap<>();


    public void setParentController(EditListController editListController) {
        this.parentController = editListController;
    }

    public void setScoringList(ScoringList scoringList) {
        this.scoringList = scoringList;

        candidates = scoringList.getCandidates();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void fillTable() {
        candidateTable.setItems(candidates);

        Candidate firstCandidate = candidates.get(0);
        parentController.setCandidate(firstCandidate);

        for(Candidate candidate : candidates){
            if(!candidateColor.containsKey(candidate.getName())){
                candidateColor.put(candidate.getName(), 0);
            }
        }

        updateCountLabel();
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

        nameColumn.setCellFactory(new ScoringListController.CellFactory());

        rankColumn.setCellValueFactory(new PropertyValueFactory<Candidate, Integer>("rank"));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        nameColumn.setOnEditCommit(cell -> {
            int row = cell.getTablePosition().getRow();
            candidateTable.getItems().set(row, getCandidateByName(cell.getNewValue()));
        });

        candidateTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> parentController.setCandidate(newValue));
    }

    public void refreshTable() {
        parentController.updateLists();
        candidateTable.refresh();
        updateCountLabel();
    }

    private void updateCountLabel() {
        int max = Settings.getOrCreateInstance().getNumCandidates();
        int actualLength = scoringList.getLength();
        countLabel.setText(actualLength + "/" + max);

        if (actualLength > max) {
            countLabel.setStyle("-fx-text-fill: #d44c3d");
        } else {
            countLabel.setStyle("-fx-text-fill: #fafafa");
        }
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

                        String status =  candidates.get(this.getIndex()).getStatus();

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

    @FXML
    public void handleExportFile() {
    	
    	/**
		 * Gson creates unnecessary fields in the json because of the property "SimpleStringProperty".
		 * FxGson is a library which removes the unnecessary fields and generates the required JSON format.
		 */
		Gson fxGson = FxGson.create();
		String json = fxGson.toJson(scoringList); //Serialize an object to json string
		System.out.println(json);
		
		FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.JSON)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getStage());
        if(file != null){
            SaveFile(json, file);
        }
//		
//        String errorMessage = validateList();
//        handleErrorMessage(errorMessage);
    }
    
    /**
	 * Save the content of gson object as string in a file.
	 */
	private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
            
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            
        }
    }

    private void handleErrorMessage(String errorMessage) {
        if (errorMessage.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Feilmeldinger");
            alert.setHeaderText("Listen har feil");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            // TODO: export the file
        }
    }
    private String validateList() {
        if (scoringList.getLength() > Settings.getOrCreateInstance().getNumCandidates()) {
            return "Det er for mange kandidater i listen. Fjern kandidater eller endre antallet aksepterte i 'Instillinger'";
        }
        // TODO: also validate if some of the candidates has fields missing?
        return "";
    }
}
