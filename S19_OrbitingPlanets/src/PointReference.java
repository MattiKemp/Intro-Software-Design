/**
 * This class is made to keep a reference to a set of points.
 * This is very important for cross body communication such as a moon
 * knowing the changed coordinates of a planet.
 * @author Matthew Kemp
 *	
 */
public class PointReference {
	private double x;
	private double y;
	public PointReference(double x, double y){
		this.x = x;
		this.y = y;
	}
	/**
	 * Getter method for x coord
	 * @return x double
	 */
	public double getX() {
		return x;
	}
	/**
	 * Getter method for y coord
	 * @return y double
	 */
	public double getY() {
		return y;
	}
	/**
	 * Setter method for x coord
	 * @param x
	 */
	public void setX(double x1) {
		this.x = x1;
	}
	/**
	 * Setter method for y coord
	 * @param y
	 */
	public void setY(double y1) {
		this.y = y1;
	}
}
