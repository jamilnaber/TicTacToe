import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.function.Consumer;
import javafx.scene.control.ListView;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType; 

//Class Client that connects between client and server.
public class Client extends Thread{
	//Declaring class global members.
	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	String iPaddress;
	int portNumber;
	GameInfo gameInfo;
	Consumer<String> clietnListCallback;
	int isConnected = 0;
	boolean playerHasMadeMove = false;
	
	//Client contructor. 
	Client(String iPaddress, int portNumber, Consumer<String> clietnListCallback){
		//Defines IP address, port number from client GUI.
		this.iPaddress = iPaddress;
		this.portNumber = portNumber;
		this.clietnListCallback = clietnListCallback;
	}//End of Constructor
	
	//Threaded run method. 
	public void run(){
		try {
			//Create a new socket connection.
			socketClient = new Socket();

			//Try to connect to the IP and port port number. If doesn't succeed after .5 seconds, then fails and user needs to try to connect again.
			socketClient.connect(new InetSocketAddress(iPaddress, portNumber), 500);

			//Defin the output stream from client to server.
			out = new ObjectOutputStream(socketClient.getOutputStream());

			//Define the input stream from server to client.
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);

			//Recieve GameInfo object from the server.
			gameInfo = recieve();

			//Declare the client has a successful connection to the server.
			isConnected = 2;

			//While true loop for recieving GameInfo (serve moves, server moves, game board and winner).
			while(true) {
				//Sending GameInfo Object.
				try {gameInfo = (GameInfo) in.readObject();} catch(Exception E) {break;}
				System.out.println("Client Has Received Server Move : " + gameInfo.serversNextMove);
				//Callback to GUI ListView.
				if(gameInfo.serversNextMove != null) {
					clietnListCallback.accept("Client Has Received Server Move : " + gameInfo.serversNextMove);
				}
			}
		}
		catch(Exception E) {
			//If connection fails, define connection as fails.
			isConnected = 1;
		}
	}//End of run method.

	//Send method to send a GameInfo object through the outstream.
	public void send(GameInfo object){
    	try{
    		out.writeObject(object);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }//End of send method.
	

	//Recieve method to recieve a GameInfo object through the instream.
	public GameInfo recieve(){
    	GameInfo newGameInfo;
    	try{
    		newGameInfo = (GameInfo) in.readObject();
    	}	
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	return newGameInfo;
    }//End of recieve method.
}//End of Client Class.