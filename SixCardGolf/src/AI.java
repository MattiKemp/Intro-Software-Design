import java.util.ArrayList;

/**
 * An ai that can pass a turing test by simulating
 * being an actual player. This would be useful if
 * you where trying to convince people/the server you are actually
 * playing the game. Don't think i'll have enough time for this :(
 * @author Matthew Kemp
 *
 */
public class AI {
	private Card[] oppBoard;
	private Card discard;
	private Card deck;
	private Card[] yourBoard;
	private ArrayList<Card> seenCards;
	public AI() {
		oppBoard = new Card[6];
		discard = null;
		deck = null;
		yourBoard = new Card[6];
		seenCards = new ArrayList<Card>();
	}
	
	public void parseBoard(String n) {
		
	}
	
	
}
