/**
 * 
 * @author Matthew Kemp
 * A class that represents a set of values linked to a type.
 * Designed to be scaleable so that it can be used with other data
 * besides S27-MLMedium.csv.
 */
public class ClassN {
	private double[] numericalFeat;
	private String classType;
	/**
	 * Public constructor that takes an array of features and a string
	 * that represents the type the object is.
	 * @param numericalFeat
	 * @param classType
	 */
	public ClassN(double[] numericalFeat, String classType) {
		this.numericalFeat = numericalFeat;
		this.classType = classType;
	}
	/**
	 * Get the list of features.
	 * @return double[] the list of features
	 */
	public double[] getNumFeat() {
		return numericalFeat;
	}
	/**
	 * Get the type this class is.
	 * @return String type
	 */
	public String getClassType() {
		return classType;
	}
	/**
	 * Compares this object's type to another's
	 * @param compareType
	 * @return boolean whether this object is of the same type
	 */
	public boolean classEquals(String compareType) {
		return compareType.equals(classType);
	}
	/**
	 * prints the features and type in easy to read fashion.
	 */
	public void print() {
		for(double e: numericalFeat) {
			System.out.print(e +", ");
		}
		System.out.println(classType);
	}
}