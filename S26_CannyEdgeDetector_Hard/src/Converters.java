import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * A class for static methods that convert their inputs into a more functional form.
 * @author Matthew Kemp
 *
 */
public class Converters {
	/**
	 * Takes a grey scale Buffered Image and converts its values into a 2d array where each
	 * index is the greyscale intensity of the pixel at the same position in the image.
	 * @param img A geryscale BufferedImage
	 * @return A 2d array of greyscale pixel intensities of the inputted image.
	 */
	public static int[][] greyImageToArray(BufferedImage img){
		int[][] pixelArray = new int[img.getHeight()][img.getWidth()];
		for(int y = 0; y < pixelArray.length; y++) {
			for(int x = 0; x < pixelArray[0].length; x++) {
				int total = 0;
				//use bit operators to perform quick and efficient conversion of the ARGB integer at each pixel into 
				//its corresponding Red Green and Blue values.
				int argb = img.getRGB(x, y);
				total += (int)((argb >> 16) & 0xff); //Red
				total += (int)((argb >> 8) & 0xff); //Green
				total += (int)(argb & 0xff); //Blue
				pixelArray[y][x] = overFlowCheck(overFlowCheck(total/3));
			}
		}
		return pixelArray;
	}
	/**
	 * A overloaded method of the greyScaleImage method where the color to greyscale conversion standard
	 * is defaulted to Luma.
	 * @param img An Image to be converted into a greyscale 
	 * @return 2d array representation of the greyscale converted image.
	 */
	public static int[][] greyScaleImage(BufferedImage img){
		return greyScaleImage(img, 1);
	}
	/**
	 * A method which converts a BufferedImage into a 2d greyscale array representation. Has several
	 * conversion standards which can be used.
	 * @param img the image you want converted to greyscale.
	 * @param GrayscaleConversion the id of the conversion standard you want performed on the image.
	 * @return 2d array representation of the greyscale converted image.
	 */
	public static int[][] greyScaleImage(BufferedImage img, int GrayscaleConversion) {
		int[][] pixelArray = new int[img.getHeight()][img.getWidth()];
		for(int y = 0; y < pixelArray.length; y++) {
			for(int x = 0; x < pixelArray[0].length; x++) {
				if(GrayscaleConversion == 1) {
					pixelArray[y][x] = lumaConversion(img.getRGB(x, y));
				}
				else if(GrayscaleConversion == 2) {
					pixelArray[y][x] = luma709(img.getRGB(x, y));
				}
				else {
					pixelArray[y][x] = lumaHDR(img.getRGB(x, y));
				}
			}
		}
		return pixelArray;
	}
	/**
	 * Uses the First luma standard described here: https://en.wikipedia.org/wiki/Grayscale
	 * to convert a pixels ARGB value into an 8bit greyscale intensity. 
	 * @param argb ARGB integer value of a pixel
	 * @return the Luma converted greyscale intensity of the color
	 */
	public static int lumaConversion(int argb) {
		//use bit operators to perform quick and efficient conversion of the ARGB integer at each pixel into 
		//its corresponding Alpha Red Green and Blue values.
		double alpha = ((argb >> 24) & 0xff)/255.0;
		double intensity = 0;
		intensity += .299*((argb >> 16) & 0xff); //Red
		intensity += .587*((argb >> 8) & 0xff); //Green
		intensity += .114*((argb) & 0xff); //Blue
		return overFlowCheck((int) (intensity * alpha));
	}
	/**
	 * Uses the second luma standard described here: https://en.wikipedia.org/wiki/Grayscale
	 * to convert a pixels ARGB value into an 8bit greyscale intensity. 
	 * @param argb ARGB integer value of a pixel
	 * @return the Luma converted greyscale intensity of the color
	 */
	public static int luma709(int argb) {
		//use bit operators to perform quick and efficient conversion of the ARGB integer at each pixel into 
		//its corresponding Alpha Red Green and Blue values.
		double alpha = ((argb >> 24) & 0xff)/255.0;
		double intensity = 0;
		intensity += .2126*((argb >> 16) & 0xff); //Red
		intensity += .7152*((argb >> 8) & 0xff); //Green
		intensity += .0722*((argb) & 0xff); //Blue
		return overFlowCheck((int) (intensity * alpha));
	}
	/**
	 * Uses the HDR luma standard described here: https://en.wikipedia.org/wiki/Grayscale
	 * to convert a pixels ARGB value into an 8bit greyscale intensity. 
	 * @param argb ARGB integer value of a pixel
	 * @return the Luma converted greyscale intensity of the color
	 */
	public static int lumaHDR(int argb) {
		//use bit operators to perform quick and efficient conversion of the ARGB integer at each pixel into 
		//its corresponding Alpha Red Green and Blue values.
		double alpha = ((argb >> 24) & 0xff)/255.0;
		double intensity = 0;
		intensity += .2627*((argb >> 16) & 0xff); //Red
		intensity += .6780*((argb >> 8) & 0xff); //Green
		intensity += .0593*((argb) & 0xff); //Blue
		return overFlowCheck((int) (intensity * alpha));
	}
	/**
	 * This method converts 2d array representations of greyscale images into Buffered Images.
	 * @param greyimg 2d greyscale array representation of an image to convert.
	 * @return A BufferedImage of the array representation 
	 */
	public static BufferedImage convertArrToImg(int[][] greyimg) {
		BufferedImage image = new BufferedImage( greyimg[0].length, greyimg.length, BufferedImage.TYPE_INT_ARGB );
		final int[] a = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		//flatten the greyimg array
		int[] flattened = new int[greyimg[0].length*greyimg.length];
		for(int y = 0; y < greyimg.length; y++) {
			for(int x = 0; x <greyimg[0].length; x++) {
				flattened[greyimg.length*y + x] = ((int)(255 << 24)) + ((int)(greyimg[y][x] << 16)) + ((int)(greyimg[y][x] << 8)) + ((int)(greyimg[y][x]));
			}
		}
		System.arraycopy(flattened, 0, a, 0, flattened.length);
		return image;
	}
	/**
	 * A safety method to check that floating point operations aren't round above the 8 bit limit of greyscale intensity.
	 * @param n greyscale intensity being checked
	 * @return either the value of n or the truncated value of n being 255.
	 */
	public static int overFlowCheck(int n) {
		if(n > 255) {
			System.out.println("overflow");
			return 255;
		}
		return n;
	}
}
