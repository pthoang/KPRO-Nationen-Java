package interfaces;

import model.Candidate;
import model.DataSourceFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface DataSourceInterface {
    //the return type here needs to change to something. todo
    public void getData(List<Candidate> candidates);

    public void setFilePath(File path);

    public String getNameOfRegister();

    public ArrayList<DataSourceFile> getRequiredFiles();

}
