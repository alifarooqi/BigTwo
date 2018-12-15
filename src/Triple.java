
/**
 * This hand consists of three cards with the same rank. 
 * The card with the highest suit in a triple is referred to as the top card of this triple.
 * 
 * @author ali
 *
 */
public class Triple extends Hand {

	/**
	 * A constructor for building a Triple from specified list of cards for a player.
	 * @param player the current player playing Triple.
	 * @param cards the list of cards from which a Triple has to be made.
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		// TODO Auto-generated constructor stub
		super(player, cards);
	}

	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if(this.getCards().size() == 3) {
			if (this.getCards().getCard(0).getRank() == this.getCards().getCard(1).getRank() && this.getCards().getCard(0).getRank() == this.getCards().getCard(2).getRank()) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "Triple";
	}

}
