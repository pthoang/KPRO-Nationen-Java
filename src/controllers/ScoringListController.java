package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.*;
import Main.MainApp;

import java.io.File;
import javafx.stage.FileChooser;
import model.Candidate;
import model.ScoringList;

import java.io.FileWriter;

import java.io.IOException;
import java.util.HashMap;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

public class ScoringListController {

    @FXML
    private TableView<Candidate> candidateTable = new TableView<Candidate>();
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

	private static ScoringListController instance = null;

	public static ScoringListController getOrCreateInstance() {
		if (instance == null) {
			instance = new ScoringListController();
		}
		return instance;
	}

	public ScoringListController() {
		instance = this;
		mainApp = MainApp.getInstance();
		loadState();
		parentController = EditListController.getOrCreateInstance();
	}

	public void loadState() {
        scoringList = ScoringList.getOrCreateInstance();
        candidates = scoringList.getCandidates();
        candidateTable.setItems(candidates);
    }

    public void fillTable() {
        candidateTable.setItems(candidates);

        Candidate firstCandidate = candidates.get(0);
        CandidateController.getOrCreateInstance().setCandidate(firstCandidate);

        for(Candidate candidate : candidates){
            if(!candidateColor.containsKey(candidate.getName())){
                candidateColor.put(candidate.getName(), 0);
            }
        }
        updateCountLabel();
    }

    private Candidate getCandidateByName(String name){
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
                (observable, oldValue, newValue) -> CandidateController.getOrCreateInstance().setCandidate(newValue));
    }

    public void refreshTable() {
        candidates = ScoringList.getOrCreateInstance().getCandidates();
        candidateTable.refresh();
        updateCountLabel();
    }

    private void updateCountLabel() {
        int max = Settings.getOrCreateInstance().getNumCandidates();
        int actualLength = scoringList.getLength();
        countLabel.setText(actualLength + "/" + max);

        if (actualLength > max) {
            countLabel.getStyleClass().add("listToLong");
        } else {
            countLabel.getStyleClass().add("listToShort");
        }
    }

    public Candidate getNextCandidate() {
        int indexToCurrentCandidate = candidateTable.getSelectionModel().getSelectedIndex();
        int newIndex = indexToCurrentCandidate + 1;
        candidateTable.getSelectionModel().select(newIndex);
        return candidateTable.getSelectionModel().getSelectedItem();
    }

    public static class CellFactory implements Callback<TableColumn<Candidate, String>, TableCell<Candidate, String>> {
        @Override
        public TableCell<Candidate, String> call(TableColumn<Candidate, String> param) {
            return new TableCell<Candidate, String>() {

                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setText(item);
                    if(this.getIndex() > -1 && this.getIndex()<candidates.size()){
                        String status = candidates.get(this.getIndex()).getStatus();

                        if(status.equals("finished")){
                            getTableRow().getStyleClass().add("finished");
                        } else if (status.equals("unfinished")){
							getTableRow().getStyleClass().add("unfinished");
                        } else if (status.equals("invalidFields")){
							getTableRow().getStyleClass().add("invalidFields");
                        } else if (status.equals("allFields")) {
							getTableRow().getStyleClass().add("allFields");
                        } else {
                            getTableRow().getStyleClass().removeAll("finished", "unfinished", "invalidFields", "allFields");
                        }
                    }
                }
            };
        }
    }

    @FXML
    // TODO: not used
    public void handleAnalyzeAll() {
        mainApp.generateAll();
    }

    @FXML
    public void handleExportFile() {
        if (listIsTooLong()) {
            String headerText = "Listen er for lang.";
            String contentText = "Alle kandidatene vil dermed ikke vises på siden. " +
                    "Fjern noen kandidater eller endre antallet kandidater i 'Instillinger'";
            newAlertError(headerText, contentText);
            return;
        };

    	/**
		 * Gson creates unnecessary fields in the json because of the property "SimpleStringProperty".
		 * FxGson is a library which removes the unnecessary fields and generates the required JSON format.

		Gson fxGson = FxGson.create();
		String json = fxGson.toJson(scoringList); //Serialize an object to json string
		System.out.println(json);*/

        JsonObject json = new JsonObject();

        JsonArray people = new JsonArray();

        for (Candidate candidate : scoringList.getCandidates()) {
            JsonObject jsonCandidate = new JsonObject();
            jsonCandidate.addProperty("firstname", candidate.getName());
            jsonCandidate.addProperty("img", candidate.getBucketImageURL());
            jsonCandidate.addProperty("key", candidate.getRank());
            jsonCandidate.addProperty("lastYear", candidate.getPreviousYearRank());
            jsonCandidate.addProperty("gender", candidate.getGender());
            jsonCandidate.addProperty("profession", candidate.getProfession());
            jsonCandidate.addProperty("residence", candidate.getMunicipality());
            jsonCandidate.addProperty("twitterAcnt", candidate.getTwitter());
            jsonCandidate.addProperty("bio", candidate.getDescription());
            jsonCandidate.addProperty("subsidies", candidate.getRawData().get("subsidies").toString());
            jsonCandidate.addProperty("stocks", candidate.getRawData().get("stocks").toString());

            jsonCandidate.addProperty("elements", candidate.getElements().toString());

            people.add(jsonCandidate);
        }

        json.add("people", people);



		
		FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.JSON)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getStage());
        if(file != null){
            saveFile(json.toString(), file);
        }
//		
//        String errorMessage = validateList();
//        handleErrorMessage(errorMessage);
    }
    
    /**
	 * Save the content of gson object as string in a file.
	 */
	private void saveFile(String content, File file){

        try {
            FileWriter fileWriter = null;
            
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Exception when writing list to file:" + e);
        }
    }

    private boolean listIsTooLong() {
        return scoringList.getLength() > Settings.getOrCreateInstance().getNumCandidates();
    }

    private void newAlertError(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Feilmeldinger");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
