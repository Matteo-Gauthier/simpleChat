// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @author Matteo gauthier
 * @version November 2022
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param loginID the login ID.
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginID, String host, int port)
  {
    try 
    {
      client= new ChatClient(loginID, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("> Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
    	
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
        
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The login ID (mandatory)
   * @param args[1] The host to connect to
   * @param args[2] The port number
   */
  public static void main(String[] args) 
  {
    String host = ""; //host name
    int port = 0; //Port to connect on

    try
    {
      host = args[1]; //Get host from command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost"; //Set host to "localhost"
    }
    
    try
    {
      port = Integer.parseInt(args[2]); //Get port from command line
    }
    catch(ArrayIndexOutOfBoundsException | NumberFormatException e)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    
    try
    {
      ClientConsole chat= new ClientConsole(args[0], host, port);
      chat.accept();  //Wait for console data
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("> Error: missing loginID argument");
    }
    
  }
}
//End of ConsoleChat class
