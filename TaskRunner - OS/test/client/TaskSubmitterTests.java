package client;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * This class acts as the end-to-end tests for the application by exercising basic functionality.
 */
public class TaskSubmitterTests {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void restoreStreams() {
	    System.setOut(originalOut);
	}

	@Test
	public void GivenAFileNameThatExistsWhenApplicationRunsThenCorrectResponseReturned() throws Exception {
		//Arrange
		String[] args = {"pom.xml"};
		String expected = "File 'pom.xml' exists: true\r\nPort 8080 is available: true\r\n";
		
		//Act
		TaskSubmitter.main(args);
		String actual = outContent.toString();
		
		//Assert
		assertEquals(expected, actual);
	}
	
	@Test
	public void GivenAFileNameThatExistsAndAnAvailablePortNumberWhenApplicationRunsThenCorrectResponseReturned() throws Exception {
		//Arrange
		String[] args = {"pom.xml", "56789"};
		String expected = "File 'pom.xml' exists: true\r\nPort 56789 is available: true\r\n";
		
		//Act
		TaskSubmitter.main(args);
		String actual = outContent.toString();
		
		//Assert
		assertEquals(expected, actual);
	}
	
	@Test
	public void GivenAFileNameThatExistsAndAnUnavailablePortNumberWhenApplicationRunsThenCorrectResponseReturned() throws Exception {
		//Arrange
		String fileName = "pom.xml";
		Integer portNumber = 56789;
		String[] args = {fileName, portNumber.toString()};;
		String expected = "File '" + fileName + "' exists: true\r\nPort " + portNumber + " is available: false\r\n";
		ServerSocket blockingSocket = new ServerSocket(portNumber);
		
		//Act
		TaskSubmitter.main(args);
		String actual = outContent.toString();
		
		//Assert
		assertEquals(expected, actual);
		blockingSocket.close();
	}
	
	@Test
	public void GivenAFileNameThatDoesNotExistAndAnUnavailablePortNumberWhenApplicationRunsThenCorrectResponseReturned() throws Exception {
		//Arrange
		String fileName = "doesnotexist.xml";
		Integer portNumber = 56788;
		String[] args = {fileName, portNumber.toString()};
		String expected = "File '" + fileName + "' exists: false\r\nPort " + portNumber + " is available: false\r\n";
		ServerSocket blockingSocket = new ServerSocket(portNumber);
		
		//Act
		TaskSubmitter.main(args);
		String actual = outContent.toString();
		
		//Assert
		assertEquals(expected, actual);
		blockingSocket.close();
	}

}
