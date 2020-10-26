import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

//Link to background: https://www.pexels.com/photo/photo-of-deep-sky-object-816608/
/**
 * A class that represents a star system on a 2D plane so
 * that it can display it with a simple GUI.
 * @author Matthew Kemp
 */
public class OPlanetPanel extends JPanel{
	private Image image;
	private ArrayList<BodyManager> bodies;
	private int planetCount;
	private int[] moonCount;
	private BadBody[] planets;
	private Timer timer;
	private int xcenter;
	private int ycenter;
	/**
	 * draw the background and bodies
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		for(BodyManager e : bodies) {
			e.getBody().draw(g);
		}
		g.dispose();
	}
	/**
	 * A panel that handles the initalization of our star system and GUI.
	 */
	public OPlanetPanel() {
		super();
		try {
			image = ImageIO.read(new File(System.getProperty("user.dir")+"/background.JPG"));
		} catch (IOException e) {
			System.out.println("Error, no background image found!");
			e.printStackTrace();
		}
		xcenter=400;
		ycenter=400;
		planetCount = 0;
		moonCount = new int[8];
		planets = new BadBody[9];
		bodies = new ArrayList<BodyManager>();
		setNewBody(new BadBody(400,400,400,400,50,0,Color.yellow),0);
		setNewBody(new BadBody(300,300,400,400,20,0,Color.BLUE),0);
		timer = new Timer();
		//set our fps to 60
		timer.scheduleAtFixedRate(new TimerTask(){
			 @Override
			    public void run() {
				 repaint();
			 }},0,16);
	}
	/**
	 * Getter method for the x position of the gravitational center of our star system, primarily
	 * implemented for future expansion on the capabilities of the
	 * physics.
	 * @return xcenter
	 */
	public int getXCenter() {
		return xcenter;
	}
	/**
	 * Getter method for the y position of the gravitational center of our star system, primarily
	 * implemented for future expansion on the capabilities of the
	 * physics.
	 * @return ycenter
	 */
	public int getYCenter() {
		return ycenter;
	}
	/**
	 * A getter method that returns the list of our body managers, primarily implemented
	 * for future expansion of the capabilites of the physics.
	 * @return
	 */
	public ArrayList<BodyManager> getBodies(){
		return bodies;
	}
	/**
	 * The logic to add new planets or moons to the system.
	 * To add a planet have the moon parameter = 0.
	 * If you want to add a moon have the moon parameter be the planet index you want to add.
	 * @param body	BadBody representing the planet/moon
	 * @param moon  number representing what planet(in order of creation) you want to add a moon to.
	 */
	public void setNewBody(BadBody body, int moon) {
		if(planetCount < 8 || moon > 0) {
			BodyManager bodyManager = null;
			if(moon>0) {
				if(moonCount[moon-1]>=2) {
					return;
				}
				else {
					if(planets[moon]==null) {
						return;
					}
					moonCount[moon-1]+=1;
					BadBody planet = planets[moon];
					double size = Math.random()*3+2;
					System.out.println("added");
					BadBody moonBody = new BadBody(planet.getCoords().getX()+(Math.random()*3+ .8)*planet.getSize(),planet.getCoords().getY()+(Math.random()*3+.8)*planet.getSize(),planet.getCoords(),size,0);
					System.out.println(planet.getCoords().getX());
					System.out.println(planet.getCoords().getY());
					bodyManager = new BodyManager(moonBody);
				}
			}
			if(moon==0) {
				int spot = 0;
				while(spot < 8 && planets[spot]!=null) {
					spot+=1;
				}
				planets[spot]=body;
				planetCount+=1;
				bodyManager = new BodyManager(body);
			}
			bodies.add(bodyManager);
			bodyManager.start();
		}
	}
}
