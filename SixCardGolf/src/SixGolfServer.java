
import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
/**
 * A Multi-threaded server that allows clients to connect and play
 * a game of modified six card golf
 * @author Matthew Kemp	
 *
 */
public class SixGolfServer extends JFrame 
{
	/**
	 * A JTextArea to display what is currently happening with the server.
	 */
   private JTextArea outputArea; // for outputting moves
   /**
    * Enum to define a Player 1 and Player 2
    * @author Matthew Kemp
    *
    */
   public enum PlayerID{
	   PLAYER_1, PLAYER_2
   }
   /**
    * An array of size 2 that contains the class Player that represents and manages
    * each player currently in the game.
    */
   private Player[] players; // array of Players
   /**
    * Sever Socket to connect with clients.
    */
   private ServerSocket server; // server socket to connect with clients
   /**
    * Keeps track of whos turn it is.
    */
   private int currentPlayer; // keeps track of player with current move
   /**
    * Final int that defines the value of Player 1
    */
   private final static int PLAYER_X = 0; // constant for first player
   /**
    * Final int that defines the value of Player 2
    */
   private final static int PLAYER_O = 1; // constant for second player
   /**
    * A final array that defines the values for each player's mark
    * This isn't really used and is mainly leftover from the Tic Tac Toe Server.
    */
   private final static String[] MARKS = {"X", "O"}; // array of marks
   /**
    * ExecutorService that creates each players thread and then runs them.
    */
   private ExecutorService runGame; // will run players
   /**
    * A lock used to keep the game synchronised.
    */
   private Lock gameLock; // to lock game for synchronization
   /**
    * A condition used to inform the first player that the second player
    * has connected.
    */
   private Condition otherPlayerConnected; // to wait for other player
   /**
    * A condition that is used to communicate that it is the other players turn.
    */
   private Condition otherPlayerTurn; // to wait for other player's turn
   /**
    * GameManager that simulates a game of six card golf.
    */
   private GameManager game;
   // set up tic-tac-toe server and GUI that displays messages
   /**
    * No argument constructor for the server.
    */
   public SixGolfServer()
   {
      super("Six-Card-Golf"); // set title of window
      // create ExecutorService with a thread for each player
      runGame = Executors.newFixedThreadPool(2);
      gameLock = new ReentrantLock(); // create lock for game

      // condition variable for both players being connected
      otherPlayerConnected = gameLock.newCondition();

      // condition variable for the other player's turn
      otherPlayerTurn = gameLock.newCondition();   
      
      game = new GameManager();
      
//      for (int i = 0; i < 9; i++)
//         board[i] = new String(""); // create tic-tac-toe board
      
      players = new Player[2]; // create array of players
      currentPlayer = 0; // set current player to first player
      try
      {
         server = new ServerSocket(12345, 2); // set up ServerSocket
      } 
      catch (IOException ioException) 
      {
         ioException.printStackTrace();
         System.exit(1);
      } 

      outputArea = new JTextArea(); // create JTextArea for output
      add(outputArea, BorderLayout.CENTER);
      outputArea.setText("Server awaiting connections\n");

      setSize(300, 300); // set size of window
      setVisible(true); // show window
   }

   // wait for two connections so game can be played
   /**
    * method that executes each players thread.
    */
   public void execute()
   {
      // wait for each client to connect
      for (int i = 0; i < players.length; i++) 
      {
         try // wait for connection, create Player, start runnable
         {
            players[i] = new Player(server.accept(), i);
            runGame.execute(players[i]); // execute player runnable
         } 
         catch (IOException ioException) 
         {
            ioException.printStackTrace();
            System.exit(1);
         } 
      }
      //System.out.println("-1 game locked");
      gameLock.lock(); // lock game to signal player X's thread

      try
      {
    	  //System.out.println("resuming: " + currentPlayer);
         players[PLAYER_X].setSuspended(false); // resume player X
         //System.out.println("woke up: " + currentPlayer);
         otherPlayerConnected.signal(); // wake up player X's thread
      } 
      finally
      {
    	  //System.out.println("game being unlocked for: " + currentPlayer);
         gameLock.unlock(); // unlock game after signalling player X
      } 
   }

   // display message in outputArea
   /**
    * Method to display a message to the server text field.
    * @param messageToDisplay
    */
   private void displayMessage(final String messageToDisplay)
   {
      // display message from event-dispatch thread of execution
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() // updates outputArea
            {
               outputArea.append(messageToDisplay); // add message
            } 
         } 
      ); 
   } 

   // determine if move is valid
   /**
    * Checks if a turn is valid, if so change turns.
    * @param location the location on the board being selected
    * @param player the player making the move.
    * @return
    */
   public int validateAndMove(int location, Player player)
   {
	   //System.out.println("player: " + player.getPlayerNumber() + " currentPlayer: " + currentPlayer);
      // while not current player, must wait for turn
      while (player.getPlayerNumber() != currentPlayer) 
      {
    	 //System.out.println("1 game locked for:" + player.getPlayerNumber());
         gameLock.lock(); // lock game to wait for other player to go

         try 
         {
            otherPlayerTurn.await(); // wait for player's turn
            //System.out.println("other player signal recieved for: " + player.getPlayerNumber());
         } 
         catch (InterruptedException exception)
         {
            exception.printStackTrace();
         } 
         finally
         {
        	 //System.out.println("1 game unlocked for:" + player.getPlayerNumber());
            gameLock.unlock(); // unlock game after waiting
         } 
      } 

      if(location == -1) {
		   return -2;
	   }
      
      // if location not occupied, make move
      boolean flip = game.getFlip();
      System.out.println(game.getTurn());
      if (validateMove(location,player.getDiscSelected(),player.getDeckSelected(),player))
      {
    	  if(game.checkTurn()) {
    		  players[0].sendScore();
    		  players[1].sendScore();
    		  players[0].sendRound();
    		  players[1].sendRound();
    		  if(player.getPlayerNumber()==0) {
    			  player.reset();
    			  players[0].roundOverPlayer0();
    			  players[1].roundOverPlayer1();
    			  return 3;
    		  }
    		  else {
    			  players[0].roundOverPlayer0();
    			  players[1].roundOverPlayer1();
    			  currentPlayer = (currentPlayer + 1) % 2;
    			  gameLock.lock();
    			  try 
    		         {
    		        	 //System.out.println("signalling otherPlayer: ");
    		            otherPlayerTurn.signal(); // signal other player to continue
    		         } 
    		         finally
    		         {
    		        	 //System.out.println("2 game unlocked for:" + currentPlayer);
    		            gameLock.unlock(); // unlock game after signaling
    		         } 
    			  return 3;
    		  }
    	  }
    	  //System.out.println(game.getP1Score());
    	  //System.out.println(game.getP2Score());
    	  //System.out.println("Valid move for " + player.getPlayerNumber());
    	  if(game.getFlip() != true) {
    		  displayMessage("flip go again"+"\n");
    		  return 0;
    	  }
    	  else if(player.deckSelected == true) {
    		  displayMessage("deck card selected, make replacement selection!\n");
    		  return 2;
    	  }
    	  else if(player.discSelected == true) {
    		  displayMessage("discard card selected, make replacement selection!\n");
    		  return 2;
    	  }
    	  
         currentPlayer = (currentPlayer + 1) % 2; // change player

         // let new current player know that move occurred
         players[currentPlayer].otherPlayerMoved();

         //System.out.println("2 game locked for:" + currentPlayer);
         gameLock.lock(); // lock game to signal other player to go

         try 
         {
        	 //System.out.println("signalling otherPlayer: ");
            otherPlayerTurn.signal(); // signal other player to continue
         } 
         finally
         {
        	 //System.out.println("2 game unlocked for:" + currentPlayer);
            gameLock.unlock(); // unlock game after signaling
         } 

         return 1; // notify player that move was valid
      } 
      else // move was not valid
         return -1; // notify player that move was invalid
   }

   /**
    * Determines whether a move is valid, if so update the game accordingly.
    * @param location the location on the board that is selected.
    * @param descard whether the player had previously selected the descard pile.
    * @param fromDeck whether the player had previously selected the top card on the deck.
    * @param player the player making the move.
    * @return
    */
   public boolean validateMove(int location, boolean descard, boolean fromDeck, Player player) {
	   if(!descard && !fromDeck) {
		   if(game.getTurnCount() >= 2 && location > 5) {
			   if(location == 6) {
				   player.flipDiscSelect();
				   return true;
			   }
			   player.flipDeckSelected();
			   game.liftTopCard();
			   return true;
		   }
		   if(game.flip(location)==GameManager.Move.VALID) {
			   displayMessage("flipped"+"\n");
			   return true;
		   }
		   return false;
	   }
	   else if(descard) {
		   if(location < 6 && game.replaceDiscard(location)==GameManager.Move.VALID) {
			   player.flipDiscSelect();
			   return true;
		   }
		   else {
			   player.flipDiscSelect();
			   return false;
		   }
	   }
	   else if(fromDeck) {
		   if(location == 6) {
			   game.skipTurn();
			   player.flipDeckSelected();
			   return true;
		   }
		   else if(game.replaceFromDeck(location)==GameManager.Move.VALID) {
			   player.flipDeckSelected();
			   return true;
		   }
		   player.flipDeckSelected();
		   return false;
	   }
	   return false;
   }
   /**
    * Method that reads the value of a cards suite and returns a string 
    * number representation.
    * @param n the card to convert.
    * @return the number representation of the input cards suite.
    */
   public String convSuite(Card n) {
	   switch(n.getCardSuite()) {
		   case SPADES:
			   return "0,";
		   case HEARTS:
			   return "1,";
		   case DIAMONDS:
			   return "2,";
		   default:
			   return "3,";
	   }
   }
   /**
    * Convert the current game board to a compact string for sending to 
    * the clients.
    * @param player the player who wants the board.
    * @return a string representation of the board that is easy to work with.
    */
   public String getBoard(boolean player) {  
	   Card[][] FullBoard = new Card[2][6];
	   Card discard = game.peekDiscard();
	   Card drawFromDeck = game.getDrawCard();
	   String board = "";
	   //true player is the top player in GameManager
	   if(player) {
		   FullBoard[0] = game.getP1Shown();
		   FullBoard[1] = game.getP2Shown();
	   }
	   else {
		   FullBoard[0] = game.getP2Shown();
		   FullBoard[1] = game.getP1Shown();
	   }

	   for(int j = 0; j < 6; j++) {
		   if(FullBoard[0][j]==null) {
			   board += ":,";
		   }
		   else {
			   board += FullBoard[0][j].getNumber()+":";
			   board += convSuite(FullBoard[0][j]);
		   }
	   }
	   if(discard == null) {
		   board += ":,";
	   }
	   else {
		   board += discard.getNumber()+":";
		   board += convSuite(discard);
	   }
	   board += ":,";
	   if(drawFromDeck==null) {
		   board += ":,";
	   }
	   else {
		   board += drawFromDeck.getNumber()+":";
		   board += convSuite(drawFromDeck);
	   }
	   for(int j = 0; j < 6; j++) {
		   if(FullBoard[1][j]==null) {
			   board += ":,";
		   }
		   else {
			   board += FullBoard[1][j].getNumber()+":";
			   board += convSuite(FullBoard[1][j]);
		   }
	   }
	   return board;
	   
   }

   /**
    * A method that based on the current state of the game, returns the 
    * valid moves the current player can make.
    * @return
    */
   public String getValidMoves() {
	   String valid = "";
	   if(game.getValidMove()==GameManager.Move.FLIP) {
		   valid += "Flip";
	   }
	   else {
		   valid += "Replace or Select";
	   }
	   return valid;
   }
   
   /**
    * A method that returns a string representation of the current scores 
    * that is easy to work with.
    * @param player the player requesting the scores.
    * @return the string representation of the current scores orgranized based on who is requesting the scores.
    */
   public String getScores(boolean player) {
	   if(player) {
		   return game.getP1Score() + ":" + game.getP2Score(); 
	   }
	   return game.getP2Score() + ":" + game.getP1Score(); 
   }

   // private inner class Player manages each Player as a runnable
   /**
    * private class that represents the players, their connections, and their currents states.
    * It also handles a lot of the input/output logic.
    * @author Matthew Kemp
    *
    */
   private class Player implements Runnable 
   {
	   /**
	    * This players Socket.
	    */
      private Socket connection; // connection to client
      /**
       * This players scanner for inputs.
       */
      private Scanner input; // input from client
      /**
       * This players formatter for outputs
       */
      private Formatter output; // output to client
      /**
       * this players server assigned player number.
       */
      private int playerNumber; // tracks which player this is
      /**
       * This players designated mark.
       */
      private String mark; // mark for this player
      /**
       * Whether this player is currently suspended.
       */
      private boolean suspended = true; // whether thread is suspended
      /**
       * whether the player has previously selected the discard pile
       */
      private boolean discSelected;
      /**
       * whther the player has previously selected the top of the deck card.
       */
      private boolean deckSelected;
      // set up Player thread
      /**
       * Constructor for a Player class
       * @param socket This players socket.
       * @param number This players number.
       */
      public Player(Socket socket, int number)
      {
    	 discSelected = false;
    	 deckSelected = false;
         playerNumber = number; // store this player's number
         mark = MARKS[playerNumber]; // specify player's mark
         connection = socket; // store socket for client
         
         try // obtain streams from Socket
         {
            input = new Scanner(connection.getInputStream());
            output = new Formatter(connection.getOutputStream());
         } 
         catch (IOException ioException) 
         {
            ioException.printStackTrace();
            System.exit(1);
         } 
      }
      /**
       * Public getter method for the players number.
       * @return The players assigned number.
       */
      public int getPlayerNumber() {
    	  return playerNumber;
      }
      
      /**
       * The public getter method for whether the player has selected the discard pile.
       * @return whether player is currently selected the discard pile
       */
      public boolean getDiscSelected() {
    	  return discSelected;
      }
      
      /**
       * The public getter method for whether the player has selected the top card from the deck.
       * @return Whether the player is currently selected the top card on the deck.
       */
      public boolean getDeckSelected() {
    	  return deckSelected;
      }
      /**
       * Public method to flip the current value of whether the player has selected the discard pile
       */
      public void flipDiscSelect() {
    	  discSelected = !discSelected;
      }
      /**
       * Public method to flip the current value of whether the player has selected the top of the deck
       */
      public void flipDeckSelected() {
    	  deckSelected = !deckSelected;
      }
      /**
       * Public method to reset what the player has selected.
       */
      public void reset() {
    	  discSelected = false;
    	  deckSelected = false;
      }
      
      // send message that other player moved
      /**
       * Method to communicate to this players client that the other player has moved. And to update their board.
       */
      public void otherPlayerMoved()
      {
         output.format("Opponent moved: " + getBoard(playerNumber==1) +"\n"); // send location of move
         output.flush(); // flush output
      }
      /**
       * Method to communicate to this players client that the current ronud is over and that they get the first move.
       */
      public void roundOverPlayer0()
      {
         output.format("Round over0: " + getBoard(playerNumber==1) +"\n"); // send location of move
         output.flush(); // flush output
      }
      /**
       * Method to communicate to this players client that the current ronud is over and that they get the second move.
       */
      public void roundOverPlayer1()
      {
         output.format("Round over1: " + getBoard(playerNumber==1) +"\n"); // send location of move
         output.flush(); // flush output
      }
      /**
       * Method to communicate to this players client the current scores of the game.
       */
      public void sendScore()
      {
         output.format("score: " + getScores(playerNumber==0) +"\n"); // send location of move
         output.flush(); // flush output
      }
      /**
       * Method to send the current round number to this players client
       */
      public void sendRound() {
    	  output.format("Round: " + game.getRound() +"\n"); // send location of move
          output.flush(); // flush output
      }
      /**
       * Method to communicate to this players client that the game is over and whether they won or not.
       * @param player
       */
      public void sendVictory(boolean player) {
    	  if(player) {
    		  output.format("Winner: " + 1 + "\n");
              output.flush(); // flush output
    	  }
    	  else {
    		  output.format("Winner: " + 0 + "\n");
              output.flush(); // flush output
    	  }
    	  
      }
      
      // control thread's execution
      /**
       * method that this players thread exectues. Handles all input, output, and player selection logic.
       */
      public void run()
      {
         // send client its mark (X or O), process messages from client
         try 
         {
            displayMessage("Player " + mark + " connected\n");
            output.format("%s\n", mark); // send player's mark
            output.flush(); // flush output

            // if player X, wait for another player to arrive
            if (playerNumber == PLAYER_X) 
            {
               output.format("%s\n%s", "Player X connected",
                  "Waiting for another player\n");
               output.flush(); // flush output

               gameLock.lock(); // lock game to  wait for second player

               try 
               {
                  while(suspended)
                  {
                	  //System.out.println(playerNumber + " suspended!");
                     otherPlayerConnected.await(); // wait for player O
                  } 
               }  
               catch (InterruptedException exception) 
               {
                  exception.printStackTrace();
               } 
               finally
               {
            	  //displayMessage("unlocked \n");
            	  //System.out.println("3 game unlocked for:" + playerNumber);
                  gameLock.unlock(); // unlock game after second player
               } 

               // send message that other player connected
               output.format("Other player connected. Your move.\n");
               output.format("Board: " + getBoard(playerNumber==1) +"\n");
               output.flush(); // flush output
            } 
            else
            {
               output.format("Player O connected, please wait\n");
               output.format("Board: " + getBoard(playerNumber==1) +"\n");
               output.flush(); // flush output
            } 

            // while game not over
            //displayMessage("game status:" + game.getStatus() + "\n");
            while (game.getStatus()) 
            {
            //System.out.println("player:" + playerNumber);
               int location = -1; // initialize move location

               int moveOutcome = validateAndMove(location, this);
               
               if (input.hasNext())
                  location = input.nextInt(); // get move location
               	  displayMessage(location+"\n");
               
               moveOutcome = validateAndMove(location, this);
               //displayMessage(moveOutcome+"\n");
               //System.out.println(playerNumber + " moveOutcome:" + moveOutcome);
               // check for valid move
               if (moveOutcome == 1) 
               {
            	  displayMessage("\nboard: ");
                 ///output.format("Valid move.\n"); // notify client
                  output.format("Board: " + getBoard(playerNumber==1) +"\n");
                  output.flush(); // flush output
               }
               
               else if(moveOutcome == 0) {
            	   location = 0; // initialize move location
            	   
            	   displayMessage("\nboard1: ");
            	   output.format("flip: " + getBoard(playerNumber==1) +"\n");
                   output.flush(); // flush output
                   //System.out.println("called");
                   if (input.hasNext())
                      location = input.nextInt();
                   
                   //System.out.println("called?");
                   moveOutcome = validateAndMove(location, this);
                   
                   //System.out.println("Move outcome: " + moveOutcome);
                   
                   if(moveOutcome == 1) {
                	   displayMessage("\nboard2: ");
                       //output.format("Valid move.\n"); // notify client
                       output.format("Board: " + getBoard(playerNumber==1) +"\n");
                       output.flush(); // flush output
                   }
                   else {
                	   output.format("Invalid move: " + getValidMoves() + "\n");
                       output.flush(); // flush output
                   }
               }
               else if(moveOutcome == 2) {
            	   location = 0;
            	   displayMessage("\nboard3: ");
        		   output.format("select: " + getBoard(playerNumber==1) +"\n");
        		   output.flush();
            	   if(input.hasNext()) {
            		   location = input.nextInt();
            	   }
            	   
            	   moveOutcome = validateAndMove(location, this);
            	   if(moveOutcome == 1) {
            		   displayMessage("\nboard3: ");
            		   output.format("Board: " + getBoard(playerNumber==1) +"\n");
                       output.flush(); // flush output
            	   }
            	   else {
            		   output.format("Invalid move: " + getValidMoves() + "\n");
                       output.flush(); // flush output
            	   }
               }
               else if(moveOutcome == -2) {
            	   //wait for other player to await.
               }
               else if(moveOutcome == 3) {
            	   //round over
               }
               else // move was invalid
               {
                  output.format("Invalid move: " + getValidMoves() + "\n");
                  output.flush(); // flush output
               } 
            } 
            //System.out.println("Game ended for: " + playerNumber);
            players[0].sendVictory(game.getWinner()==0);
            players[1].sendVictory(game.getWinner()==1);
         } 
         finally
         {
            try
            {
               connection.close(); // close connection to client
            } 
            catch (IOException ioException) 
            {
               ioException.printStackTrace();
               System.exit(1);
            } 
         } 
      }

      // set whether or not thread is suspended
      /**
       * Method that sets this player to suspended or to not suspended.
       * @param status
       */
      public void setSuspended(boolean status)
      {
         suspended = status; // set value of suspended
      }
   }
}


