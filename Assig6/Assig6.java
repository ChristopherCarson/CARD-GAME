//Assignment 6 CST 338
//John Coffelt, Michael Rose, Christopher Carson, Raul Ramirez

//Phase 3 - Michael Rose
public class Assig6 {
   public static void main(String[] args) {

      // CREATE THE GAME
      int NUM_CARDS_PER_HAND = 7;
      int NUM_PLAYERS = 2;
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      GameModel.Card[] unusedCardsPerPack = null;

      GameModel gameModel = new GameModel(numPacksPerDeck, numJokersPerPack, numUnusedCardsPerPack, unusedCardsPerPack,
            NUM_PLAYERS, NUM_CARDS_PER_HAND);

      // initialize View
      GameView gameView = new GameView("CardGame");

      GameController gameController = new GameController(gameModel, gameView);

      gameController.StartGame();

      System.exit(0);
   }// end Main

}// end Assig6
