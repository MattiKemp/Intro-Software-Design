import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Class used to create easy to work with game of life objects based
 * on predefined sets of coordinates.
 * @author Matthew Kemp
 *
 */
public class Objects {
	/**
	 * Method to get a live cell object (1 live cell).
	 * @return a set of 2d coordinates representing a live cell
	 */
	public static Set<List<Integer>> getCell(){
		Set<List<Integer>> cell = new HashSet<List<Integer>>();
		cell.add(Arrays.asList(0,0));
		return cell;
	}
	/**
	 * Method to get a glider object.
	 * @return a set of 2d coordinates reprsenting a glider gun
	 */
	public static Set<List<Integer>> getGlider(){
		Set<List<Integer>> glider = new HashSet<List<Integer>>();
		glider.add(Arrays.asList(0,2));
		glider.add(Arrays.asList(1,3));
		glider.add(Arrays.asList(2,3));
		glider.add(Arrays.asList(2,2));
		glider.add(Arrays.asList(2,1));
		return glider;
	}
	/**
	 * Method to get a beacon object.
	 * @return a set of 2d coordinates representing a beacon object
	 */
	public static Set<List<Integer>> getBeacon(){
		Set<List<Integer>> beacon = new HashSet<List<Integer>>();
		beacon.add(Arrays.asList(1,1));
		beacon.add(Arrays.asList(2,1));
		beacon.add(Arrays.asList(1,2));
		beacon.add(Arrays.asList(4,4));
		beacon.add(Arrays.asList(4,3));
		beacon.add(Arrays.asList(3,4));
		return beacon;
	}
	/**
	 * Method to get a Gosper Glider gun object
	 * @return a set of 2 coordinates representing a Gosper glider gun 
	 */
	public static Set<List<Integer>> getGosperGliderGun(){
		Set<List<Integer>> gosperGliderGun = new HashSet<List<Integer>>();
		gosperGliderGun.add(Arrays.asList(1,5));
		gosperGliderGun.add(Arrays.asList(1,6));
		gosperGliderGun.add(Arrays.asList(2,5));
		gosperGliderGun.add(Arrays.asList(2,6));
		
		gosperGliderGun.add(Arrays.asList(11,5));
		gosperGliderGun.add(Arrays.asList(11,6));
		gosperGliderGun.add(Arrays.asList(11,7));
		gosperGliderGun.add(Arrays.asList(12,4));
		gosperGliderGun.add(Arrays.asList(13,3));
		gosperGliderGun.add(Arrays.asList(14,3));
		gosperGliderGun.add(Arrays.asList(12,8));
		gosperGliderGun.add(Arrays.asList(13,9));
		gosperGliderGun.add(Arrays.asList(14,9));
		gosperGliderGun.add(Arrays.asList(15,6));
		gosperGliderGun.add(Arrays.asList(16,4));
		gosperGliderGun.add(Arrays.asList(16,8));
		gosperGliderGun.add(Arrays.asList(17,5));
		gosperGliderGun.add(Arrays.asList(17,6));
		gosperGliderGun.add(Arrays.asList(17,7));
		gosperGliderGun.add(Arrays.asList(18,6));
		
		gosperGliderGun.add(Arrays.asList(21,3));
		gosperGliderGun.add(Arrays.asList(21,4));
		gosperGliderGun.add(Arrays.asList(21,5));
		gosperGliderGun.add(Arrays.asList(22,3));
		gosperGliderGun.add(Arrays.asList(22,4));
		gosperGliderGun.add(Arrays.asList(22,5));
		gosperGliderGun.add(Arrays.asList(23,2));
		gosperGliderGun.add(Arrays.asList(23,6));
		gosperGliderGun.add(Arrays.asList(25,1));
		gosperGliderGun.add(Arrays.asList(25,2));
		gosperGliderGun.add(Arrays.asList(25,6));
		gosperGliderGun.add(Arrays.asList(25,7));
		
		gosperGliderGun.add(Arrays.asList(35,3));
		gosperGliderGun.add(Arrays.asList(35,4));
		gosperGliderGun.add(Arrays.asList(36,3));
		gosperGliderGun.add(Arrays.asList(36,4));
		
		return gosperGliderGun;
	}
}
