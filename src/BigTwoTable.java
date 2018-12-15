import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;


/**
 * The BigTwoTable class implements the CardGameTable interface and builds a GUI for the Big Two Card Game.
 *
 *@author ali
 *@version 1.0
 */
public class BigTwoTable implements CardGameTable {
	private CardGame game;
	private boolean[] selected;
	private int activePlayer;
	private int playerID;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	private boolean mouseEnabled = true;
	JTextArea incoming; // for showing messages
	JTextField outgoing; // for user inputs
	JMenuItem connect;
	
	//Constructor
	/**
	 * This constructor constructs the Panels for the game GUI.
	 * 
	 * @param game A BigTwoGame object
	 */
	public BigTwoTable(CardGame game) {
		this.game = game;
		selected = new boolean[13];
		
		//BigTwo Frame
		frame = new JFrame("Big Two Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		
		//BigTwo Panel
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setBackground(new Color(11, 79, 74));
		bigTwoPanel.setLayout(new BorderLayout());
		bigTwoPanel.addMouseListener((MouseListener) bigTwoPanel);
		
		//Button Panel at the bottom
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(166, 193, 184));
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		bigTwoPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		//Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game");
		connect = new JMenuItem("Connect");
		JMenuItem quit = new JMenuItem("Quit");
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(connect);
		menu.add(quit);
		menuBar.add(menu);
		
		//Msg Area
		msgArea = new JTextArea("Welcome to a BigTwo Game!\n");
		msgArea.setFont(new Font("Calibri", Font.PLAIN, 16));
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(msgArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(250, 400));
		
		//ChatPanel
		incoming = new JTextArea(15,50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane (incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		//Text Field for getting input
		outgoing = new JTextField(20);
		//Send Button
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		
		JPanel chatPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.add(qScroller);
		inputPanel.add(outgoing, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		chatPanel.add(inputPanel);
		
		JPanel rhs = new JPanel();
		rhs.setLayout(new BorderLayout());
		rhs.add(areaScrollPane, BorderLayout.NORTH);
		rhs.add(chatPanel, BorderLayout.SOUTH);
		
		//Create a split pane with the Msg/Chat Area on the right.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		                           bigTwoPanel, rhs);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//Provide minimum sizes for the two components in the split pane
		bigTwoPanel.setMinimumSize(new Dimension(1000, 800));
		msgArea.setMinimumSize(new Dimension(150, 50));
		splitPane.setDividerLocation(0.85);
		
		//Adding panel(s) to the frame (At the end)
		frame.setJMenuBar(menuBar);
		frame.add(splitPane, BorderLayout.CENTER);
		frame.setVisible(true);
		
		//Making Send Button to use Enter Key
		frame.getRootPane().setDefaultButton(sendButton);
		
		//Show JOptions at Startup
		connect.doClick();
	}

	/* (non-Javadoc)
	 * @see CardGameTable#setActivePlayer(int)
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		if (activePlayer>=0 && activePlayer<game.getNumOfPlayers()) {
			this.activePlayer = activePlayer;
			selected = new boolean[game.getPlayerList().get(activePlayer).getNumOfCards()];
		}
	}

	/**
	 * @return the index of player
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * This method sets player ID
	 * @param playerID an int that specifies the index of player in Player List.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/* (non-Javadoc)
	 * @see CardGameTable#getSelected()
	 */
	@Override
	public int[] getSelected() {
		return this.getSelected();
	}

	/* (non-Javadoc)
	 * @see CardGameTable#resetSelected()
	 */
	@Override
	public void resetSelected() {
		selected = new boolean[13];
	}

	/* (non-Javadoc)
	 * @see CardGameTable#repaint()
	 */
	@Override
	public void repaint() {
		frame.repaint();
	}

	/* (non-Javadoc)
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg + "\n");
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
	}
	
	/**
	 * Prints specific string to the chat area.
	 * 
	 * @param msg the string (chat input from the server) to be printed on screen.
	 */
	public void printChatMsg(String msg) {
		incoming.append(msg + "\n");
		incoming.setCaretPosition(incoming.getDocument().getLength());
	}

	/* (non-Javadoc)
	 * @see CardGameTable#clearMsgArea()
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText(null);
	}

	/* (non-Javadoc)
	 * @see CardGameTable#reset()
	 */
	@Override
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}

	/* (non-Javadoc)
	 * @see CardGameTable#enable()
	 */
	@Override
	public void enable() {
		passButton.setEnabled(true);
		playButton.setEnabled(true);
		mouseEnabled = true;
	}

	/* (non-Javadoc)
	 * @see CardGameTable#disable()
	 */
	@Override
	public void disable() {
		passButton.setEnabled(false);
		playButton.setEnabled(false);
		mouseEnabled = false;
	}
	/**
	 * This method disables the "Connect" Menu item, making it unclickable.
	 */
	public void disableConnectButton() {
		connect.setEnabled(false);
	}
	/**
	 * This method (re)enables the "Connect" Menu item.
	 */
	public void enableConnectButton() {
		connect.setEnabled(true);
	}
	
	/**
	 * This inner class implements the Mouse Listener for the GUI and paints graphics for the game interface.
	 * @author ali
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			avatars = new Image[4];
			cardImages = new Image[4][13];
			cardBackImage = new ImageIcon("assets/cards/b.gif").getImage();
			
			//Print Player's Name and Avatars			
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			Font font = new Font ("Calibri", Font.BOLD , 20);
			g.setFont(font);
			g.setColor(new Color (255,255,255));
			ArrayList<CardGamePlayer> players = game.getPlayerList();
			int avatarSize = 72; //16,24,32,48,64,72,128,256
			String[] avatarNames = {"flash_" + avatarSize + ".png", "batman_" + avatarSize + ".png", "superman_" + avatarSize + ".png", "green_lantern_" + avatarSize + ".png"};
			String avatarSrc = new String("assets/characters/png/" + avatarSize + "/");
			int heightCol = 130;
			for (int i=0; i<players.size(); i++) {
				if (players.get(i).getName() != null){
					g.drawString(players.get(i).getName(), 50, 35 + (i * heightCol));
				}
				else {
					g.drawString("Player " + i, 50, 35 + (i * heightCol));
				}
				avatars[i] = new ImageIcon(avatarSrc + avatarNames[i]).getImage();
				g.drawImage(avatars[i], 40, 40 + (i * heightCol), this);
				g2.drawLine(0, 120 + (i*heightCol), 1200, 120 + (i*heightCol));
			}

			//Initializing cardImages
			char[] suits = { 'd', 'c', 'h', 's'};
			char[] ranks = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };			
			for (int i=0; i<4; i++) {
				for (int j=0; j<13; j++) {
					cardImages[i][j] = new ImageIcon("assets/cards/" + ranks[j] + suits[i]  + ".gif").getImage();
				}
			}
			
			g.drawString("Table", 20, 50 + 4*heightCol);

			//Print Player's cards
			for (int i=0; i<game.getNumOfPlayers(); i++) {
				for (int j=0; j<players.get(i).getNumOfCards(); j++) {
					int x = 200 + 14*j;
					int y = 18 + i*heightCol;
					if (i == getPlayerID()) {
						int suit = players.get(i).getCardsInHand().getCard(j).getSuit();
						int rank = players.get(i).getCardsInHand().getCard(j).getRank();
						if(selected[j]) {
							g.drawImage(cardImages[suit][rank], x, y-10, this);
						}
						else {
							g.drawImage(cardImages[suit][rank], x, y, this);
						}
					}
					else {
						g.drawImage(cardBackImage, x, y, this);
					}
				}
			}
			
			//Print Hands on Table
			if(!game.getHandsOnTable().isEmpty()) {
				Hand lastHand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
				for (int i=0; i<lastHand.size(); i++) {
					int suit = lastHand.getCard(i).getSuit();
					int rank = lastHand.getCard(i).getRank();
					g.drawImage(cardImages[suit][rank], 200+14*i, 5+4*heightCol, this);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(mouseEnabled) {
				int clickX = e.getX();
				int clickY = e.getY();
				
				//Range of Active Player's Cards
				int x1, x2, y1, y2;
				x1 = 200;
				x2 = 200 + 14*(game.getPlayerList().get(playerID).getNumOfCards()-1) + 72;
				y1 = 18 + playerID*130;
				y2 = y1 + 96;
				
				if (clickX>x1 && clickX<x2 && clickY>y1 &&clickY<y2) {
					int selection = (clickX-200)/14;
					if (selection > game.getPlayerList().get(playerID).getNumOfCards()-1) {
						selection = game.getPlayerList().get(playerID).getNumOfCards()-1;
					}
					selected[selection] = !selected[selection];
					frame.repaint();
				}
				else { //Deselecting the already selected ones
					y1 -=10;
					y2 -=10;
					if (clickX>x1 && clickX<x2 && clickY>y1 &&clickY<y2) {
						int selection = (clickX-200)/14;
						if (selection > game.getPlayerList().get(playerID).getNumOfCards()-1) {
							selection = game.getPlayerList().get(playerID).getNumOfCards()-1;
						}
						selected[selection] = false;
						frame.repaint();
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * This inner class handles the event when Play Button is clicked.
	 * @author ali
	 *
	 */
	class PlayButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Integer> moves = new ArrayList<Integer>();
			for (int i=0; i<selected.length; i++) {
				if (selected[i]) {
					moves.add(i);
				}
			}
			int[] array = new int[moves.size()];
			for (int i=0; i<moves.size(); i++) {
				array[i] = moves.get(i);
			}
			if (moves.size() > 0) {
				game.makeMove(playerID, array);
			}
		}
	}
	
	/**
	 * This inner class handles events when the pass button is pressed.
	 * @author ali
	 */
	class PassButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			game.makeMove(playerID, null);
		}
		
	}
	
	/**
	 * This inner class handles all the events when Connect option is selected from the menu.
	 * @author ali
	 *
	 */
	class ConnectMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String playerName = "";
			String server = "";
			while (playerName == "") {
				playerName = JOptionPane.showInputDialog("Enter your Name");
			}
			while (server == "") {
				server = JOptionPane.showInputDialog("Enter Server IP");
			}
			((BigTwoClient) game).setPlayerName(playerName);
			((BigTwoClient) game).setServerIP(server.substring(0, server.indexOf(":")));
			((BigTwoClient) game).setServerPort(Integer.parseInt(server.substring(server.indexOf(":")+1)));
			System.out.println(((BigTwoClient) game).getPlayerName() + " " + ((BigTwoClient) game).getServerIP() + ":" + ((BigTwoClient) game).getServerPort() );
		}
		
	}
	
	/**
	 * This inner class handles all the events when Quit option is clicked from the menu. 
	 * 
	 * @author ali
	 *
	 */
	class QuitMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}

	/**
	 * This inner class handles all the events when Send button is clicked. It sends the chat message to the server and resets the input field.
	 * 
	 * @author ali
	 */
	public class SendButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (outgoing.getText().length()>0) {
				CardGameMessage msg = new CardGameMessage(CardGameMessage.MSG, playerID , outgoing.getText());
				try {
					((BigTwoClient) game).sendMessage(msg);
					outgoing.setText("");
					outgoing.requestFocus();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
