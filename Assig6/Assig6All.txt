//Assignment 6 CST 338
//John Coffelt, Michael Rose, Christopher Carson, Raul Ramirez

import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//Phase 3
public class Assig6
{   
   public static void main(String[] args) throws InterruptedException
   {  
      Model cardGameModel = new Model();
      View cardGameView = new View();
      Controller cardGameController = new Controller(cardGameModel, cardGameView);
      cardGameView.addController(cardGameController);
      cardGameController.init();
   }//End Main
}// end Assig6

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
   }

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
   }

   @Override
   public void mouseEntered(MouseEvent e) {}

   @Override
   public void mouseExited(MouseEvent e) {}

   @Override
   public void mouseClicked(MouseEvent arg0) {}

   @Override
   public void mouseReleased(MouseEvent e) {}
}

//Model Class for the card game.
class Model
{
   static final int NUM_CARDS_PER_HAND = 7;
   static final int NUM_PLAYERS = 2;
   static final int NUM_CARD_STACKS = 2;
   static final int HUMAN_PLAYER = 1;
   static final int COMPUTER_PLAYER = 0;

   private int[] playerScores = new int[NUM_PLAYERS];
   private Card[] cardStack = new Card[NUM_CARD_STACKS];
   private CardGameFramework cardGame;
   private int selectedIndex = -1; // Index of a card selected in a player's hand.
   private int playerTurn;
   private int turnsSkipped;
   private boolean gameOver;

   public Model()
   {
      // CREATE THE GAME
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;

      cardGame = new CardGameFramework(numPacksPerDeck, numJokersPerPack, numUnusedCardsPerPack, unusedCardsPerPack,
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
   }

   // Starts the game from a fresh state.
   public void startGame()
   {
      gameOver = false;
      playerTurn = -1;

      cardGame.newGame();
      cardGame.deal();

      for (int i = 0; i < NUM_CARD_STACKS; i++)
         addCardToStack(i);

      turnsSkipped = 0;
      setPlayerTurn(HUMAN_PLAYER); // Human player gets first turn.
      for (int i = 0; i < playerScores.length; i++)
         playerScores[i] = 0;
   }

   // Ends a player's turn.
   // Accepts boolean value to determine if the player made a move or if they
   // skipped their turn.
   public void endTurn(boolean madeMove)
   {  
      if (isGameOver())
         return;

      // If a move was made the turns skipped counter resets.
      if (madeMove)
         turnsSkipped = 0;
      else
         turnsSkipped++;

      // Determines if the game needs to add cards to the stacks from the deck due to no moves being made.
      if (turnsSkipped == NUM_PLAYERS)
      {
         for (int i = 0; i < NUM_CARD_STACKS; i++)
            addCardToStack(i);
         turnsSkipped = 0;
      }
      
      //Unselect the current selection.
      setSelectedCard(-1);
      
      //Increment so that it is the next player's turn.
      playerTurn++;
      if (playerTurn == NUM_PLAYERS)
         playerTurn = 0;
   }

   // Adds a card to a stack using the index of the stack.
   public boolean addCardToStack(int stackIndex)
   {
      // Determines if the cards will be added to the stack from the deck
      // or from player's hand.
      if ((playerTurn == -1 || turnsSkipped == NUM_PLAYERS) && !gameOver)
         cardStack[stackIndex] = cardGame.getCardFromDeck();
      else
      {
         // Check if the player is making a valid move before adding to the stack.
         if (isValidMove(stackIndex))
         {
            // Take card from player's hand.
            cardStack[stackIndex] = cardGame.playCard(playerTurn, selectedIndex);
            cardGame.takeCard(playerTurn);
            updateScore(playerTurn);
         } 
         else
            return false;
      }

      // End the game if there are no more cards in the deck.
      if (cardGame.getNumCardsRemainingInDeck() == 0)
         gameOver = true;
      return true;
   }

   // Increment the score of a player by one.
   private void updateScore(int player)
   {
      if (player < playerScores.length)
         playerScores[player]++;
   }

   // Check to see if a move on a specific stack is valid.
   // A move is valid if the card value is either one less or one greater than the
   // card on the stack.
   private boolean isValidMove(int stackIndex)
   {
      return (Math.abs(Card.getCardValueRank(cardStack[stackIndex])
            - Card.getCardValueRank(cardGame.getHand(playerTurn).inspectCard(selectedIndex))) == 1);
   }
   
   // Simulates a turn by the computer.
   public void startComputerTurn()
   {
      Hand hand = cardGame.getHand(COMPUTER_PLAYER);
      for (int i = 0; i < hand.getNumCards(); i++)
      {
         for (int j = 0; j < cardStack.length; j++)
         {
            setSelectedCard(i);
            if (addCardToStack(j))
            {
               endTurn(true); // Successful move
               return;
            }
         }
      }
      endTurn(false); // Unsuccessful move
   }
   
   // Get the images for a hand.
   public Icon[] getHandIcons(int handIndex)
   {
      Hand hand = cardGame.getHand(handIndex);
      Icon[] cardImages = new ImageIcon[hand.getNumCards()];
      for (int i = 0; i < hand.getNumCards(); i++)
      {
         if (handIndex != COMPUTER_PLAYER)
            cardImages[i] = GUICard.getIcon(hand.inspectCard(i));
         else
            cardImages[i] = GUICard.getBackCardIcon();
      }
      return cardImages;
   }

   // Gets the images for each stack.
   public Icon[] getStackIcons()
   {
      Icon[] cardImages = new ImageIcon[NUM_CARD_STACKS];
      for (int i = 0; i < NUM_CARD_STACKS; i++)
         cardImages[i] = GUICard.getIcon(cardStack[i]);
      return cardImages;
   }

   // Get the image for a card.
   public Icon getBackCardIcon() 
   {
      return GUICard.getBackCardIcon();
   }
   
   // Accessor for the deck count.
   public int getCardsInDeckCount()
   {
      return cardGame.getNumCardsRemainingInDeck();
   }
   
   // Accessor for player turn.
   public int getPlayerTurn()
   {
      return playerTurn;
   }

   // Accessor for a player's score.
   public int getPlayerScore(int playerIndex)
   {
      if (playerIndex >= 0 && playerIndex < playerScores.length)
         return playerScores[playerIndex];
      else
         return -1;
   }
   
   //Accessor for all scores.
   public int[] getPlayerScores()
   {
      return playerScores;
   }
   
   // returns the index of the card currently selected in the hand.
   public int getSelectedCardIndex()
   {
      return selectedIndex;
   }
   
   // Accessor for the game state.
   public boolean isGameOver()
   {
      return gameOver;
   }
   
   // Sets the currently selected card in the hand.
   public boolean setSelectedCard(int index)
   {
      if (index >= -1 && index < cardGame.getHand(playerTurn).getNumCards())
         selectedIndex = index;
      else
         return false;
      return true;
   }

   // Sets the player turn.
   public boolean setPlayerTurn(int player)
   {
      if (player >= -1 && player < NUM_PLAYERS)
         playerTurn = player;
      return (playerTurn == player);
   }
}

// class CardGameFramework ----------------------------------------------------
class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks; // # standard 52-card packs per deck
                         // ignoring jokers or unused cards
   private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack; // # cards removed from each pack
   private int numCardsPerHand; // # cards to deal each player
   private Deck deck; // holds the initial full deck and gets
                      // smaller (usually) during play
   private Hand[] hand; // one Hand for each player
   private Card[] unusedCardsPerPack; // an array holding the cards not used
                                      // in the game. e.g. pinochle does not
                                      // use cards 2-8 of any suit

   public CardGameFramework(int numPacks, int numJokersPerPack, int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
         numUnusedCardsPerPack = 0;
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

   public Card getCardFromDeck()
   {
      return deck.dealCard();
   }

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

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
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

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
      for (k = 0; k < numCardsPerHand && enoughCards; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard(deck.dealCard());
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
      if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         // Creates a card that does not work
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

// Class represents the image of the card.
class GUICard
{
   private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack;
   static boolean iconsLoaded = false;

   // Method loads the image depending on the card value/suit.
   static void loadCardIcons()
   {
      // build the file names ("AC.gif", "2C.gif", "3C.gif", "TC.gif", etc.)
      // in a SHORT loop. For each file name, read it in and use it to
      // instantiate each of the 57 Icons in the icon[] array.
      for (int i = 0; i < iconCards.length; i++)
      {
         for (int j = 0; j < iconCards[0].length; j++)
         {
            iconCards[i][j] = new ImageIcon("images/" + turnIntIntoCardValue(i) + turnIntIntoCardSuit(j) + ".gif");
         }
      }
      // Set the image for the back of a card.
      iconBack = new ImageIcon("images/BK.gif");
      iconsLoaded = true;
   }

   // Method for getting an image from a Card input.
   public static Icon getIcon(Card card)
   {
      // Check to see if the icons have been loaded.
      if (!iconsLoaded)
         loadCardIcons();

      // Return the location of the card in the array based on its value/suit.
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   // Returns the image for the back of a card.
   public static Icon getBackCardIcon()
   {
      if (!iconsLoaded)
         loadCardIcons();

      return iconBack;
   }

   // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
   private static String turnIntIntoCardValue(int i)
   {
      String[] value =
      { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "X" };

      if (i >= 0 && i <= 13)
         return value[i];
      else
         return value[0]; // returns default value "A".
   }

   // turns 0 - 3 into "C", "D", "H", "S"
   private static String turnIntIntoCardSuit(int j)
   {
      String[] value =
      { "C", "D", "H", "S" };

      if (j >= 0 && j <= 3)
         return value[j];
      else
         return value[0]; // Returns default value "C".
   }

   // Takes a card and returns its value as an integer.
   private static int valueAsInt(Card card)
   {
      char value = card.getValue();
      switch (value)
      {
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
   private static int suitAsInt(Card card)
   {
      switch (card.getSuit())
      {
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

// Card class represents a single playing card consisting of a suit and a value
// (Ex. Ace of Spades).
class Card
{
   // Holds the suit data
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   };

   // Private variables
   private char value;
   private Suit suit;
   private boolean errorFlag;

   // public variable for the rank of each card value.
   private static char[] valuRanks =
   { 'X', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' };

   // Constructor that sets suit and value for card
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   // Default card no values set if data is bad
   // Set data if errorFlag is true using mutator
   // No values set if data is bad
   public Card()
   {
      this('A', Suit.spades);
   }

   // Mutator that accepts valid values
   public boolean set(char value, Suit suit)
   {
      char upperValue = Character.toUpperCase(value);

      // Set the errorFlag to true if the card isn't valid.
      errorFlag = !isValid(upperValue, suit);
      if (!errorFlag)
      {
         this.value = upperValue;
         this.suit = suit;
      }
      return !errorFlag;
   }

   // Accessor for the card's suit
   public Suit getSuit()
   {
      return suit;
   }

   // Accessor for the card's value
   public char getValue()
   {
      return value;
   }

   // Accessor for the error flag
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   // Checks if two cards are equal
   public boolean equals(Card card1)
   {
      return (card1.getSuit() == suit && card1.getValue() == value);
   }

   // Checks if the card is valid
   private boolean isValid(char value, Suit suit)
   {
      char[] VALID_VALUES =
      { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };

      for (char c : VALID_VALUES)
      {
         if (value == c)
         {
            return true;
         }
      }
      return false;
   }

   // Takes a card array and its size and sorts it by card "rank" using a bubble
   // sort.
   // The higher rank cards should be at the end of the array.
   public static void arraySort(Card[] cardArray, int arraySize)
   {
      for (int i = 0; i < arraySize; i++)
      {
         for (int j = 0; j < arraySize - i; j++)
         {
            if (getCardValueRank(cardArray[j]) > getCardValueRank(cardArray[j + 1]))
            {
               Card temp = cardArray[j];
               cardArray[j] = cardArray[j + 1];
               cardArray[j + 1] = temp;
            }
         }
      }
   }

   // Takes a card and returns the rank of its value.
   public static int getCardValueRank(Card card)
   {
      int i;
      for (i = 0; i < valuRanks.length; i++)
      {
         if (card.getValue() == valuRanks[i])
            break;
      }
      return i;
   }

   // Outputs a string representation of the card.
   public String toString()
   {
      // Outputs invalid if the errorFlag is set to true.
      if (errorFlag)
      {
         return "** invalid **";
      } else
      {
         return value + " of " + suit;
      }
   }
} // End Card Class

// Hand class represents the cards held by a single player.
class Hand
{
   // Our public and private variables
   public static final int MAX_CARDS = 50;
   private Card[] myCards = new Card[MAX_CARDS];
   private int numCards;

   // The Constructor
   public Hand()
   {
      numCards = 0;
   }

   // Method to rest all card values to null and numCards to 0
   public void resetHand()
   {
      numCards = 0;
      for (int i = 0; i < myCards.length; i++)
      {
         myCards[i] = null;
      }
   }

   // Method to take a card and put it in the hand. Returns false if hand is full
   public boolean takeCard(Card card)
   {
      if (numCards < MAX_CARDS)
      {
         Card newCard = card;
         myCards[numCards] = newCard;
         numCards++;
         return true;
      } else
      {
         return false;
      }
   }

   // A method to play a card, removing it from the hand
   public Card playCard(int cardIndex)
   {
      numCards--;
      Card returnCard = myCards[cardIndex];
      myCards[cardIndex] = null;
      for (int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }

      return returnCard;
   }

   // Sorts the hand by each card's rank.
   public void sort()
   {
      Card.arraySort(myCards, numCards);
   }

   // A method to return a string of all the cards in the hand
   public String toString()
   {
      String returnString = "Hand = (";

      if (numCards > 0)
      {
         returnString = returnString + myCards[0].toString();

         // This counter is used to create a line break so the output is easier to read
         int count = 0;
         for (int i = 1; i < numCards; i++)
         {
            count++;
            if (count > 5)
            {
               returnString = returnString + ",\n" + myCards[i].toString();
               count = 0;
            } else
            {
               returnString = returnString + ", " + myCards[i].toString();
            }
         }
      }

      return returnString + ")";
   }

   // Accessor for numCards
   public int getNumCards()
   {
      return numCards;
   }

   // Method to return the card from a position in the hand
   public Card inspectCard(int k)
   {
      if (k <= numCards)
      {
         return myCards[k];
      } else
      {
         // This is our errorFlag card we return if the position is invalid
         return new Card('Z', Card.Suit.diamonds);
      }
   }
} // End Hand Class

// Deck class represents the source of playing cards that the players are
// pulling cards from.
class Deck
{
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
   public Deck(int numPacks)
   {
      allocateMasterPack();
      this.numPacks = numPacks;
      topCard = (CARDS_IN_PACK * numPacks) - 1;
      for (int i = 0; i <= topCard; i++)
      {
         cards[i] = masterPack[i % CARDS_IN_PACK];
      }
   }

   public Deck()
   {
      allocateMasterPack();
      numPacks = 1;
      topCard = (CARDS_IN_PACK * numPacks) - 1;
      for (int i = 0; i <= topCard; i++)
      {
         cards[i] = masterPack[i];
      }
   }

   // Re-populate cards[] with the standard 52 x numPacks cards.
   public void init(int numPacks)
   {
      if (numPacks > MAX_PACKS)
         this.numPacks = 1;
      else
         this.numPacks = numPacks;
      topCard = (CARDS_IN_PACK * this.numPacks) - 1;
      for (int i = 0; i <= topCard; i++)
      {
         cards[i] = masterPack[i % CARDS_IN_PACK];
      }
   }

   // This function shuffles the deck.
   public void shuffle()
   {
      // Create random number generator and temporary variables
      Random random = new Random();
      Card temp;
      int index;

      // Go through and swap parts of the array with random parts of the array.
      // TopCard is used to avoid nulls in the much bigger cards array.
      for (int i = 0; i <= this.topCard; i++)
      {
         index = random.nextInt(this.topCard + 1);
         temp = cards[index];
         cards[index] = cards[i];
         cards[i] = temp;
      }
   }

   // Take a card off the top of the deck and return it after deleting it from the
   // cards array
   public Card dealCard()
   {
      if (getTopCard() != -1)
      {
         Card dealtCard = cards[topCard];
         cards[topCard] = null;
         topCard--;
         return dealtCard;
      } else
         return new Card('Z', Card.Suit.diamonds);
   }

   // Return the value of topCard
   public int getTopCard()
   {
      return this.topCard;
   }

   // Inspect a card at position k in the cards array. if k is out of bounds or
   // pointing to a null value in the cards array,
   // return a bad card.
   public Card inspectCard(int k)
   {
      if (k <= topCard)
      {
         return cards[k];
      } else
      {
         // This is our errorFlag card we return if the position is invalid.
         return new Card('Z', Card.Suit.diamonds);
      }
   }

   // Takes a card and adds it to the deck if the deck doesn't have too many cards
   // and if the deck does not have too many of that type of card.
   public boolean addCard(Card card)
   {
      // Find the number of cards of this type that the deck already contains.
      int count = 0;
      for (int i = 0; i <= topCard; i++)
      {
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
   public boolean removeCard(Card card)
   {
      int index = -1;
      for (int i = 0; i <= topCard; i++)
      {
         if (cards[i].getSuit() == card.getSuit() && cards[i].getValue() == card.getValue())
            index = i;
      }

      if (index >= 0)
      {
         cards[index] = cards[topCard];
         topCard--;
      }

      return index >= 0;
   }

   // Sort the deck by each card's rank.
   public void sort()
   {
      Card.arraySort(cards, topCard);
   }

   // Return the total number of cards in the deck.
   public int getNumCards()
   {
      return topCard + 1;
   }

   // This function creates the master pack. After this function has run, it sets
   // the boolean variable to true
   // so the function will not run again.
   private static void allocateMasterPack()
   {
      if (isMasterPackAllocated == false)
      {
         char[] VALID_VALUES =
         { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };
         int i = 0;

         for (Card.Suit s : Card.Suit.values())
         {
            for (int j = 0; j < VALID_VALUES.length; j++)
            {
               masterPack[i] = new Card(VALID_VALUES[j], s);
               i++;
            }
         }
         // set isMasterPackAllocated to true to never run this function again.
         isMasterPackAllocated = true;
      }
   }
}// End Deck Class

//View class for a card game.
class View
{
   private static int MAX_IMAGES_IN_DECK = 56 / 5;
   private static final int HUMAN_PLAYER = Model.HUMAN_PLAYER;
   private static final int COMPUTER_PLAYER = Model.COMPUTER_PLAYER;
   private static int NUM_CARD_STACKS = Model.NUM_CARD_STACKS;
   private static int NUM_CARDS_PER_HAND = Model.NUM_CARDS_PER_HAND;
   private static int NUM_PLAYERS = Model.NUM_PLAYERS;
   private JLabel[] addToStackLabel = new JLabel[NUM_CARD_STACKS];
   private JButton cantPlayButton;
   private static JButton startTimerButton;
   private JLabel[] computerHandLabels = new JLabel[NUM_CARDS_PER_HAND];
   private JButton[] playerHandButtons = new JButton[NUM_CARDS_PER_HAND];
   private JLabel[] playerScoreLabels = new JLabel[NUM_PLAYERS];
   private JLabel[] deckLabel = new JLabel[MAX_IMAGES_IN_DECK];
   private JLabel deckCountLabel;
   private CardTable myCardTable;
   private Thread t = new Thread(new View.Timer());
   private static boolean clockRun = false;
   private static final SimpleDateFormat date = new SimpleDateFormat("mm:ss");
   private static long clockCounter = 0;
   private static JLabel clock = new JLabel();

   // Constructor sets up the table.
   public View()
   {
      // Create the table.
      myCardTable = new CardTable("CardGame", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Insets insets = myCardTable.pnlPlayArea.getInsets();
      int offset = -30;

      //Stacks
      addToStackLabel[0] = new JLabel();
      addToStackLabel[0].setName("Play Area Card Stack");
      myCardTable.pnlPlayArea.add(addToStackLabel[0]);
      addToStackLabel[0].setBounds(160 + offset + insets.left, 50 + insets.top, 100, 120);

      addToStackLabel[1] = new JLabel();
      addToStackLabel[1].setName("Play Area Card Stack");
      myCardTable.pnlPlayArea.add(addToStackLabel[1]);
      addToStackLabel[1].setBounds(330 + offset + insets.left, 50 + insets.top, 100, 120);

      //Deck images
      for(int i = 0; i < MAX_IMAGES_IN_DECK; i++)
      {
         deckLabel[i] = new JLabel();
         deckLabel[i].setName("Playing Deck " + i);
         myCardTable.pnlPlayArea.add(deckLabel[i]);
         deckLabel[i].setBounds(250 + offset + insets.left - (i / 2), 60 + insets.top - i, 100, 120);
      }
      
      //Deck count
      deckCountLabel = new JLabel("0");
      deckCountLabel.setName("Deck Count");
      myCardTable.pnlPlayArea.add(deckCountLabel);
      deckCountLabel.setBounds(274 + offset + insets.left, 45 + insets.top, 150, 20);
      
      //Buttons
      cantPlayButton = new JButton("I Cannot Play");
      Dimension size = cantPlayButton.getPreferredSize();
      cantPlayButton.setBounds(200 + offset + insets.left, 200 + insets.top, size.width + 56, size.height);
      cantPlayButton.setActionCommand("I Cannot Play");
      cantPlayButton.setVisible(true);
      myCardTable.pnlPlayArea.add(cantPlayButton);

      startTimerButton = new JButton("Click to Start Timer");
      size = startTimerButton.getPreferredSize();
      startTimerButton.setBounds(450 + offset + insets.left, 200 + insets.top, size.width + 56, size.height);
      startTimerButton.setActionCommand("Start Timer");
      startTimerButton.setVisible(true);
      myCardTable.pnlPlayArea.add(startTimerButton);

      // Player Hands
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         playerHandButtons[i] = new JButton();
         playerHandButtons[i].setVisible(true);
         playerHandButtons[i].setActionCommand("Human Player Card");
         playerHandButtons[i].setBorderPainted(false);
         myCardTable.pnlHumanHand.add(playerHandButtons[i]);

         computerHandLabels[i] = new JLabel();
         computerHandLabels[i].setVisible(true);
         myCardTable.pnlComputerHand.add(computerHandLabels[i]);
      }

      // Player Scores
      playerScoreLabels[0] = new JLabel("Score:");
      playerScoreLabels[0].setBounds(160 + offset + insets.left, 10 + insets.top, 200, 20);
      myCardTable.pnlPlayArea.add(playerScoreLabels[0]);

      playerScoreLabels[1] = new JLabel("Score:");
      playerScoreLabels[1].setBounds(300 + offset + insets.left, 10 + insets.top, 200, 20);
      myCardTable.pnlPlayArea.add(playerScoreLabels[1]);

      Date elapsed = new Date(clockCounter * 1000);
      clock.setFont(clock.getFont().deriveFont(40.0f));
      clock.setText(" " + date.format(elapsed));
      clock.setBounds(480 + offset + insets.left, 110 + insets.top, 130, 50);
      Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
      clock.setBorder(border);
      clock.setBackground(Color.WHITE);
      clock.setOpaque(true);

      myCardTable.pnlPlayArea.add(clock);
   }//End Constructor

   // Adds the controller events to the buttons and stacks.
   public void addController(Controller controller)
   {
      for (int i = 0; i < NUM_CARD_STACKS; i++)
         addToStackLabel[i].addMouseListener(controller);
      
      cantPlayButton.addActionListener(controller);
      startTimerButton.addActionListener(controller);

      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
         playerHandButtons[i].addActionListener(controller);
   }
   
   // Loads the end game prompt for the player.
   public int loadEndGamePrompt(int[] playerScores)
   {
      //Give player end game prompt message.
      String endMessage;
      if (playerScores[COMPUTER_PLAYER] > playerScores[HUMAN_PLAYER])
         endMessage = "Computer wins!\nYour score was: " + playerScores[HUMAN_PLAYER] + "\nComputer's Score was: "
               + playerScores[COMPUTER_PLAYER];
      else if (playerScores[COMPUTER_PLAYER] < playerScores[HUMAN_PLAYER])
         endMessage = "You win!\nYour score was: " + playerScores[HUMAN_PLAYER] + "\nComputer's Score was: "
               + playerScores[COMPUTER_PLAYER];
      else
         endMessage = "Tie!\nYour score was: " + playerScores[HUMAN_PLAYER] + "\nComputer's Score was: "
               + playerScores[COMPUTER_PLAYER];
      endMessage = endMessage + "\nDo you want to play again?";
      
      return (JOptionPane.showConfirmDialog(null, endMessage, "Game Over", JOptionPane.YES_NO_OPTION));
   }
   
   // Sets the card table's visibility.
   public void setVisible(boolean visible)
   {
      myCardTable.setVisible(visible);
   }

   //Sets the image for the deck using a supplied icon.
   public void setDeckImage(Icon icon)
   {
      for(int i = 0; i < deckLabel.length; i++)
         deckLabel[i].setIcon((ImageIcon) icon);
   }
   
   //Updates the deck count and images using a supplied count.
   public void updateDeckCount(int count)
   {
      if(count == 0) 
      {
         deckCountLabel.setVisible(false);
         for(int i = 0; i < deckLabel.length; i++)
            deckLabel[i].setVisible(false);
      }
      else
      {
         deckCountLabel.setText("" + count);
         deckCountLabel.setVisible(true);
         
         //Gives deck a diminished look as the count goes lower.
         for(int i = 0; i < deckLabel.length; i++)
         {
            if(i * 5 > count)
               deckLabel[deckLabel.length - i - 1].setVisible(false);
            else
               deckLabel[deckLabel.length - i - 1].setVisible(true);
         }
      }
   }
   
   // Updates the display of the hand icons for a specific player.
   public boolean updateHand(Icon[] icons, int playerIndex)
   {
      // Sets unused buttons to invisible
      if (playerIndex == HUMAN_PLAYER && icons.length < playerHandButtons.length)
         for (int i = icons.length; i < playerHandButtons.length; i++)
            playerHandButtons[i].setVisible(false);

      if (playerIndex == COMPUTER_PLAYER && icons.length < computerHandLabels.length)
         for (int i = icons.length; i < computerHandLabels.length; i++)
            computerHandLabels[i].setVisible(false);

      // Sets the icons for the hands depending on if it is a player or computer.
      for (int i = 0; i < icons.length; i++)
      {
         if (playerIndex == HUMAN_PLAYER && i < playerHandButtons.length)
         {
            if (playerHandButtons[i].getIcon() != (ImageIcon) icons[i])
               playerHandButtons[i].setIcon((ImageIcon) icons[i]);
            if (!playerHandButtons[i].isVisible())
               playerHandButtons[i].setVisible(true);
         } 
         else if (playerIndex == COMPUTER_PLAYER && i < computerHandLabels.length)
         {
            if(computerHandLabels[i].getIcon() == null) 
               computerHandLabels[i].setIcon((ImageIcon) icons[i]);
            if (!computerHandLabels[i].isVisible())
               computerHandLabels[i].setVisible(true);
         } 
         else
            return false;
      }
      update();
      return true;
   }//End UpdateHand

   // Changes the display for the stack icons.
   public boolean updateStack(Icon[] icon)
   {
      if (icon.length != NUM_CARD_STACKS)
         return false;

      for (int i = 0; i < NUM_CARD_STACKS; i++)
         addToStackLabel[i].setIcon((ImageIcon) icon[i]);
      return true;
   }

   // Updates the score on the display.
   public boolean updateScore(int score, int playerIndex)
   {
      if (playerIndex > playerScoreLabels.length)
         return false;
      else if (playerIndex == 0)
         playerScoreLabels[COMPUTER_PLAYER].setText("Computer Score: " + score);
      else
         playerScoreLabels[playerIndex].setText("Player " + playerIndex + " Score: " + score);

      return true;
   }

   // Highlights a selected button green and unhighlights any other button.
   public void updateSelectedButton(int index)
   {
      for (int i = 0; i < playerHandButtons.length; i++)
      {
         if (i == index)
            playerHandButtons[i].setBackground(Color.GREEN);
         else
            playerHandButtons[i].setBackground(null);
      }
   }

   // Updates the display.
   private void update()
   {
      myCardTable.revalidate();
      myCardTable.repaint();
   }
   
   // Returns the index of a button component in the player's hand.
   public int getPlayerButtonIndex(JButton button)
   {
      for (int i = 0; i < myCardTable.pnlHumanHand.getComponentCount(); i++)
      {
         if (button == myCardTable.pnlHumanHand.getComponent(i))
            return i;
      }
      return -1;
   }
   
   // Gets the component index of a stack label from the play area.
   public int getStackIndex(JLabel stack)
   {
      for (int i = 0; i < myCardTable.pnlPlayArea.getComponentCount(); i++)
      {
         if (stack == myCardTable.pnlPlayArea.getComponent(i))
            return i;
      }
      return -1;
   }

   //Start or Stop the timer.
   public void startTimer()
   {
      if (t.isAlive())
      {
         t = new Thread(new View.Timer());
         clockRun = false;
      } 
      else
      {
         t.start();
         clockRun = true;
      }
   }

   //Resets the timer to 0.
   public void resetTimer()
   {
      clockCounter = 0;
      clock.setText(" " + date.format(clockCounter));
   }
   
   // class for clock in game
   static class Timer extends Thread
   {
      public void run()
      {
         // long clockCounter = 0;
         while (View.clockRun == true)
         {
            clockCounter++;
            Date elapsed = new Date(clockCounter * 1000);
            // add the date to a label change test to whatever the label name is
            clock.setText(" " + date.format(elapsed));
            doNothing(1000);
            // System.out.println(clockCounter);
            startTimerButton.setText("Click to Stop Timer");

         }
         clockCounter--;
         startTimerButton.setText("Click to Restart Timer");
      }

      public void doNothing(int milliseconds)
      {
         try
         {
            Thread.sleep(milliseconds);
         } catch (InterruptedException e)
         {
            System.out.println("Unexpected interrupt.");
            System.exit(0);
         }
      }

   }//End Timer Class
}

// CardTable - Class represents the GUI for a card table.
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   // Constructor
   // Takes the title of the window, number of cards per hand, and the number of
   // players.
   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      setTitle(title);

      // Validate and set the two instance variables.
      if (numCardsPerHand > 0 && numCardsPerHand <= MAX_CARDS_PER_HAND)
         this.numCardsPerHand = numCardsPerHand;
      else
         this.numCardsPerHand = 1;

      if (numPlayers > 0 && numPlayers <= MAX_PLAYERS)
         this.numPlayers = numPlayers;
      else
         this.numPlayers = 1;

      // Create the panel layout and add the 3 panels to the frame.
      setLayout(new GridBagLayout());
      GridBagConstraints gridConstraints = new GridBagConstraints();
      gridConstraints.fill = GridBagConstraints.BOTH;
      gridConstraints.weightx = 1.0;

      // Computer Hand
      pnlComputerHand = new JPanel();
      pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
      gridConstraints.weighty = 0.0;
      gridConstraints.gridy = 0;
      add(pnlComputerHand, gridConstraints);

      // Playing Area
      pnlPlayArea = new JPanel();
      pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
      gridConstraints.weighty = 1.0;
      gridConstraints.gridy = 1;
      add(pnlPlayArea, gridConstraints);

      // Player's Hand
      pnlHumanHand = new JPanel();
      pnlHumanHand.setBorder(BorderFactory.createTitledBorder("Your Hand"));
      gridConstraints.weighty = 0.0;
      gridConstraints.gridy = 2;
      add(pnlHumanHand, gridConstraints);

      // CREATE THE PLAY AREA.
      // Create a grid layout for the play and card areas.
      // FlowLayout playAreaFlow = new FlowLayout();
      // pnlPlayArea.setLayout(playAreaFlow);
      pnlPlayArea.setLayout(null);

      GridLayout cardAreaGrid = new GridLayout(1, MAX_CARDS_PER_HAND);
      pnlComputerHand.setLayout(cardAreaGrid);
      pnlHumanHand.setLayout(cardAreaGrid);
   }// End CardTable Constructor

   // Accessors
   public int getCardsPerHand()
   {
      return numCardsPerHand;
   }

   public int getNumberOfPlayers()
   {
      return numPlayers;
   }

} // End Card Table Class.
