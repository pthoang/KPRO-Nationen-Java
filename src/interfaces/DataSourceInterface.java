package interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Candidate;
import model.DataSourceFile;

import java.io.File;
import java.util.ArrayList;

public interface DataSourceInterface {
    //the return type here needs to change to something. todo
    public JsonObject getData(Candidate candidate);

    public String getNameOfRegister();

    public ArrayList<DataSourceFile> getRequiredFiles();

}
