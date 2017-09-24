package model;

import java.util.Map;


public class ScoringList {
	
	// TODO: should year be id? What if they make multiple list during a year
	private int year;
	private Map<Candidate, Integer> candidates;
	
	final private int MAX_LENGTH = 100;
	
	// TODO: add something about jury and other information
	
	public void addCandidate(Candidate candidate, int rank) {
		candidates.put(candidate, rank);
	}
	
	public int getRankToCandidate(Candidate candidate) {
		return candidates.get(candidate);
	}

	public Candidate getCandidateByRank(int rank) {
		// TODO
		return null;
	}
	
	public int getLength() {
		return candidates.size();
	}
	
	public boolean isFull() {
		return candidates.size() == MAX_LENGTH;
	}
}
