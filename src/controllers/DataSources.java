package controllers;

import interfaces.DataSourceInterface;
import model.Candidate;

import java.util.List;

public class DataSources {

    //a list containing all the data sources that is available
    List<DataSourceInterface> dsList;

    public DataSources() {
        throw new UnsupportedOperationException("not implemented");
    }

    void getDataFromAll(Candidate candidate) {
        throw new UnsupportedOperationException("not implemented");
    }

    void getDataFromSource(DataSourceInterface source, Candidate candidate) {
        throw new UnsupportedOperationException("not implemented");
    }

}
