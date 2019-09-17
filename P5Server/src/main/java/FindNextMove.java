//FindNextMove class to initialize and call the MinMax algorithm.
public class FindNextMove{
	//Class globam methods.
	String clientBoard;
	int serverMove;
	AI_MinMax AI;

	//Class Constructor.
	FindNextMove(){
		//Resets the board to null.
		this.clientBoard = null;
	}
	
	//Method to start the MinMax algorithm.
	public void startAI(){
		//Declares a new AI_MinMax algorithm.
		AI = new AI_MinMax(clientBoard);
		serverMove = -1;

		//Defines the clientBoard. clientBoard is updates from Server class.
		AI.clientInput = clientBoard;
		
		//Starts the AI_MinMax thread.
		AI.start();

		//Wait until the MinMax algorithm is completed.
		while(AI.isRunning != 1) {
			try{Thread.sleep(10);}catch(Exception e){}
		}

		//Check if the server made a move, 
		if(AI.bestMoves.size() != 0){
			//Define the server's move.
			serverMove = AI.bestMoves.get(0).getMovedTo();	
		}
		else{
			//Otherwise, server did not make a move.
			serverMove = -2;
		}
		System.out.println("Inside of FNM, next is: " + serverMove);
	}//End of startAI method.
}//End of FindNextMove class.