package StringMatcher;

public class MatchResult {
	int numAdditions;
	int numDeletions;
	int numChanges;
	
	public MatchResult (int _numAdditions, int _numDeletions, int _numChanges) {
		numAdditions = _numAdditions;
		numDeletions = _numDeletions;
		numChanges = _numChanges;
	}
	
	public int getNumAddtions () {
		return numAdditions;
	}
	
	public int getNumDeletions () {
		return numDeletions;
	}
	
	public int getNumChanges () {
		return numChanges;
	}
	
	public String toString () {
		String out = "Additions: " + numAdditions + " Deletions: " + 
			numDeletions + " Changes: " + numChanges;
		return out;
	}
}
