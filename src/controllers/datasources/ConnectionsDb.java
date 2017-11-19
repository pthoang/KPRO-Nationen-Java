package controllers.datasources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Candidate;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by martingundersen on 08/11/2017.
 */
public class ConnectionsDb {


    public void setConnections(List<Candidate> candidates) {

        for (Candidate candidate : candidates) {

            JsonArray nodes = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("nodes", nodes);
            JsonArray edges = new JsonArray();
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
                                dataNode.addProperty("description",
                                        candidate2.getName()+" eier aksjer i samme selskap som "+candidate.getName());
                                JsonObject dataNodeObject = new JsonObject();
                                dataNodeObject.add("data", dataNode);
                                nodes.add(dataNodeObject);

                                JsonObject dataEdge = new JsonObject();
                                dataEdge.addProperty("source", "1"); //Source
                                dataEdge.addProperty("target", newId); //Target
                                JsonObject dataEdgeObject = new JsonObject();
                                dataEdgeObject.add("data", dataEdge);
                                edges.add(dataEdgeObject);
                                candidateAdded = true;
                            }
                            if(candidateAdded) {
                                break;
                            }
                        }
                        if(candidateAdded) {
                            break;
                        }
                    }



                    //Adding connection if candidates get same kind of subsidies

                    //dyrehold
                    if(candidate.getAnimalSubsidies()>0 && candidate2.getAnimalSubsidies()>0){
                        String newId = Integer.toString(i) + "apg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til dyrehold.");
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                                dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target", newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdge);
                        edges.add(dataEdgeObject);

                    }
                    //avløs
                    if(candidate.getHiredHelpSubsidies()>0 && candidate2.getHiredHelpSubsidies()>0){
                        String newId = Integer.toString(i) + "hhpg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til avløs.");
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                        dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target",newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdge);
                        edges.add(dataEdgeObject);
                    }
                    //Jordbruk
                    if(candidate.getFarmingSubsidies()>0 && candidate2.getFarmingSubsidies()>0){
                        String newId = Integer.toString(i) + "fpg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til jordbruk.");
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                        dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target",newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdge);
                        edges.add(dataEdgeObject);
                    }

                    if(rawData.get("politic") != null && rawData2.get("politic") != null) {
                        String newId = Integer.toString(i) + "p";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getBucketImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" sitter i Stortinget eller i regjering sammen med " +
                                        candidate.getName());
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNode);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                        dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target", newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdge);
                        edges.add(dataEdgeObject);
                    }

                    i++;


                    }
                }


            candidate.setElements(elements);

            }




        }


}
