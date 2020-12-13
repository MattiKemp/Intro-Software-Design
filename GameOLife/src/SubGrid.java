import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class to represent a subgrid for mananging a portion of a game of life.
 * @author Matthew Kemp
 *
 */
public class SubGrid {
	/**
	 * The global grid of the game of life this subgrid belongs to.
	 * The 2D space this subgrid managages in the game of life, represented using
	 * a hashMap with y coordinates as the main key and a set of x coordinates as their values,
	 * this allows for O(1) lookup and O(1) amortized addition time. It also allows for
	 * O(l) lookup time and storage for all live cells in this subgrids bounds which is much more efficient then
	 * the O(n^2) lookup and storage for the naive 2d array representation often used.
	 */
	private HashMap<Integer, Set<Integer>> grid;
	/**
	 * The 2D space this subgrid managages in the game of life, represented using
	 * a hashMap with y coordinates as the main key and a set of x coordinates as their values,
	 * this allows for O(1) lookup and O(1) amortized addition time. It also allows for
	 * O(l) lookup time and storage for all live cells in this subgrids bounds which is much more efficient then
	 * the O(n^2) lookup and storage for the naive 2d array representation often used.
	 */
	private HashMap<Integer, Set<Integer>> subgrid;
	/**
	 * Subgrid size NxN
	 */
	private int n;
	/**
	 * the global x coordinate of this subgrid
	 */
	private int x;
	/**
	 * the global y coordinate of this subgrid
	 */
	private int y;
	/**
	 * an array of the 4 borders of the subgrid ordered by left, top, right, bottom ascending index.
	 */
	private Set<Integer>[] borders;
	/**
	 * an array of the 4 borders of the border neighbors of the subgrid ordered by left, top, right, bottom ascending index.
	 */
	private Set<Integer>[] neighborBorders;
	/**
	 * an array of length 4 containing the value of the corner neighbors of this subgrid ordered left Top, Right top, right Bottom, left Bottom ascending index.
	 */
	private Boolean[] neighborCorners; 
	/**
	 * Constructor for SubGrid that takes the global grid, the size of the subgrid, the global x and y coordinates
	 * of this subgrid, and whether the subgrid has an initial live cell at the give coordinates primeX and primeY
	 * @param grid global grid
	 * @param n size of subgrid
	 * @param x global x coordinate of the subgrid
	 * @param y global y coordinate of the subgrid
	 * @param primeX local x coordinate of the first live cell
	 * @param primeY local y coordinate of the first live cell
	 */
	@SuppressWarnings("unchecked")
	public SubGrid(HashMap<Integer, Set<Integer>> grid, int n, int x, int y, int primeX, int primeY) {
		System.out.println("new subgrid created: " + x +"," +y );
		this.grid = grid;
		this.n = n;
		this.x = x;
		this.y = y;
		subgrid = new HashMap<Integer, Set<Integer>>();
		//Set<Integer> primeXSet = new HashSet<Integer>();
		//primeXSet.add(primeX);
		//subgrid.put(primeY, primeXSet);
		System.out.println(primeX);
		System.out.println(primeY);
		addToGrid(primeX, primeY);
		borders = new HashSet[]{new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>()};
		neighborBorders = new HashSet[]{new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>()};
		neighborCorners = new Boolean[] {false, false, false, false};
		addToSubGrid(primeX, primeY);
	}
	/**
	 * Constructor for a subgrid that takes the global gird, the subgrid size, and the global x and y coordinates
	 * of the subgrid
	 * @param grid global grid
	 * @param n the size of the subgrid
	 * @param x the global x coordinate of the subgrid
	 * @param y the global y coordinate of the subgrid
	 */
	@SuppressWarnings("unchecked")
	public SubGrid(HashMap<Integer, Set<Integer>> grid, int n, int x, int y) {
		System.out.println("new subgrid created: " + x +"," +y );
		this.grid = grid;
		this.n = n;
		this.x = x;
		this.y = y;
		subgrid = new HashMap<Integer, Set<Integer>>();
		//Set<Integer> primeXSet = new HashSet<Integer>();
		//primeXSet.add(primeX);
		//subgrid.put(primeY, primeXSet);
		//addToGrid(primeX, primeY);
		borders = new HashSet[]{new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>()};
		neighborBorders = new HashSet[]{new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>()};
		neighborCorners = new Boolean[] {false, false, false, false};
	}
	/**
	 * Public getter method to get the current live cells managed by this subgrid
	 * @return the current live cells managed by this subgrid
	 */
	public HashMap<Integer, Set<Integer>> getSubGrid(){
		return subgrid;
	}
	/**
	 * Public getter method to get the size of this subgrid NxN
	 * @return the size of this subgrid NxN
	 */
	public int getN() {
		return n;
	}
	/**
	 * Public getter method to get the global x coordinate of this subgrid
	 * @return the global x coordinate of this subgrid
	 */
	public int getX() {
		return x;
	}
	/**
	 * Public getter method to get the global y coordinate of this subgrid
	 * @return the global y coordinate of this subgrid
	 */
	public int getY() {
		return y;
	}
	/**
	 * Public getter method to get the live cells in the borders of this subgrid, used
	 * for subgrid communication. left, top, right, bottom
	 * @return the live cells on the borders of this subgrid
	 */
	public Set<Integer>[] getBorders(){
		return borders;
	}
	/**
	 * Public getter method to get the live cells on the left border of this subgrid
	 * @return the live cells on the left border of this subgrid
	 */
	public Set<Integer> getLeftBorder(){
		return borders[0];
	}
	/**
	 * Public getter method to get the live cells on the top border of this subgrid
	 * @return the live cells on the top border of this subgrid
	 */
	public Set<Integer> getTopBorder(){
		return borders[1];
	}
	/**
	 * Public getter method to get the live cells on the right border of this subgrid
	 * @return the live cells on the right border of this subgrid
	 */
	public Set<Integer> getRightBorder(){
		return borders[2];
	}
	/**
	 * Public getter method to get the live cells on the bottom border of this subgrid
	 * @return the live cells on the bottom border of this subgrid
	 */
	public Set<Integer> getBottomBorder(){
		return borders[3];
	}
	/**
	 * Public getter method to get the live cells of this subgrids neighbors touching borders.
	 * @return The lives cell on the border of this subgrids neighbors 
	 */
	public Set<Integer>[] getNeighborBorders(){
		return neighborBorders;
	}
	
//	public Boolean[] getCorners() {
//		return ;
//	}
	/**
	 * Public getter method to get the live cells in the specific corners of this subgrid based 
	 * on a cardinality input: top left:0, top right:1, bottom right:2 bottom left: 3.
	 * Used for subgrid communication
	 * @param cardinality top left:0, top right:1, bottom right:2 bottom left: 3.
	 * @return whether there is a live cell in that corner
	 */
	public boolean getCorner(int cardinality) {
		//System.out.println("cardinality");
		if(cardinality==0) {
			return getLeftTopCorner();
		}
		else if(cardinality==1) {
			//System.out.println("one");
			return getRightTopCorner();
		}
		else if(cardinality==2) {
			return getRightBottomCorner();
		}
		else {
			return getLeftBottomCorner();
		}
	}
	/**
	 * Public getter method to get whether their is a live cell in the top left corner
	 * of this subgrid
	 * @return whether there is a live cell in the top left corner
	 */
	public boolean getLeftTopCorner() {
		return borders[0].contains(0) && borders[1].contains(0);
	}
	/**
	 * Public getter method to get whether their is a live cell in the top right corner
	 * of this subgrid
	 * @return whether there is a live cell in the top right corner
	 */
	public boolean getRightTopCorner() {
		return borders[1].contains(n-1) && borders[2].contains(0);
	}
	/**
	 * Public getter method to get whether their is a live cell in the bottom right corner
	 * of this subgrid
	 * @return whether there is a live cell in the bottom right corner
	 */
	public boolean getRightBottomCorner() {
		return borders[2].contains(n-1) && borders[3].contains(n-1);
	}
	/**
	 * Public getter method to get whether their is a live cell in the bottom left corner
	 * of this subgrid
	 * @return whether there is a live cell in the bottom left corner
	 */
	public boolean getLeftBottomCorner() {
		return borders[0].contains(n-1) && borders[3].contains(0);
	}
	/**
	 * Public getter method to get all of this subgrids neigbors touching corners
	 * @return the corner live cells of the neighbors touching this subgrid
	 */
	public Boolean[] getNeighborCorners() {
		return neighborCorners;
	}
	/**
	 * Setter method to set the a specific border in the subgrid with a set of points
	 * Used for subgrid communication
	 * @param cardinality the cardinality of the border left:0, top:1, right:2, bottom:3
	 * @param points the points to replace the border with
	 */
	public void setBorder(int cardinality, Set<Integer> points) {
		if(cardinality==0) {
			setLeftBorder(points);
		}
		else if(cardinality==1) {
			setTopBorder(points);
		}
		else if(cardinality==2) {
			setRightBorder(points);
		}
		else {
			setBottomBorder(points);
		}
	}
	/**
	 * Setter method to set the left border with a set of points
	 * @param points the pointer to replace the border with
	 */
	public void setLeftBorder(Set<Integer> points) {
		borders[0] = points;
		for(Integer point : points) {
			addToGrid(0,point);
			addToSubGrid(0,point);
		}
	}
	/**
	 * Setter method to set the top border with a set of points
	 * @param points the pointer to replace the border with
	 */
	public void setTopBorder(Set<Integer> points) {
		borders[1] = points;
		for(Integer point : points) {
			addToGrid(point, 0);
			addToSubGrid(point, 0);
		}
	}
	/**
	 * Setter method to set the right border with a set of points
	 * @param points the pointer to replace the border with
	 */
	public void setRightBorder(Set<Integer> points) {
		borders[2] = points;
		for(Integer point : points) {
			addToGrid(n-1,point);
			addToSubGrid(n-1,point);
		}
	}
	/**
	 * Setter method to set the bottom border with a set of points
	 * @param points the pointer to replace the border with
	 */
	public void setBottomBorder(Set<Integer> points) {
		borders[3] = points;
		for(Integer point : points) {
			addToGrid(point,n-1);
			addToSubGrid(point,n-1);
		}
	}
	/**
	 * Method to set the neighbor borders with a new set of points
	 * @param cardinality the cardinality of the neighbor border (same as regular border)
	 * @param neighbor the set of points to replace with
	 */
	public void setNeighborBorder(int cardinality, Set<Integer> neighbor) {
		if(cardinality==0) {
			setLeftNeighborBorder(neighbor);
		}
		else if(cardinality==1) {
			setTopNeighborBorder(neighbor);
		}
		else if(cardinality==2) {
			setRightNeighborBorder(neighbor);
		}
		else {
			setBottomNeighborBorder(neighbor);
		}
	}
	/**
	 * Setter Method to set the Left neighbor border with a set of new points
	 * @param neighbor the new points to set 
	 */
	public void setLeftNeighborBorder(Set<Integer> neighbor) {
		neighborBorders[0] = neighbor;
	}
	/**
	 * Setter Method to set the top neighbor border with a set of new points
	 * @param neighbor the new points to set 
	 */
	public void setTopNeighborBorder(Set<Integer> neighbor) {
		neighborBorders[1] = neighbor;
	}
	/**
	 * Setter Method to set the right neighbor border with a set of new points
	 * @param neighbor the new points to set 
	 */
	public void setRightNeighborBorder(Set<Integer> neighbor) {
		neighborBorders[2] = neighbor;
	}
	/**
	 * Setter Method to set the bottom neighbor border with a set of new points
	 * @param neighbor the new points to set 
	 */
	public void setBottomNeighborBorder(Set<Integer> neighbor) {
		neighborBorders[3] = neighbor;
	}
	/**
	 * Setter method to set the corner neighbor cells in this subgrid with a new value
	 * @param cardinality cardinality (same as corners)
	 * @param neighbor new value of that corner
	 */
	public void setCornerNeighbor(int cardinality, boolean neighbor) {
		if(cardinality==0) {
			setLeftTopCornerNeighbor(neighbor);
		}
		else if(cardinality==1) {
			setRightTopCornerNeighbor(neighbor);
		}
		else if(cardinality==2) {
			setRightBottomCornerNeighbor(neighbor);
		}
		else {
			setLeftBottomCornerNeighbor(neighbor);
		}
	}
	/**
	 * Setter method to set the left top neighbor corner with a new value
	 * @param neighbor new value
	 */
	public void setLeftTopCornerNeighbor(boolean neighbor) {
		neighborCorners[0] = neighbor;
	}
	/**
	 * Setter method to set the right top neighbor corner with a new value
	 * @param neighbor new value
	 */
	public void setRightTopCornerNeighbor(boolean neighbor) {
		neighborCorners[1] = neighbor;
	}
	/**
	 * Setter method to set the bottom right neighbor corner with a new value
	 * @param neighbor new value
	 */
	public void setRightBottomCornerNeighbor(boolean neighbor) {
		neighborCorners[2] = neighbor;
	}
	/**
	 * Setter method to set the bottom right neighbor corner with a new value
	 * @param neighbor new value
	 */
	public void setLeftBottomCornerNeighbor(boolean neighbor) {
		neighborCorners[3] = neighbor;
	}
	/**
	 * Whether this subgrid is empty
	 * @return whether the subgrid is empty
	 */
	public boolean isEmpty() {
		return subgrid.isEmpty();
	}
	/**
	 * The number of live cells in this subgrid
	 * @return
	 */
	public int getSize() {
		int count = 0;
		for(int y : subgrid.keySet()) {
			count += subgrid.get(y).size();
		}
		return count;
	}
	
	/**
	 * Method to safely add a point in this subgrid to the global grid.
	 * @param x local x coordinate of the point you want to add to the global grid.
	 * @param y local y coordinate of the point you want to add to the global grid.
	 */
	public void addToGrid(int x, int y) {
		if(grid.containsKey(y+this.y)) {
			grid.get(y+this.y).add(x + this.x);
		}
		else {
			Set<Integer> newRow = new HashSet<Integer>();
			newRow.add(x + this.x);
			grid.put(y+this.y, newRow);
		}
	}
	
	/**
	 * Method to safely remove a point in this subgrid from the global grid.
	 * @param x local x coordinate of the point you want to remove from the global grid.
	 * @param y local y coordinate of the point you want to remove from the global grid.
	 */
	public void removeFromGrid(int x, int y) {
		grid.get(y+this.y).remove(x+this.x);
		if(grid.get(y+this.y).isEmpty()) {
			grid.remove(y+this.y);
		}
	}
	/**
	 * Method to safely add a new live cell to this subgrid
	 * @param x the local x coordinate to add a live cell to
	 * @param y the local y coordinate to add a live cell to
	 */
	public void addToSubGrid(int x, int y) {
		//System.out.println("ADDING to sub grid");
		if(subgrid.containsKey(y)) {
			//System.out.println("adding to preexisting row!");
			subgrid.get(y).add(x);
			//System.out.println(getSize());
		}
		else {
			//System.out.println("creating new row!");
			Set<Integer> newRow = new HashSet<Integer>();
			newRow.add(x);
			subgrid.put(y, newRow);
		}
		if(y==0) {
			borders[1].add(x);
		}
		else if(y==n-1) {
			borders[3].add(x);
		}
		if(x==0) {
			borders[0].add(y);
		}
		else if(x==n-1) {
			borders[2].add(y);
		}
	}
	/**
	 * Method to safely remove a cell from this sub grid at the local x and y coordinates
	 * @param x local x coordinate to remove
	 * @param y local y coordinate to remove
	 */
	public void removeFromSubGrid(int x, int y) {
		subgrid.get(y).remove(x);
		if(subgrid.get(y).isEmpty()) {
			subgrid.remove(y);
		}
		if(y==0) {
			borders[1].remove(x);
		}
		else if(y==n-1) {
			borders[3].remove(x);
		}
		if(x==0) {
			borders[0].remove(y);
		}
		else if(y==n-1) {
			borders[2].remove(y);
		}
	}
	/**
	 * Method to compute the next generation of this subgrid.
	 */
	public void computeTick() {
		Set<List<Integer>> add = new HashSet<List<Integer>>();
		Set<List<Integer>> remove = new HashSet<List<Integer>>();
		//new neighbors border.
		Set<Integer> left = new HashSet<Integer>();
		Set<Integer> top = new HashSet<Integer>();
		Set<Integer> right = new HashSet<Integer>();
		Set<Integer> bottom = new HashSet<Integer>();
		//by keeping track of coordinates that have been already checked to be added to the board
		//it is possible to prune a few operations off as computations won't be redone on the same coordinate.
		//However this only gets rid of a worse case '8' coefficient in exchange for a worse case O(n) 
		//memory requirement, where n is the number of live cells in this subgrid.
		//this only really becomes a problem when a subgrid has a lot of dispersed live cells
		//so, technically it is worse case O(1) memory space as there can only be a fixed number
		//of live cells in a sub grid. To save time on implementation I didn't not implement this 
		//algorithm. This is the main achilles heel to the hashmap grid implementation I have done, but overall
		//it should still match and in nearly all cases beat a traditional array implementation.
		//Set<List<Integer>> addChecked = new HashSet<List<Integer>>();
		for(int y : subgrid.keySet()) {
			for(int x : subgrid.get(y)) {
				//System.out.println(x + ":" + y);
				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
							int count = 0;
							//count += getCount(x,y);
							//count += getNeighborCount(x,y);
							count += getCount(x-1+j,y-1+i);
							count += getNeighborCount(x-1+j,y-1+i);
							//System.out.println(count);
							if(exists(x-1+j,y-1+i)==0) {
								//System.out.println(i + ":" + j);
								//System.out.println(count);
								if(count == 3) {
									if(x-1+j==-1) {
										left.add(y-1+i);
									}
									else if(x-1+j==n) {
										right.add(y-1+i);
									}
									else if(y-1+i==-1) {
										top.add(x-1+j);
									}
									else if(y-1+i==n) {
										bottom.add(x-1+j);
									}
									else {
										add.add(Arrays.asList(x-1+j,y-1+i));
									}
									
								}
							}
							else {
								if(!(count == 2 || count == 3)) {
									remove.add(Arrays.asList(x-1+j,y-1+i));
								}
							}
					}
				}
			}
		}
		//update local neighbor corners
		if(add.contains(Arrays.asList(-1,-1))){
			//System.out.println("TopLeft neighbor!");
			setLeftTopCornerNeighbor(true);
			add.remove(Arrays.asList(-1,-1));
		}
		if(add.contains(Arrays.asList(n,-1))) {
			//System.out.println("TopRight neighbor!");
			setRightTopCornerNeighbor(true);
			add.remove(Arrays.asList(n,-1));
		}
		if(add.contains(Arrays.asList(n,n))) {
			//System.out.println("BottomRight neighbor!");
			setRightBottomCornerNeighbor(true);
			add.remove(Arrays.asList(n,n));
		}
		if(add.contains(Arrays.asList(-1,n))) {
			//System.out.println("BottomLeft neighbor!");
			setLeftBottomCornerNeighbor(true);
			add.remove(Arrays.asList(-1,n));
		}
		//update local neighbor borders
		setLeftNeighborBorder(left);
		setTopNeighborBorder(top);
		setRightNeighborBorder(right);
		setBottomNeighborBorder(bottom);
		
		//add all new cells
		for(List<Integer> coord : add) {
			addToGrid(coord.get(0),coord.get(1));
			addToSubGrid(coord.get(0),coord.get(1));
		}
		//remove all dead cells
		for(List<Integer> coord : remove) {
			removeFromGrid(coord.get(0),coord.get(1));
			removeFromSubGrid(coord.get(0),coord.get(1));
		}
		
		
	}
	/**
	 * Method to get the count of live cells adjacent to the cell at the specified local x and y coordinate
	 * @param x local x coordinate of the origin cell
	 * @param y local y coordinate of the origin cell
	 * @return the number of live cells adjacent to that cell
	 */
	public int getCount(int x, int y) {
		int count = 0;
		if(x > 0) {
			count += exists(x-1, y);
		}
		if(x < n-1) {
			count += exists(x+1, y);
		}
		if(y > 0) {
			count += exists(x, y-1);
			if(x > 0) {
				count += exists(x-1, y-1);
			}
			if(x < n-1) {
				count += exists(x+1, y-1);
			}
		}
		if(y < n-1) {
			count += exists(x, y+1);
			if(x > 0) {
				count += exists(x-1, y+1);
			}
			if(x < n-1) {
				count += exists(x+1, y+1);
			}
		}
		return count;
	}
	/**
	 * Method to get the number of live cells in neighboring subgrids adjacent to the specified cell
	 * @param x local x coordinate for the specified cell
	 * @param y local y coordinate for the specified cell
	 * @return the number of live cells in neighboring subgrids adjacent to the specified cell
	 */
	public int getNeighborCount(int x, int y) {
		int count = 0;
		if(y==0) {
			if(x==0) {
				if(neighborCorners[0]==true) {
					count++;
				}
			}
			else if(x==n-1) {
				if(neighborCorners[1]==true) {
					count++;
				}
			}
			count += neighborExists(1, x-1);
			count += neighborExists(1, x);
			count += neighborExists(1, x+1);
		}
		else if(y==n-1) {
			if(x==0) {
				if(neighborCorners[3]==true) {
					count++;
				}
				else if(x==n-1) {
					if(neighborCorners[2]==true) {
						count++;
					}
				}
				count += neighborExists(3,x-1);
				count += neighborExists(3,x);
				count += neighborExists(3,x+1);
			}
		}
		if(x==0) {
			count += neighborExists(0, y-1);
			count += neighborExists(0, y);
			count += neighborExists(0, y+1);
		}
		else if(x==n-1) {
			count += neighborExists(2, y-1);
			count += neighborExists(2, y);
			count += neighborExists(2, y+1);
		}
		return count;
	}
	/**
	 * Whether a cell is alive(exists) in this subgrid
	 * @param x the local x coordinate to check 
	 * @param y the local y coordinate to check
	 * @return if the cell is alive(exists)
	 */
	public int exists(int x, int y) {
		if(subgrid.containsKey(y)) {
			if(subgrid.get(y).contains(x)) {
				return 1;
			}
		}
		return 0;
	}
	/**
	 * Method to check if a cell in a neighboring subgrid is alive (exists)
	 * @param border what neighbor border to check
	 * @param coord the coordinate to check
	 * @return whether the cell is alive (existS)
	 */
	public int neighborExists(int border, int coord) {
		if(neighborBorders[border].contains(coord)) {
			return 1;
		}
		return 0;
	}
	
	
}
