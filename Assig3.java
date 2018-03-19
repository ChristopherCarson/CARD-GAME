//Team Code Blooded
//Raul Ramirez, Chris Carson, Michael Rose and John Coffelt
import java.util.*;

//Begin class Assig3
public class Assig3 
{
   //This is a helper method that returns a random card to fill the testing hand
   static private Card randomCard(Card card1, Card card2, Card card3, Card card4, Card card5)
   {
      Random rand = new Random();
      int randNumber = rand.nextInt(5) + 1;

      switch (randNumber) //Our switch statement to convert the random integer into a string
      {
         case 1:  return card1;
         case 2:  return card2;
         case 3:  return card3;
         case 4:  return card4;
         case 5:  return card5;
         default: return card1;//this is never used, but the program would not compile without it
      }
   }
   //Our main method
	public static void main(String[] args) 
	{
	   //Testing of the Card class
	   Card card1 = new Card();
	   Card card2 = new Card('4', Card.Suit.diamonds);
	   Card card3 = new Card('Z', Card.Suit.clubs);
	   System.out.println(card1.toString());
	   System.out.println(card2.toString());
	   System.out.println(card3.toString());
	   
	   card1.set('$', Card.Suit.clubs);
	   card3.set('2', Card.Suit.clubs);
	   
	   System.out.println();
	   System.out.println(card1.toString());
	   System.out.println(card2.toString());
	   System.out.println(card3.toString());
	   System.out.println();
	   
	   //Testing of the Hand class
	   card1.set('T', Card.Suit.clubs);
	   Card card4 = new Card('Q', Card.Suit.spades);
	   Card card5 = new Card('8', Card.Suit.hearts);
	   
	   Hand hand1 = new Hand();

	   while (hand1.takeCard(randomCard(card1, card2, card3, card4, card5))){}
	   
	   System.out.println("Hand is now full!");
	   System.out.println(hand1.toString());
	   
	   System.out.println();
	   System.out.println("Testing inspectCard()");
	   System.out.println(hand1.inspectCard(42));
	   System.out.println(hand1.inspectCard(52));
	   System.out.println();
	   
	   while (hand1.getNumCards() != 0)
	   {
	      System.out.println("Playing " + hand1.playCard().toString());
	   }
	   System.out.println(hand1.toString());
	   
	   //Testing Deck Class
      Deck testDeck = new Deck(2);
      
      while(testDeck.getTopCard() != -1)
      {
         Card testCard = testDeck.dealCard();
         System.out.print(testCard + " / ");
      }
      System.out.println();
      testDeck.init(2);
      testDeck.shuffle();
      
      while(testDeck.getTopCard() != -1)
      {
         Card testCard = testDeck.dealCard();
         System.out.print(testCard + " / ");
      }
      
      testDeck.init(1);
      System.out.println();
      while(testDeck.getTopCard() != -1)
      {
         Card testCard = testDeck.dealCard();
         System.out.print(testCard + " / ");
      }
      
      testDeck.init(1);
      testDeck.shuffle();
      System.out.println();
      while(testDeck.getTopCard() != -1)
      {
         Card testCard = testDeck.dealCard();
         System.out.print(testCard + " / ");
      }
      
      //Phase 4
      
      //create an array of hands for the players.
      Hand[] hands = new Hand[10];
      
      //ask the user for the number of players(hands) continue to ask until a number between 1 and 10 is entered
      Scanner scan = new Scanner(System.in);
      System.out.print("How many Hands? (1 to 10 please): ");
      int numPlayers = scan.nextInt();
      while(numPlayers <= 0 || numPlayers >= 11)
      {
         System.out.print("How many hands? (1 to 10 please): ");
         numPlayers = scan.nextInt();
      }
      
      //create the players based on the user input
      for(int i = 0; i < numPlayers; i++)
      {
         hands[i] = new Hand();
      }
      
      //create the deck and deal cards to each of the players
      Deck playDeck = new Deck();
      int count = 0;
      while(playDeck.getTopCard() != -1)
      {
         hands[count%numPlayers].takeCard(playDeck.dealCard());
         count++;
      }
      //display the hands and then re-initialize and shuffle the deck, then display the shuffled cards
      System.out.println("Here are our hands from the unshuffled deck: ");
      for(int i = 0; i < numPlayers; i++)
      {
         System.out.println(hands[i] + "\n");
      }
      
      playDeck.init(1);
      playDeck.shuffle();
      
      for(int i = 0; i <numPlayers; i++)
      {
         hands[i].resetHand();
      }
      count = 0;
      while(playDeck.getTopCard() != -1)
      {
         hands[count%numPlayers].takeCard(playDeck.dealCard());
         count++;
      }
      System.out.println("Here are our hands from the SHUFFLED deck: ");
      for(int i = 0; i < numPlayers; i++)
      {
         System.out.println(hands[i] + "\n");
      }
      
      scan.close();

	}//end main
	//Phase one card class Raul
	static class Card
	{
	   //holds the suit data
	   public enum Suit {clubs, diamonds, hearts, spades};
		//private variables
		private char value;
		private Suit suit;
		private boolean errorFlag;
		
		//Constructor that sets suit and value for card
		public Card(char value, Suit suit)
		{
			set(value,suit);
		}
		//default card no values set if data is bad
		//set data if errorFlag is true using mutator
		//no values set if data is bad
		public Card() 
		{
			this('A', Suit.spades);
		}
		//mutator that accepts valid values
		public boolean set(char value, Suit suit)
		{
			errorFlag = true;
			char upperValue = Character.toUpperCase(value);
			if (isValid(upperValue, suit)) 
			{
				errorFlag = false;//false if card is valid
				this.value = upperValue;
				this.suit = suit;
			}
			return !errorFlag;
		}
		//return the suit
		public Suit getSuit() 
		{
			return suit;
		}
		//return card value
		public char getValue() 
		{
			return value;
		}
		//error flag
		public boolean getErrorFlag() 
		{
			return errorFlag;
		}
		//Checks if two cards are equal
		public boolean equals(Card card1) 
		{
			return (card1.getSuit() == suit && card1.getValue() == value);
		}
		//checks if the card is valid
		private boolean isValid(char value, Suit suit)
		{
		    char[] VALID_VALUES = {'2','3','4','5','6','7','8',
		                           '9','T','J','Q','K','A'};
		    for(char c : VALID_VALUES) 
		    {
				if(value == c) 
				{
					return true;
				}
		    }
			 return false;
		}
		//provides clean representation of the card
		public String toString() 
		{
			//returns the string
			String A_str;
			//checks for errorFlag if true and prints 
			//invalid card type
			if(errorFlag == true) 
			{
				A_str = "** invalid **";
			}
			else 
			{
				A_str = value + " of " + suit;
			}
			return A_str;
		}
	}
	
	//Phase two, Chris Carson
	static class Hand
	{
	   //Our public and private variables
	   public final int MAX_CARDS = 50;
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
	         myCards[numCards] = card;
	         numCards++;
	         return true;
	      }
	      else
	      {
	         return false;
	      }
	   }
	   //A method to play a card, removing it from the hand
	   public Card playCard()
	   {
	      numCards--;
	      Card returnCard = myCards[numCards];
	      myCards[numCards] = null;
	      
	      return returnCard;
	   }
	   //A method to return a string of all the cards in the hand
	   public String toString()
	   {
	      String returnString = "Hand = (";
	      
	      if (numCards > 0)
	      {
	         returnString = returnString + myCards[0].toString();
	         // this counter is used to create a line break so the output is easier to read
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
	      
	      returnString = returnString + ")";
	      return returnString;
	   }
	   //Accessor for numCards
      public int getNumCards() 
      {
         return numCards;
      }
      //Method to return the card from a position in the hand
      public Card inspectCard(int k)
      {
         //This is our errorflag card we return if the position is invalid
         Card errorCard = new Card('Z', Card.Suit.diamonds);
         
         if (k <= numCards)
         {
            return myCards[k];
         }
         else
         {
            return errorCard;
         }
      }
	}
	
	//Phase 3, Michael Rose
	//Had to make the class static to avoid a conflict with masterPack.
   static class Deck
   {
      //member variables
      public static final int MAX_CARDS = 312;
      
      private static Card[] masterPack = new Card[52];
      private Card[] cards = new Card[MAX_CARDS];
      private int topCard, numPacks;
      
      //private boolean to check to see if master pack has been created already.
      private static boolean isMasterPack = false;
      
      //Constructors 
      public Deck(int numPacks)
      {
         allocateMasterPack();
         this.numPacks = numPacks;
         topCard = (52 * numPacks) - 1;
         for(int i = 0; i <= topCard; i++)
         {
            cards[i] = masterPack[i%52];
         }
      }//end overloaded constructor
      
      public Deck()
      {
         allocateMasterPack();
         numPacks = 1;
         topCard = (52 * numPacks) - 1;
         for(int i = 0; i <= topCard; i++)
         {
            cards[i] = masterPack[i];
         }
         
      }//end basic constructor
      
      //re-populate cards[] with the standard 52 ~ numPacks cards.
      public void init(int numPacks)
      {
         this.numPacks = numPacks;
         topCard = (52 * this.numPacks) - 1;
         for(int i = 0; i <= topCard; i++)
         {
            cards[i] = masterPack[i%52];
         }
      }//end init
      
      //This function shuffles the deck.
      public void shuffle()
      {
         //create random number generator and temporary variables
         Random random = new Random();
         Card temp;
         int index;
         
         //go through and swap parts of the array with random parts of the array.  TopCard is used to avoid nulls in the much bigger cards array.
         for(int i = 0; i < this.topCard + 1; i++)
         {
            index = random.nextInt(this.topCard + 1);
            temp = cards[index];
            cards[index] = cards[i];
            cards[i] = temp;
         }
         
      }//end shuffle
      
      //take a card off the top of the deck and return it after deleting it from the cards array
      public Card dealCard()
      {
         Card dealtCard = cards[topCard];
         cards[topCard] = null;
         topCard--;
         return dealtCard;
      }//end dealCard
      
      //return the value of topCard
      public int getTopCard()
      {
         return this.topCard;
      }//end getTopCard
      
      //inspect a card at position k in the cards array.  if k is out of bounds or pointing to a null value in the cards array,
      //return a bad card.
      public Card inspectCard(int k)
      {
         Card errorCard = new Card('Z', Card.Suit.diamonds);
         
         if (k <= topCard)
         {
            return cards[k];
         }
         else
         {
            return errorCard;
         }
      }//end inspectCard
      
      //This function creates the master pack.  After this function has run, it sets the boolean variable to true
      //so the function will not run again.
      private static void allocateMasterPack()
      {
         if(isMasterPack == false)
         {
            char[] VALID_VALUES = {'A', '2','3','4','5','6','7','8',
                  '9','T','J','Q','K'};
            int i = 0;
            
            for(Card.Suit s : Card.Suit.values())
            {
               for(int j = 0; j < 13; j++)
               {
                  masterPack[i] = new Card(VALID_VALUES[j], s);
                  i++;
               }
            }
            //set isMasterPack to true to never run this function again.
            isMasterPack = true;
         }
      }//end allocateMasterPack
      
   }//End Deck Class

}
