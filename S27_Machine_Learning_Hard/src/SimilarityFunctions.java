
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
/**
 * 
 * @author Matthew Kemp
 * a class that contains the logic for several static machine learning related methods.
 */
public class SimilarityFunctions {
	/**
	 * A static method that computes the cosine similarity between two sets of values.
	 * Source: https://en.wikipedia.org/wiki/Cosine_similarity
	 * @param a
	 * @param b
	 * @return double the cosine similarity
	 */
	public static double cosineSim(double[] a, double[] b) {
		if(a.length != b.length) {
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
	/**
	 * A static method that computes the Euclidian distance between two sets of values.
	 * Source: https://en.wikipedia.org/wiki/Euclidean_distance
	 * @param a
	 * @param b
	 * @return
	 */
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
	/**
	 * A static method that computes the hamming distance between two strings.
	 * Soruce: https://en.wikipedia.org/wiki/Hamming_distance
	 * @param a
	 * @param b
	 * @return
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
		/*while the total lengths of our two strings can be > MAX_INT
		 * the Hamming distance is the difference between them which can only
		 * be less than or equal to MAX_INT.
		 */
		return count;
	}
	/**
	 * A static method that takes a data point and classifies it using k-nn classification.
	 * Uses a naive algorithm that I developed. Prints the predicted class the data point is.
	 * Source: https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm
	 * @param dataSet	file location of data
	 * @param dataPoint		data point you want to classify
	 * @param k		number of nearest neighbors to check
	 * @throws FileNotFoundException
	 */
	//runs in O(k*n) runtime with O(max(k,# of classes)) memory (not including the dataset obviously)
	public static void knearest(String dataSet, double[] dataPoint, int k) throws FileNotFoundException {
		double[] kNearest = new double[k];
		Arrays.fill(kNearest, Double.MAX_VALUE);
		ClassN[] kNearestClass = new ClassN[k];
		ClassN[] data = ReadData.readClassData(dataSet);
		kNearest[0] = euclidDist(data[0].getNumFeat(),dataPoint);;
		kNearestClass[0] = data[0];
		/*
		 * This algorithm keeps a list of the k closest neighbors to our datapoint.
		 * It achieves this by running through all of our datapoints, comparing them to
		 * the values in kNearest and sorting them accordingly. KNearest and KNearestCLass
		 * are sorted in ascending order. 
		 */
		for(int i = 1; i < data.length;i++) {
			double dist = euclidDist(data[i].getNumFeat(),dataPoint);
			/*
			 * if the current neighbor is closer than the largest value in 
			 * KNearest(closest neigbors) then swap it with value, do the same
			 * for all values in kNearest that are < current neighbor.
			 */
			if(dist < kNearest[k-1]) {
				kNearest[k-1]=dist;
				kNearestClass[k-1]=data[i];
				int spot = k-1;
				while(spot > 0 && dist < kNearest[spot-1]) {
					double temp = kNearest[spot-1];
					kNearest[spot-1]=kNearest[spot];
					kNearest[spot]=temp;
					ClassN tempClass = kNearestClass[spot-1]; 
					kNearestClass[spot-1]=kNearestClass[spot];
					kNearestClass[spot]=tempClass;
					spot-=1;
				}
			}
		}
		/*
		 * This algorithm loops through the closest neighbors and
		 * counts how many times each class appears in a hashmap,
		 * This technically runs in O(k) with O(class) memory.
		 */
		HashMap<String, Integer> typeCounts = new HashMap<String, Integer>();
		int max = 0;
		String type = null;
		for(ClassN e: kNearestClass) {
			if(typeCounts.get(e.getClassType())==null) {
				typeCounts.put(e.getClassType(),1);
			}
			else {
				typeCounts.put(e.getClassType(),typeCounts.get(e.getClassType())+1);
			}
			if(typeCounts.get(e.getClassType())>max) {
				max = typeCounts.get(e.getClassType());
				type = e.getClassType();
			}
		}
		System.out.println("New data point belongs to " + type);
	}
	/**
	 * A static method that tries to cluster a data set into k clusters. It achieves this by
	 * using a naive algorithm called naive k-means. It prints out # of data points in each cluster.
	 * 
	 * Source: https://en.wikipedia.org/wiki/K-means_clustering#Standard_algorithm
	 * @param dataset
	 * @param k
	 * @throws FileNotFoundException
	 */
	public static void kmeans(String dataset, int k) throws FileNotFoundException {
		PointN[] points = ReadData.readPointData(dataset);
		int dimension = points[0].getDimension();
		PointN[] clusterMeans = new PointN[k];
		/*
		 * sets our starting mean cluster values to the first k points in our data.
		 */
		for(int i = 0; i < k; i++) {
			clusterMeans[i] = points[i];
		}
		/*
		 * initalizes clusterTotals to default PointNs with
		 * all dimensions equal to 0.
		 */
		PointN[] clusterTotals = new PointN[k];
		for(int j = 0; j < k; j++) {
			clusterTotals[j] = new PointN(dimension);
		}
		int[] clusterCount = new int[k];
		boolean converged = false;
		/*
		 * calculates the closest cluster mean to all points in our data,
		 * averages the points and sets them to our new cluster means,
		 * and lastly iterates till our cluster means no longer change.
		 */
		while(!converged) {
			for(int j = 0; j < points.length; j++) {
				int lowestCluster = 0;
				double lowestValue = Double.MAX_VALUE;
				for(int q = 0 ; q < k ; q++) {
					if(euclidDist(points[j].getCoords(),clusterMeans[q].getCoords())<lowestValue) {
						lowestCluster = q;
						lowestValue = euclidDist(points[j].getCoords(),clusterMeans[q].getCoords());
					}
				}
				clusterTotals[lowestCluster].add(points[j]);
				clusterCount[lowestCluster]+=1;
			}
			for(int j = 0; j < k; j++) {
				clusterTotals[j].average(clusterCount[j]);
			}
			boolean equal = true;
			for(int j = 0; j < k; j++) {
				if(!clusterMeans[j].isEqual(clusterTotals[j])) {
					equal = false;
				}
			}
			if(equal) {
				converged = true;
			}
			else {
				clusterMeans = clusterTotals;
				clusterTotals = new PointN[k];
				for(int j = 0; j < k; j++) {
					clusterTotals[j] = new PointN(dimension);
				}
				clusterCount = new int[k];
			}
		}
		for(int i = 0; i < k; i++) {
			System.out.println("Cluster " +(i+1) +": " + clusterCount[i] +" data points");
		}
	}
}
