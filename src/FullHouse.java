

/**
 * Models a Full House. This hand consists of five cards, with two having the same rank and three having another same rank.
 * The card in the triplet with the highest suit in a full house is referred to as the top card of this full house. 
 * A full house always beats any straights and flushes.
 * 
 * @author ali
 *
 */
public class FullHouse extends Hand {

	/**
	 * A constructor for creating a Full House hand for a specific player.
	 * @param player player the player trying to play Full Hosue
	 * @param cards the list of cards that forms a Full House.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	private int topRank;
	private CardList triple = new CardList();
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
			if (count1 == 3 && count2==2) {
				this.topRank = rank1;
				return true;
			}
			else if (count1 == 2 && count2==3) {
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
		return "FullHouse";
	}
	public Card getTopCard() {
		for (int i=0; i<5; i++) {
			if(this.getCards().getCard(i).getRank() == this.topRank) {
				this.triple.addCard(this.getCards().getCard(i));
			}
		}
		Hand newHand = new Triple(this.getPlayer(), this.triple);
		return newHand.getTopCard();
	}
}
