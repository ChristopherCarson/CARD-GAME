//Assignment 5 CST 338
//John Coffelt, Michael Rose, Christopher Carson, Raul Ramirez
import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
 
//Phase 3 - Michael
public class Assign5Phase3
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static ImageIcon[] humanLabels = new ImageIcon[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   static JButton[] playCardButtons = new JButton[NUM_CARDS_PER_HAND];
   
   public static void main(String[] args)
   {   
      
      // establish main frame in which program will run
      CardTable myCardTable 
         = new CardTable("CardGame", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);
      
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      
      //variables for game
      boolean humanFirst = true;
      int player1Score = 0, player2Score = 0;
      Scanner scan = new Scanner(System.in);
      Random rand = new Random();

      CardGameFramework highCardGame = new CardGameFramework( 
            numPacksPerDeck, numJokersPerPack,  
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      
      highCardGame.deal();
      // CREATE LABELS ----------------------------------------------------
      //Computer and Player Hand.
      for(int i = 0; i < NUM_CARDS_PER_HAND; i++) 
      {
         final int temp = i;
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         humanLabels[i] = (ImageIcon) GUICard.getIcon(highCardGame.getHand(1).inspectCard(i));
         playCardButtons[i] = new JButton(humanLabels[i]);
         playCardButtons[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               String text = highCardGame.getHand(1).inspectCard(temp).toString();
               JOptionPane.showMessageDialog(null, text);
            }
         });
      }
      
      
      //Play Area.
      for(int i = 0; i < NUM_PLAYERS; i++)
      {
         playedCardLabels[i] = new JLabel(GUICard.getIcon(generateRandomCard()));
         if(i == 0)
            playLabelText[i] = new JLabel("Computer's Score: " + player1Score);
         else if(i == 1)
            playLabelText[i] = new JLabel("Your Score: " + player2Score);
         else
            playLabelText[i] = new JLabel("Player " + i);
      }
      
      // ADD LABELS TO PANELS -----------------------------------------
      //Computer and Player Hand.
      for(JLabel label : computerLabels)
      {
         label.setVisible(true);
         myCardTable.pnlComputerHand.add(label);
      }
      
      for(JButton buttons : playCardButtons)
      {
         buttons.setVisible(true);
         GridLayout cardAreaGrid = new GridLayout(1,NUM_CARDS_PER_HAND);
         myCardTable.pnlHumanHand.setLayout(cardAreaGrid);
         myCardTable.pnlHumanHand.add(buttons);
      }
      
      //Create a grid layout for the play area
      GridLayout playAreaGrid = new GridLayout(2, NUM_PLAYERS);
      myCardTable.pnlPlayArea.setLayout(playAreaGrid);
      
      //Add icons first...
      for(int i = 0; i < NUM_PLAYERS; i++)
      {
         playedCardLabels[i].setVisible(true);
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

      // show everything to the user
      myCardTable.setVisible(true);
      
      //text version of high card game made to see if I could actually convert it into the GUI
      //stops when both players are out of cards. ideally you want to stop when the deck and hands are empty.
      while(highCardGame.getHand(0).getNumCards() > 0 || highCardGame.getHand(1).getNumCards() > 0)
      {
         if(humanFirst)
         {
            System.out.println("You go first.");
            System.out.println(highCardGame.getHand(1));
            System.out.print("Pick which card you want.");
            int index = scan.nextInt();
            while(index > highCardGame.getHand(1).getNumCards() || index < 0)
            {
               System.out.print("Pick which card you want");
               index = scan.nextInt();
            }
            Card playedCard = highCardGame.playCard(1, index);
            

            int randInt = rand.nextInt(highCardGame.getHand(0).getNumCards());
            
            Card comPlayedCard = highCardGame.playCard(0,randInt);
            System.out.println("Computer played: " + comPlayedCard);
            if(playedCard.getValue() > comPlayedCard.getValue())
            {
               humanFirst = true;
               System.out.println("You won the round\n");
               player2Score = player2Score + 2;
            }
            else if(playedCard.getValue() < comPlayedCard.getValue())
            {
               humanFirst = false;
               System.out.println("You lost the round\n");
               player1Score = player1Score + 2;
            }
            else
            {
               System.out.println("Tie\n");
               player1Score = player1Score + 1;
               player2Score = player2Score + 1;
            }
         }
         else
         {
            int randInt = rand.nextInt(highCardGame.getHand(0).getNumCards());
            Card comPlayedCard = highCardGame.playCard(0,randInt);
            
            System.out.println("Computer played: " + comPlayedCard);
            
            System.out.println(highCardGame.getHand(1));
            System.out.print("Pick which card you want.\n");
            int index = scan.nextInt();
            while(index > highCardGame.getHand(1).getNumCards() || index < 0)
            {
               System.out.print("Pick which card you want\n");
               index = scan.nextInt();
            }
            Card playedCard = highCardGame.playCard(1, index);
            
            if(playedCard.getValue() > comPlayedCard.getValue())
            {
               System.out.println("You won the round\n");
               humanFirst = true;
               player2Score = player2Score + 2;
            }
            else if(playedCard.getValue() < comPlayedCard.getValue())
            {
               System.out.println("You lost the round\n");
               humanFirst = false;
               player1Score = player1Score + 2;
            }
            else
            {
               System.out.println("Tie\n");
               player1Score = player1Score + 1;
               player2Score = player2Score + 1;
            }
         }
         myCardTable.revalidate();
         myCardTable.repaint();
      }
      System.out.println("Game over");
      if(player1Score > player2Score)
         System.out.println("Computer wins\nYour score was: " + player2Score + "\nComputer's Score was: " + player1Score);
      else if(player1Score < player2Score)
         System.out.println("You win\nYour score was: " + player2Score + "\nComputer's Score was: " + player1Score);
      else
         System.out.println("Tie\nYour score was: " + player2Score + "\nComputer's Score was: " + player1Score);
      scan.close();
   }
   
   
   //Returns a randomly selected card.
   static private Card generateRandomCard()
   {
      Random rand = new Random();
      int randInt = rand.nextInt(14);
      char value;
      
      //Get the random value.
      switch(randInt)
      {
         case 0: 
            value = 'A';
            break;
         case 9: 
            value = 'T';
            break;
         case 10: 
            value = 'J';
            break;
         case 11: 
            value = 'Q';
            break;
         case 12: 
            value = 'K';
            break;
         case 13: 
            value = 'X';
            break;
         default: 
            value = (char)(randInt + 49); //+49 to account for ASCII code and +1 offset.
            break;
      }
      
      //Get the random suit.
      randInt = rand.nextInt(3);
      switch(randInt)
      {
         case 0: return new Card(value, Card.Suit.clubs);
         case 1: return new Card(value, Card.Suit.diamonds);
         case 2: return new Card(value, Card.Suit.hearts);
         default: return new Card(value, Card.Suit.spades);
      }
   }//End GenerateRandomCard
   
}//End Main

//class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
    private static final int MAX_PLAYERS = 50;
   
    private int numPlayers;
    private int numPacks;            // # standard 52-card packs per deck
                                     // ignoring jokers or unused cards
    private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
    private int numUnusedCardsPerPack;  // # cards removed from each pack
    private int numCardsPerHand;        // # cards to deal each player
    private Deck deck;               // holds the initial full deck and gets
                                     // smaller (usually) during play
    private Hand[] hand;             // one Hand for each player
    private Card[] unusedCardsPerPack;   // an array holding the cards not used
                                         // in the game.  e.g. pinochle does not
                                         // use cards 2-8 of any suit
   
    public CardGameFramework( int numPacks, int numJokersPerPack,
          int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
          int numPlayers, int numCardsPerHand)
    {
       int k;
   
       // filter bad values
       if (numPacks < 1 || numPacks > 6)
          numPacks = 1;
       if (numJokersPerPack < 0 || numJokersPerPack > 4)
          numJokersPerPack = 0;
       if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
          numUnusedCardsPerPack = 0;
       if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
          numPlayers = 4;
       // one of many ways to assure at least one full deal to all players
       if  (numCardsPerHand < 1 ||
             numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
             / numPlayers )
          numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
   
       // allocate
       this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
       this.hand = new Hand[numPlayers];
       for (k = 0; k < numPlayers; k++)
          this.hand[k] = new Hand();
       deck = new Deck(numPacks);
   
       // assign to members
       this.numPacks = numPacks;
       this.numJokersPerPack = numJokersPerPack;
       this.numUnusedCardsPerPack = numUnusedCardsPerPack;
       this.numPlayers = numPlayers;
       this.numCardsPerHand = numCardsPerHand;
       for (k = 0; k < numUnusedCardsPerPack; k++)
          this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
   
       // prepare deck and shuffle
       newGame();
    }
   
    // constructor overload/default for game like bridge
    public CardGameFramework()
    {
       this(1, 0, 0, null, 4, 13);
    }
   
    public Hand getHand(int k)
    {
       // hands start from 0 like arrays
   
       // on error return automatic empty hand
       if (k < 0 || k >= numPlayers)
          return new Hand();
   
       return hand[k];
    }
   
    public Card getCardFromDeck() { return deck.dealCard(); }
   
    public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }
   
    public void newGame()
    {
       int k, j;
   
       // clear the hands
       for (k = 0; k < numPlayers; k++)
          hand[k].resetHand();
   
       // restock the deck
       deck.init(numPacks);
   
       // remove unused cards
       for (k = 0; k < numUnusedCardsPerPack; k++)
          deck.removeCard( unusedCardsPerPack[k] );
   
       // add jokers
       for (k = 0; k < numPacks; k++)
          for ( j = 0; j < numJokersPerPack; j++)
             deck.addCard( new Card('X', Card.Suit.values()[j]) );
   
       // shuffle the cards
       deck.shuffle();
    }
   
    public boolean deal()
    {
       // returns false if not enough cards, but deals what it can
       int k, j;
       boolean enoughCards;
   
       // clear all hands
       for (j = 0; j < numPlayers; j++)
          hand[j].resetHand();
   
       enoughCards = true;
       for (k = 0; k < numCardsPerHand && enoughCards ; k++)
       {
          for (j = 0; j < numPlayers; j++)
             if (deck.getNumCards() > 0)
                hand[j].takeCard( deck.dealCard() );
             else
             {
                enoughCards = false;
                break;
             }
       }
       
       
       return enoughCards;
    }
   
    void sortHands()
    {
       int k;
   
       for (k = 0; k < numPlayers; k++)
          hand[k].sort();
    }
   
    Card playCard(int playerIndex, int cardIndex)
    {
       // returns bad card if either argument is bad
       if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
           cardIndex < 0 || cardIndex > numCardsPerHand - 1)
       {
          //Creates a card that does not work
          return new Card('M', Card.Suit.spades);      
       }
    
       // return the card played
       return hand[playerIndex].playCard(cardIndex);
    
    }
   
   
    boolean takeCard(int playerIndex)
    {
       // returns false if either argument is bad
       if (playerIndex < 0 || playerIndex > numPlayers - 1)
          return false;
      
        // Are there enough Cards?
        if (deck.getNumCards() <= 0)
           return false;
   
        return hand[playerIndex].takeCard(deck.dealCard());
    }
}