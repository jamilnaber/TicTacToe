import java.io.*;
//GameInfo class to hold server move, and the game board.
public class GameInfo implements Serializable{
	//Class is a serialized class.
	public static final long serialVersionUID = 42L;
	String currentBoard;
	String serversNextMove;
	String winner;
	boolean playerHasMadeMove;
}//End of GameInfo class.