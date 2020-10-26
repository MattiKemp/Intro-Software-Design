import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * A class used to perform processing on files, in the current implementation there is only
 * one method, to read a image and convert it to a Buffered Image for processing.
 * @author Matthew Kemp
 *
 */
public class FileProcessor {
	/**
	 * 
	 * @param path the path in the working directory of the image.
	 * @return the image converted into a buffered Image 
	 */
	public static BufferedImage readImage(String path) {
		System.out.println(System.getProperty("user.dir"));
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(System.getProperty("user.dir") + "/Files/" + path));
		}catch(Exception e) {
			System.out.println("Couldn't read file");
			e.printStackTrace();
			return null;
		}
		return img;
	}
}
