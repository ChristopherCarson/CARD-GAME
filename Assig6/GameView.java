import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//View class for a card game.
class View
{
   static int NUM_CARDS_PER_HAND = Model.NUM_CARDS_PER_HAND;
   static int  NUM_PLAYERS = Model.NUM_PLAYERS;
   static final int HUMAN_PLAYER = Model.HUMAN_PLAYER;
   static final int COMPUTER_PLAYER = Model.COMPUTER_PLAYER;
   static int NUM_CARD_STACKS = Model.NUM_CARD_STACKS;
   private JLabel[] addToStackLabel = new JLabel[NUM_CARD_STACKS];
   private JButton cantPlayButton;
   private JLabel[] computerHandLabels = new JLabel[NUM_CARDS_PER_HAND];
   private JButton[] playerHandButtons = new JButton[NUM_CARDS_PER_HAND];
   private JLabel[] playerScoreLabels = new JLabel[NUM_PLAYERS];
   private int[] playerScores = new int[NUM_PLAYERS];
   private CardTable myCardTable; 
   
   //Constructor sets up the table.
   public View()
   {
      //Create the table.
      myCardTable = new CardTable("CardGame", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //Add Play Area Buttons
      for(int i = 0; i < NUM_CARD_STACKS; i++)
      {
         addToStackLabel[i] = new JLabel();
         addToStackLabel[i].setName("Play Area Card Stack");
         myCardTable.pnlPlayArea.add(addToStackLabel[i]);
      }
      
      cantPlayButton = new JButton("I Cannot Play");
      cantPlayButton.setActionCommand("I Cannot Play");
      cantPlayButton.setVisible(true);
      myCardTable.pnlPlayArea.add(cantPlayButton);
      
      //Player Hands
      for(int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         playerHandButtons[i] = new JButton();
         playerHandButtons[i].setVisible(true);
         playerHandButtons[i].setActionCommand("Human Player Card");
         playerHandButtons[i].setBorderPainted(false);;
         myCardTable.pnlHumanHand.add(playerHandButtons[i]);
         
         computerHandLabels[i] = new JLabel();
         computerHandLabels[i].setVisible(true);
         myCardTable.pnlComputerHand.add(computerHandLabels[i]);
      }
      
      //Player Scores
      for(int i = 0; i < playerScoreLabels.length; i++)
      {
         playerScoreLabels[i] = new JLabel("Score:");
         myCardTable.pnlPlayArea.add(playerScoreLabels[i]);
      }     
   }
   
   //Adds the controller events to the buttons.
   public void addController(Controller controller)
   {
      for(int i = 0; i < NUM_CARD_STACKS; i++)
         addToStackLabel[i].addMouseListener(controller);
      cantPlayButton.addActionListener(controller);
      
      for(int i = 0; i < NUM_CARDS_PER_HAND; i++)
         playerHandButtons[i].addActionListener(controller);
   }
   
   //Sets the card table's visibility.
   public void setVisible(boolean visible)
   {
      myCardTable.setVisible(visible);
   }
   
   //Updates the display of the hand icons for a specific player.
   public boolean updateHand(Icon[] icons, int playerIndex)
   {
      //Sets unused buttons to invisible
      if(playerIndex == HUMAN_PLAYER && icons.length < playerHandButtons.length)
         for(int i = icons.length; i < playerHandButtons.length; i++)
            playerHandButtons[i].setVisible(false);
      
      if(playerIndex == COMPUTER_PLAYER && icons.length < computerHandLabels.length)
         for(int i = icons.length; i < computerHandLabels.length; i++)
            computerHandLabels[i].setVisible(false);
      
      //Sets the icons for the hands depending on if it is a player or computer.
      for(int i = 0; i < icons.length; i++)
      {
         if(playerIndex == HUMAN_PLAYER && i < playerHandButtons.length)
         {
            if(playerHandButtons[i].getIcon() != (ImageIcon)icons[i])
               playerHandButtons[i].setIcon((ImageIcon)icons[i]);
            if(!playerHandButtons[i].isVisible())
               playerHandButtons[i].setVisible(true);
         }
         else if(playerIndex == COMPUTER_PLAYER && i < computerHandLabels.length)
         {
            if(computerHandLabels[i].getIcon() != (ImageIcon)icons[i])
               computerHandLabels[i].setIcon((ImageIcon)icons[i]);
            if(!computerHandLabels[i].isVisible())
               computerHandLabels[i].setVisible(true);
         }
         else
            return false;
      }
      update();
      return true;
   }
   
   //Changes the display for the stack icons.
   public boolean updateStack(Icon[] icon)
   {
      if(icon.length != NUM_CARD_STACKS)
         return false;
      
      for(int i = 0; i < NUM_CARD_STACKS; i++)
         addToStackLabel[i].setIcon((ImageIcon)icon[i]);
      return true;
   }
   
   //Updates the score on the display.
   public boolean updateScore(int score, int playerIndex)
   {
      if(playerIndex > playerScoreLabels.length)
         return false;
      else if(playerIndex == 0)
      {
         playerScores[COMPUTER_PLAYER] = score;
         playerScoreLabels[COMPUTER_PLAYER].setText("Computer Score: " + score);
      }
      else
      {
         playerScores[playerIndex] = score;
         playerScoreLabels[playerIndex].setText("Player " + playerIndex + " Score: " + score);
      }
         
      return true;
   }
   
   //Returns the index of a button component in the player's hand.
   public int getPlayerButtonIndex(JButton button)
   {
      for(int i = 0; i < myCardTable.pnlHumanHand.getComponentCount(); i++)
      {
         if(button == myCardTable.pnlHumanHand.getComponent(i))
            return i;
      }
      return -1;
   }
   
   //Highlights a selected button green and unhighlights any other button.
   public void setSelectedButton(int index)
   {
      for(int i = 0; i < playerHandButtons.length; i++)
      {
         if(i == index)
            playerHandButtons[i].setBackground(Color.GREEN);
         else
            playerHandButtons[i].setBackground(null);
      }
   }
   
   //Updates the display.
   private void update()
   {
      myCardTable.revalidate();
      myCardTable.repaint();
   }

   //Gets the component index of a stack label from the play area.
   public int getStackIndex(JLabel stack)
   {
      for(int i = 0; i < myCardTable.pnlPlayArea.getComponentCount(); i++)
      {
         if(stack == myCardTable.pnlPlayArea.getComponent(i))
            return i;
      }
      return -1;
   }
   
   //Loads the end game prompt for the player.
   public int loadEndGamePrompt()
   {
      //End the game
      String endMessage;
      if(playerScores[COMPUTER_PLAYER] > playerScores[HUMAN_PLAYER])
         endMessage = "Computer wins!\nYour score was: " + playerScores[HUMAN_PLAYER] + "\nComputer's Score was: " + playerScores[COMPUTER_PLAYER];
      else if(playerScores[COMPUTER_PLAYER] < playerScores[1])
         endMessage = "You win!\nYour score was: " + playerScores[HUMAN_PLAYER] + "\nComputer's Score was: " + playerScores[COMPUTER_PLAYER];
      else
         endMessage = "Tie!\nYour score was: " + playerScores[HUMAN_PLAYER] + "\nComputer's Score was: " + playerScores[COMPUTER_PLAYER];
      endMessage = endMessage + "\nDo you want to play again?";
      
      //Replay the game?
      return (JOptionPane.showConfirmDialog(null, endMessage, "Game Over", JOptionPane.YES_NO_OPTION));
   }
}

//CardTable - Class represents the GUI for a card table.
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;
   
   private int numCardsPerHand;
   private int numPlayers;
   
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
   
   //Constructor
   //Takes the title of the window, number of cards per hand, and the number of players.
   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      setTitle(title);
      
      //Validate and set the two instance variables.
      if(numCardsPerHand > 0 && numCardsPerHand <= MAX_CARDS_PER_HAND)
         this.numCardsPerHand = numCardsPerHand;
      else
         this.numCardsPerHand = 1;
      
      if(numPlayers > 0 && numPlayers <= MAX_PLAYERS)
         this.numPlayers = numPlayers;
      else
         this.numPlayers = 1;
      
      //Create the panel layout and add the 3 panels to the frame.
      setLayout(new GridBagLayout());
      GridBagConstraints gridConstraints = new GridBagConstraints();
      gridConstraints.fill = GridBagConstraints.BOTH;
      gridConstraints.weightx = 1.0;
      
      //Computer Hand
      pnlComputerHand = new JPanel();
      pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
      gridConstraints.weighty = 0.0;
      gridConstraints.gridy = 0;
      add(pnlComputerHand, gridConstraints);
      
      //Playing Area
      pnlPlayArea = new JPanel();
      pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
      gridConstraints.weighty = 1.0;
      gridConstraints.gridy = 1;
      add(pnlPlayArea, gridConstraints);
      
      //Player's Hand
      pnlHumanHand = new JPanel();
      pnlHumanHand.setBorder(BorderFactory.createTitledBorder("Your Hand"));
      gridConstraints.weighty = 0.0;
      gridConstraints.gridy = 2;
      add(pnlHumanHand, gridConstraints);
      
      //CREATE THE PLAY AREA.
      //Create a grid layout for the play and card areas.
      FlowLayout playAreaFlow = new FlowLayout();
      pnlPlayArea.setLayout(playAreaFlow);
      
      GridLayout cardAreaGrid = new GridLayout(1,MAX_CARDS_PER_HAND);
      pnlComputerHand.setLayout(cardAreaGrid);
      pnlHumanHand.setLayout(cardAreaGrid);
   }//End CardTable Constructor

   //Accessors
   public int getCardsPerHand()
   {
      return numCardsPerHand;
   }
   
   public int getNumberOfPlayers()
   {
      return numPlayers;
   }
} //End Card Table Class.
