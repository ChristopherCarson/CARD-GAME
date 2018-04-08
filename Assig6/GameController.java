import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameController implements ActionListener
{
   GameModel gameModel;
   GameView gameView;
   String endMessage;
   int numCardsPerHand = GameModel.numCardsPerHand;
   int result = JOptionPane.YES_OPTION;
   int HUMAN_PLAYER1 = GameModel.HUMAN_PLAYER1;
   int COMPUTER_PLAYER1 = GameModel.COMPUTER_PLAYER1;

   public GameController(GameModel model, GameView view)
   {
      gameModel = model;
      gameView = view;
   }

   public void actionPerformed(ActionEvent e)
   {
      // code that reacts to the action...

      // Find the index of the card/button component.
      int cardIndex = 0;
      for (int i = 0; i < gameModel.getHand(HUMAN_PLAYER1).getNumCards(); i++)
      {
         if (e.getSource() == GameView.pnlHumanHand.getComponent(i))
            cardIndex = i;
      }
      gameModel.playCards(cardIndex);
      gameView.revalidate();
      gameView.repaint();
   }

   public void createHands()
   {
      for (int i = 0; i < numCardsPerHand; i++)
      {
         GameView.computerLabels[i] = new JLabel(GameModel.GUICard.getBackCardIcon());
         GameView.computerLabels[i].setVisible(true);

         GameView.pnlComputerHand.add(GameView.computerLabels[i]);
         GameView.humanLabels[i] = (ImageIcon) GameModel.GUICard.getIcon(gameModel.getHand(1).inspectCard(i));
         GameModel.playCardButtons[i] = new JButton(GameView.humanLabels[i]);
         GameModel.playCardButtons[i].addActionListener(this);
         GameModel.playCardButtons[i].setVisible(true);
         GameView.pnlHumanHand.add(GameModel.playCardButtons[i]);
      }
   }

   public void StartGame()
   {
      while (result == JOptionPane.YES_OPTION)
      {
         gameModel.newGame();
         gameModel.deal();
         createHands();

         // Show everything to the user
         gameView.setVisible(true);
         gameView.revalidate();
         gameView.repaint();

         // Run the game
         while (gameModel.getHand(COMPUTER_PLAYER1).getNumCards() > 0
               || gameModel.getHand(HUMAN_PLAYER1).getNumCards() > 0)
            try
            {
               Thread.sleep(100);
            } catch (InterruptedException ie)
            {
            }

         endMessage = gameView.EndGame();
         // Replay the game?
         result = JOptionPane.showConfirmDialog(null, endMessage, "Game Over", JOptionPane.YES_NO_OPTION);
         GameModel.playerScores[COMPUTER_PLAYER1] = 0;
         GameModel.playerScores[HUMAN_PLAYER1] = 0;
      }
   }

}
