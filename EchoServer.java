// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @author Matteo gauthier
 * @version November 2022
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  /**
   * The string added to the start of all server messages.
   */
  final public static String SERVER_MSG = "SERVER MSG> ";
  
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * @param serverUI The interface type variable.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException 
  {
    super(port);
    this.serverUI = serverUI;
    listen(); //Start listening for connections
	
  }
  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String[] msgSplit = msg.toString().split(" ");
	
	//Normal case
	if (msgSplit.length != 0 && !msgSplit[0].equals("#login")) {
	  serverUI.display("Message received: " + msg + " from " + client);
	  this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
	  return;
    }
	
	//#login case
	if (client.getInfo("loginID") == null)
	{ //Makes sure this is first message from client
	  try
	  {
		client.setInfo("loginID", msgSplit[1]); //saves loginID
		return;
	  }
	  catch (ArrayIndexOutOfBoundsException e) {} //missing arg
	}
	try {
	  client.close(); //Close client (reached if something went wrong)
	} catch (IOException e1) {}
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message) {
	
	try
    {
	  //Normal case
	  if (message == "" || message.charAt(0) != '#')
	  {
		sendToAllClients(SERVER_MSG + message);
		serverUI.display(SERVER_MSG + message);
        return;
      }
      
      //custom commands
      String[] commandArgs = message.split(" ");
      switch (commandArgs[0])
      {
      
      case "#quit":
    	close();
    	System.exit(0);
        break;
        
      case "#stop":
    	stopListening();
        break;
        
      case "#close":
    	close();
        break;
        
      case "#setport":
        if (isListening() || getNumberOfClients() != 0)
        { //Error if server is currently open
          serverUI.display("Error: Can't set port while open.");
          break;
        }
        int port;
        try
        {
          port = Integer.parseInt(commandArgs[1]); //set port
        }
        catch(ArrayIndexOutOfBoundsException | NumberFormatException e)
        { //Wrong or missing argument
          serverUI.display("Error: Wrong or missing argument.");
          break;
        }
        setPort(port);
        break;
        
      case "#start":
    	listen();
        break;
          
      case "#getport":
    	serverUI.display(Integer.toString(getPort()));
        break;
        
      default:
    	serverUI.display("Error: " + commandArgs[0] +
        		  " is not a valid argument.");
          
      }

    }
    catch(IOException e)
    {
      serverUI.display
        ("Could not send message.  Terminating server.");
  	  System.exit(0);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  /**
   * @Override
   * Method called each time a new client connection is accepted.
   * @param client the connection connected to the client.
   */
  protected void clientConnected (ConnectionToClient client)
  {
	serverUI.display("New client connection " + client);
  }
  
  /**
   * @Override
   * Method called each time a client disconnects.
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
		  ConnectionToClient client)
  {
	serverUI.display("Client disconnected " + client);
  }
  
}
//End of EchoServer class
