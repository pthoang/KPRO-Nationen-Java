package model;

import java.util.List;

/**
 * Created by vicen on 22-Oct-17.
 */
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

    public void setPoliticalParty(String party) {
        this.politicalParty = party;
    }

    public void setCommmittees(List<String> committees) {
        this.committees = committees;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPoliticalParty() {
        return this.politicalParty;
    }

    public String getMinistry() {
        return this.ministry;
    }

    public int getCommitteesSize() {
        return this.committees.size();
    }

    public String getCommittee(int index) {
        return this.committees.get(index);
    }

}