package controllers;

import interfaces.DataSourceInterface;
import model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class DataSources {

    //a list containing all the data sources that is available
    private List<DataSourceInterface> dsList = new ArrayList<>();

    public DataSources() {
        MockRegister test = new MockRegister();
        dsList.add(test);

        MockRegister test2 = new MockRegister();
        dsList.add(test2);
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
