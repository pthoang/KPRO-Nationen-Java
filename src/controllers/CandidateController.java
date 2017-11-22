package controllers;

import Main.MainApp;
import java.awt.image.BufferedImage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

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
    @FXML
    private Button deleteButton;

    private static CandidateController instance = null;
    private AmazonBucketUploader bucketUploader;
    private Candidate candidate;
    private Stage connectionDialog;
    private BufferedImage bfImage;

    private List<Object> inputFields = new ArrayList<>(Arrays.asList(nameField, previousYearRankField, rankField,
            municipalityField, genderChoiceBox, yearOfBirthField, professionField, twitterField, descriptionField, titleField));


    public CandidateController() {
        instance = this;
        bucketUploader = AmazonBucketUploader.getOrCreateInstance();
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

    public Candidate getCandidate() {
        return candidate;
    }

    @FXML
    private void initialize() {
        networkNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        networkDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());

        networkTable.setPlaceholder(new Label("Ikke noe nettverk å vise"));

        inputFields.add(networkTable);

        // Adding listeners to the textfields for feedback handling
        List<TextField> textfields = new ArrayList<>(Arrays.asList(nameField, previousYearRankField, rankField,
                municipalityField, yearOfBirthField, professionField, twitterField, titleField));

        for(TextField textField : textfields){
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                disableButtons(false);
                if(newValue == null || newValue.equals("")){
                    textField.getStyleClass().add("emptyField");
                } else {
                    textField.getStyleClass().remove("emptyField");
                }
            });
        }

        genderChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
            boolean isPerson = newValue.intValue() == 3;
            disableFieldsIfNotPerson(isPerson);

        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
            if(newValue == null || newValue.length() < 5){
                descriptionField.getStyleClass().add("emptyField");
            } else {
                descriptionField.getStyleClass().remove("emptyField");
            }
        });

        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        genderChoiceBox.getItems().addAll(GENDER_CHOICES);
        genderChoiceBox.setValue("");

        bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
        setImageField(bfImage);

        descriptionField.setWrapText(true);
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

    @FXML
    public void handleSaveChangesToCandidate() {
        saveCandidateButton.setDisable(true);

        candidate.setStatus("");
        if (candidate == null) {
            createAndAddEmptyCandidate();
        }

        String errorMessage = "";

        errorMessage += validateCandidate();

        String newYearOfBirth = yearOfBirthField.getText();
        candidate.setYearOfBirth(newYearOfBirth);

        String newMunicipality = municipalityField.getText();
        candidate.setMunicipality(new SimpleStringProperty(newMunicipality));

        ScoringListController.getOrCreateInstance().refreshTable();

        handleErrorMessage(errorMessage);
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
            else if(i == 8){ descriptionField.getStyleClass().add(color);}
            else if(i == 9){ titleField.getStyleClass().add(color);}
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
            else if(i == 8){ descriptionField.getStyleClass().removeAll("errorField", "emptyField");}
            else if(i == 9){ titleField.getStyleClass().removeAll("errorField", "emptyField");}
            else { networkTable.getStyleClass().removeAll("errorField", "emptyField");}
        }

    }

    private void setColorsOnFields(){
        int[] candidateFields = candidate.getFieldStatus();
        for(int i = 0; i < candidateFields.length; i++){
            if(candidateFields[i] == 1){
                setColorOnField(true, i, "emptyField");
            } else if (candidateFields[i] == 2){
                setColorOnField(true, i, "errorField");
            } else {
                setColorOnField(false, i, "");
            }
        }
    }

    private void disableFieldsIfNotPerson(boolean notPerson) {
        if (notPerson) {
            twitterField.setDisable(true);
            networkTable.setDisable(true);
            yearOfBirthField.setDisable(true);
            municipalityField.setDisable(true);
            professionField.setDisable(true);
            titleField.setDisable(true);
        } else {
            twitterField.setDisable(false);
            networkTable.setDisable(false);
            yearOfBirthField.setDisable(false);
            municipalityField.setDisable(false);
            professionField.setDisable(false);
            titleField.setDisable(false);
        }
    }

    @FXML
    private void handleChangeImage() {
        File file = MainApp.getInstance().chooseAndGetFile();
        if (file == null) {
            return;
        }
        bfImage = Utility.convertFileToImage(file);
        setImageField(bfImage);
    }

    @FXML
    public void handleNewCandidate() {
        bfImage = null;
        cleanFields();
        createAndAddEmptyCandidate();
        ScoringListController.getOrCreateInstance().refreshTable();
    }

    @FXML
    public void handleDelete() {
        ScoringList.getOrCreateInstance().deleteCandidate(candidate);
        ScoringList.getOrCreateInstance().updateRanksWhenDeletedCandidate();
        Candidate nextCandidate = ScoringListController.getOrCreateInstance().getNextCandidate();

        setCandidate(nextCandidate);

        ScoringListController.getOrCreateInstance().refreshTable();
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
        saveCandidateButton.setDisable(false);
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

        return errorMessage;
    }

    private void saveCandidate() {
        candidate.setStatus("allFields");
        String name = nameField.getText();
        candidate.setName(name);

        String gender = getSelectedGender();
        candidate.setGender(gender);

        String description = descriptionField.getText();
        candidate.setDescription(new SimpleStringProperty(description));

        String municipality = municipalityField.getText();
        candidate.setMunicipality(new SimpleStringProperty(municipality));

        int rank = Integer.parseInt(rankField.getText());
        ScoringList.getOrCreateInstance().updateRankWhenChangedRank(candidate, candidate.getRank(), rank);
        //candidate.setRank(new SimpleIntegerProperty(rank));

        try {
            int previousYearRank = Integer.parseInt(previousYearRankField.getText());
            candidate.setPreviousYearRank(new SimpleIntegerProperty(previousYearRank));
        } catch (Exception e) {
            System.out.println("PreviousYearRankField is not filled out: " + e);
        }

        String newYearOfBirth = yearOfBirthField.getText();
        candidate.setYearOfBirth(newYearOfBirth);
        
        String newProfession = professionField.getText();
        candidate.setProfession(newProfession);

        String twitter = twitterField.getText();
        candidate.setTwitter(new SimpleStringProperty(twitter));

        String title = titleField.getText();
        candidate.setTitle(title);

        if (gender.equals("M") || gender.equals("F")) {
            // If fields are missing the candidate's fields get a status.
            // This makes sure the field gets red or yellow
            if(networkTable.getItems().size() < 1){
                candidate.setFieldStatus(10, 1);
                candidate.setStatus("unfinished");
            } else {
                candidate.setFieldStatus(10, 0);
            }
            if(municipality == null || municipality.equals("")){
                candidate.setFieldStatus(3, 1);
                candidate.setStatus("unfinished");
            } else {
                candidate.setFieldStatus(3, 0);
            }
            if(twitter == null || twitter.equals("")){
                candidate.setFieldStatus(7, 1);
                candidate.setStatus("unfinished");
            } else {
                candidate.setFieldStatus(7, 0);
            }
            if(newProfession == null || newProfession.equals("")){
                candidate.setFieldStatus(6, 1);
                candidate.setStatus("unfinished");
            } else {
                candidate.setFieldStatus(6, 0);
            }
            if(newYearOfBirth == null || newYearOfBirth.equals("")){
                candidate.setFieldStatus(5,1);
                candidate.setStatus("unfinished");
            } else {
                candidate.setFieldStatus(5, 0);
            }
            if(title == null || title.equals("")){
                candidate.setFieldStatus(9,1);
                candidate.setStatus("unfinished");
            } else {
                candidate.setFieldStatus(9, 0);
            }
        }
    }

    private void handleErrorMessage(String errorMessage) {
        if (errorMessage.length() != 0) {
            candidate.setStatus("invalidFields");
            String headerText = "Felter til kandidaten er ikke korrekt utfylt.";
            Utility.newAlertError(headerText, errorMessage);
            setColorsOnFields();
        } else {
            saveCandidate();
            setColorsOnFields();
            ScoringListController.getOrCreateInstance().refreshTable();
        }
    }

    private void setFields() {
        getAndSetCorrectImage();

        nameField.setText(candidate.getName());
        municipalityField.setText(candidate.getMunicipality());
        rankField.setText(Integer.toString(candidate.getRank()));
        if (candidate.hasPreviousYearRank()) {
            previousYearRankField.setText(Integer.toString(candidate.getPreviousYearRank()));
        } else {
            previousYearRankField.setText("");
        }
        genderChoiceBox.getSelectionModel().select(setGenderChoice(candidate));
        descriptionField.setText(candidate.getDescription());
        yearOfBirthField.setText(candidate.getYearOfBirth());
        twitterField.setText(candidate.getTwitter());
        professionField.setText(candidate.getProfession());
        titleField.setText(candidate.getTitle());
        setColorsOnFields();
        setCompleteButton();
    }

    private void getAndSetCorrectImage() {
        if (candidate.getImageIsInBucket()) {
            bfImage = AmazonBucketUploader.getOrCreateInstance().getImageFromBucket(candidate.getImageName());
        } else {
            bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
        }
        setImageField(bfImage);
    }

    private void setCompleteButton() {
        if (candidate.getStatus().equals("finished")){
            markAsDoneButton.setText("Marker ukomplett");
        } else{
            markAsDoneButton.setText("Marker komplett");
        }
    }

    public void cleanFields() {
        bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
        setImageField(bfImage);

        nameField.setText("");
        municipalityField.setText("");
        rankField.setText("");
        previousYearRankField.setText("");
        descriptionField.setText("");
        yearOfBirthField.setText("");
        twitterField.setText("");
        professionField.setText("");
    }

    private void setImageField(BufferedImage bfImage) {
        WritableImage image = Utility.convertBufferedImageToWritable(bfImage);
        imageView.setImage(image);
    }

    private void createAndAddEmptyCandidate() {
        int nextCandidateRank = ScoringList.getOrCreateInstance().getCandidates().size() + 1;

        rankField.setText(Integer.toString(nextCandidateRank));
        candidate = new Candidate("", nextCandidateRank);
        ScoringList.getOrCreateInstance().addCandidate(candidate);
    }

    private void uploadToBucket() {
        String imageName = candidate.getImageName();
        File file = Utility.convertBufferedImageToFile(bfImage);
        bucketUploader.uploadFile(file, imageName);
        candidate.setImageIsInBucket(true);
    }

    // Connection dialog
    private void connectionDialog(Connection connection, boolean open) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(MainApp.getInstance().getStage());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/ConnectionView.fxml"));

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
        dialogScene.getStylesheets().add(this.getClass().getResource("/resources/style/style.css").toExternalForm());
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

    public void updateNumConnections() {
        markSelectedConnections();
    }

}
