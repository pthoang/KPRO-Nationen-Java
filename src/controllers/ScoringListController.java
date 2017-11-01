package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Candidate;
import model.ScoringList;

import java.util.HashMap;

public class ScoringListController {

    @FXML
    private TableView<Candidate> candidateTable;
    @FXML
    private TableColumn<Candidate, Integer> rankColumn;
    @FXML
    private TableColumn<Candidate, String> nameColumn;

    private EditListController parentController;

    private ScoringList scoringList;
    private static ObservableList<Candidate> candidates;
    private HashMap<String, Integer> candidateColor = new HashMap<>();


    public void setParentController(EditListController editListController) {
        this.parentController = editListController;
        System.out.println("Sat parent controler to scoring list: " + parentController);
    }
    public void setScoringList(ScoringList scoringList) {
        this.scoringList = scoringList;

        candidates = scoringList.getCandidates();
    }

    public void fillTable() {

        System.out.println("Candidates: " + candidates);
        System.out.println("Candidatetable: " + candidateTable);

        candidateTable.setItems(candidates);

        Candidate firstCandidate = candidates.get(0);
        System.out.println("Parent controller. " + parentController);
        System.out.println("Candidate: " + firstCandidate);
        parentController.setCandidate(firstCandidate);

        for(Candidate candidate : candidates){
            if(!candidateColor.containsKey(candidate.getName())){
                candidateColor.put(candidate.getName(), 0);
            }
        }
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

                        String status = candidates.get(this.getIndex()).getStatus();

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
}
