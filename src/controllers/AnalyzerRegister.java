package controllers;

import interfaces.AnalyzeDataInterface;
import interfaces.DataSourceInterface;
import model.Candidate;

import java.util.List;

public class AnalyzerRegister {
    private List<AnalyzeDataInterface> adList;

    public AnalyzerRegister() {
        //adds the analyzer to the list over analyzers. this way we can use it later
        adList.add(new AnalyzeEconomicConnections());

        throw new UnsupportedOperationException("not implemented");
    }

    void getDataFromAll(Candidate candidate) {
        //here the idea is that we loop over everything in adList and runs the analyzer

        throw new UnsupportedOperationException("not implemented");
    }

    void getDataFromSource(DataSourceInterface source, Candidate candidate) {
        throw new UnsupportedOperationException("not implemented");
    }
}
