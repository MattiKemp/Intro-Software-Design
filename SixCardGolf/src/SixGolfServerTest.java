
import javax.swing.JFrame;
/**
 * Class to test and drive the server.
 * @author Matthew Kemp
 *
 */
public class SixGolfServerTest
{
	/**
	 * Main tester and driver method for the server.
	 * @param args
	 */
   public static void main(String[] args)
   {
	   //Guide to testing this program:
	   //run this main method and then run two SixGolfClientTest.
	   //Your cards are on the bottom of the screen and the oppoents cards
	   //are on the top of the screen. The left middle card is the discard pile
	   //and the right most middle card is to draw from the top of the deck.
	   //the middle card is the deck but has no use other then looks.
	   //there are 11 rounds programed.
	   //To flip a card, simply click the card, if you want to replace a card
	   //click either the discard pile or the draw from top of deck card and
	   // then click the card you want to replace.
	   //You can 'skip' your turn by clicking the draw from top of deck and
	   //then clicking the discard pile (moves drawn card to discard pile).
      SixGolfServer application = new SixGolfServer();
      application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      application.execute();
   } 
}
