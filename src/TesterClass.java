
public class TesterClass {

	public static void main(String[] args) {
		/*BigTwoDeck deck = new BigTwoDeck();
		deck.shuffle();
		CardGamePlayer player = new CardGamePlayer("Ali");
		CardList cards = new CardList();
		int rank = (int) (Math.random()*13);
		
		for (int i=0; i<5; i++) {
			int suit = (int) (Math.random()*4);
			cards.addCard(new BigTwoCard(suit, (i+rank)%13));
			rank = i==10?(int) (Math.random()*13):rank;
		}
		cards.print();
		Hand hand = new Straight(player, cards);
		if (hand.isValid()) {
			System.out.println(hand.getType());
		}
		hand.print();
		CardList top = new CardList();
		top.addCard(hand.getTopCard());
		top.print();*/
		CardGame game = null;
		BigTwoTable gui = new BigTwoTable(game);
	}

}
