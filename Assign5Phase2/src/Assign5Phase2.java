//Assignment 5 CST 338
//John Coffelt, Michael Rose, Christopher Carson, Raul Ramirez
import javax.swing.*;
import java.util.Random;
import java.awt.*;
 
//Phase 2 - John Coffelt
public class Assign5Phase2
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   
   public static void main(String[] args)
   {   
      // establish main frame in which program will run
      CardTable myCardTable 
         = new CardTable("CardGame", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
      // CREATE LABELS ----------------------------------------------------
      //Computer and Player Hand.
      for(int i = 0; i < NUM_CARDS_PER_HAND; i++) 
      {
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         humanLabels[i] = new JLabel(GUICard.getIcon(generateRandomCard()));
      }

      //Play Area.
      for(int i = 0; i < NUM_PLAYERS; i++)
      {
         playedCardLabels[i] = new JLabel(GUICard.getIcon(generateRandomCard()));
         if(i == 0)
            playLabelText[i] = new JLabel("Computer");
         else if(i == 1)
            playLabelText[i] = new JLabel("You");
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
      
      for(JLabel label : humanLabels)
      {
         label.setVisible(true);
         myCardTable.pnlHumanHand.add(label);
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

//Class represents the image of the card.
class GUICard 
{
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   
   //Method loads the image depending on the card value/suit.
   static void loadCardIcons()
   {
      // build the file names ("AC.gif", "2C.gif", "3C.gif", "TC.gif", etc.)
      // in a SHORT loop. For each file name, read it in and use it to
      // instantiate each of the 57 Icons in the icon[] array. 
      for(int i = 0; i < iconCards.length; i++)
      {
         for(int j = 0; j < iconCards[0].length; j++)
         {
            iconCards[i][j] = new ImageIcon("images/" + turnIntIntoCardValue(i) 
               + turnIntIntoCardSuit(j) + ".gif");
         }      
      }
      //Set the image for the back of a card.
      iconBack = new ImageIcon("images/BK.gif");
      iconsLoaded = true;
   }
   
   //Method for getting an image from a Card input.
   public static Icon getIcon(Card card)
   {
      //Check to see if the icons have been loaded.
      if(!iconsLoaded)
         loadCardIcons();
      
      //Return the location of the card in the array based on its value/suit.
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }
   
   //Returns the image for the back of a card.
   public static Icon getBackCardIcon()
   {
      if(!iconsLoaded)
         loadCardIcons();
      
      return iconBack;
   }
   
   // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
   private static String turnIntIntoCardValue(int i)
   {
      String[] value = {"A", "2", "3", "4", "5", "6", "7", "8", "9",
            "T", "J", "Q", "K", "X"};
      
      if(i >=0 && i <= 13)
         return value[i];
      else
         return value[0]; //returns default value "A".
   }
   
   // turns 0 - 3 into "C", "D", "H", "S"
   private static String turnIntIntoCardSuit(int j)
   {
      String[] value = {"C", "D", "H", "S"};
      
      if(j >=0 && j <= 3)
         return value[j];
      else
         return value[0]; //Returns default value "C".
   }
   
   //Takes a card and returns its value as an integer.
   private static int valueAsInt(Card card)
   {
      char value = card.getValue(); 
      switch (value) 
      {
         case 'A': return 0;
         case 'T': return 9;
         case 'J': return 10;
         case 'Q': return 11;
         case 'K': return 12;
         case 'X': return 13;
         default: return value - 49; //-49 to account for ASCII code conversion and -1 offset.
      }
   }
   
   //Takes a card and returns its suit as an integer.
   private static int suitAsInt(Card card)
   {
      switch(card.getSuit())
      {
         case clubs: return 0;
         case diamonds: return 1;
         case hearts: return 2;
         case spades: return 3;
         default: return -1;
      }
   }
}//End GUICard Class

//Card class represents a single playing card consisting of a suit and a value (Ex. Ace of Spades).
class Card
{
   //Holds the suit data
   public enum Suit {clubs, diamonds, hearts, spades};
   
   //Private variables
   private char value;
   private Suit suit;
   private boolean errorFlag;
   
   //public variable for the rank of each card value.
   public static char[] valuRanks = {'X','A','2','3','4','5','6','7',
         '8','9','T','J','Q','K'};
   
   //Constructor that sets suit and value for card
   public Card(char value, Suit suit)
   {
      set(value,suit);
   }
   
   //Default card no values set if data is bad
   //Set data if errorFlag is true using mutator
   //No values set if data is bad
   public Card() 
   {
      this('A', Suit.spades);
   }
   
   //Mutator that accepts valid values
   public boolean set(char value, Suit suit)
   {
      char upperValue = Character.toUpperCase(value);
      
      //Set the errorFlag to true if the card isn't valid.
      errorFlag = !isValid(upperValue, suit);
      if (!errorFlag) 
      {
         this.value = upperValue;
         this.suit = suit;
      }
      return !errorFlag;
   }
   
   //Accessor for the card's suit
   public Suit getSuit() 
   {
      return suit;
   }
   
   //Accessor for the card's value
   public char getValue() 
   {
      return value;
   }
   
   //Accessor for the error flag
   public boolean getErrorFlag() 
   {
      return errorFlag;
   }
   
   //Checks if two cards are equal
   public boolean equals(Card card1) 
   {
      return (card1.getSuit() == suit && card1.getValue() == value);
   }
   
   //Checks if the card is valid
   private boolean isValid(char value, Suit suit)
   {
      char[] VALID_VALUES = {'A','2','3','4','5','6','7','8','9','T','J','Q','K','X'};
       
      for(char c : VALID_VALUES) 
      {
         if(value == c) 
         {
             return true;
         }
      }
      return false;
   }
   
   //Takes a card array and its size and sorts it by card "rank" using a bubble sort.
   //The higher rank cards should be at the end of the array.
   public static void arraySort(Card[] cardArray, int arraySize) 
   {
      for(int i = 0; i < arraySize; i++) 
      {
         for(int j = 0; j < arraySize - i; j++)
         {
            if(getCardValueRank(cardArray[j]) > getCardValueRank(cardArray[j + 1]))
            {
               Card temp = cardArray[j];
               cardArray[j] = cardArray[j + 1];
               cardArray[j + 1] = temp;
            }
         }
      }
   }
   
   //Takes a card and returns the rank of its value.
   private static int getCardValueRank(Card card)
   {
      int i;
      for(i = 0; i < valuRanks.length; i++)
      {
         if(card.getValue() == valuRanks[i])
            break;
      }
      return i;
   }
   
   //Outputs a string representation of the card.
   public String toString() 
   {
      //Outputs invalid if the errorFlag is set to true.
      if(errorFlag) 
      {
         return "** invalid **";
      }
      else 
      {
         return value + " of " + suit;
      }
   }
} //End Card Class

//Hand class represents the cards held by a single player.
class Hand
{
   //Our public and private variables
   public static final int MAX_CARDS = 50;
   private Card[] myCards = new Card[MAX_CARDS];
   private int numCards;
   
   //The Constructor
   public Hand()
   {
      numCards = 0;
   }
   
   //Method to rest all card values to null and numCards to 0
   public void resetHand()
   {
      numCards = 0;
      for (int i = 0; i < myCards.length; i++)
      {
         myCards[i] = null;
      }
   }
   
   //Method to take a card and put it in the hand. Returns false if hand is full
   public boolean takeCard(Card card)
   {
      if (numCards < MAX_CARDS)
      {
         Card newCard = card;
         myCards[numCards] = newCard;
         numCards++;
         return true;
      }
      else
      {
         return false;
      }
   }
   
   //A method to play a card, removing it from the hand
   public Card playCard(int cardIndex)
   {
      numCards--;
      Card returnCard = myCards[cardIndex];
      myCards[cardIndex] = null;
      for(int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }
      
      return returnCard;
   }
   
   //Sorts the hand by each card's rank.
   public void sort()
   {
      Card.arraySort(myCards, numCards);
   }
   
   //A method to return a string of all the cards in the hand
   public String toString()
   {
      String returnString = "Hand = (";
      
      if (numCards > 0)
      {
         returnString = returnString + myCards[0].toString();
       
         //This counter is used to create a line break so the output is easier to read
         int count = 0;
         for (int i = 1; i < numCards; i++)
         {
            count++;
            if (count > 5)
            {
               returnString = returnString + ",\n" + myCards[i].toString();
               count = 0;
            }
            else
            {
               returnString = returnString + ", " + myCards[i].toString();
            }
         }
      }
      
      return returnString + ")";
   }
   
   //Accessor for numCards
   public int getNumCards() 
   {
      return numCards;
   }
   
   //Method to return the card from a position in the hand
   public Card inspectCard(int k)
   {  
      if (k <= numCards)
      {
         return myCards[k];
      }
      else
      {
         //This is our errorFlag card we return if the position is invalid
         return new Card('Z', Card.Suit.diamonds);
      }
   }
} //End Hand Class

//Deck class represents the source of playing cards that the players are pulling cards from.
class Deck
{
   //Member variables
   public static final int CARDS_IN_PACK = 56;
   public static final int MAX_CARDS = 6 * CARDS_IN_PACK;
   
   private static Card[] masterPack = new Card[CARDS_IN_PACK]; //Standard cards + 4 jokers.
   private Card[] cards = new Card[MAX_CARDS];
   private int topCard, numPacks;
   
   //Private boolean to check to see if master pack has been created already.
   private static boolean isMasterPackAllocated = false;
   
   //Constructors 
   public Deck(int numPacks)
   {
      allocateMasterPack();
      this.numPacks = numPacks;
      topCard = (CARDS_IN_PACK * numPacks) - 1;
      for(int i = 0; i <= topCard; i++)
      {
         cards[i] = masterPack[i % CARDS_IN_PACK];
      }
   }
   
   public Deck()
   {
      allocateMasterPack();
      numPacks = 1;
      topCard = (CARDS_IN_PACK * numPacks) - 1;
      for(int i = 0; i <= topCard; i++)
      {
         cards[i] = masterPack[i];
      }
   }
   
   //Re-populate cards[] with the standard 52 x numPacks cards.
   public void init(int numPacks)
   {
      this.numPacks = numPacks;
      topCard = (CARDS_IN_PACK * this.numPacks) - 1;
      for(int i = 0; i <= topCard; i++)
      {
         cards[i] = masterPack[i % CARDS_IN_PACK];
      }
   }
   
   //This function shuffles the deck.
   public void shuffle()
   {
      //Create random number generator and temporary variables
      Random random = new Random();
      Card temp;
      int index;
      
      //Go through and swap parts of the array with random parts of the array.  TopCard is used to avoid nulls in the much bigger cards array.
      for(int i = 0; i <= this.topCard; i++)
      {
         index = random.nextInt(this.topCard + 1);
         temp = cards[index];
         cards[index] = cards[i];
         cards[i] = temp;
      }         
   }
   
   //Take a card off the top of the deck and return it after deleting it from the cards array
   public Card dealCard()
   {
      Card dealtCard = cards[topCard];
      cards[topCard] = null;
      topCard--;
      return dealtCard;
   }
   
   //Return the value of topCard
   public int getTopCard()
   {
      return this.topCard;
   }
   
   //Inspect a card at position k in the cards array.  if k is out of bounds or pointing to a null value in the cards array,
   //return a bad card.
   public Card inspectCard(int k)
   {
      if (k <= topCard)
      {
         return cards[k];
      }
      else
      {
        //This is our errorFlag card we return if the position is invalid.
         return new Card('Z', Card.Suit.diamonds);
      }
   }
   
   //Takes a card and adds it to the deck if the deck doesn't have too many cards
   //and if the deck does not have too many of that type of card.
   public boolean addCard(Card card)
   {
      //Find the number of cards of this type that the deck already contains.
      int count = 0;
      for(int i = 0; i <= topCard; i++)
      {
         if(cards[i].getSuit() == card.getSuit() && cards[i].getValue() == card.getValue())
            count++;
      }
      
      if(count >= numPacks || topCard + 1 == MAX_CARDS)
         return false;
      
      topCard++;
      cards[topCard] = card;
      return true;
   }
   
   //Takes a card and removes it from the deck. The top card takes its place.
   public boolean removeCard(Card card)
   {
      int index = -1;
      for(int i = 0; i <= topCard; i++)
      {
         if(cards[i].getSuit() == card.getSuit() && cards[i].getValue() == card.getValue())
            index = i;
      }
      
      if(index >= 0)
      {
         cards[index] = cards[topCard];
         topCard--;
      }
     
      return index >= 0;
   }
   
   //Sort the deck by each card's rank.
   public void sort()
   {
      Card.arraySort(cards, topCard);
   }
   
   //Return the total number of cards in the deck.
   public int getNumCards()
   {
      return topCard + 1;
   }
   
   //This function creates the master pack.  After this function has run, it sets the boolean variable to true
   //so the function will not run again.
   private static void allocateMasterPack()
   {
      if(isMasterPackAllocated == false)
      {
         char[] VALID_VALUES = {'A', '2','3','4','5','6','7','8','9','T','J','Q','K','X'};
         int i = 0;
         
         for(Card.Suit s : Card.Suit.values())
         {
            for(int j = 0; j < VALID_VALUES.length; j++)
            {
               masterPack[i] = new Card(VALID_VALUES[j], s);
               i++;
            }
         }
         //set isMasterPackAllocated to true to never run this function again.
         isMasterPackAllocated = true;
      }
   }
}//End Deck Class
