package model;

import controllers.datasources.ShareholderRegister;
import controllers.datasources.StortingetDb;
import controllers.datasources.SupportDb;
import interfaces.DataSourceInterface;

import java.util.ArrayList;
import java.util.List;

public class DataSources {

    // A list containing all the data sources that is available
    private List<DataSourceInterface> dsList;

    public DataSources() {
        dsList = new ArrayList<DataSourceInterface>();

        ShareholderRegister shareholderRegister = new ShareholderRegister();
        dsList.add(shareholderRegister);
        StortingetDb stortingetDb = new StortingetDb();
        dsList.add(stortingetDb);

        //initializes the supportDb
        SupportDb supportDb = new SupportDb();
        dsList.add(supportDb);

    }

    public List<DataSourceInterface> getDsList() {
        return dsList;
    }

}
