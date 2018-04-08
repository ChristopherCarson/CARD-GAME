import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameView extends JFrame 
{

   // CardTable - Class represents the GUI for a card table.
   // static class CardTable extends JFrame

   static JLabel[] playedCardLabels;
   static JLabel[] playLabelText;
   static JLabel[] computerLabels;
   static ImageIcon[] humanLabels;
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;

   private int numCardsPerHand;
   static int COMPUTER_PLAYER1 = GameModel.COMPUTER_PLAYER1;
   static int HUMAN_PLAYER1 = GameModel.HUMAN_PLAYER1;
   private int numPlayers = GameModel.numPlayers;

   static JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   // Constructor
   // Takes the title of the window, number of cards per hand, and the number of
   // players.
   GameView(String title) 
   {

      numCardsPerHand = GameModel.numCardsPerHand;
      // Validate and set the two instance variables.
      if (!(numCardsPerHand > 0 && numCardsPerHand <= MAX_CARDS_PER_HAND))
         this.numCardsPerHand = 1;

      if (!(numPlayers > 0 && numPlayers <= MAX_PLAYERS))
         this.numPlayers = 1;

      setSize(800, 600);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle(title);
      computerLabels = new JLabel[numCardsPerHand];
      humanLabels = new ImageIcon[numCardsPerHand];

      // Create the panel layout and add the 3 panels to the frame.
      setLayout(new GridBagLayout());
      GridBagConstraints gridConstraints = new GridBagConstraints();
      gridConstraints.fill = GridBagConstraints.BOTH;
      gridConstraints.weightx = 1.0;

      // Computer Hand
      pnlComputerHand = new JPanel();
      pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
      gridConstraints.weighty = 0.0;
      gridConstraints.gridy = 0;
      add(pnlComputerHand, gridConstraints);

      // Playing Area
      pnlPlayArea = new JPanel();
      pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
      gridConstraints.weighty = 1.0;
      gridConstraints.gridy = 1;
      add(pnlPlayArea, gridConstraints);

      // Player's Hand
      pnlHumanHand = new JPanel();
      pnlHumanHand.setBorder(BorderFactory.createTitledBorder("Your Hand"));
      gridConstraints.weighty = 0.0;
      gridConstraints.gridy = 2;
      add(pnlHumanHand, gridConstraints);

      // CREATE THE PLAY AREA.
      // Create a grid layout for the play and card areas.
      GridLayout playAreaGrid = new GridLayout(2, numPlayers);
      pnlPlayArea.setLayout(playAreaGrid);

      GridLayout cardAreaGrid = new GridLayout(1, numCardsPerHand);
      pnlComputerHand.setLayout(cardAreaGrid);
      pnlHumanHand.setLayout(cardAreaGrid);

      // Play Area.
      playedCardLabels = new JLabel[numPlayers];
      playLabelText = new JLabel[numPlayers];

      for (int i = 0; i < numPlayers; i++) {
         playedCardLabels[i] = new JLabel();
         if (i == 0)
            playLabelText[i] = new JLabel("Computer's Score: 0");
         else if (i == 1)
            playLabelText[i] = new JLabel("Your Score: 0");
         else
            playLabelText[i] = new JLabel("Player " + i);
      }

      // Play Area.
      for (int i = 0; i < numPlayers; i++) {
         playedCardLabels[i] = new JLabel();
         if (i == 0)
            playLabelText[i] = new JLabel("Computer's Score: 0");
         else if (i == 1)
            playLabelText[i] = new JLabel("Your Score: 0");
         else
            playLabelText[i] = new JLabel("Player " + i);
      }

      // Add icons first...
      for (int i = 0; i < numPlayers; i++) {
         playedCardLabels[i].setVisible(false);
         playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
         pnlPlayArea.add(playedCardLabels[i]);
      }

      // And then text.
      for (int i = 0; i < numPlayers; i++) {
         playLabelText[i].setVisible(true);
         playLabelText[i].setHorizontalAlignment(JLabel.CENTER);
         pnlPlayArea.add(playLabelText[i]);
      }
      // END PLAY AREA

   }// CardTable Constructor

   protected static void updateScoreView(int score) {
      if (score == -1) // computer wins
      {
         playLabelText[COMPUTER_PLAYER1].setText("Computer's Score: " + GameModel.playerScores[COMPUTER_PLAYER1]);
      } else if (score == 1) // player wins
      {
         playLabelText[HUMAN_PLAYER1].setText("Player's Score: " + GameModel.playerScores[HUMAN_PLAYER1]);
      } else if (score == 0) // tie
      {
         playLabelText[COMPUTER_PLAYER1].setText("Computer's Score: " + ++GameModel.playerScores[COMPUTER_PLAYER1]);
         playLabelText[HUMAN_PLAYER1].setText("Player's Score: " + ++GameModel.playerScores[HUMAN_PLAYER1]);
      }
   }

   // End the game
   public String EndGame() {
      String endMessage;
      if (GameModel.playerScores[COMPUTER_PLAYER1] > GameModel.playerScores[HUMAN_PLAYER1])
         endMessage = "Computer wins!\nYour score was: " + GameModel.playerScores[HUMAN_PLAYER1]
               + "\nComputer's Score was: " + GameModel.playerScores[COMPUTER_PLAYER1];
      else if (GameModel.playerScores[COMPUTER_PLAYER1] < GameModel.playerScores[1])
         endMessage = "You win!\nYour score was: " + GameModel.playerScores[HUMAN_PLAYER1] + "\nComputer's Score was: "
               + GameModel.playerScores[COMPUTER_PLAYER1];
      else
         endMessage = "Tie!\nYour score was: " + GameModel.playerScores[HUMAN_PLAYER1] + "\nComputer's Score was: "
               + GameModel.playerScores[COMPUTER_PLAYER1];
      endMessage = endMessage + "\nDo you want to play again?";

      playLabelText[COMPUTER_PLAYER1].setText("Computer's Score: 0");
      playLabelText[HUMAN_PLAYER1].setText("Computer's Score: 0");

      return endMessage;
   }

} // End Card Table Class.
