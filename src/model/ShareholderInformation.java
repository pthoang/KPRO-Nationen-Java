package model;

import java.math.BigInteger;

/**
 * Created by vicen on 08-Nov-17.
 */
public class ShareholderInformation {

    private int id;
    private String company;
    private BigInteger numStocksCompany;
    private int numStocks;
    private String candidateYearOfBirth;


    public ShareholderInformation(int id, String company, BigInteger numStocksCompany, int numStocks,
                                  String yearOfBirth) {

        this.id = id;
        this.company = company;
        this.numStocksCompany = numStocksCompany;
        this.numStocks = numStocks;
        this.candidateYearOfBirth = yearOfBirth;

    }

    public int getId() {
        return this.id;
    }

    public String getCompany() {
        return this.company;
    }

    public BigInteger getNumStocksCompany() {
        return this.numStocksCompany;
    }

    public int getNumStocks() {
        return this.numStocks;
    }



}
