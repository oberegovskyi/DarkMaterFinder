package Seeker.Seeker;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
    	System.setProperty("webdriver.chrome.driver", "c:\\chromedriver.exe");
		Viewer barym = new Viewer("Revenger","kokokoko1964", "Barym");
		barym.start();
    }
}
