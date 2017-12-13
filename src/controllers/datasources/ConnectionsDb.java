package controllers.datasources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Candidate;
import model.Connection;
import model.Settings;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConnectionsDb {

    public void setConnections(List<Candidate> candidates) {

        for (Candidate candidate : candidates) {

            JsonArray nodes = new JsonArray();
            JsonArray edges = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("nodes", nodes);
            jsonObject.add("edges", edges);

            //list of nodes and edges
            JsonArray elements =  new JsonArray();

            elements.add(jsonObject);

            JsonObject rawData = candidate.getRawData();

            JsonArray candidatesStocks = (JsonArray) rawData.get("stocks");

            JsonObject candidateNode = new JsonObject();
            candidateNode.addProperty("id", Integer.toString(1));
            candidateNode.addProperty("name", candidate.getName());
            candidateNode.addProperty("img", candidate.getBucketImageURL());
            candidateNode.addProperty("size", "60");
            candidateNode.addProperty("description", "");
            JsonObject candidateNodeObject = new JsonObject();
            candidateNodeObject.add("data", candidateNode);
            nodes.add(candidateNodeObject);


            //
            int i = 2;
            for (Candidate candidate2 : candidates) {
                if (!candidate.getName().equals(candidate2.getName())){
                    JsonObject rawData2 = candidate2.getRawData();
                    JsonArray otherCandidateStocks= (JsonArray) rawData2.get("stocks");
                    if (otherCandidateStocks == null) {
                        System.out.println("OtherCandidatesStocks is null");
                        break;
                    }
                    //I dont know what raw_data looks like, but this should be easy to rewrite

                    //Adding Connection if candidates own same stock
                    for(JsonElement stock1 : candidatesStocks){
                        Boolean candidateAdded = false;
                        for(JsonElement stock2 : otherCandidateStocks){
                            if(stock1.getAsJsonObject().get("id").equals(stock2.getAsJsonObject().get("id"))){
                                String newId = Integer.toString(i) + "s";
                                JsonObject dataNode = new JsonObject();
                                dataNode.addProperty("id", newId);
                                dataNode.addProperty("name", candidate2.getName());
                                dataNode.addProperty("img", candidate2.getBucketImageURL());
                                dataNode.addProperty("size", Integer.toString(30));
                                String description = candidate2.getName()+" eier aksjer i samme selskap som "+candidate.getName();
                                dataNode.addProperty("description",
                                        description);
                                JsonObject dataNodeObject = new JsonObject();
                                dataNodeObject.add("data", dataNode);

                                Connection connection = new Connection(candidate2, description);
                                candidate.addConnection(connection);

                                nodes.add(dataNodeObject);

                            }
                            if(candidateAdded) {
                                break;
                            }
                        }
                        if(candidateAdded) {
                            break;
                        }
                    }


                    /*
                    //Adding connection if candidates get same kind of subsidies

                    //dyrehold
                    if(candidate.getAnimalSubsidies()>0 && candidate2.getAnimalSubsidies()>0){
                        String newId = Integer.toString(i) + "apg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        String description = candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til dyrehold.";
                        dataNode.addProperty("description",
                                description);

                        Connection connection = new Connection(candidate2, description);
                        candidate.addConnection(connection);

                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);


                    }
                    //avløs
                    if(candidate.getHiredHelpSubsidies()>0 && candidate2.getHiredHelpSubsidies()>0){
                        String newId = Integer.toString(i) + "hhpg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        String description = candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til avløs.";
                        dataNode.addProperty("description",
                                description);

                        Connection connection = new Connection(candidate2, description);
                        candidate.addConnection(connection);

                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                    }
                    //Jordbruk
                    if(candidate.getFarmingSubsidies()>0 && candidate2.getFarmingSubsidies()>0){
                        String newId = Integer.toString(i) + "fpg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        String description = candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til jordbruk.";

                        dataNode.addProperty("description",
                                description);

                        Connection connection = new Connection(candidate2, description);
                        candidate.addConnection(connection);

                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                    }
                    */

                    if(rawData.get("politic") != null && rawData2.get("politic") != null) {
                        String newId = Integer.toString(i) + "p";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        String description = candidate2.getName()+" og " +
                                candidate.getName() + " er begge politikere. Begge sitter i regjeringen " +
                                "eller på Stortinget.";

                        dataNode.addProperty("description",
                                description);

                        Connection connection = new Connection(candidate2, description);
                        candidate.addConnection(connection);

                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                    }
                    i++;
                    }


                }

            Settings settings = Settings.getOrCreateInstance();
           /* while ((nodes.size() + candidate.getConnections().size() > settings.getNumConnections() + 1) &&
                    nodes.size() > 1) {
                Random rand = new Random();

                int removeIndex = rand.nextInt(nodes.size()-1) + 1;
                nodes.remove(removeIndex);
            }*/

            int j = 2;
            for (Connection connection:
                    candidate.getConnections()) {
                System.out.println("Connection when writing JSON: " + connection);
                JsonObject dataNode = new JsonObject();
                dataNode.addProperty("id", Integer.toString(j));
                dataNode.addProperty("name", connection.getName());
                dataNode.addProperty("img", connection.getPerson().getBucketImageURL());
                dataNode.addProperty("size", Integer.toString(30));
                dataNode.addProperty("description", connection.getDescription());
                JsonObject dataNodeObject = new JsonObject();
                dataNodeObject.add("data", dataNode);
                nodes.add(dataNodeObject);

                j++;
            }

            /*
            for (JsonElement node :
                    nodes) {
                String id = node.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
                if(!id.equals("1")) {
                    JsonObject dataEdge = new JsonObject();
                    dataEdge.addProperty("source", "1"); //Source
                    dataEdge.addProperty("target", id); //Target
                    JsonObject dataEdgeObject = new JsonObject();
                    dataEdgeObject.add("data", dataEdge);
                    edges.add(dataEdgeObject);
                }

            }*/

            candidate.setElements(elements);
            }
        }


}
