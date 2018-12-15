
/**
 * This hand consists of five cards with consecutive ranks. 
 * Note: 2 and A can only form a straight with K but not with 3. 
 * The card with the highest rank in a straight is referred to as the top card of this straight.
 * @author ali
 *
 */
public class Straight extends Hand {

	/**
	 * A constructor for building a Straight from specified list of cards for a player.
	 * @param player the current player playing Straight.
	 * @param cards the list of cards from which a Straight has to be made.
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (this.getCards().size() == 5) {
			this.getCards().sort();
			int rank1;
			int rank2;
			for (int i=4; i>0; i--) {
				rank1 = ((this.getCards().getCard(i).getRank()-2)%13 + 13) % 13;
				rank2 = ((this.getCards().getCard(i-1).getRank() - 2)%13 + 13) % 13;
				if (rank1-rank2 != 1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "Straight";
	}

}
