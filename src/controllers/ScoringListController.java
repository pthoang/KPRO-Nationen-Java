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
import java.sql.Savepoint;
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
    private Label countLabel;

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

        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        nameColumn.setOnEditCommit(cell -> {
            int row = cell.getTablePosition().getRow();
            candidateTable.getItems().set(row, getCandidateByName(cell.getNewValue()));
        });

        candidateTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> CandidateController.getOrCreateInstance().setCandidate(newValue));

        updateCountLabel();
    }

    public void refreshTable() {
        ScoringList.getOrCreateInstance().updateRanks();
        candidates = ScoringList.getOrCreateInstance().getCandidates();
        candidateTable.setItems(candidates);
        candidateTable.refresh();
        updateCountLabel();
    }

    public void updateCountLabel() {
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
    public void handleAnalyzeAll() {
        mainApp.generateAll();
    }

    @FXML
    public void handleExportFile() {
    	/**
		 * Gson creates unnecessary fields in the json because of the property "SimpleStringProperty".
		 * FxGson is a library which removes the unnecessary fields and generates the required JSON format.

		Gson fxGson = FxGson.create();
		String json = fxGson.toJson(scoringList); //Serialize an object to json string
		System.out.println(json);*/

        JsonObject json = new JsonObject();

        JsonArray people = new JsonArray();

        JsonObject juryObject = new JsonObject();
        JsonArray juryMembers = new JsonArray();

        Jury jury = Jury.getOrCreateInstance();

        juryObject.addProperty("description", jury.getDescription());


        for (Candidate candidate : scoringList.getCandidates()) {
            JsonObject jsonCandidate = new JsonObject();
            jsonCandidate.addProperty("fullName", candidate.getName());
            jsonCandidate.addProperty("img", candidate.getBucketImageURL());
            jsonCandidate.addProperty("key", candidate.getRank());
            jsonCandidate.addProperty("lastYear", candidate.getPreviousYearRank());

            jsonCandidate.addProperty("gender",
                    candidate.getGender() != null ? candidate.getGender() : "M");
            jsonCandidate.addProperty("profession",
                    candidate.getProfession() != null ? candidate.getProfession() : "");
            jsonCandidate.addProperty("residence",
                    candidate.getMunicipality() != null ? candidate.getMunicipality() : "");
            jsonCandidate.addProperty("twitterAcnt",
                    candidate.getTwitter() != null ? candidate.getTwitter() : "");
            jsonCandidate.addProperty("bio",
                    candidate.getDescription() != null ? candidate.getDescription() : "");
            jsonCandidate.addProperty("title",
                    candidate.getTitle() != null ? candidate.getTitle() : "");
            jsonCandidate.addProperty("birthYear",
                    candidate.getYearOfBirth() != null ? candidate.getYearOfBirth() : "");
             jsonCandidate.add("subsidies",
                    candidate.getRawData().get("subsidies") != null ?
                            candidate.getRawData().get("subsidies") : new JsonArray());
            jsonCandidate.add("stocks",
                    candidate.getRawData().get("stocks") != null ?
                            candidate.getRawData().get("stocks"): new JsonArray());
            jsonCandidate.add("politic",
                    candidate.getRawData().get("politic") != null ?
                            candidate.getRawData().get("politic") : new JsonObject());
            jsonCandidate.add("elements",
                    candidate.getElements() != null ? candidate.getElements() : new JsonArray());
            
            people.add(jsonCandidate);
        }

        for (JuryMember juryMember :
                jury.getJuryMembers()) {
            JsonObject jsonJuryMember = new JsonObject();

            jsonJuryMember.addProperty("fullName", juryMember.getName());
            jsonJuryMember.addProperty("img", juryMember.getBucketImageURL());
            jsonJuryMember.addProperty("title", juryMember.getTitle());

            juryMembers.add(jsonJuryMember);

        }

        juryObject.add("juryMembers", juryMembers);

        json.add("jury", juryObject);


        json.add("people", people);

        json.addProperty("description", scoringList.getAboutTheScoring());





		FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.JSON)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File fileLocal = fileChooser.showSaveDialog(mainApp.getStage());
        if(fileLocal != null) {
            saveFile(json.toString(), fileLocal);
        }



        File file = new File("maktlista.json");
        saveFile(json.toString(), file);

        AmazonBucketUploader.getOrCreateInstance().uploadFile(file, "maktlista.json");
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

}
