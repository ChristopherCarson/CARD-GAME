import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class GameView {

//CardTable - Class represents the GUI for a card table.
static class CardTable extends JFrame



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
 }//CardTable Constructor
 
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



}//End (outer) GameView Class
