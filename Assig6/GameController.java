import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;


public class GameController implements ActionListener{ 
   

   public void actionPerformed(ActionEvent e) { 
      //code that reacts to the action... 
      
   /*
      
            //Find the index of the card/button component.
            int cardIndex = 0;
            for(int i = 0; i < highCardGame.getHand(Assig6.HUMAN_PLAYER1).getNumCards(); i++)
            {
               if(e.getSource() == myCardTable.pnlHumanHand.getComponent(i))
               //   if(e.getSource() == myCardTable.pnlHumanHand.getComponent(i))
                  cardIndex = i;
            }
            
            //Play the card
            GameModel.Card playerCard = highCardGame.playCard(HUMAN_PLAYER1, cardIndex);
            playedCardLabels[HUMAN_PLAYER1].setIcon(GameModel.GUICard.getIcon(playerCard));
            playedCardLabels[HUMAN_PLAYER1].setVisible(true);
            
            //Draw from the deck.
            if(highCardGame.takeCard(HUMAN_PLAYER1))
            {
               for(int i = cardIndex; i < NUM_CARDS_PER_HAND - 1; i++)
               {
                  humanLabels[i] = humanLabels[i + 1];
                  playCardButtons[i].setIcon(humanLabels[i + 1]);
               }
               humanLabels[NUM_CARDS_PER_HAND - 1] = 
                  (ImageIcon) GameModel.GUICard.getIcon(highCardGame.getHand(1).inspectCard(NUM_CARDS_PER_HAND - 1));
               playCardButtons[NUM_CARDS_PER_HAND - 1].setIcon(humanLabels[NUM_CARDS_PER_HAND - 1]);
            }
            else
               myCardTable.pnlHumanHand.remove(cardIndex);
                  
            //Play the computer card.
            int index = GameModel.comPlay(highCardGame.getHand(COMPUTER_PLAYER1), playerCard);
            GameModel.Card computerCard = highCardGame.playCard(COMPUTER_PLAYER1,index);
            playedCardLabels[COMPUTER_PLAYER1].setIcon(GameModel.GUICard.getIcon(computerCard));
            playedCardLabels[COMPUTER_PLAYER1].setVisible(true);
            
            //Take from the deck.
            if(!highCardGame.takeCard(COMPUTER_PLAYER1))
               myCardTable.pnlComputerHand.remove(index);
            
            //Update score.
            int score = GameModel.scoreCards(playerCard,computerCard);
            GameModel.updateScore(score);
            
            myCardTable.revalidate();
            myCardTable.repaint();

      
      */
      
      System.out.println("Button was pushed");
      
      
  }



}
