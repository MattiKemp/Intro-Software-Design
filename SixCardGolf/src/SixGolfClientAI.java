
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
/**
 * Incomplete class 
 * @author Matthew Kemp
 *
 */
public class SixGolfClientAI extends JFrame implements Runnable 
{
   private JTextField idField; // textfield to display player's mark
   private JTextArea displayArea; // JTextArea to display output
   private JPanel boardPanel; // panel for tic-tac-toe board
   private Rectangle[][] board; // tic-tac-toe board
   private Rectangle currentSquare; // current square
   private Socket connection; // connection to server
   private Scanner input; // input from server
   private Formatter output; // output to server
   private String sixCardGolfHost; // host name for server
   private String myMark; // this client's mark
   private boolean myTurn; // determines which client's turn it is
   private final String X_MARK = "X"; // mark for first client
   private final String O_MARK = "O"; // mark for second client

   //maybe change the seperate boards from rectangles to coordiantes or get rid
   //of them all together.
   private Rectangle[][] oppBoard;
   private JPanel oppPanel;
   private Rectangle[] midBoard;
   private JPanel midPanel;
   private Rectangle[][] playerBoard;
   private JPanel playerPanel;
   
   private JPanel panelCenter;
   
   private int oppScore;
   private int yourScore;
   
   private JLabel oppScoreLabel;
   private JLabel yourScoreLabel;
   
   private int round;
   private JLabel roundLabel;
   // set up user-interface and board
   public SixGolfClientAI(String host)
   { 
	   sixCardGolfHost = host; // set name of server
      displayArea = new JTextArea(4, 30); // set up JTextArea
      displayArea.setEditable(false);
      add(new JScrollPane(displayArea), BorderLayout.SOUTH);

      boardPanel = new JPanel(); // set up panel for squares in board
      boardPanel.setLayout(new GridLayout(5, 3, 0, 0));

      board = new Rectangle[5][3]; // create board

      // loop over the rows in the board
      for (int row = 0; row < board.length; row++) 
      {
         // loop over the columns in the board
         for (int column = 0; column < board[row].length; column++) 
         {
            // create square
            board[row][column] = new Rectangle(" ", row * 3 + column);
            boardPanel.add(board[row][column]); // add square       
         }
      } 

      oppBoard = new Rectangle[2][3];
      oppPanel = new JPanel();
      oppPanel.setLayout(new GridLayout(2, 3, 0, 10));
      for(int i = 0; i < 2; i++) {
    	  for(int j = 0; j < 3; j++) {
    		  oppBoard[i][j] = board[i][j];
    		  oppPanel.add(oppBoard[i][j]);
    	  }
      }
      
      midBoard = new Rectangle[3];
      midPanel = new JPanel();
      midPanel.setLayout(new GridLayout(1, 3, 0, 10));
      for(int j = 0; j < 3; j++) {
    	  midBoard[j] = board[2][j];
    	  midPanel.add(midBoard[j]);
      }
      playerBoard = new Rectangle[2][3];
      playerPanel = new JPanel();
      playerPanel.setLayout(new GridLayout(2, 3, 0, 10));
      for(int i = 0; i < 2; i++) {
    	  for(int j = 0; j < 3; j++) {
    		  playerBoard[i][j] = board[i+3][j];
    		  playerPanel.add(playerBoard[i][j]);
    	  }
      }
      
      idField = new JTextField(); // set up textfield
      idField.setEditable(false);
      add(idField, BorderLayout.NORTH);
      
      round = 1;
      roundLabel = new JLabel("Round: " + round);
      
      oppScoreLabel = new JLabel("Opponent Score: " + oppScore);
      yourScoreLabel = new JLabel("Your Score: " + oppScore);
      
      panelCenter = new JPanel();
      panelCenter.setLayout(new GridLayout(6,1,0,0));
      panelCenter.add(roundLabel);
      panelCenter.add(oppPanel);
      panelCenter.add(oppScoreLabel);
      panelCenter.add(midPanel);
      panelCenter.add(yourScoreLabel);
      panelCenter.add(playerPanel);
      add(panelCenter, BorderLayout.CENTER);
      
      oppScore = 0;
      yourScore = 0;
      
      setSize(300, 900); // set size of window
      setVisible(true); // show window

      startClient();
   }

   // start the client thread
   public void startClient()
   {
      try // connect to server and get streams
      {
         // make connection to server
         connection = new Socket(
            InetAddress.getByName(sixCardGolfHost), 12345);

         // get streams for input and output
         input = new Scanner(connection.getInputStream());
         output = new Formatter(connection.getOutputStream());
      } 
      catch (IOException ioException)
      {
         ioException.printStackTrace();         
      } 

      // create and start worker thread for this client
      ExecutorService worker = Executors.newFixedThreadPool(1);
      worker.execute(this); // execute client
   }

   // control thread that allows continuous update of displayArea
   public void run()
   {
      myMark = input.nextLine(); // get player's mark (X or O)

      SwingUtilities.invokeLater(
         new Runnable() 
         {         
            public void run()
            {
               // display player's mark
               idField.setText("You are player \"" + myMark + "\"");
            } 
         } 
      ); 
            
      myTurn = (myMark.equals(X_MARK)); // determine if client's turn

      // receive messages sent to client and output them
      while (true)
      {
         if (input.hasNextLine())
            processMessage(input.nextLine());
      } 
   }

   // process messages received by client
   private void processMessage(String message)
   {
	   //vaild move occured.
      if (message.substring(0,7).equals("Board: ")) 
      {
       message = message.substring(7);
       processBoardMessage(message);
      } 
      else if(message.substring(0,6).equals("flip: ")) {
    	  message = message.substring(6);
    	  processBoardMessage(message);
    	  displayMessage("Flip another card.\n");
    	  myTurn = true;
      }
      else if(message.substring(0,8).equals("Winner: ")) {
    	  message = message.substring(8);
    	  myTurn = false;
    	  if(message.equals("1")) {
    		  roundLabel.setText("You Won!");
    	  }
    	  else {
    		  roundLabel.setText("You Lost! :(");
    	  }
    	  
      }
      else if(message.substring(0,7).equals("score: ")) {
    	  message = message.substring(7);
    	  yourScore = Integer.parseInt(message.substring(0,message.indexOf(":")));
    	  oppScore = Integer.parseInt(message.substring(message.indexOf(":")+1));
    	  oppScoreLabel.setText("Opponent Score: " + oppScore);
    	  yourScoreLabel.setText("Your Score: " + yourScore);
      }
      else if(message.substring(0,7).equals("Round: ")) {
    	  message = message.substring(7);
    	  round = Integer.parseInt(message);
    	  roundLabel.setText("Round: " + round);
      }
      else if (message.substring(0,8).equals("select: ")) {
    	  message = message.substring(8);
    	  processBoardMessage(message);
    	  displayMessage("Select card to replace.\n");
    	  myTurn = true;
      }
      else if (message.substring(0,7).equals("Invalid")) 
      {
         displayMessage(message + "\n"); // display invalid move
         myTurn = true; // still this client's turn
      }  
      else if (message.length() >= 16 && message.substring(0,16).equals(("Opponent moved: "))) 
      {
    	  message = message.substring(16);
    	  //System.out.println(message);
    	  processBoardMessage(message);              
         displayMessage("Opponent moved. Your turn.\n");
         myTurn = true; // now this client's turn
      }  
      else if (message.length() >= 14 && message.substring(0,13).equals(("Round over0: "))) 
      {
    	  message = message.substring(13);
    	  //System.out.println(message);
    	  processBoardMessage(message);               
          displayMessage("Round Over. Your Move.\n");
          myTurn = true;
      }  
      else if (message.substring(0,13).equals(("Round over1: "))) 
      {
    	  message = message.substring(13);
    	 // System.out.println(message);
    	  processBoardMessage(message);               
          displayMessage("Round Over.\n");
      }  
      else
    	 //System.out.println("didnt process");
         displayMessage(message + "\n"); // display the message
   }
   
   private void processBoardMessage(String n) {
	   for(int i = 0; i < 2; i++) {
		   for(int j = 0; j < 3; j++) {
			   String currentMark = n.substring(0,n.indexOf(","));
	      		 n = n.substring(n.indexOf(",")+1);
	      		 setMark(board[1-i][2-j], currentMark);
		   }
	   }
       for(int i = 0; i < 3; i++) {
      	 for(int j = 0; j < 3; j++) {
      		 String currentMark = n.substring(0,n.indexOf(","));
      		 n = n.substring(n.indexOf(",")+1);
      		 setMark(board[i+2][j], currentMark);
      	 }
       }
   }
   
   // manipulate displayArea in event-dispatch thread
   private void displayMessage(final String messageToDisplay)
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() 
            {
               displayArea.append(messageToDisplay); // updates output
            } 
         } 
      ); 
   } 

   // utility method to set mark on board in event-dispatch thread
   private void setMark(final Rectangle squareToMark, final String mark)
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run()
            {
               squareToMark.setMark(mark); // set mark in square
            } 
         } 
      ); 
   } 

   // send message to server indicating clicked square
   public void sendClickedSquare(int location)
   {
      // if it is my turn
      if (myTurn) 
      {
         output.format("%d\n", location); // send location to server
         output.flush();
         myTurn = false; // not my turn any more
      } 
   }

   // set current Square
   public void setCurrentSquare(Rectangle square)
   {
      currentSquare = square; // set current square to argument
   }

   // private inner class for the squares on the board
   private class Rectangle extends JPanel 
   {
      private String mark; // mark to be drawn in this square
      private int location; // location of square
   
      public Rectangle(String squareMark, int squareLocation)
      {
         mark = squareMark; // set mark for this square
         location = squareLocation; // set location of this square

         addMouseListener(
            new MouseAdapter() 
            {
               public void mouseReleased(MouseEvent e)
               {
            	  if(getSquareLocation() > 5 && getSquareLocation() != 7) {
	                  setCurrentSquare(Rectangle.this); // set current square
	                  //System.out.println(Rectangle.this.mark);
	                  // send location of this square
	                  //System.out.println(getSquareLocation());
	                  int loc = getSquareLocation();
	                  if(loc > 8) {
	                	  loc = loc - 9;
	                  }
	                  sendClickedSquare(loc);
            	  }
               } 
            } 
         ); 
      } 

      // return preferred size of Square
      public Dimension getPreferredSize() 
      { 
         return new Dimension(30, 60); // return preferred size
      }

      // return minimum size of Square
      public Dimension getMinimumSize() 
      {
         return getPreferredSize(); // return preferred size
      }

      // set mark for Square
      public void setMark(String newMark) 
      { 
    	 if(newMark.length() == 1) {
    		 mark = newMark;
    		 repaint();
    		 return;
    	 }
    	 else if(newMark.substring(0,2).equals("1:")) {
     		 newMark = 'A' + newMark.substring(1);
     	 }
    	 else if(newMark.substring(0,2).equals("11")) {
    		 newMark = 'Q' + newMark.substring(2);
    	 }
    	 else if(newMark.substring(0,2).equals("12")) {
    		 newMark = 'K' + newMark.substring(2);
    	 }
    	 if(newMark.substring(newMark.length()-1).equals("0")) {
    		 newMark = newMark.substring(0,newMark.length()-1) + 'S';
    	 }
    	 else if(newMark.substring(newMark.length()-1).equals("1")) {
    		 newMark = newMark.substring(0,newMark.length()-1) + "<3";
    	 }
    	 else if(newMark.substring(newMark.length()-1).equals("2")) {
    		 newMark = newMark.substring(0,newMark.length()-1) + 'D';
    	 }
    	 else {
    		 newMark = newMark.substring(0,newMark.length()-1) + 'C';
    	 }
         mark = newMark; // set mark of square
         repaint(); // repaint square
      }

      // return Square location
      public int getSquareLocation() 
      {
         return location; // return location of square
      }

      // draw Square
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);
         if(mark.equals("") || mark.substring(0,1).equals(":")) {
        	 g.setColor(new Color(255,0,0));
        	 g.fillRect(0, 0, 29, 55);
         }
         else {
        	 if(mark.length() > 2 && mark.substring(mark.length()-2).equals("<3") || mark.substring(mark.length()-1).equals("D")) {
        		 g.setColor(new Color(255,0,0));
        	 }
        	 g.drawRect(0, 0, 29, 55);
         }
         
         g.drawString(mark, 2, 20); // draw mark   
      } 
   }
}

