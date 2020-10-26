
public class ClassN {
	private double[] numericalFeat;
	private int classType;
	public ClassN(double[] numericalFeat, int classType) {
		this.numericalFeat = numericalFeat;
		this.classType = classType;
	}
	public double[] getNumFeat() {
		return numericalFeat;
	}
	public int getClassType() {
		return classType;
	}
}
