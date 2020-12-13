import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
/**
 * Class that manages the subgrids in a give game of life
 * @author Matthew Kemp
 *
 */
public class SubGridManager {
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
	 * The size of the subgrids
	 */
	private int n;
	/**
	 * A HashMap containing all valid tasks the manager needs to run each generation
	 */
	private HashMap<List<Integer>, Runnable> tasks;
	/**
	 * A set of tasks the manager needs to remove before the next generation calculation
	 */
	private Set<List<Integer>> toRemove;
	/**
	 * The thread pool of all subgrids
	 */
	private ExecutorService pool;
	/**
	 * Constructor for a subgrid manager. takes the global grid, all subgrids, and the size of the subgrids, 
	 * @param grid global grid
	 * @param subGrids all subgrids
	 * @param n the size of the subgrids
	 */
	public SubGridManager(HashMap<Integer, Set<Integer>> grid, HashMap<Integer, HashMap<Integer, SubGrid>> subGrids, int n) {
		this.grid = grid;
		this.subGrids = subGrids;
		//HashMap<Integer, SubGrid> origin = new HashMap<Integer, SubGrid>();
		//origin.put(0, new SubGrid(grid, n,0,0,0,0));
		//subGrids.put(0, origin);
		
		this.n = n;
		tasks = new HashMap<List<Integer>, Runnable>();
		pool = Executors.newCachedThreadPool();
		toRemove = Collections.synchronizedSet(new HashSet<List<Integer>>());
		//addTask(subGrids.get(0).get(0));
		addSubGrid(new SubGrid(grid, n, 0,0));
	}
	/**
	 * Public getter method to get the global grid
	 * @return the global grid
	 */
	public HashMap<Integer, Set<Integer>> getGrid(){
		return grid;
	}
	/**
	 * Public getter method to get the subgrids managed by this mangager.
	 * @return the subgrids managed by this manager
	 */
	public HashMap<Integer, HashMap<Integer, SubGrid>> getSubGrids(){
		return subGrids;
	}
	/**
	 * Method to add a new subgrid to the manager and to queue its correspoding ThreadHandler creation
	 * @param subgrid subgrid to add
	 */
	public void addSubGrid(SubGrid subgrid) {
		//System.out.println(subgrid.getX());
		//System.out.println(subgrid.getY());
		if(!subGrids.containsKey(subgrid.getY())) {
			subGrids.put(subgrid.getY(), new HashMap<Integer, SubGrid>());
		}
		subGrids.get(subgrid.getY()).put(subgrid.getX(),subgrid);
		/*System.out.println(subgrid.getY()/n);
		System.out.println(subgrid.getX()/n);
		System.out.println(subGrids.get(subgrid.getY()/n).get(subgrid.getX()/n));
		System.out.println("KEYSET!");
		System.out.println(subGrids.keySet());*/
		addTask(subgrid);
		
	}
	/**
	 * Creates a new ThreadHandler for the subgrid input
	 * @param subgrid subgrid to create a task for
	 */
	public void addTask(SubGrid subgrid) {
		tasks.put(Arrays.asList(subgrid.getX(),subgrid.getY()), new ThreadHandler(this,subGrids,subgrid.getX(),subgrid.getY(),subgrid));
	}
	/**
	 * Method to add a task key to the remove set for future removal
	 * @param key key of the task to remove
	 */
	public void addToRemove(List<Integer> key) {
		toRemove.add(key);
	}
	/**
	 * Method to remove a task given the key of the task from this manager
	 * @param key key of the task to remove
	 */
	public void removeTask(List<Integer> key) {
		//System.out.println(key.get(0) +":" + key.get(1));
		//if(subGrids.get(key.get(1)).size() == 1) {
			//subGrids.remove(key.get(1));
		//}
		//else {
			//subGrids.get(key.get(1)).remove(key.get(0));
		//}
		tasks.remove(key);
	}
	/**
	 * Method to run the next generation on all of the subgrids in this manager, handles all 
	 * thread communcation and deletion
	 */
	public void runGeneration() {
		System.out.println(tasks.size());
		if(tasks.size() > 0) {
			ArrayList<Future> futures = new ArrayList<Future>();
			for(Runnable run : tasks.values()) {
				futures.add(pool.submit(run));
			}
			boolean completed = false;
			while(!completed) {
				completed = true;
				for(Future e : futures) {
					if(!e.isDone()) {
						completed = false;
					}
				}
			}
			for(List<Integer> key : toRemove) {
				removeTask(key);
			}
			System.out.println("Generation Completed");
			ArrayList<SubGrid> newGrids = new ArrayList<SubGrid>();
			//Create any new subgrids.
			for(Runnable e : tasks.values()) {
				//System.out.println("Creating new subgrids!");
				ThreadHandler temp = (ThreadHandler) e;
				//check new corners
				SubGrid tempGrid = temp.getSubGrid();
				int thisX = temp.getX();
				int thisY = temp.getY();
				Boolean[] corners = tempGrid.getNeighborCorners();
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						if(corners[2*i+j]==true) {
							if(i==0 && !subGridExists(thisX-1+2*j,thisY-1)) {
								//System.out.println("new corner grid!");
								newGrids.add(new SubGrid(grid, n, (thisX-1+2*j), (thisY-1), (n-1)*(1-j), n-1));
								//addSubGrid(new SubGrid(grid, n, (thisX-1+2*j)*n, (thisY-1)*n, (n-1)*(1-j), n-1));
							}
							else if(i==1 && !subGridExists(thisX+1-2*j,thisY+1)) {
								//System.out.println("new corner grid!");
								newGrids.add(new SubGrid(grid, n, (thisX+1-2*j), (thisY+1), (n-1)*(j), 0));
								//addSubGrid(new SubGrid(grid, n, (thisX+1-2*j)*n, (thisY+1)*n, (n-1)*(j), 0));
							}
						}
					}
				}
				//check new borders
				Set<Integer>[] borders = tempGrid.getNeighborBorders();
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						if(!borders[j+2*i].isEmpty()) {
							if(i==0 && !subGridExists(thisX-1+j,thisY-j)) {
								//System.out.println("new border grid!");
								SubGrid newSubGrid = new SubGrid(grid, n, (thisX-1+j), (thisY-j));
								newSubGrid.setBorder(j+2, borders[j+2*i]);
								newGrids.add(newSubGrid);
								//addSubGrid(newSubGrid);
							}
							else if(i==1 && !subGridExists(thisX+1-j,thisY+j)) {
								//System.out.println("new border grid!");
								SubGrid newSubGrid = new SubGrid(grid, n, (thisX+1-j), (thisY+j));
								newSubGrid.setBorder(j, borders[j+2*i]);
								newGrids.add(newSubGrid);
								//addSubGrid(newSubGrid);
							}
						}
					}
				}
			}
			for(SubGrid e : newGrids) {
				addSubGrid(e);
			}
			//update neighbor values
			for(Runnable e : tasks.values()) {
				//System.out.println("Updating Neighbors!");
				ThreadHandler temp = (ThreadHandler) e;
				SubGrid tempGrid = temp.getSubGrid();
				int thisX = temp.getX();
				int thisY = temp.getY();
				//corners
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						if(i==0) {
							if(subGridExists(thisX-1+2*j,thisY-1)) {
								tempGrid.setCornerNeighbor(j, subGrids.get(thisY-1).get(thisX-1+2*j).getCorner(j+2));
							}
							else {
								tempGrid.setCornerNeighbor(j, false);
							}
						}
						else if(i==1) {
							if(subGridExists(thisX+1-2*j,thisY+1)) {
								//System.out.println("j:"+j);
								//System.out.println((thisX+1-2*j) + ":" + (thisY+1));
								//System.out.println(subGrids.get(thisY+1).get(thisX+1-2*j));
								subGrids.get(thisY+1).get(thisX+1-2*j).getCorner(j);
								tempGrid.setCornerNeighbor(2+j, subGrids.get(thisY+1).get(thisX+1-2*j).getCorner(j));
							}
							else {
								tempGrid.setCornerNeighbor(2+j, false);
							}
						}
					}
				}
				//borders
				for(int i = 0; i < 2; i++) {
					for(int j = 0; j < 2; j++) {
						if(i==0) {
							if(subGridExists(thisX-1+j,thisY-j)) {
								tempGrid.setNeighborBorder(j, new HashSet<Integer>(subGrids.get(thisY-j).get(thisX-1+j).getBorders()[j+2]));
							}
							else {
								tempGrid.setNeighborBorder(j, new HashSet<Integer>());
							}
						}
						else if(i==1) {
							if(subGridExists(thisX+1-j,thisY+j)) {
								tempGrid.setNeighborBorder(2+j, new HashSet<Integer>(subGrids.get(thisY+j).get(thisX+1-j).getBorders()[j]));
							}
							else {
								tempGrid.setNeighborBorder(2+j, new HashSet<Integer>());
							}
						}
					}
				}
			}
		}
		else {
			System.out.println("No subgrids!");
		}
	}
	/**
	 * Method to get whether a subgrid exists based on its global coordinates in this manager
	 * @param x global x coordinate to check
	 * @param y global y coordinate to check
	 * @return whether this manager contains that subgrid
	 */
	public boolean subGridExists(int x, int y) {
		if(subGrids.containsKey(y)) {
			if(subGrids.get(y).containsKey(x)) {
				return true;
			}
		}
		return false;
	}
}
