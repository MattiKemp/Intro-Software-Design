/**
 * 
 * @author matt kemp
 * A class that represents a point in n dimensions.
 * Designed to be scaleable so you can reuse this code
 * for other data besides S27-MLHard.csv
 */
public class PointN {
	private double[] coordinates;
	/**
	 * Public constructor that takes a set of coordinates.
	 * @param coordinates	a set of coordinates
	 */
	public PointN(double[] coordinates) {
		this.coordinates = coordinates;
	}
	/**
	 * Public constructor that takes a dimension size and initializes
	 * the coordinates to the origin (0,0,...,0).
	 * @param dimension 	dimension size
	 */
	public PointN(int dimension) {
		coordinates = new double[dimension];
	}
	/**
	 * 
	 * @return double[]		the set of coordinates representing the point.
	 */
	public double[] getCoords() {
		return coordinates;
	}
	/**
	 * returns the dimension size of the point, ie the length of the coordinates.
	 * @return int	the dimension size
	 */
	public int getDimension() {
		return coordinates.length;
	}
	/**
	 * prints the coordinates in an easy to read fashion.
	 */
	public void print() {
		for(double e : coordinates) {
			System.out.print(e +" ");
		}
		System.out.println();
	}
	/**
	 * Compares two PointN objects to see if they represent the same point.
	 * If the points are of different dimension, return false as this isnt defined.
	 * @param n	A PointN 
	 * @return boolean	whether the points are equal
	 */
	public boolean isEqual(PointN n) {
		if(coordinates.length != n.getDimension()) {
			return false;
		}
		double[] ncoords = n.getCoords();
		for(int i = 0; i < coordinates.length; i++) {
			if(coordinates[i]!=ncoords[i]) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Takes a PointN object and adds its coordinates to this PointN object.
	 * Does nothing if the Points are of different dimension.
	 * @param n	PointN
	 */
	public void add(PointN n) {
		if(coordinates.length != n.getDimension()) {
			return;
		}
		double[] ncoords = n.getCoords();
		for(int i = 0; i < coordinates.length; i++) {
			coordinates[i] += ncoords[i];
		}
	}
	/**
	 * Divides the Point's coordinates by some amount, used for averaging.
	 * @param n
	 */
	public void average(int n) {
		for(int i = 0; i < coordinates.length;i++) {
			coordinates[i]/=n;
		}
	}
}
