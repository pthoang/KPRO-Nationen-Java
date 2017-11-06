package controllers;

import Main.MainApp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.AmazonBucketUploader;
import model.Candidate;
import model.Connection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.input.MouseEvent;

import java.beans.EventHandler;

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
    private TextField animalsPGField = new TextField();
    @FXML
    private TextField hiredHelpPGField = new TextField();
    @FXML
    private TextField farmingPGField = new TextField();
    @FXML
    private TextField yearOfBirthField = new TextField();

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

    private final String IMAGE_PATH = "images/";
    private final String STANDARD_IMAGE_PATH = "images/standard.png";
    private AmazonBucketUploader bucketUploader;
    private Image newImage;
    private Candidate candidate;
    private MainApp mainApp;
    private Stage connectionDialog;
    private EditListController parent;

    // TODO: do with all colors used
    private final String GREEN = "-fx-background-color: rgb(53,109,48);";


    public void setBucketUploader(AmazonBucketUploader bucketUploader) {
        this.bucketUploader = bucketUploader;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setParentController(EditListController editListController) {
        this.parent = editListController;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
        setFields();
    }

    @FXML
    private void initialize() {
        networkNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        networkDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());

        //networkTable.getSelectionModel().selectedItemProperty().addListener(
        //        (observable, oldValue, newValue) -> connectionDialog(newValue, false));

        /*
        networkTable.setRowFactory(connection -> {
            TableRow<Connection> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        Connection connection = row.getItem();
                        connectionDialog(connection, true);
                    }
                }
            });
            return row;
        });
        */
        /**
         * Adding listeners to the textfields for feedback handling
         */
        // TODO: can't this be done in a loop?
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

        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });
        
        yearOfBirthField.textProperty().addListener((observable, oldValue, newValue) -> {
        	markAsDoneButton.setDisable(false);
            saveCandidateButton.setDisable(false);
        });

        genderChoiceBox.getItems().addAll(GENDER_CHOICES);
        genderChoiceBox.setValue("");
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
        municipalityField.setStyle("");
        networkTable.setStyle("");

        candidate.setStatus("");
        int fieldsMissing = 0;
        saveCandidateButton.setDisable(true);

        if (candidate == null) {
            createAndAddEmptyCandidate();
        }

        String errorMessage = "";

        errorMessage += validateCandidate();
        
//        //Year of Birth
//        String newYearOfBirth = yearOfBirthField.getText();
//        candidate.setYearOfBirth(newYearOfBirth);

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

        // Field handling only needed with persons
        if (candidate.getIsPerson()) {
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
            // TODO: should use enum instead of strings to tell the status
            if(fieldsMissing > 0){
                candidate.setStatus("unfinished");
            } else {
                candidate.setStatus("allFields");
            }
        }

        parent.refreshTable();
        // Network
        // TODO: save the temporarily network connection list

        handleErrorMessage(errorMessage);
        // TODO: not upload if not approved
        uploadToBucket();
    }

    private void disableFieldsIfNotPerson(boolean notPerson) {
        if (notPerson) {
            municipalityField.setDisable(true);
            // TODO: disable fields from other sources
        } else {
            municipalityField.setDisable(false);
            // TODO: not disable fields from other sources
        }
    }

    @FXML
    private void handleChangeImage() {
        File file = mainApp.chooseAndGetFile();
        setImageField(file);
    }

    @FXML
    public void handleNewCandidate() {
        //candidate = null;
        cleanFields();
        createAndAddEmptyCandidate();
    }

    @FXML
    public void handleDelete() {
        parent.deleteCandidate(candidate);
        cleanFields();
    }

    @FXML
    public void markAsDone(){
        if(markAsDoneButton.getText().equals("Marker komplett")){
            if(!nameField.getText().isEmpty()) {
                markAsDoneButton.setText("Marker ukomplett");
                //getCandidateByName(nameField.getText()).setStatus("finished");
                candidate.setStatus("finished");
            }
        }else{
            if(!nameField.getText().isEmpty()){
                markAsDoneButton.setText("Marker komplett");
                //getCandidateByName(nameField.getText()).setStatus("");
                candidate.setStatus("");
            }
        }
        parent.refreshTable();
    }

    @FXML
    public void handleAddConnection() {
        connectionDialog(null, true);
    }
    /*
    private Candidate getCandidateByName(String name) {
        for (Candidate candidate: parent.getCandidates()) {
            if (candidate.getName().equals(name)) {
                return candidate;
            }
        }
        return null;
    }
    */

    private String validateCandidate() {
        String errorMessage = "";

        String name = nameField.getText();
        String rank = rankField.getText();
        String previousYearRank = previousYearRankField.getText();
        String gender = getSelectedGender();
        String description = descriptionField.getText();
        errorMessage += candidate.validate(name, rank, previousYearRank, gender, description);

        if (parent.nameExistInList(name)) {
            errorMessage += "\n Det eksisterer allerede noen med det for- og etternavnet";
        }

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

        // TODO: Save all the fields related to the different sources
    }

    private void handleErrorMessage(String errorMessage) {
        if (errorMessage.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Feilmeldinger");
            alert.setHeaderText("Felter til kandidaten er ikke korrekt utfylt.");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            candidate.setStatus("invalidFields");
        } else {
            saveCandidate();
            parent.refreshTable();
        }
    }

    private void setFields() {
        // TODO: move to its own function? What do they do?
        networkTable.setStyle("");
        municipalityField.setStyle("");

        File file = new File(candidate.getImageURL());
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

        setCompleteButton();
        handleIfPersonOrNot();
    }

    private void setCompleteButton() {
        if (candidate.getStatus().equals("finished")){
            markAsDoneButton.setText("Marker ukomplett");
        } else{
            markAsDoneButton.setText("Marker komplett");
        }
    }

    // TODO: can be expanded to exclude/deactivate fields
    private void handleIfPersonOrNot() {
        if (candidate.getIsPerson()) {
            if (municipalityField.getText() == null) {
                municipalityField.setStyle("-fx-border-color: #ffff65");
            }

            networkTable.setItems(candidate.getConnections());
            if (networkTable.getItems().size() < 1) {
                networkTable.setStyle("-fx-border-color: #ffff65");
            }
        }
    }

    private void cleanFields() {
        File file = new File(STANDARD_IMAGE_PATH);
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

    private void createAndAddEmptyCandidate() {
        int nextCandidateRank = parent.getCandidates().size() + 1;

        rankField.setText(Integer.toString(nextCandidateRank));
        candidate = new Candidate("", nextCandidateRank, 0);

        parent.addCandidate(candidate);
    }

    private void uploadToBucket() {
        String imagePath = candidate.getImageURL();
        File image = new File(imagePath);
        String fileName = image.getName();
        bucketUploader.uploadFile(image, fileName);
    }

    // Connection dialog
    private void connectionDialog(Connection connection, boolean open) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(parent.getMainApp().getStage());
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
        connectionController.setMainApp(parent.getMainApp());
        connectionController.setParent(this);
        connectionController.setCandidate(candidate);
        connectionController.setConnection(connection);
        connectionController.setImageField();

        Scene dialogScene = new Scene(connectionView);
        dialog.setScene(dialogScene);
        connectionDialog = dialog;
        if (open || connection != null) {
            dialog.show();
        } else {
            System.out.println("Don't open dialog");
        }
        System.out.println("Selected: " + networkTable.getSelectionModel().getSelectedItem());
        System.out.println("Connection: " + connection);
        System.out.println("OPending dialog");
    }

    @FXML
    public void openConnection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Connection connection = networkTable.getSelectionModel().getSelectedItem();
            System.out.println("Conneciton: " + connection);
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
        System.out.println("Update networkList: set Items");
        networkTable.refresh();
        System.out.println("Refresh networktable");
        // TODO: open the dialog again
        networkTable.getSelectionModel().clearSelection();
        System.out.println("Clear selection");
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
                        int maxConnections = mainApp.getSettings().getNumConnections();
                        int actualConnections = candidate.getConnections().size();
                        int numConnToColor = Math.min(maxConnections, actualConnections);
                         if (getIndex() <  numConnToColor) {
                            setStyle(GREEN);
                        } else {
                            setStyle("");
                        }
                    }

                };
            }
        });
    }

}
