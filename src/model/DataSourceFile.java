package model;

public class DataSourceFile {

    private String name;
    private String filepath;

    public DataSourceFile(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
