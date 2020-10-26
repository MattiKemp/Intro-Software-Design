import java.io.FileNotFoundException;
/**
 * 
 * @author Matthew Kemp
 * A class used to test our functions in SimilarityFunctions.
 * 
 */
public class FunctionTest {
	public static void main(String[] args) {
		//Easy
		System.out.println("=========EASY=========");
		double[] a = {1,2,3};
		double[] b = {2,6,3};
		System.out.println(SimilarityFunctions.cosineSim(a, b));
		System.out.println(SimilarityFunctions.euclidDist(a, b));
		System.out.println(SimilarityFunctions.hammingDist("10000", "01111"));
		//Medium
		System.out.println("=========Medium=========");
		ClassN[] classes = null;
		try {
			classes = ReadData.readClassData("S27-MLMedium.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		for(ClassN e: classes) {
//			e.print();
//		}
		double[] dataPoint = {1.5, 3.5, 2, 2, 8};
		try {
			SimilarityFunctions.knearest("S27-MLMedium.csv", dataPoint, 5);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		double[] dataPoint2 = {3, 3, 2, 2, 1};
		try {
			SimilarityFunctions.knearest("S27-MLMedium.csv", dataPoint2, 5);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		//Hard part
		System.out.println("=========HARD=========");
		PointN[] points = null;
		try {
			points = ReadData.readPointData("S27-MLHard.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		for(PointN e: points) {
//			e.print();
//		}
		try {
			SimilarityFunctions.kmeans("S27-MLHard.csv", 4);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
}
