import java.util.ArrayList;

public class Assignment4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//Phase 1 Raul Define an interface, BarcodeOI
	public interface BarcodeIO
	{
	   public boolean scan(final BarcodeImage bc);//accepts some image as a Barcode object
	   public boolean readText(final String text);//accepts a text string 
	   public boolean generateImageFromText();//looks at internal text stored in implimenting class
	   public boolean translateImageToText();//looks at internal image stored and implements companion text string.
	   public void displayTextToConsole();//prints out the text string to the console
	   public void displayImageToConsole();//prints out the image to the console.
	}
	//Phase 2 BarcodeImage stores and retrieves 2D data 
	public class BarcodeImage implements Cloneable{
		//Data for the class
	    public static final int MAX_HEIGHT = 30;
	    public static final int MAX_WIDTH = 65;
	    private boolean[][] imageData; // 2D array storing image data
	    public BarcodeImage(){//puts image data into an array
	      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
	      for(int x = 0; x < MAX_WIDTH; x++){
	         for(int y = 0; y < MAX_HEIGHT; y++){
	            setPixel(x, y, false);
	         }
	      }
	   }
	   //reassigns index of the 2D array to value and checks to make sure it is successful
	   public boolean setPixel(int row, int col, boolean value){
	      try{
	         imageData[row][col] = value;
	         return true;
	      } catch (IndexOutOfBoundsException e){//throws exception in case index is out of bounds
	         return false;
	      }
	   }
	   //Accesses and returns x and y values from the 2D array
	   public boolean getPixel(int row, int col){
	      try{
	         return imageData[row][col];
	      } catch (IndexOutOfBoundsException e){//throws exception in case index is out of bounds
	         return false;
	      }
	   }
	}
}
