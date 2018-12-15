
/**
 * This hand consists of five cards with consecutive ranks and the same suit. 
 * Note: 2 and A can only form a straight flush with K but not with 3. 
 * The card with the highest rank in a straight flush is referred to as the top card of this straight flush. 
 * A straight flush always beats any straights, flushes, full houses and quads.
 * 
 * @author ali
 *
 */
public class StraightFlush extends Hand {

	/**
	 * A constructor for building a Straight Flush from specified list of cards for a player.
	 * @param player the current player playing Straight Flush.
	 * @param cards the list of cards from which a Straight Flush has to be made.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		Straight straight = new Straight(this.getPlayer(), getCards());
		Flush flush = new Flush(this.getPlayer(), getCards());
		return (flush.isValid() && straight.isValid());
	}

	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "StraightFlush";
	}

}
