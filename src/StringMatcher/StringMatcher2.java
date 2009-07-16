package StringMatcher;

public class StringMatcher2 {	
	public static MatchResult match (String mold, String clay) {
		mold = mold.toLowerCase();
		clay = clay.toLowerCase();
		int numAdditions = 0;
		int numDeletions = 0;
		int numChanges = 0;
		int slower = 0;
		int sright = 0;
		int upper = -1;
		int left = -1;
		int right = -1;
		int lower = -1;
		int swing = 1;
		
		while (sright < mold.length() && slower < clay.length()) {
			if (mold.charAt(sright) == clay.charAt(slower)) {
				int width = sright - left;
				int height = slower - upper;
				if (height > width) {
					numDeletions += height - width;
					height = width;
				} else if (width > height) {
					numAdditions += width - height;
					width = height;
				}
				if (width == height)
					numChanges += width - 1;
				upper = slower;
				left = sright;
				slower++; sright++;
				right = sright; lower = slower;
				swing = 1;
			} else {
				if (slower == upper + 1 && sright == left + 1) {
					slower++; sright++;
					right = sright; lower = slower;
					continue;
				}
				if (++swing % 2 == 0) {
					slower -= swing / 2;
					sright += swing / 2 - 1;
				} else {
					sright -= swing / 2;
					slower += swing / 2;
				}
				if (swing / 2 == lower - upper) { // same as right - left
					right++; lower++;
					slower = lower; sright = right;
					swing = 1;
				}
			}
		}
		
		right = mold.length();
		lower = clay.length();
		int width = right - left;
		int height = lower - upper;
		while (width > height) {
			width--;
			numAdditions++;
		}
		while (height > width) {
			height--;
			numDeletions++;
		}
		if (width == height)
			numChanges += width - 1;
		
		return new MatchResult(numAdditions, numDeletions, numChanges);
	}
	
	public static void main(String[] args) {
		String s1 = "Full Metal Alchemist";
		String s2 = "Fullmetal Alchemist Ghost";
		
		System.out.println(match(s1,s2));
	}
}
