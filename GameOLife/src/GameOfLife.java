import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
/**
 * Class the represents a basic version of conways game of life.
 * @author Matthew Kemp
 *
 */
public class GameOfLife {
	/**
	 * The 2D space this subgrid managages in the game of life, represented using
	 * a hashMap with y coordinates as the main key and a set of x coordinates as their values,
	 * this allows for O(1) lookup and O(1) amortized addition time. It also allows for
	 * O(l) lookup time and storage for all live cells in this subgrids bounds which is much more efficient then
	 * the O(n^2) lookup and storage for the naive 2d array representation often used.
	 * HashMap representing all live cells in the current game based on their coordinates
	 * Fragment of early implementation, not necessary but also not to computational intesive  ie
	 * im too lazy to get rid of it :/
	 * 
	 * Using this implementation allow for an infinite grid to be much more efficently computed and stored
	 * as instead of keep tracking of NxN cells we only keep track of cells that are alive, this makes 
	 * memory usage much lower and computing the next generation O(l) where l is the number of live cells
	 * instead of O(n^2) where n is size of either the subgrid or the maximum size of implemented game if
	 * subgrids arent used. The only downsides are that it makes implementation a bit trickier and
	 * computing the next generation a bit tricker as well. Overall a much better system then the standard
	 * naive implementation. 
	 */
	private HashMap<Integer, Set<Integer>> grid;
	/**
	 * The 2D space this subgrid managages in the game of life, represented using
	 * a hashMap with y coordinates as the main key and a set of x coordinates as their values,
	 * this allows for O(1) lookup and O(1) amortized addition time. It also allows for
	 * O(l) lookup time and storage for all live cells in this subgrids bounds which is much more efficient then
	 * the O(n^2) lookup and storage for the naive 2d array representation often used.
	 * A HashMap containing all the subgrids current existing in our game. The subgrids are stored according
	 * to their origin coordiantes/n, essentially the above grid implementation but cut up into nxn squares
	 * A improvent for performance/memory usage would be to not have the subgrids be fixed, but to have them have
	 * floating dimenstions. For example the subgrids size would be based on the maximum area needed to bound the
	 * lives cells in the given area, up to a defined maximum size. Something like 1024 for x and y could be a good max size
	 * as this would cap each threads maxmium compute responsibility. However, this would require a large change in the way 
	 * the process of subgrids is handled. Maybe for a future project I would implement this and possibly the hashlife algorithm.
	 */
	private HashMap<Integer, HashMap<Integer, SubGrid>> subGrids;
	/**
	 * SubGridManager for this game of life. Deals with the logic of computing the next generation and handling
	 * the subgrid threads.
	 */
	private SubGridManager manager;
	/**
	 * The current generation of this game of life.
	 */
	private int generation;
	/**
	 * the subgrid size of this current game of life.
	 */
	private int n;
	/**
	 * the highest and lowest y and x values of all subgrids, calculated each iterations of 
	 * getBoundedGrids and used to help guide the user in the ui. Index 0-3: left lowest, left highest,
	 * right lowest, right highest, respectively. This is unimplemented as I didn't have enough time :/
	 * essentially it would be used to show the user the cardinal direction of more live cells in the game
	 * incase they got lost on the coordinate system.
	 */
	private int[] bounds;
	/**
	 * Constructor for the game of life
	 * @param n subgrid size NxN for this game of life.
	 */
	public GameOfLife(int n) {
		grid = new HashMap<Integer, Set<Integer>>();
		subGrids = new HashMap<Integer, HashMap<Integer, SubGrid>>();
		manager = new SubGridManager(grid, subGrids, n);
		this.n = n;
		bounds = new int[4];
		generation = 1;
	}
	/**
	 * Public getter method for the current generation number.
	 * @return the current generation this game is on
	 */
	public int getGeneration() {
		return generation;
	}
	/**
	 * Public getter method to get the bounds for the most west, north, east, and south live cells 
	 * for user navigation, not implemented.
	 * @return an array of size 4 containing the x or y coordinates for the most  west, north, east, and south live cells 
	 */
	public int[] getBounds() {
		return bounds;
	}
	/**
	 * returns a set containing subgrids that are being computed within the bounds x1<x2
	 * y1<y2. Where x and y are based on the global grid coordinates currently displayed on the gui.
	 * @param x1 the left most x coordinate being displayed by the GUI
	 * @param x2 the right most x coordinate being displayed by the GUI
	 * @param y1 the top most y coordinate being displayed by the GUI
	 * @param y2 the bottom most y coordinate being displayed by the GUI
	 * @return
	 */
	public Set<SubGrid> getBoundedGrids(int x1, int x2, int y1, int y2){
		Set<SubGrid> bounded = new HashSet<SubGrid>();
		bounds = new int[4];
		for(int y : subGrids.keySet()) {
			for(int x : subGrids.get(y).keySet()) {
				if(x1 <= x*n && x*n+n <= x2) {
					if(y1 <= y*n && y*n+n <= y2) {
						System.out.println("added: " + x + "," +y + " to bounded");
						bounded.add(subGrids.get(y).get(x));
					}
				}
				if(y < bounds[0]) {
					bounds[0] = y;
				}
				if(y > bounds[1]) {
					bounds[1] = y;
				}
				if(x < bounds[2]) {
					bounds[2] = x;
				}
				if(x > bounds[3]) {
					bounds[3] = x;
				}
			}
		}
		return bounded;
	}
	/**
	 * Method to add a live cell to this game of life at the specified x and y coordinates
	 * @param x the x coordinate to add a live cell at
	 * @param y the y coordinate to add a live cell at
	 * @return returns whether a new subgrid was created
	 */
	public boolean addCell(int x, int y) {
		if(x < 0) {
			x -= n;
		}
		if(y < 0) {
			y -= n;
		}
		int modX = x%n;
		int modY = y%n;
		if(x < 0) {
			modX = (n+x%n);
		}
		if(y < 0) {
			modY = (n+y%n);
		}
		System.out.println(modX);
		System.out.println(modY);
		if(subGrids.containsKey((y)/n)) {
			if(subGrids.get((y)/n).containsKey((x)/n)){
				System.out.println("adding!");
				subGrids.get(y/n).get(x/n).addToSubGrid(modX, modY);
				subGrids.get(y/n).get(x/n).addToGrid(modX, modY);
				return false;
			}
			else {
				//System.out.println("creating new subgrid!");
				System.out.println("creating new subgrid1!");
				System.out.println(x/n);
				System.out.println(y/n);
				manager.addSubGrid(new SubGrid(grid, n, x/n, y/n,modX, modY));
				return true;
			}
		}
		else {
			System.out.println("creating new subgrid2!");
			manager.addSubGrid(new SubGrid(grid, n, x/n, y/n, modX, modY));
			return true;
		}
	}
	/**
	 * Method to simulate the next generation of the game of life.
	 */
	public void runGeneration() {
		manager.runGeneration();
		generation++;
	}
	
	
}
