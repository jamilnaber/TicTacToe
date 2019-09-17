import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import java.lang.Thread;
import java.io.IOException;

//Class to set up the Server and server-client connections and threads.
public class Server{	
	//Server class global members.
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer theServer;
	private Consumer<String> callback;
	int portNumber;
	int count;
	int isConnected = 0;
	GameInfo gameInfo;
	FindNextMove nextMove;

	//Server class constructor.
	Server(Consumer<String> call, int portNumber){
			//Defines port number, and attempts to start a new thread.
			theServer = new TheServer();
			this.portNumber = portNumber;
			this.callback = call;
			try{theServer.start();}catch(Exception e){throw e;}
			nextMove = new FindNextMove();
	}//End of Server constructor.


	//Nested inner class to connect to new clients, and start a new client-server thred.
	public class TheServer extends Thread {
		//Run method that defines the new thread.
		public void run(){
			//Attempting to start the server.
			try(ServerSocket mysocket = new ServerSocket(portNumber);){
		   	 	System.out.println("Server is waiting for a client!");
		   	 	isConnected = 1;
		   	 	//Wait for clients to connect. If connect, start that Server <-> Client thread.
			    while(true){
			    	//Define a client thread. 
					ClientThread c = new ClientThread(mysocket.accept(), count);

					//Add client to client list.
					clients.add(c);
					callback.accept("Client "+ count +" Has Connected");
					
					//Start the client-server thread.
					c.start();	

					//Increment the client count.
					count++;	
				}
			}
			catch(Exception e) {
				//If server fails to start, then do not advance and user must start server again.  
				isConnected = -1;
			}
		}//End of run method.
	}//End of TheServer class.


	//Nested inner class for the client-server conections. 
	public class ClientThread extends Thread{
		//Global inner class members.
		Socket connection;
		int count;
		ObjectOutputStream out;
		ObjectInputStream in;

		//Class constructor.
		ClientThread(Socket socket, int count){
			//Defines the socket connection.
			this.connection = socket;
		
			//Defines the player count number.
			this.count = count;	
		}


		//Defines the threaded funciton.
		public void run(){
			try{
				//Define the out stream to the client.
				out = new ObjectOutputStream(connection.getOutputStream());
				
				//define this server's thread in stream.
				in = new ObjectInputStream(connection.getInputStream());
				connection.setTcpNoDelay(true);
				
				//Declare a new GameInfo object for this player.
				gameInfo = new GameInfo();
			}
			catch(Exception e){	
				System.out.println("Streams not open");
				System.out.println(e.toString());
				e.printStackTrace();
			}
			
			//Send the cleint the new gameInfo object.
			try{out.writeObject(gameInfo);} catch(Exception E){}

			//Callback to GUI.
			callback.accept("SENT: Client "+ count);

			//Loop of sending the GameInfo object between the client and server.
			while(true) {
				//Recieve the players move.
				try{gameInfo = (GameInfo) in.readObject();} catch(Exception E){break;}
				
				//Callback to GUI to write to ListView.
				callback.accept("RECEIVED: Client "+ count);

				//Initialize that server has not made a move yet.
				nextMove.serverMove = -1;

				//Get the current board for the server to make its move.
				nextMove.clientBoard = gameInfo.currentBoard;
				
				//Check if the player has won the game before the server makes its move.
				gameInfo.winner = checkWinner(gameInfo.currentBoard, -1);

				//If the player did not win.
				if(gameInfo.winner.equals("None")) {
					
					//Get the next move by calling the FindNextMove method. 
					//This is syncronized. This will be locked until method is finished.
					synchronized(nextMove){
						nextMove.startAI();
					}

					//While the server is figuring out the next move, just wait.
					while(nextMove.serverMove == -1) {
						try{Thread.sleep(1);}catch(Exception e){}
					}
					
					//Server's next move is recievd. 
					if(nextMove.serverMove > 0) {
						//Notifiy the Server GUI in the ListView.
						callback.accept("In Client "+ count +", the servers move is " + nextMove.serverMove);
						//Check if there is a winner and write it to the gameInfo object.
						gameInfo.winner = checkWinner(gameInfo.currentBoard, nextMove.serverMove);
					}
					//Write the Server's move to the GameInfo object.
					gameInfo.serversNextMove = Integer.toString(nextMove.serverMove);	
				}
				//If there is a winner...
				if(!gameInfo.winner.equals("None")){
					//Notify the GUI server ListView of the game's winner.
					callback.accept("In Client "+ count +", the " + gameInfo.winner +" Has Won" );	
				}
				//Send the client the updated GameInfo object with potential winner and server move.
				try{out.writeObject(gameInfo);} catch(Exception E){break;}
				callback.accept("SENT: Client "+ count);
			}//End of while(true) loop.
		}//End of run method.
	}//End of ClientThread class.

	
	//Method to check the winner. 
	public String checkWinner(String board, int serverIntMove){
		//Takes board string, and deletes the white space.
		String boardMinusWhiteSpace = board.replaceAll("\\s+", "");
		
		//Converts the spaceless board string and converts it to a character array.
		char[] charArr = boardMinusWhiteSpace.toCharArray();
		
		//Defines a 3x3 move array.
		int [][] moveArr = new int[3][3];

		//Updates move array with Servers new move, if made.
		if(serverIntMove != -1) {
			charArr[serverIntMove - 1] = 'X';
		}
		//Converts the 1D array of moves to a 2D array.
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(charArr[(i*3) + j] == 'b')
     				moveArr[i][j] = 0;
				else if (charArr[(i*3) + j] == 'X')
					moveArr[i][j] = -1;
				else if (charArr[(i*3) + j] == 'O')
					moveArr[i][j] = 1;
     		}
     	}
     	  
      	//Checking each row for a winner.
     	int sum = 0;
    	for(int i = 0; i < 3; i++){
    		sum = 0;
			for(int j = 0; j < 3; j++)
				sum += moveArr[i][j];
			if(sum == 3 || sum == -3)
				return declareWinner(sum);
		}

		//Checking each collumn for a winner.
		for(int i = 0; i < 3; i++){
    		sum = 0;
			for(int j = 0; j < 3; j++)
				sum += moveArr[j][i];
			if(sum == 3 || sum == -3)
				return declareWinner(sum);
		}

		//Checking the [0][0], [1][1], [2][2] diagnol for a winner.
		sum = 0;
		sum = moveArr[0][0] + moveArr[1][1] + moveArr[2][2];
		if(sum == 3 || sum == -3)
			return declareWinner(sum);
		
		//Checking the [0][2], [1][1], [2][0] diagnol for a winner.
		sum = 0;
		sum = moveArr[0][2] + moveArr[1][1] + moveArr[2][0];
		if(sum == 3 || sum == -3)
			return declareWinner(sum);
		

		//Check if there is a tie.
		for(int i = 0; i < charArr.length; i++){
			if(charArr[i] == 'b')
				return "None";
		}

		//If no one won and there are no spaces left, then game is a tie.
		return "Tie";
	}//End of checkWinner method.


	//Method declare the winner depending on the sum of row/collumn, diagnol.
	String declareWinner(int sum){
		//If sum is 3, then player is the winner.
		if(sum == 3){
			return "Player";
		}
		//If sum is -3, then the Server is the winner.
		else if(sum == -3){
			return "Computer";
		}
		//If sum is not 3, then no one won.
		else{
			return "None";
		}
	}//End of declareWinner method.
}//End of Server class.