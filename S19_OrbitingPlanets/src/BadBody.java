import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
/** A class that represents a naive version of a body in space.
 * Has the properties of position in space, center it is orbiting, radius of 
 * the body, color of body, and the rotation with respect to the body it is orbiting.
 * Scalable so you can technically have as many bodies orbiting each other.
 * Ex: A star orbiting a black hole, a planet orbiting that star, and a moon orbiting that planet.
 * @author Matthew Kemp
 *
 */
public class BadBody{
	private double x;
	private double y;
	private PointReference xy;
	private double xcenter;
	private double ycenter;
	private PointReference xycenter;
	private double size;
	private Color color;
	private double degree;
	//Equation for circle: https://en.wikipedia.org/wiki/Circle
	/**
	 * Constructor to generate a body with a random color.
	 * @param x 	x position in space
	 * @param y 	x position in space
	 * @param xcenter 	x position of the body this body is orbiting in space
	 * @param ycenter	y position of the body this body is orbiting in space
	 * @param size	radius of your body
	 * @param degree	the rotation with respect to the body you are orbiting.
	 */
	public BadBody(double x, double y, double xcenter, double ycenter, double size, double degree) {
		this.x = x;
		this.y = y;
		xy = new PointReference(x,y);
		this.xcenter = xcenter;
		this.ycenter = ycenter;
		xycenter = new PointReference(xcenter,ycenter);
		color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
		this.size = size;
		this.degree=degree;
	}
	/**
	 * Constructor to generate a body with a specified color.
	 * @param x
	 * @param y
	 * @param xcenter
	 * @param ycenter
	 * @param size
	 * @param degree
	 * @param color
	 */
	public BadBody(double x, double y, double xcenter, double ycenter, double size, double degree, Color color) {
		this.x = x;
		this.y = y;
		xy = new PointReference(x,y);
		this.xcenter = xcenter;
		this.ycenter = ycenter;
		xycenter = new PointReference(xcenter,ycenter);
		this.color = color;
		this.size = size;
		this.degree=degree;
	}
	/**
	 * Constructor to generate a body with a random color orbiting a non staionary object.
	 * @param x
	 * @param y
	 * @param xycenter
	 * @param size
	 * @param degree
	 */
	public BadBody(double x, double y, PointReference xycenter, double size, double degree) {
		this.x = x;
		this.y = y;
		this.xy = new PointReference(x,y);
		this.xcenter = xycenter.getX();
		this.ycenter = xycenter.getY();
		this.xycenter = xycenter;
		color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
		this.size = size;
		this.degree=degree;
	}
	/**
	 * Getter method for the current x position in space.
	 * @return x
	 */
	public double getX() {
		return x;
	}
	/**
	 * Getter method for the current y position in space.
	 * @return
	 */
	public double getY() {
		return y;
	}
	/**
	 * Getter method for the current live x and y position in space.
	 * @return xy
	 */
	public PointReference getCoords() {
		return xy;
	}
	/**
	 * Getter method for the current x position of the object this body is orbiting.
	 * @return xcenter
	 */
	public double getXCenter() {
		return xcenter;
	}
	/**
	 * Getter method for the current y position of the object this body is orbiting.
	 * @return ycenter
	 */
	public double getYCenter() {
		return ycenter;
	}
	/**
	 * Getter method for the live x and y position of object this body is orbiting.
	 * @return xycenter
	 */
	public PointReference getCenterCoords() {
		return xycenter;
	}
	/**
	 * Getter method for the radius of this body.
	 * @return
	 */
	public double getSize() {
		return size;
	}
	/**
	 * Method to increment the position of this body by a certain amount. Properly
	 * simulates the inverse square law so that the speed of rotation changes
	 * based on the distance^2.
	 */
	public void increment(double amount) {
		double radius = Math.sqrt(Math.pow(xycenter.getX()-xy.getX(), 2) + Math.pow(xycenter.getY()-xy.getY(), 2));
		degree+=(1/Math.pow(radius,2))*amount;
		if(degree>360) {
			degree=0;
		}
		double x = xy.getX();
		double y = xy.getY();
		double xcenter = xycenter.getX();
		double ycenter = xycenter.getY();
		radius =  Math.sqrt(Math.pow(this.xcenter-x, 2) + Math.pow(this.ycenter-y, 2));
		double newX = (xcenter + radius*Math.cos(Math.toRadians(degree)));
		double newY = (ycenter  + radius*Math.sin(Math.toRadians(degree)));
		xy.setX(newX);
		xy.setY(newY);
		this.xcenter = xcenter;
		this.ycenter = ycenter;
		this.x = x;
		this.y = y;
	}
	/**
	 * Method to draw a circle representation of this body on a 2D plane.
	 * @param g
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		Ellipse2D circle = new Ellipse2D.Double(xy.getX()-size, xy.getY()-size , 2*size, 2*size);
		g2d.setColor(color);
		g2d.fill(circle);
		
	}
}
