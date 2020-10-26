import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * This class contains static methods for image processing.
 * @author Matthew Kemp
 *
 */
public class ImageProcessing {
	/**
	 * This method performs vertical edge detection by using the Prewitt x operator on 
	 * each pixel. Essentially, at each pixel we examine its 8 neighbors value's to determine
	 * the likelihood this pixel is an edge. Treats borders as zero-padded with the getValidCoords function.
	 * @param greyimg a 2d greyscale array
	 * @return vertical edge detected 2d geryscale array
	 */
	public static int[][] verticalEdge(int[][] greyimg){
		int[][] vertEdgeImg = new int[greyimg.length][greyimg[0].length];
		for(int y = 0; y < greyimg.length; y++) {
			for(int x = 0; x < greyimg[0].length; x++) {
				int magnitude = 0;
				for(int[] coord : getValidCoords3x3(x,y,greyimg[0].length, greyimg.length)) {
					if(coord[0] < x) {
						magnitude += -1*greyimg[coord[1]][coord[0]];
					}
					if(coord[0] > x) {
						magnitude += greyimg[coord[1]][coord[0]];
					}
				}
				if(magnitude < 0) {
					magnitude = -magnitude;
				}
				magnitude = Converters.overFlowCheck((int)((magnitude/765.0)*255));
				vertEdgeImg[y][x] = magnitude;
			}
		}
		return vertEdgeImg;
	}
	/**
	 * This method performs the second and third steps of canny edge detection: 
	 * https://en.wikipedia.org/wiki/Canny_edge_detector#Process_of_Canny_edge_detection_algorithm
	 * It uses a vertical and horizontal pretwitt operator to figure out the intensity at each pixel
	 * and then performs non-maximum suppression for edges that are 0, 45, 90, and 135 degrees.
	 * @param greyimg a 2d greyscale array
	 * @return canny edge detected 2d geryscale array
	 */
	public static int[][] cannyEdge(int[][] greyimg){
		int[][] cannyEdgeImg = new int[greyimg.length][greyimg[0].length];
		Set<int[]> zeroD = new HashSet<int[]>(); //horizontal
		Set<int[]> oneD = new HashSet<int[]>(); //45
		Set<int[]> twoD = new HashSet<int[]>(); //90
		Set<int[]> threeD = new HashSet<int[]>(); //135
		//finding the intensity gradients of the image and the direction of each edge stored in the sets above.
		for(int y = 0; y < greyimg.length; y++) {
			for(int x = 0; x < greyimg[0].length; x++) {
				int[] magnitudes = new int[4];
				for(int[] coord : getValidCoords3x3(x,y,cannyEdgeImg[0].length, cannyEdgeImg.length)) {
					if(coord[0] < x) {
						if(coord[1] < y) {
							magnitudes[0] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[1] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[2] += -1*greyimg[coord[1]][coord[0]];
						}
						else if(coord[1] == y){
							magnitudes[1] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[2] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[3] += -1*greyimg[coord[1]][coord[0]];
						}
						else {
							magnitudes[0] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[2] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[3] += -1*greyimg[coord[1]][coord[0]];
						}
					}
					else if(coord[0]==x) {
						if(coord[1] < y) {
							magnitudes[0] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[1] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[3] += 1*greyimg[coord[1]][coord[0]];
						}
						else {
							magnitudes[0] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[1] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[3] += -1*greyimg[coord[1]][coord[0]];
						}
					}
					else {
						if(coord[1] < y) {
							magnitudes[0] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[2] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[3] += 1*greyimg[coord[1]][coord[0]];
						}
						else if(coord[1] == y){
							magnitudes[1] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[2] += 1*greyimg[coord[1]][coord[0]];
							magnitudes[3] += 1*greyimg[coord[1]][coord[0]];
						}
						else {
							magnitudes[0] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[1] += -1*greyimg[coord[1]][coord[0]];
							magnitudes[2] += 1*greyimg[coord[1]][coord[0]];
						}
					}
				}
				int max = 0;
				for(int i = 0; i < magnitudes.length; i++) {
					magnitudes[i] = Math.abs(magnitudes[i]);
					if(magnitudes[i] > magnitudes[max]) {
						max = i;
					}
				}
				if(max == 0) {
					zeroD.add(new int[] {x,y});
				}
				else if(max == 1) {
					oneD.add(new int[] {x,y});
				}
				else if(max == 2) {
					twoD.add(new int[] {x,y});
				}
				else {
					threeD.add(new int[] {x,y});
				}
				magnitudes[0] = Converters.overFlowCheck((int)((magnitudes[0]/765.0)*255));
				magnitudes[2] = Converters.overFlowCheck((int)((magnitudes[2]/765.0)*255));
				cannyEdgeImg[y][x] = Converters.overFlowCheck((int)Math.sqrt(magnitudes[0]*magnitudes[0] + magnitudes[2]*magnitudes[2]));
			}
		}
		//the beginning of the non-maximum suppression process.
		int[][] nonMaxSupp = new int[cannyEdgeImg.length][cannyEdgeImg[0].length];
		//point of major error, double check later
		for(int[] zero : zeroD) {
			boolean changed = false;
			if(zero[1] > 0) {
				if(cannyEdgeImg[zero[1]-1][zero[0]] > cannyEdgeImg[zero[1]][zero[0]]) {
					nonMaxSupp[zero[1]][zero[0]] = 0;
					changed = true;
				}
			}
			if(zero[1] < cannyEdgeImg.length-1) {
				if(cannyEdgeImg[zero[1]+1][zero[0]] > cannyEdgeImg[zero[1]][zero[0]]) {
					nonMaxSupp[zero[1]][zero[0]] = 0;
					changed = true;
				}
			}
			if(!changed) {
				nonMaxSupp[zero[1]][zero[0]] = cannyEdgeImg[zero[1]][zero[0]];
			}
		}
		for(int[] one : oneD) {
			boolean changed = false;
			if(one[1] > 0 && one[0] > 0) {
				if(cannyEdgeImg[one[1]-1][one[0]-1] > cannyEdgeImg[one[1]][one[0]]) {
					nonMaxSupp[one[1]][one[0]] = 0;
					changed = true;
				}
			}
			if(one[1] < cannyEdgeImg.length-1 && one[0] < cannyEdgeImg[0].length-1) {
				if(cannyEdgeImg[one[1]+1][one[0]+1] > cannyEdgeImg[one[1]][one[0]]) {
					nonMaxSupp[one[1]][one[0]] = 0;
					changed = true;
				}
			}
			if(!changed) {
				nonMaxSupp[one[1]][one[0]] = cannyEdgeImg[one[1]][one[0]];
			}
		}
		for(int[] two : twoD) {
			boolean changed = false;
			if(two[0] > 0) {
				if(cannyEdgeImg[two[1]][two[0]-1] > cannyEdgeImg[two[1]][two[0]]) {
					nonMaxSupp[two[1]][two[0]] = 0;
					changed = true;
				}
			}
			if(two[0] < cannyEdgeImg[0].length-1) {
				if(cannyEdgeImg[two[1]][two[0]+1] > cannyEdgeImg[two[1]][two[0]]) {
					nonMaxSupp[two[1]][two[0]] = 0;
					changed = true;
				}
			}
			if(!changed) {
				nonMaxSupp[two[1]][two[0]] = cannyEdgeImg[two[1]][two[0]];
			}
		}
		for(int[] three : threeD) {
			boolean changed = false;
			if(three[1] > cannyEdgeImg.length-1 && three[0] > 0) {
				if(cannyEdgeImg[three[1]+1][three[0]-1] > cannyEdgeImg[three[1]][three[0]]) {
					nonMaxSupp[three[1]][three[0]] = 0;
					changed = true;
				}
			}
			if(three[1] > 0 && three[0] < cannyEdgeImg[0].length-1) {
				if(cannyEdgeImg[three[1]-1][three[0]+1] > cannyEdgeImg[three[1]][three[0]]) {
					nonMaxSupp[three[1]][three[0]] = 0;
					changed = true;
				}
			}
			if(!changed) {
				nonMaxSupp[three[1]][three[0]] = cannyEdgeImg[three[1]][three[0]];
			}
		}
		return nonMaxSupp;
	}
	/**
	 * This method gives all of the valid neighbor coordinates in a bounded space in a radius of 1.
	 * For example in a space with a width and length of 2, every pixel can only
	 * have 3 valid neighbors as all other neighbors are out sides the range of the space.
	 * An element is considered invalid if it isn't in the boundries of the space.
	 * @param x x coordinate you want to get the neighbors of
	 * @param y y coordinate you want to get the neighbors of
	 * @param width width of the space you are examining
	 * @param height height of the space you are examining
	 * @return a Set of the valid coordinates represented in arrays of length 2 where the first
	 * index is x and the second is y
	 */
	public static Set<int[]> getValidCoords3x3(int x, int y, int width, int height){
		Set<int[]> coords = new HashSet<int[]>();
		if(x > 0) {
			coords.add(new int[]{x-1,y});
			if(y > 0) {
				coords.add(new int[] {x-1,y-1});
			}
			if(y < height-1) {
				coords.add(new int[] {x-1,y+1});
			}
		}
		if(x < width-1) {
			coords.add(new int[] {x+1,y});
			if(y > 0) {
				coords.add(new int[] {x+1,y-1});
			}
			if(y < height-1) {
				coords.add(new int[] {x+1,y+1});
			}
		}
		if(y > 0) {
			coords.add(new int[] {x,y-1});
		}
		if(y < height-1) {
			coords.add(new int[] {x,y+1});
		}
		return coords;
	}
	/**
	 * This method gives all of the valid neighbor coordinates in a bounded space in a floor(n/2) radius
	 * from the coordinate (x, y), where the distance is defined as how far away something is normally and diagnollay. 
	 * Better explanation: a coordinate the is diagnoally touching an element is the same distance as a coordinate 
	 * to the left of an element. An element is considered invalid if it isn't in the boundries of the space.
	 * @param x the x coordinate you want to examine
	 * @param y the y coordinate you want to examine
	 * @param width the width of the bounded space
	 * @param height the height of the bounded space
	 * @param n the 
	 * @return a Set containing all of the valid coordinates in a floor(n/2) radius from x and y
	 */
	public static Set<int[]> getValidCoordsNxN(int x, int y, int width, int height, int n){
		int ndiv2 = n/2;
		Set<int[]> valid = new HashSet<int[]>();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(x-ndiv2+i >= 0 && x-ndiv2+i < width) {
					if(y-ndiv2+j >= 0 && y-ndiv2+j < height) {
						if(x-ndiv2+i != x || y-ndiv2+j!=y) {
							valid.add(new int[] {x-ndiv2+i,y-ndiv2+j});
						}
					}
				}
			}
		}
		return valid;
	}
	/**
	 * Overloaded hysterersis method for default low value of 10% and default high value of 60%.
	 * @param greyimg the 2d greyscale array you want to perform hysterersis on.
	 * @return A new 2d greyscale array with hysterersis performed.
	 */
	public static int[][] hysteresis(int[][] greyimg){
		return hysteresis(greyimg, 10, 60);
	}
	/**
	 * This method performs hysterersis on a greyscale image represented as a 2d Array.
	 * Hysterersis: https://en.wikipedia.org/wiki/Hysteresis
	 * Hysterersis is the process of finding uncertain edge intensities and determining whether they
	 * are neighbors to true edges, if they are then they are also likely true edges so they will be set to true edges.
	 * If no true edge neighbor is found they are likely not true edges so they will be set to not edges.
	 * @param greyimg greyimg the 2d greyscale array you want to perform hysterersis on.
	 * @param percentLow the threshold for false edges.
	 * @param percentHigh the threshold for true edges
	 * @return A new 2d greyscale array with hysterersis performed.
	 */
	public static int[][] hysteresis(int[][] greyimg, int percentLow, int percentHigh){
		int[][] hysterized = new int[greyimg.length][greyimg[0].length];
	
		int highThresh = (int)(255 - (255*(percentHigh/100.0)));
		int lowThresh = (int)(0 + (255*(percentLow/100.0)));
		
		//determining false edges and true edges based on our high and low thresholds.
		for(int y = 0; y < greyimg.length; y++) {
			for(int x = 0; x <greyimg[0].length; x++) {
				int currPixel = greyimg[y][x];
				if(currPixel > highThresh) {
					currPixel = 255;
				}
				if(currPixel < lowThresh) {
					currPixel = 0;
				}
				hysterized[y][x] = currPixel;
			}
		}
		
		//finding all uncertain edges
		Set<int[]> uncertainEdges = new HashSet<int[]>();
		for(int y = 0; y < hysterized.length; y++) {
			for(int x = 0; x <hysterized[0].length; x++) {
				int currPixel = hysterized[y][x];
				if(currPixel < lowThresh) {
					hysterized[y][x] = 0;
				}
				if(currPixel < highThresh && currPixel > lowThresh) {
					boolean adjTrueEdge = false;
					for(int[] coords : getValidCoords3x3(x, y, hysterized[0].length, hysterized.length)) {
						//uncomment this line to make it 8-connect instead of 4
						if(Math.abs(coords[0] + coords[1] - x - y) == 1) {
							if(hysterized[coords[1]][coords[0]] > highThresh) {
								adjTrueEdge = true;
								//hysterized[coords[1]][coords[0]]=255;
							}
						}
					}
					if(adjTrueEdge) {
						hysterized[y][x] = 255;//Converters.overFlowCheck(highThresh+1);
					}
					else {
						uncertainEdges.add(new int[] {x,y});
					}
				}
			}
		}
		//determining whether a uncertain edge is near a true edge.
		Set<int[]> remove = new HashSet<int[]>();
		boolean found = true;
		while(uncertainEdges.size() > 0 && found) {
			found = false;
			for(int[] edges : uncertainEdges) {
				boolean adjTrueEdge = false;
				boolean adjUncertainEdge = false;
				for(int[] coords : getValidCoords3x3(edges[0], edges[1], hysterized[0].length, hysterized.length)) {

					if(Math.abs(coords[0]- edges[0]) + Math.abs(coords[1]- edges[1]) == 1) {
						if(hysterized[coords[1]][coords[0]] > highThresh) {

							adjTrueEdge = true;
							found = true;
						}
						else if(hysterized[coords[1]][coords[0]] > lowThresh) {
							adjUncertainEdge = true;
						}
					}
				}
				if(adjTrueEdge) {
					hysterized[edges[1]][edges[0]] = 255; //Converters.overFlowCheck(highThresh+1);
					remove.add(edges);
				}
				else {
					if(!adjUncertainEdge) {
						hysterized[edges[1]][edges[0]] = 0;
						remove.add(edges);
					}
				}
			}
			uncertainEdges.removeAll(remove);
			remove.clear();
		}
		for(int[] edges: uncertainEdges) {
			hysterized[edges[1]][edges[0]] = 0;
		}
		return hysterized;
	}
	/**
	 * This methods performs a 5x5 Gaussian filter: https://en.wikipedia.org/wiki/Gaussian_filter
	 * on a 2d greyscale image represented as an array. The Gaussian filter will attempt to smooth our noise
	 * in our image, generally making image processing more accurate.
	 * @param greyimg the 2d greyscale array you want to perform the filter on.
	 * @return a 2d greyscale array with the filter applied.
	 */
	public static int[][] GaussianFilter(int[][] greyimg){
		int[][] filtered = new int[greyimg.length][greyimg[0].length];
		//easier to have it stored then computing each index for every pixel in the image
		int[][] fiveFilter = new int[5][5];
		fiveFilter[0] = new int[]{2,4,5,4,2};
		fiveFilter[1] = new int[]{4,9,12,9,4};
		fiveFilter[2] = new int[]{5,12,15,12,5};
		fiveFilter[3] = new int[]{4,9,12,9,4};
		fiveFilter[4] = new int[]{2,4,5,4,2};
		
		for(int y = 0; y < greyimg.length; y++) {
			for(int x = 0; x < greyimg[0].length; x++) {
				int total = 15*greyimg[y][x];
				for(int[] coord : getValidCoordsNxN(x, y, greyimg[0].length, greyimg.length, 5)) {
					total += fiveFilter[coord[0] - x + 2][coord[1] - y + 2] * greyimg[coord[1]][coord[0]];
				}
				total /= 159;
				filtered[y][x] = total;
			}
		}
		return filtered;
	}
}
