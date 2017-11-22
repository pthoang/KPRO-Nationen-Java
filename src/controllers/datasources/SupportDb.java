package controllers.datasources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import interfaces.DataSourceInterface;
import model.Candidate;
import model.DataSourceFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SupportDb implements DataSourceInterface {
    private String name ="supportregister";
    private ArrayList<DataSourceFile> requiredFiles;
    private DataSourceFile part1;

    public SupportDb() {

        requiredFiles = new ArrayList<DataSourceFile>();

        part1 = new DataSourceFile("Produksjon og avløser tilskudd: ");

        requiredFiles.add(part1);
    }

    @Override
    public void getData(List<Candidate> candidates) {

        if (this.part1.getFilepath() == null) {
            return;
        }

        for (Candidate candidate : candidates) {
            JsonObject returnData = new JsonObject();

//            //todo. this is just to add test data before stock register is implemented.
//            candidate.testOrg();

            JsonObject rawData = candidate.getRawData();

            JsonArray dataToAnalyze = (JsonArray) rawData.get("stocks");


            int animalSubsidies = 0;
            int hiredHelpSubsidies = 0;
            int farmingSubsidies = 0;

            //gets all organization id numbers
            ArrayList<String> org = new ArrayList<String>();
            for (JsonElement stock : dataToAnalyze) {
                org.add(stock.getAsJsonObject().get("id").getAsString());
            }


            BufferedReader bufferedReader = null;
            try (FileReader reader = new FileReader(part1.getFilepath())) {
                bufferedReader = new BufferedReader(reader);

                String line = "";
                String csvSplitBy = ";";

                while ((line = bufferedReader.readLine()) != null) {
                    String[] fields = line.split(csvSplitBy);

                    //finds any matching orgidnumbers that the candidate has on his profile
                    if (org.contains(fields[0])) {
                        //We found something, lets read the data


                        if (Integer.parseInt(fields[3])>0){
                            //Increase Animals
                            animalSubsidies += Integer.parseInt(fields[3]);
                        }
                        if (Integer.parseInt(fields[4])>0){
                            //Increase Animals
                            animalSubsidies += Integer.parseInt(fields[4]);
                        }
                        if (Integer.parseInt(fields[5])>0){
                            //Increase Animals
                            animalSubsidies += Integer.parseInt(fields[5]);
                        }
                        if (Integer.parseInt(fields[6])>0){
                            //Increase Animals
                            animalSubsidies += Integer.parseInt(fields[6]);
                        }
                        if (Integer.parseInt(fields[7])>0){
                            //Increase Farming
                            farmingSubsidies += Integer.parseInt((fields[7]));
                        }
                        if (Integer.parseInt(fields[8])>0){
                            //Increase Farming
                            farmingSubsidies += Integer.parseInt((fields[8]));
                        }
                        if (Integer.parseInt(fields[9])>0){
                            //Increase Animals
                            animalSubsidies += Integer.parseInt(fields[9]);
                        }
                        if (Integer.parseInt(fields[10])>0){
                            //Increase Animals
                            animalSubsidies += Integer.parseInt(fields[10]);
                        }
                        if (Integer.parseInt(fields[11])>0){
                            //Increase Hired help
                            hiredHelpSubsidies += Integer.parseInt(fields[11]);
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            JsonArray subsidiesArray = new JsonArray();

            JsonObject animal = new JsonObject();
            animal.addProperty("id", 1);
            animal.addProperty("name", "Dyrehold");
            animal.addProperty("value", animalSubsidies);

            JsonObject farming = new JsonObject();
            farming.addProperty("id", 2);
            farming.addProperty("name", "Areal");
            farming.addProperty("value", farmingSubsidies);

            JsonObject hiredhelp = new JsonObject();
            hiredhelp.addProperty("id", 3);
            hiredhelp.addProperty("name", "Avløser");
            hiredhelp.addProperty("value", hiredHelpSubsidies);

            subsidiesArray.add(animal);
            subsidiesArray.add(farming);
            subsidiesArray.add(hiredhelp);

            candidate.setAnimalSubsidies(animalSubsidies);
            candidate.setFarmingSubsidies(farmingSubsidies);
            candidate.setHiredHelpSubsidies(hiredHelpSubsidies);

            candidate.addRawData("subsidies", subsidiesArray);
        }

        ConnectionsDb connectionsDb = new ConnectionsDb();
        connectionsDb.setConnections(candidates);
        System.out.println(candidates);
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
