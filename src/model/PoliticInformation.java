package model;

import java.util.List;

public class PoliticInformation {

    private String title;
    private String politicalParty;
    private List<String> committees;
    private String ministry;

    public PoliticInformation(String title, String politicalParty, List<String> committees, String ministry) {
        this.title = title;
        this.politicalParty = politicalParty;
        this.committees = committees;
        this.ministry = ministry;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

}
