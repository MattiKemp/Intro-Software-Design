/**
 * A class that has implementations of distance functions.
 * @author Matthew Kemp
 */
public class DistanceFunctions {
	/**
	 * A function that takes two strings and returns whether they are
	 * only one character apart.
	 * @param s1 The first string you want to compare
	 * @param s2 The second string you want to compare
	 * @return Whether the two strings are only one character apart
	 */
	public static boolean editDist(String s1, String s2) {
		/* Don't know how it handles words of different length
		 * You could count the extra letters in the distance for a more general
		 * program that can handle different types of data.
		 * but I will just return false since our data is of fixed length.
		 */
		if(s1.length() != s2.length()) {
			return false;
		}
		boolean difByOne = false;
		for(int i = 0; i < s1.length(); i++) {
			if(s1.charAt(i)!=s2.charAt(i)) {
				if(difByOne) {
					return false;
				}
				difByOne = true;
			}
		}
		return difByOne;
	}
}
