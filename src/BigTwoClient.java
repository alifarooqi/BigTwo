import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * The Big Two Client creates a Big Two Card Game and makes connection with a server.
 * 
 * @author ali
 * @version 1.0
 */
public class BigTwoClient implements CardGame, NetworkGame {
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	
	//My instance variables
	//BufferedReader reader; // for receiving messages
	//PrintWriter writer; // for sending messages
	InputStreamReader streamReader;
	private int passCount;
	private boolean firstTurn = true;
	private CardGamePlayer winner = null;
	private int[] input;
	private Hand currentHand;
	public boolean end = false;
	
	/**
	 * A constructor for creating a BigTwo Game with four players.
	 * 
	 */
	public BigTwoClient() {
		//Initialize
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		serverIP = "localhost";
		serverPort = 2396;
		
		//Create 4 Players
		for (int i=0; i<4; i++) {
			this.playerList.add(new CardGamePlayer());
		}
		
		//Create BigTwo table
		this.table = new BigTwoTable(this);
		table.enable();
		
		//Make connection to the game server
		makeConnection();
	}
	
	/**
	 * Resets the parameter of the game so that it can be restarted when 4 players join the server.
	 */
	public void restart() {
		handsOnTable = new ArrayList<Hand>();
		firstTurn = true;
		passCount =0;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#getPlayerID()
	 */
	@Override
	public int getPlayerID() {
		return numOfPlayers;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#setPlayerID(int)
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#getPlayerName()
	 */
	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#setPlayerName(java.lang.String)
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		//playerList.get(currentIdx).setName(playerName);
	}

	/* (non-Javadoc)
	 * @see NetworkGame#getServerIP()
	 */
	@Override
	public String getServerIP() {
		return this.serverIP;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#setServerIP(java.lang.String)
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#getServerPort()
	 */
	@Override
	public int getServerPort() {
		return this.serverPort;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#setServerPort(int)
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort=serverPort;
	}

	/* (non-Javadoc)
	 * @see NetworkGame#makeConnection()
	 */
	@Override
	public void makeConnection() {
		try {
			sock = new Socket(serverIP, serverPort);
			this.oos = new ObjectOutputStream(sock.getOutputStream());
			table.printMsg("Networking established :)");
		} catch (Exception ex) {
			ex.printStackTrace();
			table.printMsg("Unable to connect... Try again :(");
		}
		Thread thread = new Thread(new ServerHandler());
		thread.start();
		table.repaint();
		sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
		sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));			   //READY
	}
	/**
	 * This class inputs the message from the server.
	 * @author ali
	 */
	public class ServerHandler implements Runnable{

		@Override
		public void run() {
			try {
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				while (true) {
					parseMessage((CardGameMessage) ois.readObject());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see NetworkGame#parseMessage(GameMessage)
	 */
	@Override
	public synchronized void parseMessage(GameMessage message) {
		if (message.getType() == CardGameMessage.FULL) {
			table.printMsg("I am sorry, the table is full");
		} else if (message.getType() == CardGameMessage.JOIN){
			playerList.get(message.getPlayerID()).setName((String) message.getData());
			setPlayerName((String) message.getData());
			numOfPlayers=0;
			for (int i=0; i<playerList.size(); i++) {
				if (playerList.get(i).getName() != null) {
					numOfPlayers++;
				}
			}
			table.repaint();
		} else if (message.getType() == CardGameMessage.MOVE){
			checkMove(message.getPlayerID(), (int[]) message.getData());
		} else if (message.getType() == CardGameMessage.MSG){
			table.printChatMsg ((String) message.getData());
		} else if (message.getType() == CardGameMessage.PLAYER_LIST){
			playerID = message.getPlayerID();
			String[] names = (String[]) message.getData();
			for (int i=0; i<names.length; i++) {
				if (names[i] != null) {
					playerList.get(i).setName(names[i]);
				}
			}
			table.setPlayerID(message.getPlayerID());
			table.disableConnectButton();
			table.repaint();
		} else if (message.getType() == CardGameMessage.QUIT){
			restart();
			table.printMsg(playerList.get(playerID).getName() + " lost connection");
			try {
				Thread.sleep(50);
			} catch (Exception ex) { }
			playerList.get(playerID).setName(null);
			for (CardGamePlayer player : playerList) {
				player.removeAllCards();
			}
			//reset();
			table.repaint();
		} else if (message.getType() == CardGameMessage.READY){
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!");
			table.repaint();
		} else if (message.getType() == CardGameMessage.START){
			table.printMsg("Let the game begin!");
			table.enable();
			start((BigTwoDeck) message.getData());
			table.repaint();
		}
	}

	/* (non-Javadoc)
	 * @see NetworkGame#sendMessage(GameMessage)
	 */
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see CardGame#getNumOfPlayers()
	 */
	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}

	/* (non-Javadoc)
	 * @see CardGame#getDeck()
	 */
	@Override
	public Deck getDeck() {
		return this.deck;
	}

	/* (non-Javadoc)
	 * @see CardGame#getPlayerList()
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}

	/* (non-Javadoc)
	 * @see CardGame#getHandsOnTable()
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}

	/* (non-Javadoc)
	 * @see CardGame#getCurrentIdx()
	 */
	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}

	/* (non-Javadoc)
	 * @see CardGame#start(Deck)
	 */
	@Override
	public void start(Deck deck) {
		//remove all the cards from the players as well as from the table;
		for (int i=0; i<this.getNumOfPlayers(); i++) {
			this.playerList.get(i).removeAllCards();
		}
		this.handsOnTable = new ArrayList<Hand>();
		this.firstTurn = true;
		this.deck = deck;
		//Set 3 of Diamond
		BigTwoCard startCard = new BigTwoCard(0,2);
		//Distribute 13 cards to each player
		for (int i=0; i<52; i++) {
			this.getPlayerList().get(i%(this.getNumOfPlayers())).addCard(deck.getCard(i));
			if (startCard.equals(deck.getCard(i))) {
				currentIdx = (i%this.getPlayerList().size());
				table.setActivePlayer(currentIdx);
				table.printMsg(playerList.get(currentIdx).getName() + " starts the game..!!");
			}
		}
		this.passCount = 0;
		table.setActivePlayer(this.getCurrentIdx());
		table.repaint();
	}

	/**
	 * Checks if the move is valid. Valid move is any move after which the active player changes. It can be a pass, or any other legal move in Big Two.
	 * @param currentHand The current hand being played. Null if the player is passing his/her turn.
	 * @return True if the move is legal, and not passed; false otherwise.
	 */
	public boolean isValidMove(Hand currentHand) { 
		if (currentHand == null && this.passCount<3 && this.input == null) { //Pass
			if (this.passCount<=3) { //Legal Pass
				table.printMsg("{pass}");
				this.passCount++;
				if (this.passCount == 3) {
					this.handsOnTable.removeAll(getHandsOnTable());
				}
				currentIdx =(this.getCurrentIdx()+1)%this.getPlayerList().size();
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
				table.printMsg("Not a legal move!!!");
				table.resetSelected();
				return false;
			}
		}
		table.printMsg("Not a legal move!");
		table.resetSelected();
		return false;
	}
	
	/* (non-Javadoc)
	 * @see CardGame#makeMove(int, int[])
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
		checkMove(playerID, cardIdx);
		table.repaint();
	}

	/* (non-Javadoc)
	 * @see CardGame#checkMove(int, int[])
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		this.input = cardIdx;
		this.currentHand = composeHand(this.playerList.get(this.currentIdx), this.playerList.get(this.currentIdx).play(this.input));
		BigTwoCard startCard = new BigTwoCard(0,2);
		if(firstTurn) {
			if (currentHand != null && currentHand.contains(startCard)) {
				if(isValidMove(this.currentHand)) { //Valid Move: When this move beats previous move
					firstTurn = false;
					this.getPlayerList().get(this.getCurrentIdx()).removeCards(this.playerList.get(this.currentIdx).play(this.input));
					this.handsOnTable.add(this.currentHand);
					if (isWinner(this.getPlayerList().get(this.getCurrentIdx()))) {
						this.winner = (this.getPlayerList().get(this.getCurrentIdx()));
					}
					else{
						currentIdx = ((this.getCurrentIdx()+1)%this.getPlayerList().size());
						table.printMsg(playerList.get(currentIdx).getName() + "'s Turn..!!");
					}
				}
			}
			else {
				table.printMsg("Not a legal move!");
				table.resetSelected();
			}
		}
		else if (winner == null) {
			if(isValidMove(this.currentHand)) { //Valid Move: When this move beats previous move
				this.getPlayerList().get(this.getCurrentIdx()).removeCards(this.playerList.get(this.currentIdx).play(this.input));
				this.handsOnTable.add(this.currentHand);
				if (isWinner(this.getPlayerList().get(this.getCurrentIdx()))) {
					this.winner = (this.getPlayerList().get(this.getCurrentIdx()));
					for (int i=0; i<getPlayerList().size(); i++) {
						if(isWinner(getPlayerList().get(i))) {
							table.printMsg(getPlayerList().get(i).getName() + " wins the game.");
						}
						else {
							table.printMsg(getPlayerList().get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " cards in the game.");
						}
					}
				}
				else{
					this.currentIdx = ((this.getCurrentIdx()+1)%this.getPlayerList().size());
				}
			}
		}
		table.repaint();
	}

	/* (non-Javadoc)
	 * @see CardGame#endOfGame()
	 */
	@Override
	public boolean endOfGame() {
		for (int i=0; i<numOfPlayers; i++) {
			if(isWinner(playerList.get(i))) {
				return true;
			}
		}
		return false;
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


	/**
	 * Starts a new Big Two Client
	 * @param args
	 */
	public static void main(String[] args) {
		BigTwoClient b2c = new BigTwoClient();
	}
}
