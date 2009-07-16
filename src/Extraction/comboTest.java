package Extraction;

public class comboTest {
	
	public static void main (String[] args) {
		String[] elements = {"a", "b", "c", "d", "e", "f", "g","h","i","j"};
		int[] indices;
		int count = 0;
		for (int j = 0; j < elements.length; j++) {
			CombinationGenerator x = new CombinationGenerator (elements.length, j);
			StringBuffer combination;
			while (x.hasMore ()) {
			  combination = new StringBuffer ();
			  indices = x.getNext ();
			  for (int i = 0; i < indices.length; i++) {
			    combination.append (elements[indices[i]]);
			  }
			  System.out.println (combination.toString ());
			  count++;
			}
		}
		System.out.println(count);
	}
}
