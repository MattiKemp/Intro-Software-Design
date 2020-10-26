import java.util.Scanner;
import java.io.File;
public class SimilarityFunctions {
	//should this function work with double arrays or int arrays?
	public static double cosineSim(double[] a, double[] b) {
		if(a.length != b.length) {
			//look into what this error should be 2 should work
			//but there may be something simpler
			return 2;
		}
		double dotProduct = 0;
		double aDist = 0;
		double bDist = 0;
		for(int i = 0; i < a.length;i++) {
			dotProduct += a[i]*b[i];
			aDist += a[i]*a[i];
			bDist += b[i]*b[i];
		}
		aDist = Math.sqrt(aDist);
		bDist = Math.sqrt(bDist);
		return dotProduct/(aDist*bDist);
	}
	public static double euclidDist(double[] a, double[] b) {
		if(a.length != b.length) {
			return -1;
		}
		double sumOSquares = 0;
		for(int i = 0; i < a.length;i++) {
			double temp = Math.abs(a[i]-b[i]);
			sumOSquares += temp*temp;
		}
		return Math.sqrt(sumOSquares);
	}
	/*while the total lengths of our two strings can be > MAX_INT
	 * the Hamming distance is the difference between them which can only
	 * be less than or equal to MAX_INT.
	 */
	public static int hammingDist(String a, String b) {
		if(a.length() != b.length()) {
			return -1;
		}
		int count = 0;
		for(int i = 0; i < a.length();i++) {
			if(a.charAt(i)!=b.charAt(i)) {
				count+=1;
			}
		}
		return count;
	}
	//runs in O(k*n) runtime with O(k) memory
	public static void knearest(String dataSet, double[] dataPoint, int k) {
		double[] kNearest = new double[k];
		ClassN[] kNearestClass = new ClassN[k];
		Scanner scnr = new Scanner(new File(System.getProperty("user.dir") + "\\" + dataSet));
		int numOfFeats = 0;
		while(scnr.hasNextDouble()) {
			numOfFeats+=1;
			scnr.nextDouble();
		}
		double[] tempNumFeat = new double[numOfFeats];
		scnr = new Scanner(new File(System.getProperty("user.dir") + "\\" + dataSet));
		for(int i = 0; i < numOfFeats; i++) {
			tempNumFeat[i] = scnr.nextDouble();
		}
		String type = scnr.next(pattern)();
		kNearestClass[0] = new ClassN(tempNumFeat,type.)
		double max = 
	}
}
