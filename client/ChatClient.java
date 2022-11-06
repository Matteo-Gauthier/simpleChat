// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Matteo gauthier
 * @version November 2022
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  /**
   * The login ID used to identify the client to the server
   */
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param loginID the login ID.
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginID = loginID;
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
	
	try
    {
	  if (message == "" || message.charAt(0) != '#')
	  {
		sendToServer(message);
        return;
      }
      
      //custom commands
      String[] commandArgs = message.split(" ");
      switch (commandArgs[0])
      {
      
      case "#quit":
        quit();
        break;
        
      case "#logoff":
        closeConnection();
        break;
        
      case "#sethost":
        if (isConnected())
        {
          clientUI.display("Error: Can't set host while "
        		+ "already connected.");
          break;
        }
        String host;
        try
        {
          host = commandArgs[1];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
          clientUI.display("Error: Missing argument.");
          break;
        }
        setHost(host);
        break;
        
      case "#setport":
        if (isConnected())
        {
          clientUI.display("Error: Can't set port while "
        	  + "already connected.");
          break;
        }
        int port;
        try
        {
          port = Integer.parseInt(commandArgs[1]);
        }
        catch(ArrayIndexOutOfBoundsException | NumberFormatException e)
        {
          clientUI.display("Error: Wrong or missing argument.");
          break;
        }
        setPort(port);
        break;
          
      case "#login":
        if (isConnected())
        {
          clientUI.display("Error: Already logged in.");
          break;
        }
        openConnection();
        break;
        
      case "#gethost":
        clientUI.display(getHost());
        break;
          
      case "#getport":
        clientUI.display(Integer.toString(getPort()));
        break;
        
      default:
        clientUI.display("Error: " + commandArgs[0] +
        		  " is not a valid argument.");
          
      }

    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
   * Called after a connection has been established.
   */
  protected void connectionEstablished() {
	try
    {
	  sendToServer("#login " + loginID);
    }
    catch(IOException e) {
      clientUI.display
        ("Could not login.  Terminating client.");
      quit();
    }
  }
  
}
//End of ChatClient class
