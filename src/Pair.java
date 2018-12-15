
/**
 * This hand consists of two cards with the same rank. 
 * The card with a higher suit in a pair is referred to as the top card of this pair.
 * @author ali
 */
public class Pair extends Hand {

	/**
	 * A constructor for creating a pair from specified list of cards for a player.
	 * @param player the current player playing a pair.
	 * @param cards the list of cards from which a Pair has to be made.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		return (this.getCards().size() == 2 && this.getCards().getCard(0).getRank() == this.getCards().getCard(1).getRank());
	}

	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "Pair";
	}
}
