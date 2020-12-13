import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
/**
 * Class used to manage and display a user interactable gui for Conway's game of life
 * @author Matthew Kemp
 */
public class GameOfLifeFrame extends JFrame{
	/**
	 * The Game of life Panel used to manage and display the game
	 */
	private GameOfLifePanel gameOfLifePanel;
	/**
	 * JButton to simulate the next generation
	 */
	private JButton nextGen;
	/**
	 * JButton to travel west on the coordinate system by one whole displayed region
	 */
	private JButton west;
	/**
	 * JButton to travel North on the coordinate system by one whole displayed region
	 */
	private JButton north;
	/**
	 * JButton to travel east on the coordinate system by one whole displayed region
	 */
	private JButton east;
	/**
	 * JButton to travel south on the coordinate system by one whole displayed region
	 */
	private JButton south;
	/**
	 * JPanel used to manage the top buttons and labels
	 */
	private JPanel northPanel;
	/**
	 * JPanel used to manage the bottoms buttons and labels
	 */
	private JPanel southPanel;
	/**
	 * JLabel used to indicate the coordinate value of the top left of the screen
	 */
	private JLabel topOrigin;
	/**
	 * JLabel used to indicate the coordinate value of the bottom right of the screen
	 */
	private JLabel bottomOrigin;
	/**
	 * JPanel used to manage and organize the game and its navigation buttons
	 */
	private JPanel game;
	/**
	 * JPanel used to manage and organize the tools the user can use (the right buttons and objects)
	 */
	private JPanel tools;
	/**
	 * JTextField used to take user input on the speed the game should be simulated at.
	 */
	private JTextField speed;
	/**
	 * Boolean used to manage whether the game is current running, don't think it is actually used tho lol
	 */
	private boolean running;
	/**
	 * JLabel to display to the user the current generation the game is on.
	 */
	private JLabel gen;
	/**
	 * Timer used to continously simulate generations at a fixed rate
	 */
	private Timer timer;
	/**
	 * ButtonGroup used to manage the object radio buttons the user can spawn in the game.
	 */
	private ButtonGroup objects;
	/**
	 * The object the user has currently selected for spawning
	 */
	private int selectedObject;
	/**
	 * no argument constructor for the game of life frame.
	 */
	public GameOfLifeFrame() {
		super("Game Of Life");
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);
		setSize(700,400);
		running = false;
		game = new JPanel();
		game.setLayout(new BorderLayout());
		tools = new JPanel();
		tools.setLayout(new GridLayout(15,1));
		setupGame(64);
		setupGameButtons();
		setupTools();
		add(game, BorderLayout.CENTER);
		add(tools, BorderLayout.LINE_END);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	/**
	 * Method to setup the tools gui and its logic
	 */
	public void setupTools() {
		timer = new Timer();
		gen = new JLabel();
		updateGen();
		JButton run = new JButton("Run");
		JButton stop = new JButton("Stop");
		JLabel speedText = new JLabel("Time per generation ms:");
		speed = new JTextField();
		JButton zoomin = new JButton("Zoom In");
		JButton zoomout = new JButton("Zoom Out");
		objects = new ButtonGroup();
		JRadioButton cell = new JRadioButton ("cell");
		cell.doClick();
		selectedObject = 0;
		JRadioButton beacon = new JRadioButton ("Beacon");
		JRadioButton glider = new JRadioButton ("Glider");
		JRadioButton gosperGliderGun = new JRadioButton ("G Glider Gun");
		objects.add(cell);
		objects.add(beacon);
		objects.add(glider);
		objects.add(gosperGliderGun);
		
		JLabel subGridSizeText = new JLabel("Subgrid Size NxN:");
		JTextField subGridSize = new JTextField("64");
		JButton apply = new JButton("Apply");
		
		zoomin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.zoomIn();
				updateTopOrigin();
				updateBottomOrigin();
			}
		});
		zoomout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.zoomOut();
				updateTopOrigin();
				updateBottomOrigin();
			}
		});
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.scheduleAtFixedRate(new TimerTask(){
					@Override
					public void run() {
						gameOfLifePanel.nextGen();
						updateGen();
					}
				}, 100, Integer.parseInt(speed.getText()));
			}
		});
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.cancel();
				timer = new Timer();
			}
		});
		cell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedObject = 0;
			}
		});
		beacon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedObject = 1;
			}
		});
		glider.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedObject = 2;
			}
		});
		gosperGliderGun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedObject = 3;
			}
		});
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.remove(gameOfLifePanel);
				setupGame(Integer.parseInt(subGridSize.getText()));
				updateTopOrigin();
				updateBottomOrigin();
				updateGen();
				gameOfLifePanel.reloadSubGrids();
				repaint();
			}
		});
		tools.add(gen);
		tools.add(speedText);
		tools.add(speed);
		tools.add(run);
		tools.add(stop);
		tools.add(zoomin);
		tools.add(zoomout);
		tools.add(cell);
		tools.add(beacon);
		tools.add(glider);
		tools.add(gosperGliderGun);
		tools.add(subGridSizeText);
		tools.add(subGridSize);
		tools.add(apply);
		repaint();
	}
	/**
	 * Method used to setup the game of life and its logic, such as handling if the user clicks in the game
	 * area for spawning objects.
	 * @param n the size of the subgrids NxN
	 */
	public void setupGame(int n) {
		gameOfLifePanel = new GameOfLifePanel(n);
		gameOfLifePanel.setBounds(new Rectangle(50,50,300,300));
		gameOfLifePanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("Mouse clicked:");
				//System.out.println(e.getX() + ":" + e.getY());
				if(selectedObject == 0) {
					gameOfLifePanel.addObject(e.getX(), e.getY(), Objects.getCell());
				}
				else if(selectedObject == 1) {
					gameOfLifePanel.addObject(e.getX(), e.getY(), Objects.getBeacon());
				}
				else if(selectedObject == 2) {
					gameOfLifePanel.addObject(e.getX(), e.getY(), Objects.getGlider());
				}
				else if(selectedObject == 3) {
					gameOfLifePanel.addObject(e.getX(), e.getY(), Objects.getGosperGliderGun());
				}
				//gameOfLifePanel.addCell(e.getX(), e.getY());
				repaint();
				
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		game.add(gameOfLifePanel, BorderLayout.CENTER);
	}
	/**
	 * Method used to setup the game buttons gui and logic.
	 */
	public void setupGameButtons() {
		nextGen = new JButton("Next Gen");
		west = new JButton("W");
		north = new JButton("NORTH");
		east = new JButton("E");
		south = new JButton("SOUTH");
		nextGen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.nextGen();
				updateGen();
			}
		});
		west.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.west();
				updateTopOrigin();
				updateBottomOrigin();
			}
		});
		north.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.north();
				updateTopOrigin();
				updateBottomOrigin();
			}
		});
		east.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.east();
				updateTopOrigin();
				updateBottomOrigin();
			}
		});
		south.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameOfLifePanel.south();
				updateTopOrigin();
				updateBottomOrigin();
			}
		});
		nextGen.setBounds(new Rectangle(0,0,50,25));

		topOrigin = new JLabel();
		bottomOrigin = new JLabel();
		updateTopOrigin();
		updateBottomOrigin();
		northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(topOrigin,BorderLayout.LINE_START);
		northPanel.add(north, BorderLayout.CENTER);
		northPanel.add(nextGen, BorderLayout.LINE_END);
		
		
		southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(south,BorderLayout.CENTER);
		southPanel.add(bottomOrigin,BorderLayout.LINE_END);
		
		game.add(northPanel, BorderLayout.NORTH);
		game.add(west, BorderLayout.LINE_START);
		game.add(east, BorderLayout.LINE_END);
		game.add(southPanel, BorderLayout.SOUTH);
	}
	/**
	 * Method that updates the value of the top origin JLabel
	 */
	public void updateTopOrigin() {
		topOrigin.setText(gameOfLifePanel.getX1() + ":" + gameOfLifePanel.getY1());
	}
	/**
	 * Method that updates the value of the bottom origin JLabel
	 */
	public void updateBottomOrigin() {
		bottomOrigin.setText(gameOfLifePanel.getX2() + ":" + gameOfLifePanel.getY2());
	}
	/**
	 * Method that updates the value of the current generation for the user.
	 */
	public void updateGen() {
		gen.setText("Generation: " + gameOfLifePanel.getGeneration());
	}
	/**
	 * Unused paint method
	 */
	public void paint(Graphics g) {
		super.paint(g);	
	}
	
	/**
	 * Main method to launch the Game Of Life Frame
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new GameOfLifeFrame();
	}
}
