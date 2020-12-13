
import javax.swing.JFrame;
/**
 * Class to represent a client tester and driver.
 * @author Matthew Kemp
 *
 */
public class SixGolfClientTest
{
	/**
	 * Main method to test the SixGolfClient.
	 * @param args
	 */
   public static void main(String[] args)
   {
      SixGolfClient application = new SixGolfClient("127.0.0.1");
      application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   } 
}

