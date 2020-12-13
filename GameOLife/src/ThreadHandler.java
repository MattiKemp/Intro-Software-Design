import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 * Class the maps a new thread to a subgrid so that each subgrid can be computed independentaly
 * @author Matthew Kemp
 *
 */
public class ThreadHandler implements  Runnable{
	/**
	 * The manager this handler belongs to
	 */
	private SubGridManager manager;
	/**
	 * The subgrids the manager manages, not used
	 */
	private HashMap<Integer, HashMap<Integer, SubGrid>> subGrids;
	/**
	 * The global x coordinate of the subgrid this handler represents.
	 */
	private int x;
	/**
	 * The global y coordinate of the subgrid this handler represents.
	 */
	private int y;
	/**
	 * The subgrid this handles represents
	 */
	private SubGrid subgrid;
	/**
	 * The unique key for this handles
	 */
	private List<Integer> taskKey;
	/**
	 * Constructor for this thread handler, takes the manager, subgrids, global x and y, and the subgrid for this handler.
	 * @param manager manager this handler belongs to
	 * @param subGrids subgrids the manager manages
	 * @param x the global x coordinate of the subgrid this handler represents
	 * @param y the global y coordinate of the subgrid this handler represents
	 * @param subgrid the subgrid this handler manager
	 */
	public ThreadHandler(SubGridManager manager, HashMap<Integer, HashMap<Integer, SubGrid>> subGrids, int x, int y, SubGrid subgrid) {
		this.manager = manager;
		this.subGrids = subGrids;
		this.x = x;
		this.y = y;
		this.subgrid = subgrid;
		
		taskKey = Arrays.asList(x,y);
	}
	/**
	 * Public getter method for the global x coordinate of the subgrid this handler represents
	 * @return the global x coordinate of the subgrid this handler represents
	 */
	public int getX() {
		return x;
	}
	/**
	 * Public getter method for the global y coordinate of the subgrid this handler represents
	 * @return the global y coordinate of the subgrid this handler represents
	 */
	public int getY() {
		return y;
	}
	/**
	 * Public getter method to get the subgrid this handler represents
	 * @return the subgrid this handler represents
	 */
	public SubGrid getSubGrid(){
		return subgrid;
	}
	/**
	 * Method ran each time the manager runs a handler, computes the next generation for the subgrid
	 * this handler represents.
	 */
	@Override
	public void run() {
		//System.out.println("running!");
		//System.out.println(subgrid.getSize());
		subgrid.computeTick();
		//System.out.println(subgrid.getSize());
		if(subgrid.isEmpty()) {
			System.out.println("SubGrid is empty, deleting! " + x + "," + y);
			subGrids.get(y).remove(x);
			if(subGrids.get(y).size()==0) {
				subGrids.remove(y);
			}
			manager.addToRemove(taskKey);
			//subGrids.get(y).remove(x);
		}
		//System.out.println("Thread: " + taskKey.get(0) + ":" + taskKey.get(1) + " finished!");
	}

}
