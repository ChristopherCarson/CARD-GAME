import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;


public class GameController implements ActionListener{ 
   
   GameModel.CardGameFramework highCardGame;
   GameView.CardTable myCardTable;
   
   public GameController(GameModel.CardGameFramework a, GameView.CardTable b ){
      
   highCardGame = a;
   myCardTable = b;
   
   }

   public void actionPerformed(ActionEvent e) { 
      //code that reacts to the action... 

      
            //Find the index of the card/button component.
            int cardIndex = 0;
            for(int i = 0; i < highCardGame.getHand(Assig6.HUMAN_PLAYER1).getNumCards(); i++)
            {
               if(e.getSource() == myCardTable.pnlHumanHand.getComponent(i))
                  cardIndex = i;
            }
            
            //Play the card
            GameModel.Card playerCard = highCardGame.playCard(Assig6.HUMAN_PLAYER1, cardIndex);
            Assig6.playedCardLabels[Assig6.HUMAN_PLAYER1].setIcon(GameModel.GUICard.getIcon(playerCard));
            Assig6.playedCardLabels[Assig6.HUMAN_PLAYER1].setVisible(true);
            
            //Draw from the deck.
            if(highCardGame.takeCard(Assig6.HUMAN_PLAYER1))
            {
               for(int i = cardIndex; i < Assig6.NUM_CARDS_PER_HAND - 1; i++)
               {
                  Assig6.humanLabels[i] = Assig6.humanLabels[i + 1];
                  Assig6.playCardButtons[i].setIcon(Assig6.humanLabels[i + 1]);
               }
               Assig6.humanLabels[Assig6.NUM_CARDS_PER_HAND - 1] = 
                  (ImageIcon) GameModel.GUICard.getIcon(highCardGame.getHand(1).inspectCard(Assig6.NUM_CARDS_PER_HAND - 1));
               Assig6.playCardButtons[Assig6.NUM_CARDS_PER_HAND - 1].setIcon(Assig6.humanLabels[Assig6.NUM_CARDS_PER_HAND - 1]);
            }
            else
               myCardTable.pnlHumanHand.remove(cardIndex);
                  
            //Play the computer card.
            int index = GameModel.comPlay(highCardGame.getHand(Assig6.COMPUTER_PLAYER1), playerCard);
            GameModel.Card computerCard = highCardGame.playCard(Assig6.COMPUTER_PLAYER1,index);
            Assig6.playedCardLabels[Assig6.COMPUTER_PLAYER1].setIcon(GameModel.GUICard.getIcon(computerCard));
            Assig6.playedCardLabels[Assig6.COMPUTER_PLAYER1].setVisible(true);
            
            //Take from the deck.
            if(!highCardGame.takeCard(Assig6.COMPUTER_PLAYER1))
               myCardTable.pnlComputerHand.remove(index);
            
            //Update score.
            int score = GameModel.scoreCards(playerCard,computerCard);
            GameModel.updateScore(score);
            
            myCardTable.revalidate();
            myCardTable.repaint();
      
  }


}
