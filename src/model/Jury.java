package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Jury {
    private ObservableList<JuryMember> juryMembers;
    private String description;

    public static Jury instance = null;

    public static Jury getOrCreateInstance() {
        if (instance == null) {
            instance = new Jury();
        }
        return instance;
    }

    private Jury() {
        juryMembers = FXCollections.observableArrayList();
    }

    public void addJuryMember(JuryMember member){
        juryMembers.add(member);
    }

    public void deleteJuryMember(JuryMember juryMember) {
        juryMembers.remove(juryMember);
    }

    public ObservableList<JuryMember> getJuryMembers() {
        return juryMembers;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void empty() {
        description = "";
        juryMembers.removeAll();
    }

}
