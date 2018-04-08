import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;

//Controller class for the card game.
class Controller implements ActionListener, MouseListener
{
   static final int HUMAN_PLAYER    = 1;
   static final int COMPUTER_PLAYER = 0;
   private Model    cardGameModel;
   private View     cardGameView;

   // Constructor sets the model and view objects.
   public Controller(Model model, View view)
   {
      cardGameModel = model;
      cardGameView = view;
   }

   // Readies the model and view objects.
   public void init()
   {
      cardGameModel.startGame();
      cardGameView.updateHand(cardGameModel.getHandIcons(COMPUTER_PLAYER), COMPUTER_PLAYER);
      cardGameView.updateHand(cardGameModel.getHandIcons(HUMAN_PLAYER), HUMAN_PLAYER);
      cardGameView.updateScore(0, HUMAN_PLAYER);
      cardGameView.updateScore(0, COMPUTER_PLAYER);
      cardGameView.updateStack(cardGameModel.getStackIcons());
      cardGameView.setVisible(true);
   }

   // Actions performed on click of the button.
   @Override
   public void actionPerformed(ActionEvent e)
   {
      // For cards in the player's hand.
      if (e.getActionCommand() == "Human Player Card" && cardGameModel.getPlayerTurn() == HUMAN_PLAYER)
      {
         // Check to make sure the button isn't already selected.
         if (cardGameView.getPlayerButtonIndex((JButton) e.getSource()) != cardGameModel.getSelected())
            cardGameModel.setSelected(cardGameView.getPlayerButtonIndex((JButton) e.getSource()));
         else
            // Unselect if already selected.
            cardGameModel.setSelected(-1);

         // Update view with selected button.
         cardGameView.setSelectedButton(cardGameModel.getSelected());
      }

      if (e.getActionCommand() == "I Cannot Play" && cardGameModel.getPlayerTurn() == HUMAN_PLAYER)
      {
         // End the players turn.
         cardGameModel.endTurn(false); // False since no move was made.
         if (cardGameModel.getPlayerTurn() == COMPUTER_PLAYER)
            computerTurn();
      }

      if (cardGameModel.isGameOver())
      {
         // If the player chooses to play again rerun the init method.
         if (cardGameView.loadEndGamePrompt() == 0)
            init();
         else
            System.exit(0);
      }
   }

   // Mouse clicks events matter when player clicks on one of the cards on a stack.
   @Override
   public void mouseClicked(MouseEvent arg0)
   {
      if (arg0.getComponent().getName() == "Play Area Card Stack" && cardGameModel.getSelected() >= 0
            && cardGameModel.getPlayerTurn() == HUMAN_PLAYER)
      {
         // If the player is making a valid move.
         if (cardGameModel.addCardToStack(cardGameView.getStackIndex((JLabel) arg0.getComponent())))
         {
            cardGameView.updateStack(cardGameModel.getStackIcons());
            cardGameView.updateHand(cardGameModel.getHandIcons(HUMAN_PLAYER), HUMAN_PLAYER);
            cardGameModel.endTurn(true); // True since a move was made.
            cardGameView.updateScore(cardGameModel.getPlayerScore(HUMAN_PLAYER), HUMAN_PLAYER);
         }
         cardGameView.setSelectedButton(cardGameModel.getSelected());

         if (cardGameModel.getPlayerTurn() == COMPUTER_PLAYER && !cardGameModel.isGameOver())
            computerTurn();
      }

      if (cardGameModel.isGameOver())
      {
         // If the player chooses to play again rerun the init method.
         if (cardGameView.loadEndGamePrompt() == 0)
            init();
         else
            System.exit(0);
      }
   }

   // Simulates a computer turn.
   private void computerTurn()
   {
      cardGameModel.startComputerTurn();
      cardGameView.updateStack(cardGameModel.getStackIcons());
      cardGameView.updateHand(cardGameModel.getHandIcons(COMPUTER_PLAYER), COMPUTER_PLAYER);
      cardGameView.updateScore(cardGameModel.getPlayerScore(COMPUTER_PLAYER), COMPUTER_PLAYER);
   }

   @Override
   public void mouseEntered(MouseEvent e) {}

   @Override
   public void mouseExited(MouseEvent e) {}

   @Override
   public void mousePressed(MouseEvent e){}

   @Override
   public void mouseReleased(MouseEvent e) {}
}
