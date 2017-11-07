package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.Candidate;
import model.ScoringList;
import Main.MainApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

public class ScoringListController {

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
        int max = mainApp.getSettings().getNumCandidates();
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
        if (scoringList.getLength() > mainApp.getSettings().getNumCandidates()) {
            return "Det er for mange kandidater i listen. Fjern kandidater eller endre antallet aksepterte i 'Instillinger'";
        }
        // TODO: also validate if some of the candidates has fields missing?
        return "";
    }
}
