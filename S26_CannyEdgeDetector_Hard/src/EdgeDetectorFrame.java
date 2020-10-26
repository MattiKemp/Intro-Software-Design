import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * The GUI Frame we use to display and manage our image processing capabilites.
 * @author Matthew Kemp
 */
public class EdgeDetectorFrame extends JFrame{
	/**
	 * The unprocessed image, will never be processed.
	 */
	private BufferedImage image;
	/**
	 * The edge detected processed image. Has display precedence over the 
	 * unprocessed image.
	 */
	private BufferedImage detectEdgeImage;
	/**
	 * The hysterersis process image. Has display precedence over all images.
	 */
	private BufferedImage hysImage;
	
	/**
	 * The overloaded paint method for our frame. 
	 * Will load hysImage if it exists, otherwide detectEdgeImage if it exists, and 
	 * if neither exists the default image will be drawn.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(hysImage!=null) {
			g.drawImage(hysImage, 50, 50, null);
		}
		else if(detectEdgeImage!=null) {
			g.drawImage(detectEdgeImage, 50, 50, null);
		}
		else {
			g.drawImage(image, 50, 50, null);
		}
	}
	/**
	 * Sole constructor for our frame, takes a boolean that determines
	 * whether the processing images and sliders will be created.
	 * @param enabled whether the processing elements will be created.
	 */
	public EdgeDetectorFrame(boolean enabled) {
		super("Image");
		setLayout(null);
		if(enabled) {
			JSlider sliderLow = new JSlider();
			sliderLow.setBounds(0, 650, 550, 40);
			sliderLow.setMajorTickSpacing(10);
			sliderLow.setPaintLabels(true);
			sliderLow.setPaintTicks(true);
			JSlider sliderHigh = new JSlider();
			sliderHigh.setBounds(0, 720, 550, 40);
			sliderHigh.setMajorTickSpacing(10);
			sliderHigh.setPaintLabels(true);
			sliderHigh.setPaintTicks(true);
			JLabel slideLabels = new JLabel("^low  high\\/");
			slideLabels.setBounds(250, 690, 100, 40);
			JButton edgeD = new JButton("Edge Detect");
			edgeD.setBounds(0, 600, 200, 50);
			edgeD.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					detectEdgeImage = null;
					hysImage = null;
					edgeDetector();
				}
			});
			JButton cannyD = new JButton("Canny Edge Detect");
			cannyD.setBounds(200, 600, 200, 50);
			cannyD.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					detectEdgeImage = null;
					hysImage = null;
					cannyEdgeDetector();
				}
			});
			JButton hysteresis = new JButton("Hysteresis");
			hysteresis.setBounds(400, 600, 200, 50);
			hysteresis.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					hysImage = null;
					hys(sliderLow.getValue(),sliderHigh.getValue());
				}
			});
			add(sliderLow);
			add(sliderHigh);
			add(slideLabels);
			add(edgeD);
			add(cannyD);
			add(hysteresis);
		}
		setSize(600,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		image = null;
		
	}
	/**
	 * Resets all image values and then sets the default image 
	 * to the input image.
	 * @param image New default image.
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		detectEdgeImage = null;
		hysImage = null;
		repaint();
	}
	/**
	 * Sets the edge detect image to a new image.
	 * @param detectEdgeImage New edge detected image.
	 */
	public void setDetectImage(BufferedImage detectEdgeImage) {
		this.detectEdgeImage = detectEdgeImage;
		repaint();
	}
	/**
	 * Performs edge detection on the default image.
	 */
	public void edgeDetector() {
		if(image == null) {
			return;
		}
		int[][] greyImg = Converters.greyScaleImage(image);
		greyImg = ImageProcessing.cannyEdge(greyImg);
		detectEdgeImage = Converters.convertArrToImg(greyImg);
		repaint();
	}
	/**
	 * performs canny edge detection without hysterersis on the default image
	 */
	public void cannyEdgeDetector() {
		if(image == null) {
			return;
		}
		int[][] greyImg = Converters.greyScaleImage(image);
		greyImg = ImageProcessing.GaussianFilter(greyImg);
		greyImg = ImageProcessing.cannyEdge(greyImg);
		detectEdgeImage = Converters.convertArrToImg(greyImg);
		repaint();
	}
	/**
	 * performs hysterersis processing on the edge detected image.
	 * @param low the percentage threshold for false edges.
	 * @param high the percentage threshold for true edges.
	 */
	public void hys(int low, int high) {
		if(detectEdgeImage == null) {
			return;
		}
		int[][] greyImg = Converters.greyScaleImage(detectEdgeImage);
		greyImg = ImageProcessing.hysteresis(greyImg, low, high);
		hysImage = Converters.convertArrToImg(greyImg);
		repaint();
	}
	/**
	 * main testing method.
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new EdgeDetectorFrame(true);
	}
}
