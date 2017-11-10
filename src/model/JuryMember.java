package model;

public class JuryMember extends Person {

    private String title;

    public JuryMember(String name, String imageName, String title) {
        super(name, imageName);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
