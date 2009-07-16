package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;

import Extraction.CombinationGenerator;
import StringMatcher.StringMatcher2;
import StringMatcher.StringPermuter;

public class Main {
	public static String url = "http://forums.animebw.com/index.php?showtopic=55&st=220";
	
	public static void main (String[] args) {
		AnimePicker ap = new AnimePicker();
		ap.readTuples(new File("TupleSet.dat"));
		if (false) {
			ap.extractWebPage(url);
			ap.writeTuples("TupleSet.dat");
		} else {
			BitSet b = new BitSet();
			b.flip(397); //b.flip(209);//GITS & CowboyBeebop
			//b.flip(179); b.flip(777);
			ap.getNearest(b,20);
		}
	}
	
	private static void readSer (ArrayList<String> animeTitles) {
		try {
			BufferedReader input =  new BufferedReader(new FileReader("list2.txt"));
			try {
				String line = null;
				while (( line = input.readLine()) != null){
					int ind = line.indexOf('*');
					if (ind == -1)
						continue;
					animeTitles.add(line.substring(ind+1, line.length()).trim());
				}
				
				// Serialize to a file
		        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("animeTitles.ser"));
		        out.writeObject(animeTitles);
		        out.close();
			}
			finally {
				input.close();
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
