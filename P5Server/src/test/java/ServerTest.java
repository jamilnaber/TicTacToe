import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

//Class to test the MinMax algorithm
class ServerTest {
	//Declare a FindNextMove object.
	FindNextMove minMaxAlogorthm;
	
	//Before each test is ran, define the findNextMove object.
	@BeforeEach
	void init(){
		//Defining the fineNextMove object.
		minMaxAlogorthm = new FindNextMove();
	}//End of init() method.

	/*-------------------Start of Testing Methods----------------------*/
	/*
		Each test declares a unique game board, and validates if the 
		Min Max Algorithm returns the expected correct move.		   */

	@Test
	void testMinMaxAlogorthm1(){
		minMaxAlogorthm.clientBoard = "b b b "
									+ "b b b " 
									+ "b b b";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(1, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm2(){
		minMaxAlogorthm.clientBoard = "O b b "
									+ "b X b " 
									+ "b b b";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(2, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm3(){
		minMaxAlogorthm.clientBoard = "O O X "
									+ "b X b " 
									+ "b b b";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(4, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm4(){
		minMaxAlogorthm.clientBoard = "O O X "
									+ "O X b " 
									+ "b b X";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(6, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm5(){
		minMaxAlogorthm.clientBoard = "X X b "
									+ "O b b " 
									+ "O b b";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(3, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm6(){
		minMaxAlogorthm.clientBoard = "X X O "
									+ "O b b " 
									+ "O b b";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(5, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm7(){
		minMaxAlogorthm.clientBoard = "X O O "
									+ "O X X " 
									+ "O X b";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(9, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm8(){
		minMaxAlogorthm.clientBoard = "X b b "
									+ "b b b " 
									+ "b b O";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(3, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm9(){
		minMaxAlogorthm.clientBoard = "X O X "
									+ "b b b " 
									+ "b b O";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(4, minMaxAlogorthm.serverMove, "wrong move!");
	}

	@Test
	void testMinMaxAlogorthm10(){
		minMaxAlogorthm.clientBoard = "X O X "
									+ "X b b " 
									+ "O b O";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(8, minMaxAlogorthm.serverMove, "wrong move!");
	}


	@Test
	void testMinMaxAlogorthm11(){
		minMaxAlogorthm.clientBoard = "X O X "
									+ "X b b " 
									+ "O X O";
		minMaxAlogorthm.startAI();
		System.out.println(minMaxAlogorthm.serverMove);
		assertEquals(5, minMaxAlogorthm.serverMove, "wrong move!");
	}

	/*-------------------End of Testing Methods----------------------*/
}//End of ServerTest class.