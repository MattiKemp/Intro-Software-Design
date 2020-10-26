import java.awt.image.BufferedImage;

/**
 * The main driver class for our image processing problems
 * @author Matthew Kemp
 *
 */
public class CannyEdgeDetectorDriver {
	/**
	 * Main method to test our image processing implementations
	 * @param args
	 */
	public static void main(String[] args) {
		//I wanted to add a webcam but I ran out of time :(
		
		//uncomment each part to get a gui with the image.
		
		//Easy
		
//		BufferedImage img = FileProcessor.readImage("EASY_sample_image.bmp");
//		EdgeDetectorFrame frame = new EdgeDetectorFrame(false);
//		int[][] greyImg = Converters.greyScaleImage(img);
//		greyImg = ImageProcessing.verticalEdge(greyImg);
//		img = Converters.convertArrToImg(greyImg);
//		frame.setImage(img);
		
		//Medium
		//run and press hystersis button, you can change the threshold values to get a better result.
		
//		BufferedImage img = FileProcessor.readImage("MEDIUM_sample_image.bmp");
//		EdgeDetectorFrame frame = new EdgeDetectorFrame(true);
//		frame.setImage(img);
//		frame.setDetectImage(img);
		
		//Hard 
		//Run and press the Canny Edge Detection button along with the hysterersis button.
		// you can change the threshold values to get a better result.
		
//		BufferedImage img = FileProcessor.readImage("HARD_sample_coins.bmp");
//		EdgeDetectorFrame frame = new EdgeDetectorFrame(true);
//		frame.setImage(img);
		
		//Another Hard example
		
//		BufferedImage img = FileProcessor.readImage("HARD_sample_blocks.bmp");
//		EdgeDetectorFrame frame = new EdgeDetectorFrame(true);
//		frame.setImage(img);
		
		
		
		//Test Examples
		
//		BufferedImage img = FileProcessor.readImage("ImageProcessingDemo.png");
//		EdgeDetectorFrame frame = new EdgeDetectorFrame(true);
//		frame.setImage(img);
		
		
	}
}
