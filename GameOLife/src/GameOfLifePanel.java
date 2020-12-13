import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
/**
 * Class to represent a resizable JPanel that displays a game of life 
 * @author Matthew Kemp
 *
 */
public class GameOfLifePanel extends JPanel{
	/**
	 * the game of life to display
	 */
	private GameOfLife gol;
	/**
	 * the left most x coordinate being displayed
	 */
	private int x1;
	/**
	 * the right most x coordinate being displayed
	 */
	private int x2;
	/**
	 * the top most y coordinate being displayed (the game of life is handled like
	 * java swing guis where going down means the y coordinate increases.
	 */
	private int y1;
	/**
	 * the bottom most coordainte being displayed (the game of life is handled like
	 * java swing guis where going down means the y coordinate increases.
	 */
	private int y2;
	/**
	 * the subgrid size of the game of life, I don't think this variable is actually needed
	 */
	private int n;
	/**
	 * The set of subgrids being currently displayed by this JPanel
	 */
	private Set<SubGrid> subgrids;
	/**
	 * Constructor for the Game of life panel, initalizes a new game of life given the subgrid input size
	 * @param n the subgrid size for the game of life NxN
	 */
	public GameOfLifePanel(int n) {
		gol = new GameOfLife(n);
		x1 = 0;
		x2 = n;
		y1 = 0;
		y2 = n;
		this.n = n;
		subgrids = gol.getBoundedGrids(x1, x2, y1, y2);
	}
	/**
	 * Public getter method for the current generation of the game of life this JPanel is displaying
	 * @return the current generation of this JPanels game of life
	 */
	public int getGeneration() {
		return gol.getGeneration();
	}
	/**
	 * Public getter method for The left most x coordinate displayed by this JPanel
	 * @return left most x coordinate displayed
	 */
	public int getX1() {
		return x1;
	}
	/**
	 * Public getter method for the right most x coordinate displayed by this JPanel
	 * @return the right most x coordinate displayed
	 */
	public int getX2() {
		return x2;
	}
	/**
	 * Public getter method for the top most y coordinate displayed by this JPaenl
	 * @return the top most y coordinate displayed
	 */
	public int getY1() {
		return y1;
	}
	/**
	 * Public getter method for the bottom most y coordinate displayed by this JPaenl
	 * @return the bottom most y coordinate displayed
	 */
	public int getY2() {
		return y2;
	}
	/**
	 * Method to properly display each live cell being displayed with the right width and height according to this 
	 * JPanels dimensions.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for(SubGrid e : subgrids) {
			int x = e.getX();
			int y = e.getY();
			HashMap<Integer, Set<Integer>> grid = e.getSubGrid();
			for(int yGrid : grid.keySet()) {
				for(int xGrid : grid.get(yGrid)) {
					//System.out.println("found!" + x + ":" + y + "/" + xGrid + ":" + yGrid);
					//g2d.draw(new Rectangle((x +xGrid -x1)*(((double) this.getWidth())/(x2-x1)),  (y+yGrid-y1)*(((double) this.getHeight())/(y2-y1)), ((double) this.getWidth())/(x2-x1), ((double) this.getHeight())/(y2-y1)));
					g2d.fillRect((int)((x*n +xGrid -x1)*(((double) this.getWidth())/(x2-x1))),  (int)((y*n+yGrid-y1)*(((double) this.getHeight())/(y2-y1))), (int)((double) this.getWidth())/(x2-x1), (int) ((double) this.getHeight())/(y2-y1));
				}
			}
		}
		g.dispose();
	}
	/**
	 * Method to update the subgrids this JPanel will display (used when a new subgrid is created
	 * in the screen bounds or the screen bounds themself changed)
	 */
	public void reloadSubGrids() {
		subgrids = gol.getBoundedGrids(x1, x2, y1, y2);
	}
	/**
	 * Method to tell this panels game of life to simulate the next generation
	 */
	public void nextGen() {
		gol.runGeneration();
		reloadSubGrids();
		repaint();
	}

	/**
	 * Method to add a new live cell to this panel's game of life given the screen coordinates
	 * of the location displayed by this JPanel
	 * @param x the screen x coordinate where the live cell should be added
	 * @param y the screen y coordinate where the live cell should be added
	 */
	public void addCell(int x, int y) {
		//check for trunaction errors here later
		//System.out.println((int)(x1 + (((double)x)/this.getWidth())*(x2-x1)));
		//System.out.println((int)(y1 + (((double)y)/this.getHeight())*(y2-y1)));
		if(gol.addCell((int)(x1 + (((double)x)/this.getWidth())*(x2-x1)), (int)(y1 + (((double)y)/this.getHeight())*(y2-y1)))) {
			reloadSubGrids();
		}
		
	}
	/**
	 * Method to add a new live cell to this panel's game of life given the screen coordinates
	 * of the location displayed by this JPanel and an offset value for each coordinate,
	 * this method is used to properly generate objects based on a user click location
	 * @param xOffSet the offset x coordinate
	 * @param yOffSet the offset y coordinate 
	 * @param x the screen x coordinate where the live cell should be added
	 * @param y the screen y coordinate where the live cell should be added
	 */
	public void addCell(int xOffSet, int yOffSet, int x, int y) {
		if(gol.addCell((int)(x1 + x +(((double)xOffSet)/this.getWidth())*(x2-x1)), (int)(y1 + y + (((double)yOffSet)/this.getHeight())*(y2-y1)))) {
			reloadSubGrids();
		}
	}
	/**
	 * Method to add an object, represented as a set of 2D coordinates represented by a list, to
	 * the screen given the onscreen location selected by the user as an origin point for the object
	 * @param x the x coordinate the user selected to place the object
	 * @param y the y coordinate the user selected to place the object
	 * @param object
	 */
	public void addObject(int x ,int y, Set<List<Integer>> object) {
		for(List<Integer> e : object) {
			//System.out.println((x+e.get(0)) +":" + (y+e.get(1)));
			addCell(x,y,e.get(0),e.get(1));
		}
		reloadSubGrids();
		repaint();
	}
	/**
	 * Move the current displayed region west
	 */
	public void west() {
		int change = x2-x1;
		x1-=change;
		x2-=change;
		reloadSubGrids();
		repaint();
	}
	/**
	 * Move the current displayed region north
	 */
	public void north() {
		int change = y2-y1;
		y1-=change;
		y2-=change;
		reloadSubGrids();
		repaint();
	}
	/**
	 * Move the current displayed region east
	 */
	public void east() {
		int change = x2-x1;
		x1+=change;
		x2+=change;
		reloadSubGrids();
		repaint();
	}
	/**
	 * Move the current displayed region south
	 */
	public void south() {
		int change = y2-y1;
		y1+=change;
		y2+=change;
		reloadSubGrids();
		repaint();
	}
	/**
	 * increase the current displayed region of the game of life by doubling the current
	 * each dimension ie x-> 2x, y->2y so total 4x the area
	 */
	public void zoomOut() {
		int size = x2-x1;
		x1-=size;
		y1-=size;
		reloadSubGrids();
		repaint();
	}
	/**
	 * decrease the current displayed region of the game of life by halving the current
	 * each dimension ie 2x-> x, 2y->y so total 1/4 the area
	 */
	public void zoomIn() {
		int size = x2-x1;
		x1+= size/2;
		y1+= size/2;
		reloadSubGrids();
		repaint();
	}
}
