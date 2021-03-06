/** Name: Jacob Smith
 *  Email:jsmith2021@brandeis.edu
 *  Assignment: 
 *  Date: May 16, 2019
 *  Sources: redirecting system.in to string: https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
 *  redirexting system.out to string:https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
 *  Bugs:
 */
package client;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import testBackgroundCode.AssertMethods;
import enums.ArduinoClassExample;
import enums.ArduinoClassHardCoded;

public class ArduinoClassClientTest {

	@Test
	/**
	 * Verifies that the ArdunoClass Client Scanner example
	 * produced the hardcoded correct body header and keyword files
	 * when primed with an input stream of correct inputs
	 */
	public void testScannerExample () {
		//save the old input and output streams
		InputStream keyboard=System.in;
		
		setStream();
		
		//run the console printing example
		ArduinoClassContainer cont=ArduinoClassClient.scannerExample();
		resetStream(keyboard);
		
		//verify that files where created correctly
		assertContainer(cont);
		
		
	}
	/**
	 * asserts that the contents of the ArduinoClassContainer matches the hardcoded exmaple files
	 * @param cont the ArduinoClassContainer to verify
	 */
	private static void assertContainer(ArduinoClassContainer cont){
		System.out.println(AssertMethods.assertEqualsFeedback(ArduinoClassHardCoded.CPP_FILE.toString(), cont.getBody()));
		assertEquals(ArduinoClassHardCoded.CPP_FILE.toString(),cont.getBody());
		System.out.print("Header File Test Console:");
		System.out.println(AssertMethods.assertEqualsFeedback(ArduinoClassHardCoded.H_FILE.toString(), cont.getHeader()));
		assertEquals(ArduinoClassHardCoded.KEYWORDS_FILE.toString(),cont.getKeywords());
		System.out.print("Keywords File Test Console:");
		System.out.println(AssertMethods.assertEqualsFeedback(ArduinoClassHardCoded.KEYWORDS_FILE.toString(), cont.getKeywords()));
		assertEquals(ArduinoClassHardCoded.KEYWORDS_FILE.toString(),cont.getKeywords());
	}
	
	/**
	 * Sets the input and output streams of the system for testing
	 * System.in redirects to a predifined string of user responces
	 * System.out redirects to 
	 */
	private static void setStream(){
		//set the System.in input stream to predfined input for testing
		System.setIn(getInputStream());
	}
	
	/**
	 * generates an input stream corresponding to what the user could enter to
	 * automatically generate an arduino class
	 * @return the input stream used
	 */
	private static ByteArrayInputStream getInputStream(){
		//build a string of all the resopnces needed to create an arduino class
		String allResponces="";
		for(ArduinoClassExample example : ArduinoClassExample.values()){
			allResponces+=example+ArduinoClassClient.consoleToken+"\n";
		}
		//set System.in to feed in that stream
		ByteArrayInputStream in = new ByteArrayInputStream(allResponces.getBytes());
		return in;
	}
	/**
	 * resets the keyboard to correct input
	 */
	private static void resetStream(InputStream keyboard){
		// optionally, reset System.in and out
		System.setIn(keyboard);
	}

}
