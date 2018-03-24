//Phase 1 Raul define an interface, BarcodeOI
public interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);//accepts some image as a Barcode object
   public boolean readText(String text);//accepts a text string 
   public boolean generateImageFromText();//looks at internal text stored in implementing class
   public boolean translateImageToText();//looks at internal image stored and implements companion text string.
   public void displayTextToConsole();//prints out the text string to the console
   public void displayImageToConsole();//prints out the image to the console.
}