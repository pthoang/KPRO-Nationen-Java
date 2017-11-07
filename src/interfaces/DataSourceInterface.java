package interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Candidate;
import model.DataSourceFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface DataSourceInterface {
    public void getData(List<Candidate> candidates);

    public String getNameOfRegister();

    public ArrayList<DataSourceFile> getRequiredFiles();

}
