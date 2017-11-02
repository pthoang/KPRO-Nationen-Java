package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    private String name="shareholderregister";
    private ArrayList<DataSourceFile> requiredFiles;
    private DataSourceFile shareholderFile;

    public ShareholderRegister() {
        requiredFiles = new ArrayList<>();
        shareholderFile = new DataSourceFile("Shareholder register");
        requiredFiles.add(shareholderFile);
    }

    public void getData(List<Candidate> candidates) {
        HashMap<String, JsonArray> candidateShareholderInformation = new HashMap<>();

        for (Candidate candidate :
                candidates) {
            String[] candidateNameSplit = candidate.getName().split(" ");
            String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1];
            candidateShareholderInformation.put(hashKey.toLowerCase(), new JsonArray());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(this.shareholderFile.getFilepath()))) {

            String csvSplit = ";";
            String[] fields = br.readLine().replaceAll("[^a-zA-Z0-9;_]+", "").split(csvSplit);
            List<String> fieldsList = Arrays.asList(fields);

            int orgNoIndex = fieldsList.indexOf("selskap_orgnr");
            int orgNameIndex = fieldsList.indexOf("selskap_navn");
            int totalStocksIndex = fieldsList.indexOf("aksjer_totalt_selskapet");
            int stocksCandidateIndex = fieldsList.indexOf("aksjer_antall");
            int shareholderNameIndex = fieldsList.indexOf("aksjonr_navn");

            System.out.println(this.shareholderFile.getFilepath());
            Gson gson = new Gson();

            JsonParser jsonParser = new JsonParser();

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
                    JsonObject json = (JsonObject) jsonParser.parse(gson.toJson(shareholderInformation));
                    candidateShareholderInformation.get(shareholderName).add(json);
                }
            }

            for (Candidate candidate :
                    candidates) {
                String[] candidateNameSplit = candidate.getName().split(" ");
                String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1];
                JsonArray data = candidateShareholderInformation.get(hashKey.toLowerCase());
                System.out.println(data);
                System.out.println(candidateShareholderInformation);
                candidate.addRawData("stocks", data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getNameOfRegister() {
        return this.name;
    }

    public ArrayList<DataSourceFile> getRequiredFiles() {
        return this.requiredFiles;
    }



}
