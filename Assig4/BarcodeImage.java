//Phase 2 BarcodeImage stores and retrieves 2D data (Raul and Chris)
public class BarcodeImage implements Cloneable
{
   //Data for the class
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
   
   //Our first over-loaded constructor, taking one String Variable as a parameter
   public BarcodeImage(String[] string)
   {
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      int x, y, row = -1;//x and y coordinates, along with a row variable
      
      //We use our optional helper method here to insure the string is within proper
      //dimensions. Smaller is OK, larger or NULL is not.
      if (checkSize(string))
      {
         //for loop for each row in our image_data array
         for (x = image_data.length - string.length; x < image_data.length; x++)
         {
            row++;
            for (y = 0; y < string[row].length(); y++)
            {
               if (string[row].charAt(y) == '*')
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