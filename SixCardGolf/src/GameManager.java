import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
/**
 * A class to represent the logic behind a game of six card golf.
 * rules: https://bicyclecards.com/how-to-play/six-card-golf/
 * @author Matthew Kemp
 *
 */
public class GameManager {
	/**
	 * Enum to represent move possibilites or whether a move is valid or not.
	 * @author bootycheeks
	 *
	 */
	enum Move{
		FLIP, REPLACE, SKIP, VALID, ERROR
	}
	/**
	 * The scores of each player.
	 */
	private int[] scores;
	/**
	 * The current round.
	 */
	private int round;
	/**
	 * A deck of cards.
	 */
	private Set<Card> deck;
	/**
	 * The card taken from the top of the deck.
	 */
	private Card drawFromDeck;
	/**
	 * The stack of discarded cards.
	 */
	private Stack<Card> discard;
	/**
	 * A 2d Array of cards used to represent the board.
	 */
	private Card[][] board;
	/**
	 * A 2D array of Cards used to represent the cards the players can see.
	 */
	private Card[][] shownBoard;
	/**
	 * Whose turn it is.
	 */
	private boolean turn;
	/**
	 * the number of turns that have happened this round.
	 */
	private int turnCount;
	/**
	 * Whether the player can flip another card.
	 */
	private boolean flip;
	
	//private boolean discardCard;
	//private boolean deckCard;
	
	public GameManager() {
		scores = new int[2];
		round = 1;
		deck = new HashSet<Card>();
		drawFromDeck = null;
		discard = new Stack<Card>();
		board = new Card[2][6];
		//initalizing the deck
		resetDeck();
		//initalize the hands
		resetHands();
		turn = true;
		shownBoard = new Card[2][6];
		turnCount = 0;
		flip = true;
		//discardCard = false;
		//deckCard = false;
	}
	/**
	 * Public getter method to get player ones board.
	 * @return player ones board of cards
	 */
	public Card[] getP1Board() {
		return board[0];
	}
	/**
	 * Public getter method to get player twos board.
	 * @return player twos board of cards.
	 */
	public Card[] getP2Board() {
		return board[1];
	}
	/**
	 * Public getter method to get player ones shown cards.
	 * @return player ones shown cards, null if the card is not shown.
	 */
	public Card[] getP1Shown() {
		return shownBoard[0];
	}
	/**
	 * Public getter method to get player twos shown cards
	 * @return player twos shown cards, null if the card is not shown.
	 */
	public Card[] getP2Shown() {
		return shownBoard[1];
	}
	/**
	 * Method to get the values of the top of the discard pile
	 * @return The value of the card on top of the discard pile.
	 */
	public Card peekDiscard() {
		if(discard.size()>0) {
			return discard.peek();
		}
		return null;
	}
	/**
	 * Method to get the value of the card drawn from the top of the deck.
	 * @return the card drawn from the top of the deck
	 */
	public Card getDrawCard() {
		return drawFromDeck;
	}
	/**
	 * Get whether the current players moving can flip another card.
	 * @return whether the player can flip another card.
	 */
	public boolean getFlip() {
		return flip;
	}
	/**
	 * Get whose turn it is.
	 * @return whose turn it is.
	 */
	public boolean getTurn() {
		return turn;
	}
	/**
	 * Get the number of turns that have happened this round.
	 * @return the number of turns that have happened this round.
	 */
	public int getTurnCount() {
		return turnCount;
	}
	/**
	 * Get players ones scores.
	 * @return player ones score.
	 */
	public int getP1Score() {
		return scores[0];
	}
	/**
	 * Get players two score
	 * @return player twos score
	 */
	public int getP2Score() {
		return scores[1];
	}
	
//	public boolean getDiscard() {
//		return discardCard;
//	}
	
//	public boolean getDeckCard() {
//		return deckCard;
//	}
	/**
	 * Get the current round number
	 * @return the current round number
	 */
	public int getRound() {
		return round;
	}
	
//	public void setNotDiscard() {
//		discardCard = false;
//	}
	
//	public void setDiscard() {
//		discardCard = true;
//	}
	
//	public void setNotDeckCard() {
//		deckCard = false;
//	}
	
//	public void setDeckCard() {
//		deckCard = true;
//	}
	/**
	 * Method to reset the deck
	 */
	public void resetDeck() {
		discard = new Stack<Card>();
		drawFromDeck = null;
		deck = new HashSet<Card>();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 12; j++) {
				switch(i) {
				case 0:
					deck.add(new Card(j+1,Card.CardSuite.DIAMONDS));
					break;
				case 1:
					deck.add(new Card(j+1,Card.CardSuite.CLUBS));
					break;
				case 2:
					deck.add(new Card(j+1,Card.CardSuite.HEARTS));
					break;
				case 3:
					deck.add(new Card(j+1,Card.CardSuite.SPADES));
					break;
				}
			}
		}
	}
	/**
	 * Method to reset each players hands.
	 */
	public void resetHands() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 6; j++) {
				liftTopCard();
				board[i][j] = drawFromDeck;
				drawFromDeck = null;
			}
		}
	}
	/**
	 * Method to lift the top card from the deck.
	 */
	public void liftTopCard() {
		if(drawFromDeck == null) {
			Card randCard;
			int rand = (int)(Math.random()*deck.size());
			int i = 0;
			for(Card e : deck) {
				if(i==rand) {
					randCard = e;
					deck.remove(e);
					drawFromDeck = randCard;
					return;
				}
				i++;
			}
		}
	}
	/**
	 * Method add the card drawn from the top of the deck to the discard pile.
	 */
	public void addDrawCardToDiscard() {
		if(drawFromDeck != null) {
			discard.add(drawFromDeck);
			drawFromDeck = null;
		}
	}
	/**
	 * Method to handle when players flip cards.
	 * @param spot the spot the player wants to attempt flip.
	 * @return whether the move was valid.
	 */
	public Move flip(int spot) {
		if(turnCount < 2 && spot < 6) {
			if(flip==true) {
				System.out.println("first flipped!");
				System.out.println(shownBoard[turnCount][spot]);
				shownBoard[turnCount][spot] = board[turnCount][spot];
				System.out.println(shownBoard[turnCount][spot]);
				flip=false;
				return Move.VALID;
			}
			else if(flip==false) {
				if(shownBoard[turnCount][spot]==null) {
					shownBoard[turnCount][spot] = board[turnCount][spot];
					flip = true;
					turnCount++;
					turn = !turn;
					return Move.VALID;
				}
				else {
					return Move.ERROR;
				}
			}
		}
		return Move.ERROR;
	}
	/**
	 * Method to handle when a player wants to replace a card with the top
	 * card on the discard pile
	 * @param spot the spot of the card they want to replace.
	 * @return whether the move was valid.
	 */
	public Move replaceDiscard(int spot) {
		if(discard.size() == 0) {
			return Move.ERROR;
		}
		if(turnCount >= 2 && spot < 6) {
			if(shownBoard[turnCount%2][spot]==null) {
				Card swapped = board[turnCount%2][spot];
				board[turnCount%2][spot] = discard.pop();
				shownBoard[turnCount%2][spot] = board[turnCount%2][spot];
				discard.add(swapped);
				turnCount++;
				turn = !turn;
				return Move.VALID;
			}
			return Move.ERROR;
		}
		return Move.ERROR;
	}
	/**
	 * Method to handle when a player wants to replace a card with the card draw from the top of the deck
	 * @param spot the spot of the card they want to replace
	 * @return whether the move was valid
	 */
	public Move replaceFromDeck(int spot) {
		if(drawFromDeck == null) {
			return Move.ERROR;
		}
		if(turnCount >= 2 && spot < 6) {
			if(shownBoard[turnCount%2][spot]==null) {
				Card swapped = board[turnCount%2][spot];
				board[turnCount%2][spot] = drawFromDeck;
				shownBoard[turnCount%2][spot] = board[turnCount%2][spot];
				drawFromDeck = null;
				discard.add(swapped);
				turnCount++;
				turn = !turn;
				return Move.VALID;
			}
			return Move.ERROR;
		}
		return Move.ERROR;
	}
	/**
	 * Method to handle when a player wants to skip their turn by placing the card 
	 * drawn from the top of the deck into the discard pile.
	 * @return whether the move was valid
	 */
	public Move skipTurn() {
		if(drawFromDeck == null) {
			return Move.ERROR;
		}
		addDrawCardToDiscard();
		turnCount++;
		turn = !turn;
		return Move.VALID;
	}
	/**
	 * Method to get the current moves this player can make.
	 * @return Whether the player must replace or flip
	 */
	public Move getValidMove() {
		if(turnCount >= 2) {
			return Move.REPLACE;
		}
		return Move.FLIP;
	}
	/**
	 * Method to get whether the game is over or not.
	 * @return true if the game is over, false otherwise
	 */
	public boolean getStatus() {
		if(round < 11) {
			return true;
		}
		return false;
	}
	/**
	 * Method to get which player won the game based on the lowest score
	 * @return which player had the lowest score.
	 */
	public int getWinner() {
		if(scores[0] < scores[1]) {
			return 0;
		}
		else if(scores[0] > scores[1]) {
			return 1;
		}
		return-1;
	}
	/**
	 * Method to check if this round is over or not, if so change the score
	 * @return whether the round is over.
	 */
	public boolean checkTurn() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 6; j++) {
				if(shownBoard[i][j]==null) {
					return false;
				}
			}
		}
		//check coloumns
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 3; j++) {
				if(shownBoard[i][j].getNumber()==shownBoard[i][j+3].getNumber()) {
					shownBoard[i][j] = null;
					shownBoard[i][j+3] = null;
				}
			}
		}
		//sum remaining cards
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 6; j++) {
				if(shownBoard[i][j]!=null) {
					int number = shownBoard[i][j].getNumber();
					if(number==2) {
						scores[i]+=-2;
					}
					else if(number==11 || number ==12) {
						scores[i]+=10;
					}
					else {
						scores[i]+=number;
					}
				}
			}
		}
		turnCount = 0;
		turn = !turn;
		round++;
		System.out.println("New Round!");
		resetDeck();
		resetHands();
		shownBoard = new Card[2][6];
		return true;
	}
}
