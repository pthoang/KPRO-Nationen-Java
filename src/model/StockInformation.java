package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StockInformation {

    private SimpleStringProperty orgNo;
    private SimpleStringProperty orgName;
    private SimpleIntegerProperty totalStocks;
    private SimpleIntegerProperty candidateStocks;


    public StockInformation(String orgNo, String orgName, int totalStocks, int candidateStocks) {

        this.orgNo = new SimpleStringProperty(orgNo);
        this.orgName = new SimpleStringProperty(orgName);
        this.totalStocks = new SimpleIntegerProperty(totalStocks);
        this.candidateStocks = new SimpleIntegerProperty(candidateStocks);

    }
}
