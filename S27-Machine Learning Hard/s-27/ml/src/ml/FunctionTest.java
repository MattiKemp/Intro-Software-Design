package ml;


public class FunctionTest {
	public static void main(String[] args) {
		double[] a = {1,2,3};
		double[] b = {2,6,3};
		System.out.println(SimilarityFunctions.cosineSim(a, b));
		System.out.println(SimilarityFunctions.euclidDist(a, b));
		System.out.println(SimilarityFunctions.hammingDist("10000", "01111"));
	}
}
