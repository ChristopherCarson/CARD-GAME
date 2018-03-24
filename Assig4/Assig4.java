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

//Phase 1 Raul define an interface, BarcodeOI
interface BarcodeIO
{
 public boolean scan(BarcodeImage bc);//accepts some image as a Barcode object
 public boolean readText(String text);//accepts a text string 
 public boolean generateImageFromText();//looks at internal text stored in implementing class
 public boolean translateImageToText();//looks at internal image stored and implements companion text string.
 public void displayTextToConsole();//prints out the text string to the console
 public void displayImageToConsole();//prints out the image to the console.
}


//Phase 2 BarcodeImage stores and retrieves 2D data (Raul and Chris)
class BarcodeImage implements Cloneable
{
 //Variables for the class
 public static final int MAX_HEIGHT = 30;
 public static final int MAX_WIDTH = 65;
 private boolean[][] image_data; // 2D array for storing image data
 
 //Default Constructor (No parameters)
 public BarcodeImage()
 {
    image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
    //Stuffs it with all blanks
    for(int x = 0; x < MAX_WIDTH; x++)
    {
       for(int y = 0; y < MAX_HEIGHT; y++)
       {
          setPixel(x, y, false);
       }
    }
 
 }//End BarcodeImage()
 
 //Our first over-loaded constructor, taking our 1d string array as a parameter
 public BarcodeImage(String[] stringArray)
 {
    image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
    int x, y, row = -1;//x and y coordinates, along with a row variable
    
    //We use our optional helper method here to insure the string is within proper
    //dimensions. Smaller is OK, larger or NULL is not.
    if (checkSize(stringArray))
    {
       //for loop for each row in our image_data array
       for (x = image_data.length - stringArray.length; x < image_data.length; x++)
       {
          row++;
          for (y = 0; y < stringArray[row].length(); y++)
          {
             if (stringArray[row].charAt(y) == '*')
             {
                setPixel(x, y, true);
             }
             else
             {
                setPixel(x, y, false);
             }
          }
       }//CheckSize(string)
    }
 }//End BarcodeImage(String[] string)
 
 public void displayToConsole()
 {
    int x, y;//x and y coordinates
    String output = "";
    
    //Create the first line
    for ( y = 0; y < MAX_WIDTH + 2; y++ )
    {
       output += "-";
    }
    output += "\r\n";
    
    //Here we pack in the actual data per row
    for (x = 0; x < MAX_HEIGHT; x++)
    {
       output += "|";
       for (y = 0; y < MAX_WIDTH; y++)
       {
          if (image_data[x][y] == true)
          {
             output += "*";
          }else {
             output += " ";
          }
       }
       output += "|";
       output += "\r\n";
    }

    //Then we wrap it up with one more line
    for ( y = 0; y < MAX_WIDTH + 2; y++ )
    {
       output += "-";
    }
    output += "\r\n";
    
    //And finally print it
    System.out.print(output);
 }//End displayToConsole()
 
 private boolean checkSize(String[] stringArray)
 {
    //Here is check all values in the 1D string array to insure
    //none are null or longer than our image_data 2D array
    if (stringArray == null || stringArray.length > MAX_HEIGHT)
    {
       return false;
    }
    
    for (int x = 0; x < stringArray.length; x++)
    {
       if (stringArray[x] == null || stringArray[x].length() > MAX_WIDTH)
       {
          return false;
       }
    }
    
    return true;
 }//End checkSize(String[] stringArray)
 
 //reassigns index of the 2D array to value and checks to make sure it is successful
 public boolean setPixel(int x, int y, boolean value)
 {
    try
    {
       image_data[x][y] = value;
       return true;
    } 
    catch (IndexOutOfBoundsException e)
    {
       //throws exception in case index is out of bounds
       return false;
    }
 }//End setPixel
 
 //Accesses and returns x and y values from the 2D array
 public boolean getPixel(int x, int y)
 {
    try
    {
       return image_data[x][y];
    } 
    catch (IndexOutOfBoundsException e)
    {
       //throws exception in case index is out of bounds
       return false;
    }
 }//End getPixel
}//End BarcodeImage class

