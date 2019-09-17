import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to read in a state of a tic tac toe board. It creates a MinMax object and passes the state to it. What returns is a list 
 * of possible moves for the player X that have been given min/max values by the method findMoves. The moves that can result in a win or a 
 * tie for X are printed out with the method printBestMoves()
 * 
 * @author Mark Hallenbeck
 *
 * Copyright© 2014, Mark Hallenbeck, All Rights Reservered.
 *
 */
public class AI_MinMax extends Thread{
	
	private String[] init_board;
	
	public ArrayList<Node> movesList;
	
	public ArrayList<Node> bestMoves;
	
	public String clientInput;
	
	public int isRunning = 0;
	
	AI_MinMax(String clientBoard){
		this.clientInput = clientBoard;
		bestMoves = new ArrayList<Node>();
	}
	
	//Thread method.
	public void run() {
		//Define that algorithm is running
		isRunning = 2;

		//Get the game board.
		init_board = getBoard(clientInput);
		
		//If the game board is not size 9, then big error happened.
		if(init_board.length != 9)
		{
			System.out.println("You have entered an invalid state for tic tac toe, exiting......");
			System.exit(-1);
		}
		
		//Call the minMax algorithm
		MinMax sendIn_InitState = new MinMax(init_board);
		
		//Get the move list.
		movesList = sendIn_InitState.findMoves();
		
		printBestMoves();	
		//Define that the algorithm is done.
		isRunning = 1;
	}
	
	
	/**
	 * reads in a string from the input parameter.
	 * @return String[]
	 */
	private String[] getBoard(String clientInput)
	{
			String puzzle;
			String[] puzzleParsed;
			String delim = "[ ]+";
			
			//Gets the string from the input parameter.
			puzzle = clientInput;					
			puzzleParsed = puzzle.split(delim);			
			return puzzleParsed;
	}
	
	/**
	 * goes through a node list and prints out the moves with the best result for player X
	 * checks the min/max function of each state and only recomends a path that leads to a win or tie
	 */
	private void printBestMoves()
	{
		System.out.print("\n\nThe moves list is: < ");
		
		for(int x = 0; x < movesList.size(); x++)
		{
			Node temp = movesList.get(x);
			
			if(temp.getMinMax() == 10 || temp.getMinMax() == 0)
			{
				bestMoves.add(temp);
				System.out.print(temp.getMovedTo() + " ");
			}
		}
		
		System.out.print(">");
	}
}
