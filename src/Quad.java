
/**
 * This hand consists of five cards, with four having the same rank. 
 * The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad.
 * A full house always beats any straights and flushes.
 * @author ali
 */
public class Quad extends Hand {

	/**
	 * A constructor for creating a Quad for a specific player.
	 * @param player player the player trying to play Quad
	 * @param cards the list of cards that forms a Quad
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	private int topRank;
	private CardList quadruplet = new CardList();
	
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (this.getCards().size() == 5) {
			int rank1 = this.getCards().getCard(0).getRank();
			int rank2 = -1;
			int count1 = 1;
			int count2 = 0;
			for (int i=1; i<5; i++) {
				if(this.getCards().getCard(i).getRank() == rank1) {
					count1++;
				}
				else if(rank2 == -1) {
					rank2 = this.getCards().getCard(i).getRank();
					count2++;
				}
				else if(this.getCards().getCard(i).getRank() == rank2) {
					count2++;
				}
				else {
					return false;
				}
			}
			if (count1 == 4 && count2 == 1) {
				this.topRank = rank1;
				return true;
			}
			else if (count1 == 1 && count2==4) {
				this.topRank = rank2;
				return true;
			}
			return false;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "Quad";
	}
	
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		for (int i=0; i<5; i++) {
			if(this.getCards().getCard(i).getRank() == this.topRank) {
				this.quadruplet.addCard(this.getCards().getCard(i));
			}
		}
		Hand newHand = new Triple(this.getPlayer(), this.quadruplet);
		return newHand.getTopCard();
	}
}
