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

            JsonArray edges = new JsonArray();

            //list of nodes and edges
            JsonObject elements =  new JsonObject();
            elements.add("nodes",nodes);
            elements.add("edges",edges);


            JsonObject rawData = candidate.getRawData();

            JsonArray candidatesStocks = (JsonArray) rawData.get("stocks");


            //
            int i = 2;
            for (Candidate candidate2 : candidates) {
                if (candidate.getName()!=candidate2.getName()){
                    JsonObject rawData2 = candidate2.getRawData();
                    JsonArray otherCandidateStocks= (JsonArray) rawData.get("stocks");

                    //I dont know what raw_data looks like, but this should be easy to rewrite

                    //Adding Connection if candidates own same stock
                    for(JsonElement stock1 : candidatesStocks){
                        for(JsonElement stock2 : otherCandidateStocks){
                            if(stock1.getAsJsonObject().get("orgNo")==stock2.getAsJsonObject().get("orgNo")){
                                String newId = Integer.toString(i) + "s";
                                JsonObject dataNode = new JsonObject();
                                dataNode.addProperty("id", newId);
                                dataNode.addProperty("name", candidate2.getName());
                                dataNode.addProperty("img", candidate2.getImageURL());
                                dataNode.addProperty("size", Integer.toString(30));
                                dataNode.addProperty("description",
                                        candidate2.getName()+" eier aksjer i samme selskap som "+candidate.getName());
                                JsonObject dataNodeObject = new JsonObject();
                                dataNodeObject.add("data", dataNodeObject);
                                nodes.add(dataNodeObject);

                                JsonObject dataEdge = new JsonObject();
                                dataEdge.addProperty("source", "1"); //Source
                                dataEdge.addProperty("target", newId); //Target
                                JsonObject dataEdgeObject = new JsonObject();
                                dataEdgeObject.add("data", dataEdgeObject);
                                edges.add(dataEdgeObject);
                            }
                        }
                    }

                    //Adding connection if candidates get same kind of subsidies

                    //dyrehold
                    if(candidate.getAnimalsPG()>0 && candidate2.getAnimalsPG()>0){
                        String newId = Integer.toString(i) + "apg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til dyrehold.");
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNodeObject);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                                dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target", newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdgeObject);
                        edges.add(dataEdgeObject);

                    }
                    //avløs
                    if(candidate.getHiredHelpPG()>0 && candidate2.getHiredHelpPG()>0){
                        String newId = Integer.toString(i) + "hhpg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til avløs.");
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNodeObject);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                        dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target",newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdgeObject);
                        edges.add(dataEdgeObject);
                    }
                    //Jordbruk
                    if(candidate.getFarmingPG()>0 && candidate2.getFarmingPG()>0){
                        String newId = Integer.toString(i) + "fpg";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til jordbruk.");
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNodeObject);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                        dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target",newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdgeObject);
                        edges.add(dataEdgeObject);
                    }

                    if(rawData.get("politic") != null && rawData2.get("politic") != null) {
                        String newId = Integer.toString(i) + "p";
                        JsonObject dataNode = new JsonObject();
                        dataNode.addProperty("id", newId);
                        dataNode.addProperty("name", candidate2.getName());
                        dataNode.addProperty("img", candidate2.getImageURL());
                        dataNode.addProperty("size", Integer.toString(30));
                        dataNode.addProperty("description",
                                candidate2.getName()+" sitter i Stortinget eller i regjering sammen med " +
                                        candidate.getName());
                        JsonObject dataNodeObject = new JsonObject();
                        dataNodeObject.add("data", dataNodeObject);
                        nodes.add(dataNodeObject);

                        JsonObject dataEdge = new JsonObject();
                        dataEdge.addProperty("source", "1"); //Source
                        dataEdge.addProperty("target", newId); //Target
                        JsonObject dataEdgeObject = new JsonObject();
                        dataEdgeObject.add("data", dataEdgeObject);
                        edges.add(dataEdgeObject);
                    }

                    i++;


                    }
                }




            candidate.setElements(elements);

            }




        }


}
