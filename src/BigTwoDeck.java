
/**
 * This class represents a deck of cards in a Big Two game.
 * 
 * @author ali
 */
public class BigTwoDeck extends Deck {
	
	/* (non-Javadoc)
	 * @see Deck#initialize()
	 * Creates a deck of 52 Big Two game cards.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}

}
