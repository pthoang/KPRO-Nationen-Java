package model;

import javafx.beans.property.SimpleStringProperty;

public class Organization {

    private String name;
    private String org_number;
    public Organization(String name, String number) {
        this.name = name;
        this.org_number = number;
    }

    public String getName() {
        return this.name;
    }
    public String getOrg_number(){
        return this.org_number;
    }
}
