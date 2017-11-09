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
            JsonObject returnData = new JsonObject();

            ArrayList<ArrayList> nodes = new ArrayList<ArrayList>();

            ArrayList<ArrayList> edges = new ArrayList<ArrayList>();

            //list of nodes and edges
            ArrayList<ArrayList> elements =  new ArrayList<ArrayList>();
            elements.add(nodes);
            elements.add(edges);


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
                            if(stock1==stock2){
                                ArrayList<String> data = new ArrayList<String>();
                                data.add(Integer.toString(i)); //id
                                data.add(candidate2.getName()); //name
                                data.add(Integer.toString(30)); //size
                                data.add(candidate2.getName()+" eier aksjer i samme selskap som "+candidate.getName()); //description
                                data.add(candidate2.getImageURL()); //img
                                nodes.add(data);
                                data.clear();
                                data.add("1"); //Source
                                data.add(Integer.toString(i)); //Target
                                edges.add(data);
                                data.clear();
                            }
                        }
                    }

                    //Adding connection if candidates get same kind of subsidies

                    //dyrehold
                    if(candidate.getAnimalsPG()>0 && candidate2.getAnimalsPG()>0){
                        ArrayList<String> data = new ArrayList<String>();
                        data.add(Integer.toString(i)); //id
                        data.add(candidate2.getName()); //name
                        data.add(Integer.toString(30)); //size
                        data.add(candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til dyrehold."); //description
                        data.add(candidate2.getImageURL()); //img
                        nodes.add(data);
                        data.clear();
                        data.add("1"); //Source
                        data.add(Integer.toString(i)); //Target
                        edges.add(data);
                        data.clear();
                    }
                    //avløs
                    if(candidate.getHiredHelpPG()>0 && candidate2.getHiredHelpPG()>0){
                        ArrayList<String> data = new ArrayList<String>();
                        data.add(Integer.toString(i)); //id
                        data.add(candidate2.getName()); //name
                        data.add(Integer.toString(30)); //size
                        data.add(candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til avløs."); //description
                        data.add(candidate2.getImageURL()); //img
                        nodes.add(data);
                        data.clear();
                        data.add("1"); //Source
                        data.add(Integer.toString(i)); //Target
                        edges.add(data);
                        data.clear();
                    }
                    //Jordbruk
                    if(candidate.getFarmingPG()>0 && candidate2.getFarmingPG()>0){
                        ArrayList<String> data = new ArrayList<String>();
                        data.add(Integer.toString(i)); //id
                        data.add(candidate2.getName()); //name
                        data.add(Integer.toString(30)); //size
                        data.add(candidate2.getName()+" eier også andeler i selskaper som mottar subsidier til jordbruk."); //description
                        data.add(candidate2.getImageURL()); //img
                        nodes.add(data);
                        data.clear();
                        data.add("1"); //Source
                        data.add(Integer.toString(i)); //Target
                        edges.add(data);
                        data.clear();
                    }


                    i++;


                    }
                }






            }




        }


}
