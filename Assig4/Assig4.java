//Team Code Blooded
//Raul Ramirez, Chris Carson, Michael Rose and John Coffelt

//Begin class Assig4
public class Assig4 
{

	public static void main(String[] args) 
	{
	   String[] sImageIn =
	   {
	      "                                               ",
	      "                                               ",
	      "                                               ",
	      "     * * * * * * * * * * * * * * * * * * * * * ",
	      "     *                                       * ",
	      "     ****** **** ****** ******* ** *** *****   ",
	      "     *     *    ****************************** ",
	      "     * **    * *        **  *    * * *   *     ",
	      "     *   *    *  *****    *   * *   *  **  *** ",
	      "     *  **     * *** **   **  *    **  ***  *  ",
	      "     ***  * **   **  *   ****    *  *  ** * ** ",
	      "     *****  ***  *  * *   ** ** **  *   * *    ",
	      "     ***************************************** ",  
	      "                                               ",
	      "                                               ",
	      "                                               "

	   };      
	            
	         
	      
	   String[] sImageIn_2 =
	   {
	         "                                          ",
	         "                                          ",
	         "* * * * * * * * * * * * * * * * * * *     ",
	         "*                                    *    ",
	         "**** *** **   ***** ****   *********      ",
	         "* ************ ************ **********    ",
	         "** *      *    *  * * *         * *       ",
	         "***   *  *           * **    *      **    ",
	         "* ** * *  *   * * * **  *   ***   ***     ",
	         "* *           **    *****  *   **   **    ",
	         "****  *  * *  * **  ** *   ** *  * *      ",
	         "**************************************    ",
	         "                                          ",
	         "                                          ",
	         "                                          ",
	         "                                          "

	   };
	     
	      BarcodeImage bc = new BarcodeImage(sImageIn);
	      bc.displayToConsole();
	      
         BarcodeImage bc2 = new BarcodeImage(sImageIn_2);
         bc2.displayToConsole();
	      

	}//End main

}//End Class Assig4




