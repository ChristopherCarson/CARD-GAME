//Assignment 6 CST 338
//John Coffelt, Michael Rose, Christopher Carson, Raul Ramirez

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
