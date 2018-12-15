
/**
 * An abstract class, acting as a super class to the following types of hands: Single, Pair, Triple, Straight, Flush, FullHouse, Quad and StraighFlush.
 * @author ali
 */

public abstract class Hand extends CardList {
	private CardGamePlayer player;
	/**
	 * A constructor for building a hand from specified list of cards for a player.
	 * @param player the current player playing hand.
	 * @param cards the list of cards from which a hand has to be made.
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		for(int i=0; i<cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	/**
	 * A method to retrieve the cards in this hand
	 * @return the Card List forming the current hand.
	 */
	public CardList getCards() {
		return this;
	}
	/**
	 * A method to retrieve the player playing this hand.
	 * @return A player, of class CardGamePlayer.
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	/**
	 * This method returns the top card of each hand, which is used to compare a hand to another. The top card is usually the card of highest order in the hand, unless specified otherwise.
	 * @return a Card from the hand considered to have highest order.
	 */
	public Card getTopCard() {
		getCards().sort();
		return getCards().getCard(getCards().size()-1);
	}
	/**
	 * A method to compare a hand with another.
	 * 
	 * @param hand the hand to be compared with.
	 * @return true if this hand beats the one it's been compared with; false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (this.getCards().size() == hand.getCards().size()) {
			if(this.getType() == hand.getType()) {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (this.getCards().size() == 5) {
				if (this.getType() == "StraightFlush") {
					return true;
				}
				else if (this.getType() == "Quad" && hand.getType() != "StraightFlush") {
					return true;
				}
				else if (this.getType() == "FullHouse" && hand.getType() != "StraightFlush" && hand.getType() != "Quad") {
					return true;
				}
				else if (this.getType() == "Flush" && hand.getType() != "StraightFlush" && hand.getType() != "Quad" && hand.getType() != "FullHouse") {
					return true;
				}
					return false;
			}
		}
		return false;
	}
	/**
	 * A method to determine if a specific hand is valid or not.
	 * 
	 * @return true if the card list of the hand are in accordance with it's description; false otherwise
	 */
	public abstract boolean isValid();
	/**
	 * A method to return the name of the hand being formed.
	 * 
	 * @return A string of the name of the hand, without spaces, and capitalized first letters.
	 */
	public abstract String getType();
	
}
