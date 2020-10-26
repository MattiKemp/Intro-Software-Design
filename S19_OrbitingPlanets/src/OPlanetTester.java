import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * the main logic for the event handles and gui creation.
 * @author Matthew Kemp
 *
 */
public class OPlanetTester extends JFrame{
	public OPlanetPanel planets;
	/**
	 * constructor that initalizes our star system panel and event
	 * handlers.
	 */
	public OPlanetTester(){
		super("Star System");
		this.planets = new OPlanetPanel();
		add(planets,BorderLayout.CENTER);
		MouseHandler mouseHandler = new MouseHandler();
		planets.addMouseListener(mouseHandler);
		KeyHandler keyHandler = new KeyHandler();
		addKeyListener(keyHandler);
	}
	/**
	 * main method for the program. Creates our GUI.
	 * @param args
	 */
	public static void main(String[] args) {
		OPlanetTester orbitingPlanets = new OPlanetTester();
		orbitingPlanets.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		orbitingPlanets.setSize(800,800);
		orbitingPlanets.setVisible(true);
	}
	/**
	 * The logic for our mouse listener, appends a new planet to the current
	 * position of the users mouse.
	 * @author Matthew Kemp
	 *
	 */
	private class MouseHandler implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent event) {
			int hypot = (int) Math.sqrt((Math.pow(event.getY()-planets.getYCenter(),2) + Math.pow(event.getX()-planets.getXCenter(),2)));
			int degree = 0;
			if(event.getX()-planets.getYCenter()>=0) {
				degree = (int) Math.toDegrees(Math.asin(((double)(event.getY()-planets.getYCenter()))/hypot));
			}
			else {
				degree = 180-(int) Math.toDegrees(Math.asin(((double)(event.getY()-planets.getYCenter()))/hypot));
			}
			planets.setNewBody(new BadBody(event.getX(),event.getY(),400,400,(int)(Math.random()*20+5),degree),0);
		}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {	}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	/**
	 * The logic for our key listener, checks if the user pressed 1-8, if so
	 * add a new moon to the key pressed(1-8) planet that was created.
	 * @author Matthew Kemp
	 *
	 */
	private class KeyHandler implements KeyListener{
		@Override
		public void keyTyped(KeyEvent e) {			
		}
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()<57 && e.getKeyCode()>48) {
				planets.setNewBody(null, e.getKeyCode()-48);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {			
		}
	}
}
