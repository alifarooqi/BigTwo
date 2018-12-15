
/**
 * This class is a subclass of Card. It includes a overwritten card comparing method, one following the Big Two game rules.
 * For ranks and suits, we use the following order.
 * From HIGH to LOW: 2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3;
 * From HIGH to LOW: Spades ♠ > Hearts ♥ > Clubs ♣ > Diamonds ♦
 * @author ali
 *
 */
public class BigTwoCard extends Card {

	/**
	 * A constructor to make a Big Two card.
	 * @param suit An int value [0,3] specifying the the suit of the card:
	 * <p>0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade</p>
	 * @param rank An int value [0,12] specifying the the rank of the card:
	 * <p>0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 = 'Q', 12 = 'K'</p>
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/* (non-Javadoc)
	 * @see Card#compareTo(Card)
	 * Compares this card with the specified card for order in Big Two game.
	 */
	public int compareTo(Card card) {
		int rank = ((this.rank - 2) % 13 + 13 ) % 13;
		int otherRank = ((card.rank - 2) % 13 + 13 ) % 13;
		if (rank > otherRank) {
			return 1;
		} else if (rank < otherRank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
