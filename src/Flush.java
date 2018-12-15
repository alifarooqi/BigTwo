
/**
 * This class models a Flush hand. A flush consists of five cards with the same suit.
 * The card with the highest rank in a flush is referred to as the top card of this flush. 
 * A flush always beats any straights.
 * @author ali
 *
 */
public class Flush extends Hand {

	/**
	 * A constructor for building a flush hand for a specific player.
	 * @param player the player trying to play flush
	 * @param cards the lists of cards to make flush from.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	@Override
	public boolean isValid() {
		if (this.getCards().size() == 5) {
			int suit = this.getCards().getCard(0).getSuit();
			for(int i=1; i<5; i++) {
				if(this.getCards().getCard(i).getSuit() != suit) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String getType() {
		return "Flush";
	}

}
