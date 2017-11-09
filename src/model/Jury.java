package model;

import java.util.List;

public class Jury {
    private List<JuryMember> juryMembers;
    private String description;

    public Jury() {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addMemberToJury(JuryMember member){
        juryMembers.add(member);
    }
}
