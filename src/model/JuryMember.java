package model;

public class JuryMember extends Person {
    private String description;

    // TODO: we might not need this one? Just use a normal person?
    public JuryMember(String name, String description, String imageURL) {
        super(name, imageURL);
        if (super.validateName(name) == "") {
            this.description = description;
        } else {
            // TODO: should abort the new JuryMember
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
