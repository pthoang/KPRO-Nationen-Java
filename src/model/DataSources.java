package model;

import controllers.datasources.SupportDb;
import interfaces.DataSourceInterface;
import model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class DataSources {

    // A list containing all the data sources that is available
    private List<DataSourceInterface> dsList;

    public DataSources() {
        dsList = new ArrayList<DataSourceInterface>();

        //initializes the supportDb
        SupportDb supportDb = new SupportDb();
        dsList.add(supportDb);




    }

    public List<DataSourceInterface> getDsList() {
        return dsList;
    }

    void getDataFromAll(Candidate candidate) {
        throw new UnsupportedOperationException("not implemented");
    }

    void getDataFromSource(DataSourceInterface source, Candidate candidate) {
        throw new UnsupportedOperationException("not implemented");
    }

}
