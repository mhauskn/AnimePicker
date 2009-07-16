package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

import Extraction.ForumTextExtractor;
import Extraction.ForumTextParser;

public class AnimePicker {
	ArrayList<BitSet> tuples = null;
	ArrayList<String> animeTitles = null;
	
	@SuppressWarnings("unchecked")
	public AnimePicker () {
		animeTitles = (ArrayList<String>) deSerialize(new File("animeTitles.ser"));
		tuples = new ArrayList<BitSet>();
	}
	
	public void extractTuples (File file) {
		ArrayList<String> posts =  ForumTextExtractor.extractForumText(file);
		parsePosts(posts);
	}
	
	public void extractTuples (String content) {
		ArrayList<String> posts =  ForumTextExtractor.extractForumText(content);
		parsePosts(posts);
	}
	
	private void parsePosts (ArrayList<String> posts) {
		ForumTextParser ftp = new ForumTextParser(animeTitles);
		for (int i = 0; i < posts.size(); i++) {
			BitSet bs = ftp.getAnimeFromPost(posts.get(i));
			if (bs != null)
				tuples.add(bs);
		}
	}
	
	public void extractWebPage (String pageURL) {
		try {
			URL url = new URL(pageURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;
			String content = "";
			while ((str = in.readLine()) != null) {
				content += str;
			}
			in.close();
			extractTuples(content);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class NearBitTuple implements Comparable<NearBitTuple> {
		int distance;
		BitSet b;
		BitSet full;
		
		public NearBitTuple (int _dist, BitSet _b, BitSet _full) {
			distance = _dist;
			b = _b;
			full = _full;
		}
		
		public int compareTo(NearBitTuple other) {
			return other.distance - this.distance;
		}
	}
	
	class result implements Comparable<result> {
		int index;
		double score;
		
		public result (int _ind, double _score) {
			index = _ind;
			score = _score;
		}
		
		public void addScore (double _d) {
			score += _d;
		}
		
		public int compareTo(result other) {
			if (this.score < other.score) 
				return 1;
			return -1;
		}
	}
	
	public void printTitleKey () {
		for (int i = 0; i < animeTitles.size(); i++) {
			System.out.println(i + "\t" + animeTitles.get(i));
		}
	}
	
	public void getNearest (BitSet query, int numResults) {
		double adder = 1.0;
		int currDist = Integer.MAX_VALUE;
		PriorityQueue<NearBitTuple> pq = new PriorityQueue<NearBitTuple>();
		for (BitSet bs : tuples) {
			NearBitTuple nbt = getDistance(query, bs);
			if (nbt != null) 
				pq.add(nbt);
		}
		Hashtable<Integer,result> top = new Hashtable<Integer,result>();
		ArrayList<BitSet> bs = new ArrayList<BitSet>();
		//Read scored animes into hashtable
		while (pq.size() > 0) {
			NearBitTuple nbt = pq.remove();
			if (bs.size() < numResults)
				bs.add(nbt.full);
			BitSet best = nbt.b;
			int dist = nbt.distance;
			if (currDist == Integer.MAX_VALUE)
				currDist = dist;
			if (dist < currDist) {
				adder *= Math.pow(.5, currDist - dist);
				currDist = dist;
			}
			for (int i = 0; i < best.size(); i++) {
				if (best.get(i))
					if (top.containsKey(i))
						top.get(i).addScore(adder);
					else
						top.put(i, new result(i,adder));
			}
			//System.out.println(nbt.distance);
			//printBitSet(best);
		}
		Vector<result> v = new Vector<result>(top.values());
		Collections.sort(v);
		Iterator<result> it = v.iterator();
		int i = 0;
		while (it.hasNext() && i < numResults) {
			i++;
			result r = it.next();
			System.out.println(i + ". " + animeTitles.get(r.index) + "\t\tScore: " + r.score);
		}
		System.out.println("\nNearest Neighbors\n");
		for (i = 0; i < numResults; i++) {
			if (i < bs.size())
				System.out.println(i+1 + ". " + printBitSet(bs.get(i)));
		}
	}
	
	private NearBitTuple getDistance (BitSet query, BitSet bs) {
		//int picked = query.cardinality();
		//int bspicked = bs.cardinality();
		BitSet intersect = (BitSet) query.clone();
		intersect.and(bs);
		int sizeIntersect = intersect.cardinality();
		
		BitSet offering = (BitSet) bs.clone();
		offering.andNot(query);
		int sizeOffering = offering.cardinality();
		
		if (sizeIntersect == 0 || sizeOffering == 0)
			return null;
		
		int distance = sizeIntersect - sizeOffering;
		
		return new NearBitTuple(distance, offering, bs);
	}
	
	public String printBitSet (BitSet b) {
		String out = "";
		for (int i = 0; i < b.length(); i++) {
			if (b.get(i)) {
				out += animeTitles.get(i);
				if (i != b.length()-1)
					out += ", ";
			}
		}
		return out;
	}
	
	public void writeTuples (String fileName) {
		serialize(tuples, fileName);
	}
	
	@SuppressWarnings("unchecked")
	public void readTuples (File fileName) {
		tuples = (ArrayList<BitSet>) deSerialize(fileName);
		System.out.println("Tuples has " + tuples.size() + " elements.");
	}
	
	public Object deSerialize (File file) {
		try {
	        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
	        Object out = in.readObject();
	        in.close();
	        return out;
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public void serialize (Object toWrite, String fileName) {
		try {
			// Serialize to a file
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(toWrite);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
