package StringMatcher;

import java.util.Enumeration;
import java.util.Hashtable;

import Extraction.CombinationGenerator;
import PermutationAction.PermutationAction;

//http://anime.wikia.com/wiki/List_of_Anime_Series

public class StringPermuter {
	String original;
	String[] words; 
	int numWords;
	Hashtable<String,Boolean> p;
	PermutationAction[] actions;
	
	public StringPermuter (String _original) {
		original = _original;
		original = original.trim();
		words = original.split(" ");
		numWords = words.length;
		p = new Hashtable<String,Boolean>();
		actions = new PermutationAction[3];
		addActions();
	}
	
	public void addActions () {
		actions[0] = new LowerCaseAction();
		actions[1] = new FirstUpperCaseAction();
		actions[2] = new SpecialCharAction();
	}
	
	public class LowerCaseAction implements PermutationAction {
		public String doPermuation (String s) {
			return s.toLowerCase();
		}
	}
	public class FirstUpperCaseAction implements PermutationAction {
		public String doPermuation (String s) {
			String out = "";
			String[] words = s.split(" ");
			for (String w: words) {
				String start = w.substring(0, 1).toUpperCase();
				out += start + w.substring(1, w.length()) + " ";
			}
			return out;
		}
	}
	public class SpecialCharAction implements PermutationAction {
		public String doPermuation (String s) {
			return s.replaceAll("(!|-|:|'|%)", " ").trim();
		}
	}
	
	public static void main(String[] args) {
		/*String test = "gundam-wing!";
		StringPermuter sp = new StringPermuter(test);
		PermutationAction p = sp.new SpecialCharAction();
		
		System.out.println(p.doPermuation(test));*/
		
		String test = "gundam-Wing!";
		StringPermuter sp = new StringPermuter(test);
		
		int[] indices;
		int count = 0;
		for (int j = 0; j < sp.actions.length; j++) {
			CombinationGenerator x = new CombinationGenerator (sp.actions.length, j);
			String curr;
			while (x.hasMore ()) {
				curr = sp.original;
			  indices = x.getNext ();
			  for (int i = 0; i < indices.length; i++) {
				  //System.out.println("Got: " + curr + "with index " + i);
				  String newS = sp.actions[indices[i]].doPermuation(curr);
				  //System.out.println("Made: " + newS);
				  curr = newS;
			  }
			  count++;
			  System.out.println(curr);
			  sp.p.put(curr, true);
			}
		}
		System.out.println(count);
		Enumeration<String> e = sp.p.keys();
		while (e.hasMoreElements())
			System.out.println(e.nextElement());
	}
}
