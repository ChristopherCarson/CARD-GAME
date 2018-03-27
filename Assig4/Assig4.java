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
	      DataMatrix dm = new DataMatrix(bc);
	     
	      // First secret message
	      dm.translateImageToText();
	      dm.displayTextToConsole();
	      dm.displayImageToConsole();
	      
	      // second secret message
	      bc = new BarcodeImage(sImageIn_2);
	      dm.scan(bc);
	      dm.translateImageToText();
	      dm.displayTextToConsole();
	      dm.displayImageToConsole();
	      
	      // create your own message
	      dm.readText("Thi§ is a te§t. There are many like it, but thi§ one i§ mine.");
	      dm.generateImageFromText();
	      dm.displayTextToConsole();
	      dm.displayImageToConsole();
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
 public static final int MAX_WIDTH = 65;
 public static final int MAX_HEIGHT = 30;
 private boolean[][] image_data; // 2D array for storing image data
 
 //Default Constructor (No parameters)
 public BarcodeImage()
 {
    image_data = new boolean[MAX_WIDTH][MAX_HEIGHT];
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
    image_data = new boolean[MAX_WIDTH][MAX_HEIGHT];
    int x, y, row = -1;//x and y coordinates, along with a row variable
    
    //We use our optional helper method here to insure the string is within proper
    //dimensions. Smaller is OK, larger or NULL is not.
    if (checkSize(stringArray))
    {
       //for loop for each row in our image_data array
       for (y = 0; y < stringArray.length; y++)
       {
          row++;
          for (x = 0; x < stringArray[row].length(); x++)
          {
             if (stringArray[row].charAt(x) == '*')
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
    for (x = 0; x < MAX_WIDTH + 2; x++)
    {
       output += "-";
    }
    output += "\r\n";
    
    //Here we pack in the actual data per row
    for (y = 0; y < MAX_HEIGHT; y++)
    {
       output += "|";
       for (x = 0; x < MAX_WIDTH; x++)
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
    for (x = 0; x < MAX_WIDTH + 2; x++)
    {
       output += "-";
    }
    output += "\r\n";
    
    //And finally print it
    System.out.print(output);
 }//End displayToConsole()
 
 private boolean checkSize(String[] stringArray)
 {
    //Here is check all values in the 1D string array to ensure
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
 
 public Object clone() throws CloneNotSupportedException
 {
     return super.clone();
 }
}//End BarcodeImage class

//Phase 3 - John Coffelt
//Class represents the DataMatrix for a Barcode Image.
class DataMatrix implements BarcodeIO 
{
    //Global variables that represent the images for a boolean value.
    public static final char BLACK_CHAR = '*';
    public static final char WHITE_CHAR = ' ';
    
    //Private variables
    private BarcodeImage image;
    private String text;
    private int actualWidth, actualHeight;
    
    //Constructors
    public DataMatrix() 
    {
        image = new BarcodeImage();
        text = "undefined";
        actualWidth = 0;
        actualHeight = 0;    
    }
    
    public DataMatrix(BarcodeImage image) {
        text = "undefined";
        scan(image);
    }
    
    public DataMatrix(String text) 
    {
        image = new BarcodeImage();
        readText(text);
    }
    
    //Accessors
    public int getActualWidth() 
    {
        return actualWidth;
    }
    
    public int getActualHeight() 
    {
        return actualHeight;
    }
    
    //Reads a text string and after validation, sets it to the barcode text, but does not change image.
    public boolean readText(String text) 
    {
        //First make sure the string is not null.
        try 
        {
            //Make sure that the text can fit within the limits of the barcode image.
            if(text.length() < BarcodeImage.MAX_WIDTH - 2) 
            {
                this.text = text;
                return true;
            }
            else 
            {
                return false;
            }
        }
        catch(NullPointerException e) {
            return false;
        }
    }
    
    //Scans a barcode image and puts it into a standard format for further manipulation.
    public boolean scan(BarcodeImage image) 
    {
        //First, create a clone of the of the current image.
        try 
        {
            this.image = (BarcodeImage)image.clone();
        } 
        catch (CloneNotSupportedException e) {} 
        
        cleanImage();
        actualHeight = computeSignalHeight();
        actualWidth = computeSignalWidth();

        return true;
    }
    
    //Checks if the signal is already lower left oriented and if not calls a method to move the image.
    private void cleanImage()
    {
        if (!image.getPixel(0, BarcodeImage.MAX_HEIGHT - 1)) 
        {
            moveImageToLowerLeft(); 
        }
    }
    
    //Moves the signal data to the lower left of the barcode image.
    private void moveImageToLowerLeft() 
    {
        //Starting in the lower left corner ...
        for(int x = 0; x < BarcodeImage.MAX_WIDTH; x++)
        {
            for(int y = BarcodeImage.MAX_HEIGHT - 1; y >= 0; y--) 
            {
                //... Look for the first sign of the signal.
                if(image.getPixel(x, y)) 
                {
                    shiftImageLeft(x);
                    shiftImageDown(BarcodeImage.MAX_HEIGHT - 1 - y);
                    return;
                }
            }
        }
    }
    
    //Shifts the image downwards using an offset value that represents the distance from the bottom.
    private void shiftImageDown(int offset) 
    {
        if(offset > 0) {
            for(int x = 0; x  < BarcodeImage.MAX_WIDTH; x++) 
            {
                for(int y = BarcodeImage.MAX_HEIGHT - 1; y >= 0; y--) 
                {
                    if(y - offset >= 0)
                    {
                        image.setPixel(x, y, image.getPixel(x, y - offset));
                    }
                    else 
                    {
                        image.setPixel(x, y, false);
                    }    
                }
            }
        }
    }
    
    //Shifts the image towards the left using an offset value that represents the distance from the left.
    private void shiftImageLeft(int offset)
    {
        if(offset > 0) {
            for(int x = 0; x < BarcodeImage.MAX_WIDTH; x++) 
            {
                for(int y = 0; y < BarcodeImage.MAX_HEIGHT; y++) 
                {
                    if(x + offset <= BarcodeImage.MAX_WIDTH)
                    {
                        image.setPixel(x, y, image.getPixel(x + offset, y));
                    }
                    else 
                    {
                        image.setPixel(x, y, false);
                    }
                }
            }
        }
    }
    
    //Calculates the actual height of the barcode signal including the borders. 
    private int computeSignalHeight()
    {
        int y;
        for(y = BarcodeImage.MAX_HEIGHT - 1; y >= 0; y--) 
        {
            if(!this.image.getPixel(0, y)) { 
                break;
            }
        }
        return BarcodeImage.MAX_HEIGHT - 1 - y;
    }
    
    //Calculates the actual width of the barcode signal including the borders. 
    private int computeSignalWidth()
    {
        int x;
        for(x = 0; x < BarcodeImage.MAX_WIDTH; x++) 
        {
            if(!this.image.getPixel(x, BarcodeImage.MAX_HEIGHT - 1)) {
                break;
            }
        }
        return x;
    }
    
    //Converts the text value into a barcode image.
    public boolean generateImageFromText() 
    {
        clearImage();
        for(int x = 0; x <= text.length() + 1; x++) {
            for(int y = BarcodeImage.MAX_HEIGHT - 10; y < BarcodeImage.MAX_HEIGHT; y++) {
                if(x == 0 || y == BarcodeImage.MAX_HEIGHT - 1) 
                {
                    image.setPixel(x,y,true);
                }
                else if (y == BarcodeImage.MAX_HEIGHT - 10)
                {
                    image.setPixel(x,y,x % 2 == 0);
                }
                else if (x == text.length() + 1)
                {
                    image.setPixel(x,y,y % 2 == 1);
                }
                else 
                {
                    WriteCharToCol(x, (int)text.charAt(x - 1));
                }
            }
        }
        
        //After conversion, rescan the image.
        scan(image);
        return true;
    }
    
    //Writes the character to the signal column based on its ascii value.
    private boolean WriteCharToCol(int col, int code) 
    {
        int[] factors = {128,64,32,16,8,4,2,1};

        for(int y = 0; y < factors.length; y++)
        {
            image.setPixel(col, BarcodeImage.MAX_HEIGHT - 9 + y, code / factors[y] == 1);
            if(code / factors[y] == 1)
            {
                code = code - factors[y];
            }
        }
        return code == 0;
    }
    
    //Translates the signal image into a text string.
    public boolean translateImageToText() 
    {
        text = "";
        for(int x = 1; x < actualWidth - 1; x++)
        {
            text += readCharFromCol(x);
        }
        return text != "" && text != null;
    }
    
    //Returns a character by evaluating the total ascii value of one column in the signal.
    private char readCharFromCol(int col)
    {
        int colSum = 0;
        for(int y = 0; y < actualHeight; y++) 
        {
            if(image.getPixel(col, BarcodeImage.MAX_HEIGHT - actualHeight + y))
            {
                colSum += getRowValue(y);
            }
        }
        return (char)colSum;
    }
    
    //Returns a value based on the row of the signal. 
    private int getRowValue(int value)
    {
        switch (value) 
        {
            case 1: return 128;
            case 2: return 64;
            case 3: return 32;
            case 4: return 16;
            case 5: return 8;
            case 6: return 4;
            case 7: return 2;
            case 8: return 1;
            default: return 0;
        }
    }
    
    //Displays the text to the console.
    public void displayTextToConsole() {
        System.out.println(text);
    }
    
    //Displays the image to the console.
    public void displayImageToConsole() 
    {
        //Start at -1 to account for bordering.
        for(int y = -1; y <= actualHeight; y++) 
        {
            //Width is the inner loop since the console will print left to right before a new line.
            for(int x = -1; x <= actualWidth; x++)
            {
                if(y == -1 || y == actualHeight) 
                {
                    System.out.print('-');
                }
                else if(x == -1 || x == actualWidth) 
                {
                    System.out.print('|');
                }
                else 
                {
                    if(image.getPixel(x, BarcodeImage.MAX_HEIGHT - actualHeight + y))
                    {
                        System.out.print(BLACK_CHAR);
                    }
                    else 
                    {
                        System.out.print(WHITE_CHAR);
                    }
                }
            }
            System.out.println("");
        }
    }
    
    //Displays the raw image to the console.
    public void displayRawImage() 
    {
        image.displayToConsole();
    }
    
    //Clears the barcode image completely by setting every value to false.
    private void clearImage() 
    {
        for(int x = 0; x < BarcodeImage.MAX_WIDTH; x++) 
        {
            for(int y = 0; y < BarcodeImage.MAX_HEIGHT; y++)
            {
                image.setPixel(x, y, false);
            }
        }
    }
} //End DataMatrix Class

/*
CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
-------------------------------------------
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
----------------------------------------
Thi§ is a te§t. There are many like it, but thi§ one i§ mine.
-----------------------------------------------------------------
|* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *|
|*   *        *                                  *      *      *|
|****  ** * ** *  ***** *** **** **** **  *** ***  *** *  ****  |
|* *************** *********************************************|
|**     *   *  *  *  *   *     *       *   ** *                 |
|* **  *        *  *        * ** ***  * *      **  **  *  *** **|
|**  *      ***** * * *   * * *  *  *  **  ** *  * ***  * * *** |
|*   *  *     * *    *   *    *    *      *      * **   *   * **|
|*  ** ** *  **     * * * * ** *  *** *    *    ** * * ** ** *  |
|***************************************************************|
-----------------------------------------------------------------
*/
