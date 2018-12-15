import java.util.ArrayList;

/**
 * This class is used for modeling a Big Two card game. It provides the main logics for the game.
 * 
 * @author FAROOQI Muhammad Ali 3035393157
 * @version 1.0 (Initial Release)
 *
 */
public class BigTwo implements CardGame{
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentIdx;
	//private BigTwoConsole bigTwoConsole;
	private BigTwoTable bigTwoTable;
	
	//My instance variables
	private CardGamePlayer winner = null;
	private int[] input;
	private Hand currentHand;
	private int passCount;
	private boolean firstTurn = true;
	public boolean end = false;
	
	/**
	 * Public constructor. It creates a Big Two card game with four players and a Big Two Console.
	 */
	public BigTwo(){
		this.playerList = new ArrayList<CardGamePlayer>();
		this.handsOnTable = new ArrayList<Hand>();
		for (int i=0; i < 4; i++) {
			this.getPlayerList().add(new CardGamePlayer());
		}
		bigTwoTable = new BigTwoTable(this);
	}
	
	/**
	 * Returns the deck of cards in the game.
	 * 
	 * @return Deck of cards of class Deck.
	 */
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * Returns the List of players playing the ga,e
	 * 
	 * @return An ArrayList of class CardGamePlayer
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return this.playerList;
	}
	
	/**
	 * Returns the list of cards which have been (legally) played.
	 * 
	 * @return An ArrayList of class Hand.
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return this.handsOnTable;
	}
	
	/**
	 * Returns the index of currently active player whose turn is to play.
	 * 
	 * @return an int specifying the index of current player.
	 */
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	/**
	 * Sets the index of current player to the value specified in parameter.
	 * 
	 * @param idx an integer specifying the index of active player.
	 */
	public void setCurrentIdx(int idx) {
		if (idx>=0 && idx<this.getPlayerList().size()) {
			this.currentIdx = idx;
			bigTwoTable.setActivePlayer(idx);
		}
	}
	
	/**
	 * Returns the winner of current game. Returns null if there is no winner;
	 * 
	 * @return Winning player of class CardGamePlayer if the game has ended. Null otherwise.
	 */
	public CardGamePlayer getWinner() {
		return winner;
	}

	/**
	 * Sets the winner of the game. This also marks the end of the current game.
	 * 
	 * @param winner: Player, of class CardGamePlayer, who has won the game.
	 */
	public void setWinner(CardGamePlayer winner) {
		this.winner = winner;
	}

	/**
	 * Checks if the move is valid. Valid move is any move after which the active player changes. It can be a pass, or any other legal move in Big Two.
	 * @param currentHand The current hand being played. Null if the player is passing his/her turn.
	 * @return True if the move is legal, and not passed; false otherwise.
	 */
	public boolean isValidMove(Hand currentHand) { 
		if (currentHand == null && this.passCount<3 && this.input == null) { //Pass
			if (this.passCount<=3) { //Legal Pass
				bigTwoTable.printMsg("{pass}");
				this.passCount++;
				if (this.passCount == 3) {
					this.handsOnTable.removeAll(getHandsOnTable());
				}
				this.setCurrentIdx((this.getCurrentIdx()+1)%this.getPlayerList().size());
				return false;
			}
			else {//3 Passes done + pass!
				return false;
			}
		}
		else if (currentHand != null && this.handsOnTable.isEmpty()) {//First Move or Move after 3 passes
			this.passCount=0;
			return true;
		}
		else if (currentHand != null && this.handsOnTable.size()>0) {//Not first move
			if (currentHand.beats(this.handsOnTable.get(handsOnTable.size() - 1))) {//Legal Move
				this.passCount=0;
				return true;
			}
			else { //Illegal Move and !pass
				bigTwoTable.printMsg("Not a legal move!!!");
				bigTwoTable.resetSelected();
				return false;
			}
		}
		bigTwoTable.printMsg("Not a legal move!");
		bigTwoTable.resetSelected();
		return false;
	}
	/**
	 * A method to start the Big Two game, and play it until the game is over.
	 * 
	 * @param deck a deck of shuffled cards of class BigTwoDeck.
	 */
	public void start(Deck deck) {
		for (int i=0; i<this.getNumOfPlayers(); i++) {
			this.playerList.get(i).removeAllCards();
		}
		end = false;
		this.handsOnTable = new ArrayList<Hand>();
		this.firstTurn = true;
		this.deck = deck;
		//Set 3 of Diamond
		BigTwoCard startCard = new BigTwoCard(0,2);
		//Distribute 13 cards to each player
		for (int i=0; i<52; i++) {
			this.getPlayerList().get(i%(this.getNumOfPlayers())).addCard(deck.getCard(i));
			if (startCard.equals(deck.getCard(i))) {
				setCurrentIdx(i%this.getPlayerList().size());
			}
		}
		this.passCount = 0;
		bigTwoTable.setActivePlayer(this.getCurrentIdx());
		bigTwoTable.repaint();
	}
	
	/**
	 * Checks if the player who played the cards has won or not.
	 * 
	 * @param player the current player who has played a legal move.
	 * @return True if the game is over, and current player has won; false otherwise.
	 */
	private boolean isWinner(CardGamePlayer player) {
		return (player.getNumOfCards() == 0);
	}

	public void restart() {
		this.handsOnTable = new ArrayList<Hand>();
		this.deck.initialize();
		this.deck.shuffle();
		this.start(deck);
		
	}
	/**
	 * Main method for declaring, initializing and starting a new game with shuffled cards, and printing out the results after the game is over.
	 * @param args None required.
	 */
	public static void main(String[] args) {
		BigTwo game = new BigTwo();
		game.setDeck(new BigTwoDeck());
		game.getDeck().shuffle();
		game.start((BigTwoDeck) game.getDeck());
	}
	/**
	 * Composes a legal hand from the provided card list. 
	 * Can make the following legal combinations of hands: Single, Pair, Triple, Straight, Flush, FullHouse, Quad and StraighFlush.
	 * 
	 * @param player the current player, of class CardGamePlayer, who has composed the hand.
	 * @param cards the list of cards, of class CardList, of which the hand has to be made
	 * @return one of the legal combination of hand (of the highest order) possible from the given combination of cards. If no possible combination is there, it returns null.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand hand;
		if(cards != null) {
			if (cards.size() == 1) {
				hand = new Single(player, cards);
				if(hand.isValid()) {
					return hand;
				}
			}
			else if (cards.size() == 2) {
				hand = new Pair(player, cards);
				if(hand.isValid()) {
					return hand;
				}
			}
			else if (cards.size() == 3) {
				hand = new Triple(player, cards);
				if(hand.isValid()) {
					return hand;
				}
			}
			else if (cards.size() == 5) {
				hand = new StraightFlush(player, cards);
				if(hand.isValid()) {
					return hand;
				}
				else {
					hand = new Quad(player, cards);
					if(hand.isValid()) {
						return hand;
					}
					else {
						hand = new FullHouse(player, cards);
						if(hand.isValid()) {
							return hand;
						}
						else {
							hand = new Flush(player, cards);
							if(hand.isValid()) {
								return hand;
							}
							else {
								hand = new Straight(player, cards);
								if(hand.isValid()) {
									return hand;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see CardGame#getNumOfPlayers()
	 */
	public int getNumOfPlayers() {
		return this.getPlayerList().size();
	}
	/**
	 * Set deck for the game
	 * 
	 * @param deck
	 */
	public void setDeck(Deck deck) {
		this.deck = deck;
	}


	/* (non-Javadoc)
	 * @see CardGame#makeMove(int, int[])
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		this.input = cardIdx;
		BigTwoCard startCard = new BigTwoCard(0,2);
		this.currentHand = composeHand(this.playerList.get(this.currentIdx), this.playerList.get(this.currentIdx).play(this.input));
		if(firstTurn) {
			if (currentHand != null && currentHand.contains(startCard)) {
				if(isValidMove(this.currentHand)) { //Valid Move: When this move beats previous move
					firstTurn = false;
					this.getPlayerList().get(this.getCurrentIdx()).removeCards(this.playerList.get(this.currentIdx).play(this.input));
					this.handsOnTable.add(this.currentHand);
					if (isWinner(this.getPlayerList().get(this.getCurrentIdx()))) {
						this.setWinner(this.getPlayerList().get(this.getCurrentIdx()));
					}
					else{
						this.setCurrentIdx((this.getCurrentIdx()+1)%this.getPlayerList().size());
					}
				}
			}
			else {
				bigTwoTable.printMsg("Not a legal move!");
				bigTwoTable.resetSelected();
			}
		}
		else if (winner == null) {
			if(isValidMove(this.currentHand)) { //Valid Move: When this move beats previous move
				this.getPlayerList().get(this.getCurrentIdx()).removeCards(this.playerList.get(this.currentIdx).play(this.input));
				this.handsOnTable.add(this.currentHand);
				if (isWinner(this.getPlayerList().get(this.getCurrentIdx()))) {
					this.setWinner(this.getPlayerList().get(this.getCurrentIdx()));
					for (int i=0; i<getPlayerList().size(); i++) {
						if(isWinner(getPlayerList().get(i))) {
							bigTwoTable.printMsg(getPlayerList().get(i).getName() + " wins the game.");
						}
						else {
							bigTwoTable.printMsg(getPlayerList().get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " cards in the game.");
						}
					}
				}
				else{
					this.setCurrentIdx((this.getCurrentIdx()+1)%this.getPlayerList().size());
				}
			}
		}
		bigTwoTable.repaint();
	}

	/* (non-Javadoc)
	 * @see CardGame#checkMove(int, int[])
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		//Does Nothing
	}

	/* (non-Javadoc)
	 * @see CardGame#endOfGame()
	 */
	@Override
	public boolean endOfGame() {
		return winner!=null;
	}

}
