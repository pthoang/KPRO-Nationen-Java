package model;

public class JuryMember extends Person {

    private String title;

    public JuryMember(String name, String title) {
        super(name);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
