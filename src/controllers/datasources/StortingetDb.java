package controllers.datasources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controllers.AddSourcesController;
import interfaces.DataSourceInterface;
import model.Candidate;
import model.DataSourceFile;
import model.PoliticInformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vicen on 08-Nov-17.
 */
public class StortingetDb implements DataSourceInterface{

    private String name="stortingetdb";
    private ArrayList<DataSourceFile> requiredFiles;
    private String representativeUrl = "https://data.stortinget.no/eksport/dagensrepresentanter?format=json";
    private String governmentUrl = "https://data.stortinget.no/eksport/regjering?format=json";

    public StortingetDb() {
        System.out.println("initi");
        requiredFiles = new ArrayList<>();
    }

    public void getData(List<Candidate> candidates) {
        HashMap<String, PoliticInformation> candidatePoliticInformation = new HashMap<>();

        for (Candidate candidate	:
                candidates) {
            String[] candidateNameSplit = candidate.getName().split(" ");
            String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1];
            candidatePoliticInformation.put(hashKey.toLowerCase(), null);
        }

        Gson gson = new Gson();
        try {
            String representativeJson = readUrl(representativeUrl);
            RepResponse repResponse = gson.fromJson(representativeJson, RepResponse.class);

            for (Representative rep :
                    repResponse.dagensrepresentanter_liste) {
                String[] repFullNameSplit = (rep.fornavn + " " + rep.etternavn).toLowerCase()
                        .split(" ");
                String repName = repFullNameSplit[0] + " " + repFullNameSplit[repFullNameSplit.length-1];
                if(candidatePoliticInformation.containsKey(repName)) {
                    List<String> committees = rep.komiteer_liste.stream()
                            .map(committee -> committee.navn).collect(Collectors.toList());
                    PoliticInformation politicInformation =
                            new PoliticInformation(null, rep.parti.navn, committees, null);
                    candidatePoliticInformation.put(repName, politicInformation);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            String governmentJson = readUrl(governmentUrl);
            GovResponse govResponse = gson.fromJson(governmentJson, GovResponse.class);

            for (GovMember govMember :
                    govResponse.regjeringsmedlemmer_liste) {
                String[] govMemberFullNameSplit = (govMember.fornavn + " " + govMember.etternavn).toLowerCase()
                        .split(" ");
                String govMemberName = govMemberFullNameSplit[0] + " " +
                        govMemberFullNameSplit[govMemberFullNameSplit.length-1];
                if(candidatePoliticInformation.containsKey(govMemberName)) {
                    PoliticInformation politicInformation = candidatePoliticInformation.get(govMemberName);
                    if(politicInformation != null) {
                        politicInformation.setTitle(govMember.tittel);
                        politicInformation.setMinistry(govMember.departement);
                    } else {
                        politicInformation = new PoliticInformation(govMember.tittel, govMember.parti.navn,
                                null, govMember.departement);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();

        for (Candidate candidate :
                candidates) {
            String[] candidateNameSplit = candidate.getName().split(" ");
            String hashKey = candidateNameSplit[0] + " " + candidateNameSplit[candidateNameSplit.length-1];
            PoliticInformation politicInformation = candidatePoliticInformation.get(hashKey.toLowerCase());
            if( politicInformation != null) {
                JsonObject jsonObject = (JsonObject) jsonParser
                        .parse(gson.toJson(politicInformation));
                candidate.addRawData("politic", jsonObject);
            }

        }


    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);

            }
            return buffer.toString();
        } finally {
            if(reader != null) {
                reader.close();
            }
        }

    }

    static class RepResponse {
        List<Representative> dagensrepresentanter_liste;
    }

    static class GovResponse {
        List<GovMember> regjeringsmedlemmer_liste;
    }

    static class Representative {
        String etternavn;
        String fornavn;
        String id;
        int kjoenn;
        Municipality fylke;
        PoliticalParty parti;
        List<Committee> komiteer_liste;
    }

    static class GovMember {
        String etternavn;
        String fornavn;
        String id;
        int kjoenn;
        String departement;
        PoliticalParty parti;
        String tittel;
    }

    static class PoliticalParty {
        String id;
        String navn;
    }

    static class Municipality {
        String id;
        String navn;
    }

    static class Committee {
        String id;
        String navn;
    }

    public String getNameOfRegister() {
        return this.name;
    }

    public ArrayList<DataSourceFile> getRequiredFiles() {
        return this.requiredFiles;
    }
}
