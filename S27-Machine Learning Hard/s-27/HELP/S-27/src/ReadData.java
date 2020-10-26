import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadData {
	/**
	 * Static method to read a csv file and translate it to an array of ClassN objects.
	 * @param fileLoc
	 * @return ClassN[]
	 * @throws FileNotFoundException
	 */
	public static ClassN[] readClassData(String fileLoc) throws FileNotFoundException {
		ArrayList<ClassN> classes = new ArrayList<ClassN>();
		Scanner scnr = new Scanner(new File(System.getProperty("user.dir") + "\\" + fileLoc));
		scnr.useDelimiter(",");
		int numOfFeats = 0;
		try {
			while(scnr.hasNextDouble()) {
				numOfFeats+=1;
				scnr.nextDouble();
			}
			scnr = new Scanner(new File(System.getProperty("user.dir") + "\\" + fileLoc));
			scnr.useDelimiter(",");
			while(scnr.hasNext()) {
				double[] tempNumFeat = new double[numOfFeats];
				for(int i = 0; i < numOfFeats; i++) {
					tempNumFeat[i] = scnr.nextDouble();
				}
				String type = scnr.nextLine().substring(2);
				//I Couldn't figure out why the scanner couldn't read the type properly
				//It was like the type was having the next lines first double appended to it.
				//So I just used substrings to format it with nextLine().
				classes.add(new ClassN(tempNumFeat,type.substring(0, type.length()-1)));
				scnr.hasNextLine();
			}
			scnr.close();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("There is either an error in your data or your data is not formatted properly.");
		}
		return classes.toArray(new ClassN[classes.size()]);
	}
	/**
	 * Static method to read a csv file of data and convert it to an array of PointN objects.
	 * 
	 * @param fileLoc
	 * @return PointN[]
	 * @throws FileNotFoundException
	 */
	public static PointN[] readPointData(String fileLoc) throws FileNotFoundException {
		ArrayList<PointN> points = new ArrayList<PointN>();
		Scanner scnr = new Scanner(new File(System.getProperty("user.dir") + "\\" + fileLoc));
		scnr.useDelimiter(",");
		int numOfPoints = 1;
		try {
//			while(scnr.hasNextDouble()) {
//				 numOfPoints+=1;
//				System.out.println(scnr.nextDouble());
//				System.out.println("found");
//			}
			scnr = new Scanner(new File(System.getProperty("user.dir") + "\\" + fileLoc));
			scnr.useDelimiter(",");
			while(scnr.hasNextLine()) {
				String[] line = scnr.nextLine().split(",");
				double[] point = new double[line.length];
				for(int i = 0; i < line.length;i++) {
					point[i] = Double.parseDouble(line[i]);
				}
				points.add(new PointN(point));
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("There is either an error in your data or your data is not formatted properly.");
		}
		return points.toArray(new PointN[points.size()]);
	}
}
