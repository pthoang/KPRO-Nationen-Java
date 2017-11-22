package model;

import com.google.gson.JsonObject;

import java.util.List;

public class PoliticInformation {

    private String title;
    private String politicalParty;
    private List<String> committees;
    private String ministry;
    private Boolean government;
    private Boolean stortinget;
    private String municipality;

    public PoliticInformation(String title, String politicalParty, List<String> committees, String ministry, String municipality) {
        this.title = title;
        this.politicalParty = politicalParty;
        this.committees = committees;
        this.ministry = ministry;
        this.municipality = municipality;
        this.government = false;
        this.stortinget = false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public void setGovernment(Boolean government) {
        this.government = government;
    }

    public void setStortinget(Boolean stortinget) {
        this.stortinget = stortinget;
    }

}
