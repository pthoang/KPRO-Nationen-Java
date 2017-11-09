package controllers;

import Main.MainApp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.input.MouseEvent;

public class CandidateController {

    private ObservableList GENDER_CHOICES = FXCollections.observableArrayList("", "Kvinne", "Mann", "Annet");

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
    private ChoiceBox<String> genderChoiceBox = new ChoiceBox<String>(GENDER_CHOICES);
    @FXML
    private TextField yearOfBirthField = new TextField();
    @FXML
    private TextField twitterField = new TextField();
	@FXML
    private TextField professionField = new TextField();
    @FXML
    private TextField animalsPGField = new TextField();
    @FXML
    private TextField hiredHelpPGField = new TextField();
    @FXML
    private TextField farmingPGField = new TextField();
    @FXML
    private TextField titleField = new TextField();

    @FXML
    private TableView<Connection> networkTable;
    @FXML
    private TableColumn<Connection, String> networkNameColumn;
    @FXML
    private TableColumn<Connection, String> networkDescriptionColumn;

    @FXML
    private Button saveCandidateButton;
    @FXML
    private Button markAsDoneButton;

    private static CandidateController instance = null;
    private final String IMAGE_PATH = "images/";
    private AmazonBucketUploader bucketUploader;
    private Image newImage;
    private Candidate candidate;
    private MainApp mainApp;
    private Stage connectionDialog;
    private EditListController parent;

    private List<Object> inputFields = new ArrayList<>(Arrays.asList(nameField, previousYearRankField, rankField,
            municipalityField, genderChoiceBox, yearOfBirthField, professionField, twitterField, animalsPGField,
            hiredHelpPGField, farmingPGField, descriptionField, titleField));


    public CandidateController() {
        instance = this;
        mainApp = MainApp.getInstance();
        bucketUploader = AmazonBucketUploader.getOrCreateInstance();
        parent = EditListController.getOrCreateInstance();
    }

    public static CandidateController getOrCreateInstance() {
        if (instance == null) {
            instance = new CandidateController();
        }
        return instance;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
        setFields();
        updateNetworkList();
        markSelectedConnections();
    }

    @FXML
    private void initialize() {
        networkNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        networkDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());

        networkTable.setPlaceholder(new Label("Ikke noe nettverk å vise"));

        inputFields.add(networkTable);
        /**
         * Adding listeners to the textfields for feedback handling
         */
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        previousYearRankField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        rankField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        genderChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
            boolean isPerson = newValue.intValue() == 3;
            disableFieldsIfNotPerson(isPerson);

        });

        municipalityField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });
        
        yearOfBirthField.textProperty().addListener((observable, oldValue, newValue) -> {
        	disableButtons(false);
        });

        genderChoiceBox.getItems().addAll(GENDER_CHOICES);
        genderChoiceBox.setValue("");

        professionField.textProperty().addListener((observable, oldValue, newValue) -> {
        	disableButtons(false);
        });
      
        // TODO: Move to its own class
        animalsPGField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        hiredHelpPGField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        farmingPGField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

    }

    private void disableButtons(boolean disable) {
        markAsDoneButton.setDisable(disable);
        saveCandidateButton.setDisable(disable);
    }

    private String getSelectedGender() {
        String gender = genderChoiceBox.getValue();
        switch (gender) {
            case "Kvinne":
                return "F";
            case "Mann":
                return "M";
            case "Annet":
                return "O";
        }
        return "";
    }

    private int setGenderChoice(Candidate candidate) {
        String gender = candidate.getGender();
        switch (gender) {
            case "F":
                return 1;
            case "M":
                return 2;
            case "O":
                return 3;
        }
        return 0;
    }

    // Button actions
    @FXML
    public void handleSaveChangesToCandidate() {
        // TODO: called multiple times - change to a function?
        saveCandidateButton.setDisable(true);

        candidate.setStatus("");
        if (candidate == null) {
            createAndAddEmptyCandidate();
        }

        String errorMessage = "";

        errorMessage += validateCandidate();
        
//      //Year of Birth
        // TODO
//      String newYearOfBirth = yearOfBirthField.getText();
//      candidate.setYearOfBirth(newYearOfBirth);

        // Municipality
        // TODO: missing validation
        String newMunicipality = municipalityField.getText();
        candidate.setMunicipality(new SimpleStringProperty(newMunicipality));

        // ProductionGrants
        // TODO: this should be moved to SaveCandidate, and here it should be some validation
        try {
            int animalsPG = Integer.parseInt(animalsPGField.getText());
            candidate.setAnimalsPG(new SimpleIntegerProperty(animalsPG));
        } catch (NumberFormatException e) {
            candidate.setStatus("unfinished");
            System.out.println("Candidate don't have a animalsPG");
        }

        try {
            int hiredHelpPG = Integer.parseInt(hiredHelpPGField.getText());
            candidate.setHiredHelpPG(new SimpleIntegerProperty(hiredHelpPG));
        } catch (NumberFormatException e) {
            candidate.setStatus("unfinished");
            System.out.println("Candidate don't have a hiredHelpPG");
        }

        try {
            int farmingPG = Integer.parseInt(farmingPGField.getText());
            candidate.setFarmingPG(new SimpleIntegerProperty(farmingPG));
        } catch (NumberFormatException e) {
            candidate.setStatus("unfinished");
            System.out.println("Candidate don't have a farmingPG");
        }

        // Field handling only needed with persons
        if (candidate.getIsPerson()) {
            System.out.println("isPerson");
            //Checks if network table is empty, if so give a warning
            if(networkTable.getItems().size() < 1){
                candidate.setFieldStatus(12, 1);
                candidate.setStatus("unfinished");
            }
            if(newMunicipality == null || newMunicipality.equals("")){
                candidate.setFieldStatus(3, 1);
                candidate.setStatus("unfinished");
            }
            if(twitterField.getText().equals("")){
                candidate.setFieldStatus(7, 1);
                candidate.setStatus("unfinished");
            }
            if(professionField.getText().equals("")){
                candidate.setFieldStatus(6, 1);
                candidate.setStatus("unfinished");
            }
            if(yearOfBirthField.getText().equals("")){
                candidate.setFieldStatus(5,1);
                candidate.setStatus("unfinished");
            }
            if(titleField.getText().equals("")){
                candidate.setFieldStatus(13,1);
                candidate.setStatus("unfinished");
            }

        }
        ScoringListController.getOrCreateInstance().refreshTable();
        // Network
        // TODO: save the temporarily network connection list

        handleErrorMessage(errorMessage);
        // TODO: not upload if not approved
        uploadToBucket();
    }

    private void setColorOnField(boolean addStyle, int i, String color){
        if(addStyle){
            if(i == 0){ nameField.getStyleClass().add(color);}
            else if(i == 1){ previousYearRankField.getStyleClass().add(color); }
            else if(i == 2){ rankField.getStyleClass().add(color);}
            else if(i == 3){ municipalityField.getStyleClass().add(color);}
            else if(i == 4){ genderChoiceBox.getStyleClass().add(color);}
            else if(i == 5){ yearOfBirthField.getStyleClass().add(color);}
            else if(i == 6){ professionField.getStyleClass().add(color);}
            else if(i == 7){ twitterField.getStyleClass().add(color);}
            else if(i == 8){ animalsPGField.getStyleClass().add(color);}
            else if(i == 9){ hiredHelpPGField.getStyleClass().add(color);}
            else if(i == 10){ farmingPGField.getStyleClass().add(color);}
            else if(i == 11){descriptionField.getStyleClass().add(color);}
            else if(i == 12){titleField.getStyleClass().add(color);}
            else { networkTable.getStyleClass().add(color);}
        } else {
            if(i == 0){ nameField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 1){ previousYearRankField.getStyleClass().removeAll("errorField", "emptyField"); }
            else if(i == 2){ rankField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 3){ municipalityField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 4){ genderChoiceBox.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 5){ yearOfBirthField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 6){ professionField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 7){ twitterField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 8){ animalsPGField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 9){ hiredHelpPGField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 10){ farmingPGField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 11){descriptionField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 12){titleField.getStyleClass().removeAll("errorField", "emptyField");}
            else { networkTable.getStyleClass().removeAll("errorField", "emptyField");}
        }

    }

    private void setColorsOnFields(){
        int[] candidateFields = candidate.getFieldStatus();
        for(int i = 0; i < candidateFields.length; i++){
            if(candidateFields[i] == 1 && candidate.getIsPerson()){
                System.out.println("emptyField");
                setColorOnField(true, i, "emptyField");
            } else if (candidateFields[i] == 2){
                System.out.println("errorField");
                setColorOnField(true, i, "errorField");
            } else {
                System.out.println("default field");
                setColorOnField(false, i, "");
            }
        }
    }

    private void disableFieldsIfNotPerson(boolean notPerson) {
        if (notPerson) {
            twitterField.setDisable(true);
            networkTable.setDisable(true);
            animalsPGField.setDisable(true);
            hiredHelpPGField.setDisable(true);
            farmingPGField.setDisable(true);
            yearOfBirthField.setDisable(true);
            municipalityField.setDisable(true);
            professionField.setDisable(true);
            titleField.setDisable(true);
        } else {
            twitterField.setDisable(false);
            networkTable.setDisable(false);
            animalsPGField.setDisable(false);
            hiredHelpPGField.setDisable(false);
            farmingPGField.setDisable(false);
            yearOfBirthField.setDisable(false);
            municipalityField.setDisable(false);
            professionField.setDisable(false);
            titleField.setDisable(false);
        }
    }

    @FXML
    private void handleChangeImage() {
        File file = mainApp.chooseAndGetFile();
        setImageField(file);
    }

    @FXML
    public void handleNewCandidate() {
        cleanFields();
        createAndAddEmptyCandidate();
    }

    @FXML
    public void handleDelete() {
        ScoringList.getOrCreateInstance().deleteCandidate(candidate);
        cleanFields();
    }

    @FXML
    public void markAsDone(){
        if(markAsDoneButton.getText().equals("Marker komplett")){
            if(!nameField.getText().isEmpty()) {
                markAsDoneButton.setText("Marker ukomplett");
                candidate.setStatus("finished");
            }
        }else{
            if(!nameField.getText().isEmpty()){
                markAsDoneButton.setText("Marker komplett");
                candidate.setStatus("");
            }
        }
        ScoringListController.getOrCreateInstance().refreshTable();
    }

    @FXML
    public void handleAddConnection() {
        connectionDialog(null, true);
    }

    private String validateCandidate() {
        String errorMessage = "";

        String name = nameField.getText();
        String rank = rankField.getText();
        String previousYearRank = previousYearRankField.getText();
        String gender = getSelectedGender();
        String description = descriptionField.getText();
        errorMessage += candidate.validate(name, rank, previousYearRank, gender, description);

        /*
        if (EditListController.getOrCreateInstance().nameExistInList(name)) {
            errorMessage += "\n Det eksisterer allerede noen med det for- og etternavnet";
        }
        */

        return errorMessage;
    }

    private void saveCandidate() {
        String name = nameField.getText();
        candidate.setName(name);

        String gender = getSelectedGender();
        candidate.setGender(gender);

        String description = descriptionField.getText();
        candidate.setDescription(new SimpleStringProperty(description));

        String municipality = municipalityField.getText();
        candidate.setMunicipality(new SimpleStringProperty(municipality));

        int rank = Integer.parseInt(rankField.getText());
        candidate.setRank(new SimpleIntegerProperty(rank));

        int previousYearRank = Integer.parseInt(previousYearRankField.getText());
        candidate.setPreviousYearRank(new SimpleIntegerProperty(previousYearRank));
        
        String newYearOfBirth = yearOfBirthField.getText();
        candidate.setYearOfBirth(newYearOfBirth);
        
        String newProfession = professionField.getText();
        candidate.setProfession(newProfession);

        String twitter = twitterField.getText();
        candidate.setTwitter(new SimpleStringProperty(twitter));

        String title = titleField.getText();
        candidate.setTitle(title);

        // TODO: Save all the fields related to the different sources
    }

    private void handleErrorMessage(String errorMessage) {
        if (errorMessage.length() != 0) {
            candidate.setStatus("invalidFields");
            setColorsOnFields();
            String headerText = "Felter til kandidaten er ikke korrekt utfylt.";
            Utility.newAlertError(headerText, errorMessage);

        } else {
            saveCandidate();
            setColorsOnFields();
            ScoringListController.getOrCreateInstance().refreshTable();
        }
    }

    private void setFields() {
        File file = new File(candidate.getImageName());
        System.out.println("File when setFields in candidateC: " + file);
        setImageField(file);

        nameField.setText(candidate.getName());
        municipalityField.setText(candidate.getMunicipality());
        rankField.setText(Integer.toString(candidate.getRank()));
        previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
        genderChoiceBox.getSelectionModel().select(setGenderChoice(candidate));
        descriptionField.setText(candidate.getDescription());
        animalsPGField.setText(Integer.toString(candidate.getAnimalsPG()));
        hiredHelpPGField.setText(Integer.toString(candidate.getHiredHelpPG()));
        farmingPGField.setText(Integer.toString(candidate.getFarmingPG()));
        yearOfBirthField.setText(candidate.getYearOfBirth());
        twitterField.setText(candidate.getTwitter());
        professionField.setText(candidate.getProfession());
        titleField.setText(candidate.getTitle());

        setCompleteButton();
    }

    private void setCompleteButton() {
        if (candidate.getStatus().equals("finished")){
            markAsDoneButton.setText("Marker ukomplett");
        } else{
            markAsDoneButton.setText("Marker komplett");
        }
    }

    private void cleanFields() {
        String standardImagePath = "resources/standard.png";
        File file = new File(standardImagePath);
        setImageField(file);

        nameField.setText("");
        municipalityField.setText("");
        rankField.setText("");
        previousYearRankField.setText("");
        descriptionField.setText("");
        animalsPGField.setText("");
        hiredHelpPGField.setText("");
        farmingPGField.setText("");
        yearOfBirthField.setText("");
        twitterField.setText("");
        professionField.setText("");
    }

    private void setImageField(File file) {
        System.out.println("File when setIamgeFIeld in canddiateC: " + file);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            newImage = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(newImage);
        } catch (IOException ex) {
            System.out.println("Error when loading image: " + ex);
        }
    }

    private void createAndAddEmptyCandidate() {
        int nextCandidateRank = ScoringList.getOrCreateInstance().getCandidates().size() + 1;

        rankField.setText(Integer.toString(nextCandidateRank));
        candidate = new Candidate("", nextCandidateRank, 0);
        ScoringList.getOrCreateInstance().addCandidate(candidate);
    }

    private void uploadToBucket() {
        String imagePath = candidate.getImageName();
        File image = new File(imagePath);
        String fileName = image.getName();
        bucketUploader.uploadFile(image, fileName);
    }

    // Connection dialog
    private void connectionDialog(Connection connection, boolean open) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(MainApp.getInstance().getStage());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("../view/ConnectionView.fxml"));

        GridPane connectionView = null;
        try {
            connectionView = (GridPane) loader.load();
        } catch (IOException e) {
            System.out.println("Error when loading connectionVIew");
            e.printStackTrace();
        }

        ConnectionController connectionController = loader.getController();
        connectionController.setCandidate(candidate);
        connectionController.setConnection(connection);

        Scene dialogScene = new Scene(connectionView);
        dialog.setScene(dialogScene);
        connectionDialog = dialog;
        if (open || connection != null) {
            dialog.show();
        }
    }

    @FXML
    public void openConnection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Connection connection = networkTable.getSelectionModel().getSelectedItem();
            if (connection != null) {

                connectionDialog(connection, true);
            }
        }
    }

    public void closeDialog() {
        System.out.println("Calling close dialog");
        connectionDialog.close();
        updateNetworkList();
    }

    private void updateNetworkList() {
        networkTable.setItems(candidate.getConnections());
        networkTable.refresh();
        networkTable.getSelectionModel().clearSelection();
    }

    public void chooseConnection(Connection selectedConnection) {
         // Move them to the top
        reorderConnectionList(selectedConnection);

        markSelectedConnections();

        closeDialog();
    }

    private void reorderConnectionList(Connection selectedConnection) {
        candidate.getConnections().remove(selectedConnection);
        candidate.getConnections().add(0, selectedConnection);
        updateNetworkList();
    }

    private void markSelectedConnections() {
        networkTable.setRowFactory(new Callback<TableView<Connection>, TableRow<Connection>>() {
            @Override
            public TableRow<Connection> call(TableView<Connection> tableView) {
                return new TableRow<Connection>() {
                    @Override
                    protected void updateItem(Connection connection, boolean empty){
                        super.updateItem(connection, empty);
                        int maxConnections = Settings.getOrCreateInstance().getNumConnections();
                        int actualConnections = candidate.getConnections().size();
                        int numConnToColor = Math.min(maxConnections, actualConnections);
                         if (getIndex() <  numConnToColor) {
                            getStyleClass().add("finished");
                        } else {
                            getStyleClass().removeAll("finished");
                        }
                    }

                };
            }
        });
    }

}
