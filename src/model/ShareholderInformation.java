package model;


import java.math.BigInteger;

public class ShareholderInformation {

    private String orgNo;
    private String orgName;
    private BigInteger totalStocks;
    private int candidateStocks;


    public ShareholderInformation(String orgNo, String orgName, BigInteger totalStocks, int candidateStocks) {

        this.orgNo = orgNo;
        this.orgName = orgName;
        this.totalStocks = totalStocks;
        this.candidateStocks = candidateStocks;

    }

    public String getOrgNo() {
        return this.orgNo;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public BigInteger getTotalStocks() {
        return this.totalStocks;
    }

    public int getCandidateStocks() {
        return this.candidateStocks;
    }


}
