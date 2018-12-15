
/**
 * This hand consists of only one single card. 
 * The only card in a single is referred to as the top card of this single.
 * 
 * @author ali
 */
public class Single extends Hand {

	/**
	 * A constructor for creating a Single hand for a specific player.
	 * @param player player the player trying to play Single
	 * @param cards the list of cards that forms a Single
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		return (this.getCards().size() == 1);
	}

	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "Single";
	}
}
