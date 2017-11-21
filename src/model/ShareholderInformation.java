package model;

import java.math.BigInteger;

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

}
