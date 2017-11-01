package controllers.datasources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import interfaces.DataSourceInterface;
import model.Candidate;
import model.DataSourceFile;

import java.io.*;
import java.util.ArrayList;

public class SupportDb implements DataSourceInterface {
    private String name ="supportregister";
    private ArrayList<DataSourceFile> requiredFiles;
    private DataSourceFile part1;

    public SupportDb() {
        System.out.println("SupportDb initialized");

        requiredFiles = new ArrayList<DataSourceFile>();

        part1 = new DataSourceFile("support register part 1");

        requiredFiles.add(part1);
    }

    @Override
    public JsonObject getData(Candidate candidate) {
        JsonObject returnData = new JsonObject();

        //todo. this is just to add test data before stock register is implemented.
        candidate.testOrg();

        JsonObject rawData = candidate.getRawData();

        JsonArray dataToAnalyze = (JsonArray) rawData.get("stocks");

        //gets all organization id numbers
        ArrayList<String> org = new ArrayList<String>();
        for (JsonElement stock : dataToAnalyze) {
            org.add(stock.getAsJsonObject().get("org").getAsString());
        }


        BufferedReader bufferedReader = null;
        try (FileReader reader = new FileReader(part1.getFilepath())) {
            bufferedReader = new BufferedReader(reader);

            String line = "";
            String csvSplitBy = ";";

            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(csvSplitBy);
                //System.out.println(fields[0]);

                if (org.contains(fields[0])) {
                    System.out.println("WE WON!!!!");
                }
            }

            System.out.println("done");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnData;
    }

    @Override
    public String getNameOfRegister() {
        return name;
    }

    @Override
    public ArrayList<DataSourceFile> getRequiredFiles() {
        return requiredFiles;
    }
}
