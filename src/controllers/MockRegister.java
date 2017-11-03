package controllers;


import interfaces.DataSourceInterface;
import model.Candidate;
import model.DataSourceFile;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;

/*
 * This class is just used to test the input of files for the datasources. It can also be considered an example file.
 */
public class MockRegister implements DataSourceInterface {
    private String name = "mockregister";

    //the objects put in here are the filepaths that will be asked about in settingscontroller.
    private ArrayList<DataSourceFile> requiredFiles;

    //example usage of a module that requires two files
    private DataSourceFile file1;
    private DataSourceFile file2;


    public MockRegister() {

        requiredFiles = new ArrayList<DataSourceFile>();

        //creating the objects. the name is the label that will be displayed next to the input field
        file1 = new DataSourceFile("Support register part 1");
        file2 = new DataSourceFile("Support register part 2");

        //now that we have created the files we require, we need to append them to the requiredFiles array.
        //this way the settings controller will ask for the files and set the path in the
        // DataSourceFile objects, which we can retrive later.
        requiredFiles.add(file1);
        requiredFiles.add(file2);
    }

    //returns the data that we want
    @Override
    public void getData(Candidate candidate) {
        // TODO
        System.out.println("Getting data about " + candidate);

    }

    public ArrayList<DataSourceFile> getRequiredFiles(){
        return requiredFiles;
    }

    //used to store the file path to the file that we are going to extract data from
    @Override
    public void setFilePath(File path) {
        System.out.println("Set file path" + path);

    }

    @Override
    public String getNameOfRegister() {
        return name;
    }
}
