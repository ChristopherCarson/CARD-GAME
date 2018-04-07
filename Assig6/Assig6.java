//Assignment 6 CST 338
//John Coffelt, Michael Rose, Christopher Carson, Raul Ramirez
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
//Phase 3 - Michael Rose
public class Assig6
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static final int HUMAN_PLAYER1 = 1;
   static final int COMPUTER_PLAYER1 = 0;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static ImageIcon[] humanLabels = new ImageIcon[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   static JButton[] playCardButtons = new JButton[NUM_CARDS_PER_HAND];
   static int[] playerScores = new int[NUM_PLAYERS];
   
   static int cardInHand = 0;
   
   

   
   public static void main(String[] args) throws InterruptedException
   {   
      // establish main frame in which program will run
      GameView.CardTable myCardTable 
         = new GameView.CardTable("CardGame", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //CREATE THE PLAY AREA.
      //Create a grid layout for the play and card areas.
      GridLayout playAreaGrid = new GridLayout(2, NUM_PLAYERS);
      myCardTable.pnlPlayArea.setLayout(playAreaGrid);
      
      GridLayout cardAreaGrid = new GridLayout(1,NUM_CARDS_PER_HAND);
      myCardTable.pnlComputerHand.setLayout(cardAreaGrid);
      myCardTable.pnlHumanHand.setLayout(cardAreaGrid);
      
      //Play Area.
      for(int i = 0; i < NUM_PLAYERS; i++)
      {
         playedCardLabels[i] = new JLabel();
         if(i == 0)
            playLabelText[i] = new JLabel("Computer's Score: 0");
         else if(i == 1)
            playLabelText[i] = new JLabel("Your Score: 0");
         else
            playLabelText[i] = new JLabel("Player " + i);
      }
      
      //Add icons first...
      for(int i = 0; i < NUM_PLAYERS; i++)
      {
         playedCardLabels[i].setVisible(false);
         playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
         myCardTable.pnlPlayArea.add(playedCardLabels[i]);
      }
      
      //And then text.
      for(int i = 0; i < NUM_PLAYERS; i++)
      {
         playLabelText[i].setVisible(true);
         playLabelText[i].setHorizontalAlignment(JLabel.CENTER);
         myCardTable.pnlPlayArea.add(playLabelText[i]);
      }
      //END PLAY AREA
 
      
      //CREATE THE GAME
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      GameModel.Card[] unusedCardsPerPack = null;
      
      GameModel.CardGameFramework highCardGame = new GameModel.CardGameFramework( 
            numPacksPerDeck, numJokersPerPack,  
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      

      GameController buttonAction = new GameController();
      
      
  
      //START THE GAME
      int result = JOptionPane.YES_OPTION;
      while(result == JOptionPane.YES_OPTION)
      {
         highCardGame.newGame();
         highCardGame.deal();
         
         //Create the Computer and Player Hand.
         for(int i = 0; i < NUM_CARDS_PER_HAND; i++) 
         {
            computerLabels[i] = new JLabel(GameModel.GUICard.getBackCardIcon());
            computerLabels[i].setVisible(true);
            myCardTable.pnlComputerHand.add(computerLabels[i]);
            
            humanLabels[i] = (ImageIcon) GameModel.GUICard.getIcon(highCardGame.getHand(1).inspectCard(i));
            playCardButtons[i] = new JButton(humanLabels[i]);
            playCardButtons[i].addActionListener(buttonAction);
            playCardButtons[i].setVisible(true);
            myCardTable.pnlHumanHand.add(playCardButtons[i]);
         }
         
         //Show everything to the user
         myCardTable.setVisible(true);
         myCardTable.revalidate();
         myCardTable.repaint();
         
         //Run the game
         while(highCardGame.getHand(COMPUTER_PLAYER1).getNumCards() > 0 || 
               highCardGame.getHand(HUMAN_PLAYER1).getNumCards() > 0)
            Thread.sleep(100);
         
         //End the game
         String endMessage;
         if(playerScores[COMPUTER_PLAYER1] > playerScores[HUMAN_PLAYER1])
            endMessage = "Computer wins!\nYour score was: " + playerScores[HUMAN_PLAYER1] + "\nComputer's Score was: " + playerScores[COMPUTER_PLAYER1];
         else if(playerScores[COMPUTER_PLAYER1] < playerScores[1])
            endMessage = "You win!\nYour score was: " + playerScores[HUMAN_PLAYER1] + "\nComputer's Score was: " + playerScores[COMPUTER_PLAYER1];
         else
            endMessage = "Tie!\nYour score was: " + playerScores[HUMAN_PLAYER1] + "\nComputer's Score was: " + playerScores[COMPUTER_PLAYER1];
         endMessage = endMessage + "\nDo you want to play again?";
         
         //Replay the game?
         result = JOptionPane.showConfirmDialog(null, endMessage, "Game Over", JOptionPane.YES_NO_OPTION);
         
         playerScores[COMPUTER_PLAYER1] = 0;
         playerScores[HUMAN_PLAYER1] = 0;
         playLabelText[COMPUTER_PLAYER1].setText("Computer's Score: 0");
         playLabelText[HUMAN_PLAYER1].setText("Computer's Score: 0");
      }
      System.exit(0);
   }//end Main
   

   
   



    
   
   
   
   
}//End Main






