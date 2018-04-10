import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;

//Controller class for the card game.
class Controller implements ActionListener, MouseListener
{
   static final int HUMAN_PLAYER = 1;
   static final int COMPUTER_PLAYER = 0;
   private Model cardGameModel;
   private View cardGameView;

   // Constructor sets the model and view objects.
   public Controller(Model model, View view)
   {
      cardGameModel = model;
      cardGameView = view;
      
      cardGameView.setDeckImage(cardGameModel.getBackCardIcon());
   }

   // Readies the model and view objects.
   public void init()
   {
      cardGameModel.startGame();
      cardGameView.resetTimer();
      cardGameView.updateHand(cardGameModel.getHandIcons(HUMAN_PLAYER), HUMAN_PLAYER);
      cardGameView.updateScore(cardGameModel.getPlayerScore(HUMAN_PLAYER), HUMAN_PLAYER);
      cardGameView.updateHand(cardGameModel.getHandIcons(COMPUTER_PLAYER), COMPUTER_PLAYER);
      cardGameView.updateScore(cardGameModel.getPlayerScore(COMPUTER_PLAYER), COMPUTER_PLAYER);
      cardGameView.updateStack(cardGameModel.getStackIcons());
      cardGameView.updateDeckCount(cardGameModel.getCardsInDeckCount());
      cardGameView.setVisible(true);
   }

   // Simulates a computer turn.
   private void computerTurn()
   {
      cardGameModel.startComputerTurn();
      cardGameView.updateStack(cardGameModel.getStackIcons());
      cardGameView.updateHand(cardGameModel.getHandIcons(COMPUTER_PLAYER), COMPUTER_PLAYER);
      cardGameView.updateScore(cardGameModel.getPlayerScore(COMPUTER_PLAYER), COMPUTER_PLAYER);
      cardGameView.updateDeckCount(cardGameModel.getCardsInDeckCount());
   }
   
   //Checks the game's state and prompts user if game is over.
   private void checkGameState()
   {
      if (cardGameModel.isGameOver())
      {
         cardGameModel.setSelectedCard(-1);
         cardGameView.updateSelectedButton(cardGameModel.getSelectedCardIndex());
         
         // If the player chooses to play again rerun the init method.
         if (cardGameView.loadEndGamePrompt(cardGameModel.getPlayerScores()) == 0)
            init();
         else
            System.exit(0);
      }
   }
   
   // Actions performed on click of the button.
   @Override
   public void actionPerformed(ActionEvent e)
   {
      // For cards in the player's hand.
      if (e.getActionCommand() == "Human Player Card" && cardGameModel.getPlayerTurn() == HUMAN_PLAYER)
      {
         // Check to make sure the button isn't already selected.
         if (cardGameView.getPlayerButtonIndex((JButton) e.getSource()) != cardGameModel.getSelectedCardIndex())
            cardGameModel.setSelectedCard(cardGameView.getPlayerButtonIndex((JButton) e.getSource()));
         else
            // Unselect if already selected.
            cardGameModel.setSelectedCard(-1);

         // Update view with selected button.
         cardGameView.updateSelectedButton(cardGameModel.getSelectedCardIndex());
      }

      if (e.getActionCommand() == "I Cannot Play" && cardGameModel.getPlayerTurn() == HUMAN_PLAYER)
      {
         // End the players turn.
         cardGameModel.endTurn(false); // False since no move was made.
         // Update selected card.
         cardGameView.updateSelectedButton(cardGameModel.getSelectedCardIndex());
         if (cardGameModel.getPlayerTurn() == COMPUTER_PLAYER)
            computerTurn();
      }

      if (e.getActionCommand() == "Start Timer")
         cardGameView.startTimer();
      
      checkGameState();
   }//End Actions Performed

   // Mouse pressed events matter when player clicks on one of the cards on a stack.
   @Override
   public void mousePressed(MouseEvent e)
   {
      if (e.getComponent().getName() == "Play Area Card Stack" && cardGameModel.getSelectedCardIndex() >= 0
            && cardGameModel.getPlayerTurn() == HUMAN_PLAYER)
      {
         //Check that the player made a successful move before moving on.
         if (cardGameModel.addCardToStack(cardGameView.getStackIndex((JLabel) e.getComponent())))
         {
            cardGameModel.endTurn(true); // True since a move was made.
            cardGameView.updateStack(cardGameModel.getStackIcons());
            
            cardGameView.updateHand(cardGameModel.getHandIcons(HUMAN_PLAYER), HUMAN_PLAYER);
            cardGameView.updateScore(cardGameModel.getPlayerScore(HUMAN_PLAYER), HUMAN_PLAYER);
            cardGameView.updateDeckCount(cardGameModel.getCardsInDeckCount());
         }
         cardGameView.updateSelectedButton(cardGameModel.getSelectedCardIndex());

         if (cardGameModel.getPlayerTurn() == COMPUTER_PLAYER && !cardGameModel.isGameOver())
            computerTurn();
      }

      checkGameState();
   }//End Mouse Pressed

   @Override
   public void mouseEntered(MouseEvent e)
   {
   }

   @Override
   public void mouseExited(MouseEvent e)
   {
   }

   @Override
   public void mouseClicked(MouseEvent arg0)
   {
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
   }
}//End Controller Class
