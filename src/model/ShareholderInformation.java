package model;

import java.math.BigInteger;

public class ShareholderInformation {

    private String orgNo;
    private String orgName;
    private BigInteger totalStocks;
    private int candidateStocks;
    private String zipCode;
    private String city;


    public ShareholderInformation(String orgNo, String orgName, BigInteger totalStocks, int candidateStocks,
                                  String zipCode, String city) {

        this.orgNo = orgNo;
        this.orgName = orgName;
        this.totalStocks = totalStocks;
        this.candidateStocks = candidateStocks;
        this.zipCode = zipCode;
        this.city = city;

    }

}
