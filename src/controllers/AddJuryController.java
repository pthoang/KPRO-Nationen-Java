package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;

import Main.MainApp;
import model.JuryMember;
import model.Jury;
import model.AmazonBucketUploader;
import model.Utility;

public class AddJuryController {

    @FXML
    private TableView<JuryMember> juryMembersTable = new TableView<>();
    @FXML
    private TableColumn<JuryMember, String> nameColumn = new TableColumn<>();

    @FXML
    private TextField nameField = new TextField();
    @FXML
    private TextField titleField = new TextField();
    @FXML
    private ImageView imageView = new ImageView();
    @FXML
    private TextArea descriptionField = new TextArea();

    @FXML
    private Button addJuryMemberButton;
    @FXML
    private Button saveDescriptionButton;
    @FXML
    private Button deleteJuryMemberButton;

    private Jury jury;
    private JuryMember member;
    private MainApp mainApp;
    private static AddJuryController instance = null;
    private BufferedImage bfImage = null;

    public static AddJuryController getOrCreateInstance() {
        if (instance == null) {
            instance = new AddJuryController();
        }
        return instance;
    }

    public AddJuryController(){
        mainApp = MainApp.getInstance();
        bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
        setImageField(bfImage);
        jury = Jury.getOrCreateInstance();
    }

    @FXML
    public void initialize() {
        setImageField(Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH));
        String description = jury.getDescription();
        if (description != null) {
            descriptionField.setText(description);
        }

        juryMembersTable.setItems(jury.getJuryMembers());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        juryMembersTable.setPlaceholder(new Label("Ingen juryer er registrert"));

        juryMembersTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setFields(newValue)
        );

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> saveDescriptionButton.setDisable(false));

        disableButtons(true);
        saveDescriptionButton.setDisable(true);

        descriptionField.setWrapText(true);
    }

    private void disableButtons(boolean disable) {
        addJuryMemberButton.setDisable(disable);
        deleteJuryMemberButton.setDisable(disable);
    }

    @FXML
    private void fileChooser() {
        File imageFile = mainApp.chooseAndGetFile();
        if (imageFile == null) {
            return;
        }
        bfImage = Utility.getBufferedImageFromFile(imageFile);
        setImageField(bfImage);
    }

    @FXML
    public void handleBack() {
            mainApp.showEditListView();
    }

    @FXML
    public void handleSaveJury() {
        mainApp.showEditListView();
        addJuryMemberButton.setDisable(true);
    }

    @FXML
    public void handleAddJuryMember() {

        String name = nameField.getText();

        String title = titleField.getText();

        if (member == null) {
            member = new JuryMember(name, title);
            jury.addJuryMember(member);
        } else {
            member.setName(name);
            member.setTitle(title);
        }

        uploadToBucket();

        ObservableList<JuryMember> juryMembers = jury.getJuryMembers();
        juryMembersTable.setItems(juryMembers);
        juryMembersTable.refresh();
        cleanFields();
    }

    private void uploadToBucket() {
        String imageName = member.getImageName();
        File file = Utility.convertBufferedImageToFile(bfImage);
        AmazonBucketUploader.getOrCreateInstance().uploadFile(file, imageName);
        member.setImageIsInBucket(true);
    }

    @FXML
    public void handleDeleteJuryMember() {
        JuryMember juryMember = juryMembersTable.getSelectionModel().getSelectedItem();
        jury.deleteJuryMember(juryMember);
        cleanFields();
    }

    private void cleanFields() {
        member = null;
        bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
        setImageField(bfImage);
        nameField.setText("");
        titleField.setText("");
        addJuryMemberButton.setDisable(true);
        deleteJuryMemberButton.setDisable(true);
        juryMembersTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleAddDescription() {
        String description = descriptionField.getText();
        jury.setDescription(description);
        saveDescriptionButton.setDisable(true);
    }

    private void setImageField(BufferedImage image) {
        Image newImage = SwingFXUtils.toFXImage(image, null);
        imageView.setImage(newImage);
    }

    private void setFields(JuryMember juryMember) {
        if (juryMember == null) {
            cleanFields();
            return;
        }
        member = juryMember;
        nameField.setText(juryMember.getName());
        titleField.setText(juryMember.getTitle());

        getAndSetCorrectImage();

        addJuryMemberButton.setDisable(true);
        deleteJuryMemberButton.setDisable(false);
    }

    private void getAndSetCorrectImage() {
        if (member.getImageIsInBucket()) {
            bfImage = AmazonBucketUploader.getOrCreateInstance().getImageFromBucket(member.getImageName());
        } else {
            bfImage = Utility.getResourceAsImage(Utility.STANDARD_IMAGE_PATH);
        }

        setImageField(bfImage);
    }

}