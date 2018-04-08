import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameModel {

   static final int HUMAN_PLAYER1 = 1;
   static final int COMPUTER_PLAYER1 = 0;

   static JButton[] playCardButtons;
   static int[] playerScores;
   static int numCardsPerHand; // # cards to deal each player
   static int numPlayers;

   private int numPacks; // # standard 52-card packs per deck
                         // ignoring jokers or unused cards
   private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack; // # cards removed from each pack
   private Deck deck; // holds the initial full deck and gets
                      // smaller (usually) during play
   private Hand[] hand; // one Hand for each player
   private Card[] unusedCardsPerPack; // an array holding the cards not used
                                      // in the game. e.g. pinochle does not
                                      // use cards 2-8 of any suit
   private static final int MAX_PLAYERS = 50;

   public GameModel(int numPacks, int numJokersPerPack, int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand) {
      int k;
      playCardButtons = new JButton[numCardsPerHand];
      playerScores = new int[numPlayers];

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50)
         numUnusedCardsPerPack = 0;// > 1 card
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack) / numPlayers)
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
      GameModel.numPlayers = numPlayers;
      GameModel.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public GameModel() {
      this(1, 0, 0, null, 4, 13);
   }

   public void playCards(int cardIndex) {
      // Play the card
      GameModel.Card playerCard = playCard(HUMAN_PLAYER1, cardIndex);
      GameView.playedCardLabels[HUMAN_PLAYER1].setIcon(GameModel.GUICard.getIcon(playerCard));
      GameView.playedCardLabels[HUMAN_PLAYER1].setVisible(true);

      // Draw from the deck.
      if (takeCard(HUMAN_PLAYER1)) {
         for (int i = cardIndex; i < numCardsPerHand - 1; i++) {
            GameView.humanLabels[i] = GameView.humanLabels[i + 1];
            GameModel.playCardButtons[i].setIcon(GameView.humanLabels[i + 1]);
         }
         GameView.humanLabels[numCardsPerHand - 1] = (ImageIcon) GameModel.GUICard
               .getIcon(getHand(1).inspectCard(numCardsPerHand - 1));
         GameModel.playCardButtons[numCardsPerHand - 1].setIcon(GameView.humanLabels[numCardsPerHand - 1]);
      } else
         GameView.pnlHumanHand.remove(cardIndex);

      // Play the computer card.
      int index = GameModel.comPlay(getHand(COMPUTER_PLAYER1), playerCard);
      GameModel.Card computerCard = playCard(COMPUTER_PLAYER1, index);
      GameView.playedCardLabels[COMPUTER_PLAYER1].setIcon(GameModel.GUICard.getIcon(computerCard));
      GameView.playedCardLabels[COMPUTER_PLAYER1].setVisible(true);

      // Take from the deck.
      if (!takeCard(COMPUTER_PLAYER1))
         GameView.pnlComputerHand.remove(index);

      // Update score.
      int score = scoreCards(playerCard, computerCard);
      updateScore(score);
      GameView.updateScoreView(score);

   }

   public Hand getHand(int k) {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() {
      return deck.dealCard();
   }

   public int getNumCardsRemainingInDeck() {
      return deck.getNumCards();
   }

   public void newGame() {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal() {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards; k++) {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard(deck.dealCard());
            else {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands() {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex) {
      // returns bad card if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
         // Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }

   boolean takeCard(int playerIndex) {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }

   // Updates the score based on the result of the scoreCards function below.
   // Gives the winner 2 points or each player 1 point in the result of a tie.
   protected static void updateScore(int score) {
      if (score == -1) // computer wins
      {
         playerScores[COMPUTER_PLAYER1] += 2;
      } else if (score == 1) // player wins
      {
         playerScores[HUMAN_PLAYER1] += 2;
      }
   }

   // Scores the cards based on their rank values
   // Returns a 0 if they are the same rank
   protected static int scoreCards(GameModel.Card playerCard, GameModel.Card computerCard) {
      if (GameModel.Card.getCardValueRank(playerCard) > GameModel.Card.getCardValueRank(computerCard))
         return 1;
      else if (GameModel.Card.getCardValueRank(playerCard) < GameModel.Card.getCardValueRank(computerCard))
         return -1;
      return 0;
   }

   // Allows the computer to play a card based on what you played.
   protected static int comPlay(Hand comHand, Card playedCard) {
      int returnIndex = 0;
      Hand compHand = comHand;
      Card checkCard = playedCard;
      int checkCardRank = Card.getCardValueRank(checkCard);
      boolean cardChanged = false;

      // Go through and find a card that can beat the card that was played.
      // If a card is smaller than the one chosen but beats the played card, play that
      // one instead.
      // Update the card changed flag if a card was found
      for (int i = 0; i < compHand.getNumCards(); i++) {
         compHand.inspectCard(i);
         int handRank = Card.getCardValueRank(compHand.inspectCard(i));
         compHand.inspectCard(returnIndex);
         if (handRank > checkCardRank && handRank < Card.getCardValueRank(compHand.inspectCard(returnIndex))) {
            returnIndex = i;
            cardChanged = true;
         }
      }

      // If there is no card in hand has a higher rank the played card, play the
      // lowest ranked card in hand
      if (cardChanged == false) {
         for (int i = 0; i < compHand.getNumCards(); i++) {
            compHand.inspectCard(i);
            int handRank = Card.getCardValueRank(compHand.inspectCard(i));
            compHand.inspectCard(returnIndex);
            if (handRank < Card.getCardValueRank(compHand.inspectCard(returnIndex))) {
               returnIndex = i;
            }
         }
      }
      return returnIndex;
   }// end comPlay

   // inner class represents the image of the card.
   static class GUICard {
      private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
      private static Icon iconBack;
      static boolean iconsLoaded = false;

      // Method loads the image depending on the card value/suit.
      static void loadCardIcons() {
         // build the file names ("AC.gif", "2C.gif", "3C.gif", "TC.gif", etc.)
         // in a SHORT loop. For each file name, read it in and use it to
         // instantiate each of the 57 Icons in the icon[] array.
         for (int i = 0; i < iconCards.length; i++) {
            for (int j = 0; j < iconCards[0].length; j++) {
               iconCards[i][j] = new ImageIcon("images/" + turnIntIntoCardValue(i) + turnIntIntoCardSuit(j) + ".gif");
            }
         }
         // Set the image for the back of a card.
         iconBack = new ImageIcon("images/BK.gif");
         iconsLoaded = true;
      }

      // Method for getting an image from a Card input.
      public static Icon getIcon(Card card) {
         // Check to see if the icons have been loaded.
         if (!iconsLoaded)
            loadCardIcons();

         // Return the location of the card in the array based on its value/suit.
         return iconCards[valueAsInt(card)][suitAsInt(card)];
      }

      // Returns the image for the back of a card.
      public static Icon getBackCardIcon() {
         if (!iconsLoaded)
            loadCardIcons();

         return iconBack;
      }

      // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
      private static String turnIntIntoCardValue(int i) {
         String[] value = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "X" };

         if (i >= 0 && i <= 13)
            return value[i];
         else
            return value[0]; // returns default value "A".
      }

      // turns 0 - 3 into "C", "D", "H", "S"
      private static String turnIntIntoCardSuit(int j) {
         String[] value = { "C", "D", "H", "S" };

         if (j >= 0 && j <= 3)
            return value[j];
         else
            return value[0]; // Returns default value "C".
      }

      // Takes a card and returns its value as an integer.
      private static int valueAsInt(Card card) {
         char value = card.getValue();
         switch (value) {
         case 'A':
            return 0;
         case 'T':
            return 9;
         case 'J':
            return 10;
         case 'Q':
            return 11;
         case 'K':
            return 12;
         case 'X':
            return 13;
         default:
            return value - 49; // -49 to account for ASCII code conversion and -1 offset.
         }
      }

      // Takes a card and returns its suit as an integer.
      private static int suitAsInt(Card card) {
         switch (card.getSuit()) {
         case clubs:
            return 0;
         case diamonds:
            return 1;
         case hearts:
            return 2;
         case spades:
            return 3;
         default:
            return -1;
         }
      }
   }// End GUICard Class

   // inner Card class represents a single playing card consisting of a suit and a
   // value (Ex. Ace of Spades).
   static class Card {
      // Holds the suit data
      public enum Suit {
         clubs, diamonds, hearts, spades
      };

      // Private variables
      private char value;
      private Suit suit;
      private boolean errorFlag;

      // public variable for the rank of each card value.
      private static char[] valuRanks = { 'X', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' };

      // Constructor that sets suit and value for card
      public Card(char value, Suit suit) {
         set(value, suit);
      }

      // Default card no values set if data is bad
      // Set data if errorFlag is true using mutator
      // No values set if data is bad
      public Card() {
         this('A', Suit.spades);
      }

      // Mutator that accepts valid values
      public boolean set(char value, Suit suit) {
         char upperValue = Character.toUpperCase(value);

         // Set the errorFlag to true if the card isn't valid.
         errorFlag = !isValid(upperValue, suit);
         if (!errorFlag) {
            this.value = upperValue;
            this.suit = suit;
         }
         return !errorFlag;
      }

      // Accessor for the card's suit
      public Suit getSuit() {
         return suit;
      }

      // Accessor for the card's value
      public char getValue() {
         return value;
      }

      // Accessor for the error flag
      public boolean getErrorFlag() {
         return errorFlag;
      }

      // Checks if two cards are equal
      public boolean equals(Card card1) {
         return (card1.getSuit() == suit && card1.getValue() == value);
      }

      // Checks if the card is valid
      private boolean isValid(char value, Suit suit) {
         char[] VALID_VALUES = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };

         for (char c : VALID_VALUES) {
            if (value == c) {
               return true;
            }
         }
         return false;
      }

      // Takes a card array and its size and sorts it by card "rank" using a bubble
      // sort.
      // The higher rank cards should be at the end of the array.
      public static void arraySort(Card[] cardArray, int arraySize) {
         for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize - i; j++) {
               if (getCardValueRank(cardArray[j]) > getCardValueRank(cardArray[j + 1])) {
                  Card temp = cardArray[j];
                  cardArray[j] = cardArray[j + 1];
                  cardArray[j + 1] = temp;
               }
            }
         }
      }

      // Takes a card and returns the rank of its value.
      public static int getCardValueRank(Card card) {
         int i;
         for (i = 0; i < valuRanks.length; i++) {
            if (card.getValue() == valuRanks[i])
               break;
         }
         return i;
      }

      // Outputs a string representation of the card.
      public String toString() {
         // Outputs invalid if the errorFlag is set to true.
         if (errorFlag) {
            return "** invalid **";
         } else {
            return value + " of " + suit;
         }
      }
   } // End Card Class

   // inner Hand class represents the cards held by a single player.
   class Hand {
      // Our public and private variables
      public static final int MAX_CARDS = 50;
      private Card[] myCards = new Card[MAX_CARDS];
      private int numCards;

      // The Constructor
      public Hand() {
         numCards = 0;
      }

      // Method to rest all card values to null and numCards to 0
      public void resetHand() {
         numCards = 0;
         for (int i = 0; i < myCards.length; i++) {
            myCards[i] = null;
         }
      }

      // Method to take a card and put it in the hand. Returns false if hand is full
      public boolean takeCard(Card card) {
         if (numCards < MAX_CARDS) {
            Card newCard = card;
            myCards[numCards] = newCard;
            numCards++;
            return true;
         } else {
            return false;
         }
      }

      // A method to play a card, removing it from the hand
      public Card playCard(int cardIndex) {
         numCards--;
         Card returnCard = myCards[cardIndex];
         myCards[cardIndex] = null;
         for (int i = cardIndex; i < numCards; i++) {
            myCards[i] = myCards[i + 1];
         }

         return returnCard;
      }

      // Sorts the hand by each card's rank.
      public void sort() {
         Card.arraySort(myCards, numCards);
      }

      // A method to return a string of all the cards in the hand
      public String toString() {
         String returnString = "Hand = (";

         if (numCards > 0) {
            returnString = returnString + myCards[0].toString();

            // This counter is used to create a line break so the output is easier to read
            int count = 0;
            for (int i = 1; i < numCards; i++) {
               count++;
               if (count > 5) {
                  returnString = returnString + ",\n" + myCards[i].toString();
                  count = 0;
               } else {
                  returnString = returnString + ", " + myCards[i].toString();
               }
            }
         }

         return returnString + ")";
      }

      // Accessor for numCards
      public int getNumCards() {
         return numCards;
      }

      // Method to return the card from a position in the hand
      public Card inspectCard(int k) {
         if (k <= numCards) {
            return myCards[k];
         } else {
            // This is our errorFlag card we return if the position is invalid
            return new Card('Z', Card.Suit.diamonds);
         }
      }
   } // End Hand Class

   // inner Deck class represents the source of playing cards that the players are
   // pulling cards from.
   static class Deck {
      // Member variables
      public static final int CARDS_IN_PACK = 56;
      public static final int MAX_CARDS = 6 * CARDS_IN_PACK;
      public static final int MAX_PACKS = 6;

      private static Card[] masterPack = new Card[CARDS_IN_PACK]; // Standard cards + 4 jokers.
      private Card[] cards = new Card[MAX_CARDS];
      private int topCard, numPacks;

      // Private boolean to check to see if master pack has been created already.
      private static boolean isMasterPackAllocated = false;

      // Constructors
      public Deck(int numPacks) {
         allocateMasterPack();
         this.numPacks = numPacks;
         topCard = (CARDS_IN_PACK * numPacks) - 1;
         for (int i = 0; i <= topCard; i++) {
            cards[i] = masterPack[i % CARDS_IN_PACK];
         }
      }

      public Deck() {
         allocateMasterPack();
         numPacks = 1;
         topCard = (CARDS_IN_PACK * numPacks) - 1;
         for (int i = 0; i <= topCard; i++) {
            cards[i] = masterPack[i];
         }
      }

      // Re-populate cards[] with the standard 52 x numPacks cards.
      public void init(int numPacks) {
         if (numPacks > MAX_PACKS)
            this.numPacks = 1;
         else
            this.numPacks = numPacks;
         topCard = (CARDS_IN_PACK * this.numPacks) - 1;
         for (int i = 0; i <= topCard; i++) {
            cards[i] = masterPack[i % CARDS_IN_PACK];
         }
      }

      // This function shuffles the deck.
      public void shuffle() {
         // Create random number generator and temporary variables
         Random random = new Random();
         Card temp;
         int index;

         // Go through and swap parts of the array with random parts of the array.
         // TopCard is used to avoid nulls in the much bigger cards array.
         for (int i = 0; i <= this.topCard; i++) {
            index = random.nextInt(this.topCard + 1);
            temp = cards[index];
            cards[index] = cards[i];
            cards[i] = temp;
         }
      }

      // Take a card off the top of the deck and return it after deleting it from the
      // cards array
      public Card dealCard() {
         if (getTopCard() != -1) {
            Card dealtCard = cards[topCard];
            cards[topCard] = null;
            topCard--;
            return dealtCard;
         } else
            return new Card('Z', GameModel.Card.Suit.diamonds);
      }

      // Return the value of topCard
      public int getTopCard() {
         return this.topCard;
      }

      // Inspect a card at position k in the cards array. if k is out of bounds or
      // pointing to a null value in the cards array,
      // return a bad card.
      public Card inspectCard(int k) {
         if (k <= topCard) {
            return cards[k];
         } else {
            // This is our errorFlag card we return if the position is invalid.
            return new Card('Z', Card.Suit.diamonds);
         }
      }

      // Takes a card and adds it to the deck if the deck doesn't have too many cards
      // and if the deck does not have too many of that type of card.
      public boolean addCard(Card card) {
         // Find the number of cards of this type that the deck already contains.
         int count = 0;
         for (int i = 0; i <= topCard; i++) {
            if (cards[i].getSuit() == card.getSuit() && cards[i].getValue() == card.getValue())
               count++;
         }

         if (count >= numPacks || topCard + 1 == MAX_CARDS)
            return false;

         topCard++;
         cards[topCard] = card;
         return true;
      }

      // Takes a card and removes it from the deck. The top card takes its place.
      public boolean removeCard(Card card) {
         int index = -1;
         for (int i = 0; i <= topCard; i++) {
            if (cards[i].getSuit() == card.getSuit() && cards[i].getValue() == card.getValue())
               index = i;
         }

         if (index >= 0) {
            cards[index] = cards[topCard];
            topCard--;
         }

         return index >= 0;
      }

      // Sort the deck by each card's rank.
      public void sort() {
         Card.arraySort(cards, topCard);
      }

      // Return the total number of cards in the deck.
      public int getNumCards() {
         return topCard + 1;
      }

      // This function creates the master pack. After this function has run, it sets
      // the boolean variable to true
      // so the function will not run again.
      private static void allocateMasterPack() {
         if (isMasterPackAllocated == false) {
            char[] VALID_VALUES = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };
            int i = 0;

            for (Card.Suit s : Card.Suit.values()) {
               for (int j = 0; j < VALID_VALUES.length; j++) {
                  masterPack[i] = new Card(VALID_VALUES[j], s);
                  i++;
               }
            }
            // set isMasterPackAllocated to true to never run this function again.
            isMasterPackAllocated = true;
         }
      }
   }// End Deck Class

}// End GameModel Class
