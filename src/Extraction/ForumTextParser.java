package Extraction;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.StringTokenizer;

import StringMatcher.MatchResult;
import StringMatcher.StringMatcher2;

public class ForumTextParser {
	String post;
	ArrayList<String> animeTitles = null;
	String[] words;
	BitSet tuple;
	int bestDeviation;
	int bestTitleIndex;
	int wordIndex;
	int titleIndex;
	int possIndex;
	int matchCount;
	
	public ForumTextParser (ArrayList<String> _animeTitles) {
		animeTitles = _animeTitles;
	}
	
	public BitSet getAnimeFromPost (String _post) {
		post = _post;
		tuple = new BitSet(animeTitles.size());
		StringTokenizer st = new StringTokenizer(post);
		words = new String[st.countTokens()];
		wordIndex = 0;
		while (st.hasMoreTokens())
			words[wordIndex++] = st.nextToken();
		return findMatch();
	}
	
	public BitSet findMatch () {
		matchCount = 0;
		for (wordIndex = 0; wordIndex < words.length; wordIndex++) {
			bestDeviation = Integer.MAX_VALUE;
			bestTitleIndex = -1;
			for (titleIndex = 0; titleIndex < animeTitles.size(); titleIndex++) {
				getTitleMatch();
			}
			if (bestTitleIndex != -1) {
				matchCount++;
				System.out.print(animeTitles.get(bestTitleIndex) + ", ");
				tuple.set(bestTitleIndex);
			}
		}
		if (matchCount >= 2) {
			System.out.println("");
			return tuple;
		}
		return null;
	}
	
	public void getTitleMatch() {
		String title = animeTitles.get(titleIndex);
		if (title.indexOf(":") != -1)  //Remove annoying :
			title = title.substring(0, title.indexOf(":"));
		String[] titleWords = title.split(" ");
		
		String poss = "";
		for (possIndex = 0; possIndex < titleWords.length; possIndex++)
			if (wordIndex+possIndex < words.length)
				poss += words[wordIndex+possIndex] + " ";
		poss = poss.trim();
		checkMatch(title, poss);
	}
	
	public void checkMatch (String title, String poss) {
		MatchResult mr = StringMatcher2.match(title, poss);
		int numAdditions = mr.getNumAddtions();
		int numDeletions = mr.getNumDeletions();
		int numChanges = mr.getNumChanges();
		int totalDiffs = numAdditions + numDeletions + numChanges;
		
		String ss;
		//String ls;
		if (poss.length() < title.length()) {
			ss = poss;
			//ls = title;
		} else {
			ss = title;
			//ls = poss;
		}
		
		if (numChanges + Math.min(numDeletions, numAdditions) >= (ss.length()+1) / 4)
			return;
		
		if (numAdditions > 2*(numChanges + numDeletions)) {
			if (wordIndex+possIndex < words.length)
				poss += words[wordIndex+possIndex] + " ";
			else 
				return;
			poss = poss.trim();
			checkMatch(title, poss);
			return;
		}
		
		if (totalDiffs >= (ss.length()+1)/3)
			return;
		
		if (totalDiffs < bestDeviation) {
			bestDeviation = totalDiffs;
			bestTitleIndex = titleIndex;
		}
	}
}
