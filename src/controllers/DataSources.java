package controllers;

import controllers.datasources.MockRegister;
import controllers.datasources.SupportDb;
import interfaces.DataSourceInterface;
import model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class DataSources {

    //a list containing all the data sources that is available
    private List<DataSourceInterface> dsList = new ArrayList<>();

    public DataSources() {
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
