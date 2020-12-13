/**
 * A class to represent basic cards.
 * @author Matthew Kemp
 *
 */
public class Card {
	/**
	 * the number of this card
	 */
	private int number;
	/**
	 * Enum to define the 4 suites a card can have
	 * @author Matthew Kemp
	 *
	 */
	enum CardSuite{
		SPADES, HEARTS, DIAMONDS, CLUBS
	}
	/**
	 * the suite of this card
	 */
	private CardSuite suite;
	/**
	 * Constructor for a card, takes the card's number and suite.
	 * @param number the number of this card
	 * @param suite the suite of this card
	 */
	public Card(int number, CardSuite suite) {
		this.number = number;
		this.suite = suite;
	}
	/**
	 * Constructor make a copy of another card
	 * @param copy the card being copied
	 */
	public Card(Card copy) {
		this.number = copy.getNumber();
		this.suite = copy.getCardSuite();
	}
	/**
	 * Public getter method for this cards number.
	 * @return the number of this card.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * Public getter method for this cards suite
	 * @return this cards suite.
	 */
	public CardSuite getCardSuite() {
		return suite;
	}
}
