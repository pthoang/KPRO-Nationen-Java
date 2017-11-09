package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import Main.MainApp;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import model.*;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;


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
    private MainApp mainApp;
    private File imageFile;
    private Image newImage;
    private static AddJuryController instance = null;


    public static AddJuryController getOrCreateInstance() {
        if (instance == null) {
            instance = new AddJuryController();
        }
        return instance;
    }

    public AddJuryController(){
        mainApp = MainApp.getInstance();
        setImageField(new File("resources/standard.png"));
        jury = Jury.getOrCreateInstance();
    }

    @FXML
    public void initialize() {
        setImageField(new File("resources/standard.png"));
        String description = jury.getDescription();
        if (description != null) {
            descriptionField.setText(description);
        }

        juryMembersTable.setItems(jury.getJuryMembers());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        juryMembersTable.setPlaceholder(new Label("Ingen juryer er registrert"));

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            disableButtons(false);
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveDescriptionButton.setDisable(false);
        });

        disableButtons(true);
        saveDescriptionButton.setDisable(true);
    }

    public void disableButtons(boolean disable) {
        addJuryMemberButton.setDisable(disable);
        deleteJuryMemberButton.setDisable(disable);
    }

    @FXML
    private void fileChooser() {
        imageFile = mainApp.chooseAndGetFile();
        setImageField(imageFile);
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
        String imageName = name.replace(" ", "");
        AmazonBucketUploader.getOrCreateInstance().uploadFile(imageFile, imageName);
        String title = titleField.getText();
        JuryMember member = new JuryMember(name, imageName, title);
        jury.addJuryMember(member);

        ObservableList<JuryMember> juryMembers = jury.getJuryMembers();
        juryMembersTable.setItems(juryMembers);
        juryMembersTable.refresh();
        cleanFields();
    }

    @FXML
    public void handleDeleteJuryMember() {
        JuryMember juryMember = juryMembersTable.getSelectionModel().getSelectedItem();
        jury.deleteJuryMember(juryMember);
        cleanFields();
    }

    private void cleanFields() {
        nameField.setText("");
        titleField.setText("");
        addJuryMemberButton.setDisable(true);
        deleteJuryMemberButton.setDisable(true);
    }

    @FXML
    public void handleAddDescription() {
        String description = descriptionField.getText();
        jury.setDescription(description);
        saveDescriptionButton.setDisable(true);
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

    private void setFields(JuryMember juryMember) {
        nameField.setText(juryMember.getName());
        titleField.setText(juryMember.getTitle());
        File image = new File(juryMember.getImageName());
        setImageField(image);
        addJuryMemberButton.setDisable(true);
        deleteJuryMemberButton.setDisable(false);
    }

    @FXML
    public void selectJuryMember(MouseEvent event) {
        if (event.getClickCount() == 2) {
            JuryMember juryMember = juryMembersTable.getSelectionModel().getSelectedItem();
            setFields(juryMember);
        }
    }

}