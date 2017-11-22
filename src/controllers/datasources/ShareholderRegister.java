package controllers.datasources;

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
 * Created by vicen on 08-Nov-17.
 */
public class ShareholderRegister implements DataSourceInterface {

    private String name="shareholderregister";
    private ArrayList<DataSourceFile> requiredFiles;
    private DataSourceFile shareholderFile;

    public ShareholderRegister() {
        requiredFiles = new ArrayList<>();
        shareholderFile = new DataSourceFile("Aksjonærregisteret: ");
        requiredFiles.add(shareholderFile);
    }

    public void getData(List<Candidate> candidates) {

        if (this.shareholderFile.getFilepath() == null) {
            return;
        }

        HashMap<String, JsonArray> candidateShareholderInformation = new HashMap<>();

        for (Candidate candidate :
                candidates) {
            String[] candidateNameSplit = candidate.getName().split(" ");
            String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1]
                    + candidate.getYearOfBirth();
            candidateShareholderInformation.put(hashKey.toLowerCase(), new JsonArray());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(this.shareholderFile.getFilepath()))) {

            String csvSplit = ";";
            String[] fields = br.readLine().replaceAll("[^a-zA-Z0-9;_]+", "").split(csvSplit);
            List<String> fieldsList = Arrays.asList(fields);

            int orgNoIndex = fieldsList.indexOf("Orgnr");
            int orgNameIndex = fieldsList.indexOf("Selskap");
            int totalStocksIndex = fieldsList.indexOf("Antallaksjerselskap");
            int stocksCandidateIndex = fieldsList.indexOf("Antallaksjer");
            int shareholderNameIndex = fieldsList.indexOf("Navnaksjonr");
            int yearOfBirthIndex = fieldsList.indexOf("Fdselsrorgnr");

            Gson gson = new Gson();

            JsonParser jsonParser = new JsonParser();

            String line;
            while((line = br.readLine()) != null) {
                String[] information = line.split(csvSplit);
                String[] shareholderNameSplit = information[shareholderNameIndex].split(" ");
                String shareholderKey = ((shareholderNameSplit.length > 1 ? shareholderNameSplit[1] + " ": "") +
                        shareholderNameSplit[0]
                        + information[yearOfBirthIndex]
                    ).toLowerCase();

                if(candidateShareholderInformation.containsKey(shareholderKey)) {
                    ShareholderInformation shareholderInformation =
                            new ShareholderInformation(Integer.parseInt(information[orgNoIndex]),
                                    information[orgNameIndex],
                                    new BigInteger(information[totalStocksIndex]),
                                    Integer.parseInt(information[stocksCandidateIndex]),
                                    information[yearOfBirthIndex]);
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(gson.toJson(shareholderInformation));
                    candidateShareholderInformation.get(shareholderKey).add(jsonObject);
                }
            }

            for (Candidate candidate :
                    candidates) {
                String[] candidateNameSplit = candidate.getName().split(" ");
                String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1]
                        + candidate.getYearOfBirth();
                JsonArray data = candidateShareholderInformation.get(hashKey.toLowerCase());
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
