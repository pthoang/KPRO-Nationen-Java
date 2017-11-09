package model;

import java.util.ArrayList;
import java.util.List;

public class Jury {
    private ArrayList<JuryMember> juryMembers;
    private String description;

    public static Jury instance = null;

    public static Jury getOrCreateInstance() {
        if (instance == null) {
            instance = new Jury();
        }
        return instance;
    }

    private Jury() {
        juryMembers = new ArrayList<JuryMember>();

    }

    public void addMemberToJury(JuryMember member){
        juryMembers.add(member);
    }


}
