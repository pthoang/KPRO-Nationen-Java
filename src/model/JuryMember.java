package model;

public class JuryMember {
    private String name;
    private String description;
    private String imageUrl;

    public JuryMember(String name, String description, String imageUrl) {

        if (validateName(name)){
            //creates a new JuryMember with props
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        else {
            //should abort the new JuryMember
            throw new UnsupportedOperationException("not implemented");
        }
    }

    //returns true if name is valid.
    private Boolean validateName(String name){
        throw new UnsupportedOperationException("not implemented");
    }

}
