package controllers;

import com.google.gson.Gson;
import interfaces.DataSourceInterface;
import model.Candidate;
import model.DataSourceFile;
import model.ShareholderInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vicen on 01-Nov-17.
 */
public class ShareholderRegister implements DataSourceInterface {
    private File path;


    public void getData(List<Candidate> candidates) {
        HashMap<String, ArrayList<String>> candidateShareholderInformation = new HashMap<>();

        for (Candidate candidate :
                candidates) {
            String[] candidateNameSplit = candidate.getName().split(" ");
            String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1];
            candidateShareholderInformation.put(hashKey.toLowerCase(), new ArrayList<>());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(this.path))) {

            String csvSplit = ";";
            String[] fields = br.readLine().replaceAll("[^a-zA-Z0-9;_]+", "").split(csvSplit);
            List<String> fieldsList = Arrays.asList(fields);

            int orgNoIndex = fieldsList.indexOf("selskap_orgnr");
            int orgNameIndex = fieldsList.indexOf("selskap_navn");
            int totalStocksIndex = fieldsList.indexOf("aksjer_totalt_selskapet");
            int stocksCandidateIndex = fieldsList.indexOf("aksjer_antall");
            int shareholderNameIndex = fieldsList.indexOf("aksjonr_navn");

            Gson gson = new Gson();

            String line;
            while((line = br.readLine()) != null) {
                String[] information = line.split(csvSplit);
                String[] shareholderNameSplit = information[shareholderNameIndex].split(" ");
                String shareholderName = (shareholderNameSplit[0] + " " +
                        shareholderNameSplit[shareholderNameSplit.length-1]).toLowerCase();

                if(candidateShareholderInformation.containsKey(shareholderName)) {
                    ShareholderInformation shareholderInformation =
                            new ShareholderInformation(information[orgNoIndex], information[orgNameIndex],
                                    new BigInteger(information[totalStocksIndex]),
                                    Integer.parseInt(information[stocksCandidateIndex]));
                    String json = gson.toJson(shareholderInformation);
                    candidateShareholderInformation.get(shareholderName).add(json);
                }
            }

            for (Candidate candidate :
                    candidates) {
                String[] candidateNameSplit = candidate.getName().split(" ");
                String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1];
                String data = gson.toJson(candidateShareholderInformation.get(hashKey));
                candidate.addRawData("stocks", data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setFilePath(File path) {
        this.path = path;
    }

    public String getNameOfRegister() {
        return null;
    }

    public ArrayList<DataSourceFile> getRequiredFiles() {
        return null;
    }



}
