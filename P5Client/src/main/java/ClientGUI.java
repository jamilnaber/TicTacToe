import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//Class for the Client front end GUI.
public class ClientGUI extends Application{
	//Defining Client class global variables.
	Text numberOfWins;
	HashMap<String, Scene> sceneMap;
	TextField userPortNumber, userIpAddress;
	Button r1c1, r1c2, r1c3, r2c1, r2c2, r2c3, r3c1, r3c2, r3c3, playAgainBtn, quitBtn;
	ListView<String> topThree;
	EventHandler<ActionEvent> boardClick, playGame, playAgain, quit;
	Alert alert = new Alert(AlertType.ERROR);
	Alert alert3 = new Alert(AlertType.INFORMATION);
	Alert alert2 = new Alert(AlertType.INFORMATION);
	Client myClient;
	ArrayList<Button> allButtons;
	List<Button> namesList = Arrays.asList(r1c1, r1c2, r1c3, r2c1, r2c2, r2c3, r3c1, r3c2, r3c3);
	int intNumberOfWins = 0;
	
	//Method main.
 	public static void main(String[] args) {
		launch(args);
	}//End of main method.

	//Method start to begin the GUI and stage.
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Define the scene hash map.
		sceneMap = new HashMap<String, Scene>();

		//Define the buttons.
		allButtons = new ArrayList<Button>();
		allButtons.addAll(namesList);
		
		//Set up the event handler for this stage.
		eventHandlerSetup(primaryStage);

		//Put the connection screen on the scene hash map,
		sceneMap.put("connectionScreen", genConnectionScreen());
		
		//Set the title of the GUI window.
		primaryStage.setTitle("TicTacToe Client");
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.setResizable(false);
		//Set the scene to the connection screen and show. 
		primaryStage.setScene(sceneMap.get("connectionScreen"));
		alert.initOwner(primaryStage);
		alert3.initOwner(primaryStage);
		primaryStage.show();
	}//End of method start().
	

	//Method for the connection GUI.
	private Scene genConnectionScreen(){
		//Declaring the GUI elements for this scene.
		BorderPane basePane;
		HBox serverAndDirHBox, logoHBox;
		VBox rightSideVBox;
		ImageView logo, server;
		Text directionsText;
		Button startServerButton;
		
		//Setting up the textfield for the server port number entry.
		userPortNumber = new TextField();
		userPortNumber.setFocusTraversable(false);
		userPortNumber.setPromptText("Port Number. Ex: 5555");
		userPortNumber.setPrefWidth(50);
		
		//Setting up the textfield for the server IP address.
		userIpAddress = new TextField();
		userIpAddress.setFocusTraversable(false);
		userIpAddress.setPromptText("IP Address. Ex: 127.0.0.1");
		userIpAddress.setPrefWidth(50);
		
		//Setting up the text box for the connection instructions.
		directionsText = new Text("Enter the Server's IP\nAddress and Port Number\nto start playing");
		directionsText.setStyle("-fx-font-size: 15;" + "-fx-fill: BLACK");
		
		//Setting up the logo image.
		logo = imageSet("logo.png");
		logo.setFitHeight(400);
		logo.setPreserveRatio(true);
		
		//Setting up the server imgage icon.
		server = imageSet("server.png");
		server.setFitHeight(75);
		server.setPreserveRatio(true);
		

		//Setting up the server start button.
		startServerButton = new Button("Start Game");  
		startServerButton.setStyle("-fx-font-size: 22;" + "-fx-background-color: GREEN;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: BOLD;");
		startServerButton.setPrefSize(275, 50);     
		startServerButton.setOnAction(playGame); 
		
		//Hbox for the server server icon and instructions text box.
		serverAndDirHBox = new HBox(10, server, directionsText);
		serverAndDirHBox.setAlignment(Pos.BOTTOM_LEFT);
		
		//Setting up the right side of the pane. 
		rightSideVBox = new VBox(serverAndDirHBox, userIpAddress, userPortNumber, startServerButton);
		rightSideVBox.setPadding(new Insets(100,20,0,0));
		logoHBox = new HBox(logo);
		logoHBox.setPadding(new Insets(0,0,0,0));
		
		//Defining and setting up the border pane and where elemetns are aligned in the pane.
		basePane = new BorderPane();
		basePane.setPrefSize(800, 600);
		basePane.setCenter(logoHBox);
		basePane.setRight(rightSideVBox);
		basePane.setTop(getSceneTitle());
		
		//Defining the scene with the border pane and returing the scene.
		Scene scene = new Scene(basePane); 
		return scene; 
	}//End of genConnectionScreen() method.
	

	//Method for the Tic Tac Toe game GUI.
	private Scene genGameScreen() {
		//Declaring the GUI elements for this scene.
		BorderPane basePane;
		HBox TopOpitionButtons, row1HBox, row2HBox, row3HBox;
		VBox NumberOfWinVBox, ListViewVBox, rowHolder;
		
		//Setting up the play again button.
		playAgainBtn = new Button("Play Again");
		playAgainBtn.setMinSize(100, 25);
		playAgainBtn.setMaxSize(100, 25);
		playAgainBtn.setDisable(true);
		playAgainBtn.setOnAction(playAgain);
		playAgainBtn.setStyle("-fx-background-color: Green;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: BOLD;");
		
		//Setting up the Quit button.
		quitBtn = new Button("Quit");
		quitBtn.setMinSize(100, 25);
		quitBtn.setMaxSize(100, 25);
		quitBtn.setOnAction(quit);
		quitBtn.setStyle("-fx-background-color: RED;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: BOLD;");
		
		//Setting up the list view.
		topThree = new ListView<String>();
		topThree.setPrefWidth(230);

		//Setting up the text box for the number of player wins.
		numberOfWins = new Text("Player Number of Wins: " + intNumberOfWins);
		numberOfWins.setStyle("-fx-font-size: 15;" + "-fx-fill: GOLD;" + "-fx-font-weight: BOLD;");
		
		//Calling method to create the tic tac tow row buttons and placing into hboxes.
		createRowButtons();
		row1HBox = rowBuilder(allButtons.get(0), allButtons.get(1), allButtons.get(2));
		row2HBox = rowBuilder(allButtons.get(3), allButtons.get(4), allButtons.get(5));
		row3HBox = rowBuilder(allButtons.get(6), allButtons.get(7), allButtons.get(8));
		rowHolder = new VBox(25,row1HBox, row2HBox, row3HBox);
		rowHolder.setPadding(new Insets(87,0,70,40));
		
		//Setting the quit and play again button in hboxes.
		TopOpitionButtons = new HBox(600,playAgainBtn, quitBtn);
		TopOpitionButtons.setPadding(new Insets(0,0,30,0));
		
		//Putting the listView in a Vbox
		ListViewVBox = new VBox(topThree);
		
		//Putting the number of wins text box into a vbox.
		NumberOfWinVBox = new VBox(numberOfWins);
		NumberOfWinVBox.setPrefWidth(200);
		NumberOfWinVBox.setPadding(new Insets(0,0,0,0));
		
		//Defining the border pane and positioning elements in the pane.
		basePane = new BorderPane();
		basePane.setMinSize(800, 600);
		basePane.setMaxSize(800, 600);
		basePane.setCenter(rowHolder);
		basePane.setTop(TopOpitionButtons);
		basePane.setLeft(ListViewVBox);
		basePane.setRight(NumberOfWinVBox);
		basePane.setStyle("-fx-background-image: url(\"/board.png\");-fx-background-size: 800, 600;-fx-background-repeat: no-repeat;");
		
		//Creating and returning the scene with the border pane.
		Scene scene = new Scene(basePane);   
		return scene; 
	}//End of genGameScreen() method.
	

	//Method to create the row of buttons. 
	private void createRowButtons() {
		for(int i = 0; i < allButtons.size(); i++) {
			allButtons.set(i, genButton());
		}
	}//End of createRowButtons() method.
	

	//Method to generate game buttons.
	private Button genButton() {
		Button temp = new Button();
		temp.setMinSize(78,87);
		temp.setMaxSize(78,87);
		temp.setId("b");
		temp.setText("");
		temp.setOnAction(boardClick);
		temp.setStyle("-fx-font-size: 34;" + "-fx-background-color: WHITE;" + "-fx-text-fill: RED;" + "-fx-font-weight: BOLD;");
		return temp;
	}//End of genButton() method.
	

	//Method to buid a row of game buttons.
	private HBox rowBuilder(Button one, Button two, Button three) {
		HBox temp = new HBox(20, one, two, three);
		temp.setPadding(new Insets(0,0,0,0));
		return temp;
	}//End of rowBuilder() method.
	

	//Method to input a file string and output a ImageView object.
	private ImageView imageSet(String file) {
		ImageView pictureView = new ImageView(new Image(file)); //create ImageView of picture
		return pictureView;                                     
	}//End of imageSet() method.


	//Method to get the Scene title.
	public ImageView getSceneTitle(){
		Image pageTitle = new Image("title.png");
		ImageView titleView = new ImageView(pageTitle);
		titleView.setFitWidth(750);
		titleView.setPreserveRatio(true);
		return titleView;
	}//End of getSceneTitle() method.
//-------------------------------------------------------------------------------------------------//
//-------------------------------------End of Scene Setting methods--------------------------------//
//-------------------------------------------------------------------------------------------------//
//-------------------------------------Start Of EventHandler Defin.--------------------------------//
	private void eventHandlerSetup(Stage primaryStage) {
		//Set up handler for clicking on the game board buttons.
		boardClick = new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				String currentBoard;
				Button sourceButton;
				disableAllButtons();
				sourceButton = (Button)event.getSource();
				sourceButton.setText("O");
				sourceButton.setId("O");
				sourceButton.setDisable(true);
				sourceButton.setOpacity(1);
				currentBoard = getcurrentBoard();
				System.out.println(currentBoard);
				myClient.gameInfo.currentBoard = currentBoard;
				myClient.gameInfo.playerHasMadeMove = true;
				myClient.send(myClient.gameInfo);
				buttonReableThread();
				checkForWinner();
			}
			
		};//End of boardClick event.
		
		//Set up the connect to server button.
		playGame =  new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				String portNumber = userPortNumber.getText(); 
				String iPNumber = userIpAddress.getText();
				//Checking the validity of the port and iP address 
				if ((portNumber != null && portNumber.matches("[0-9]+") && portNumber.length() > 0) 
					&& (iPNumber != null && iPNumber.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}") && iPNumber.length() > 0))  
				{
					try {
						//Try to connect to the server. If fails, then exception is thrown. 
						myClient = new Client(iPNumber, Integer.parseInt(portNumber), data->{Platform.runLater( ()->{topThree.getItems().add(data);});});
						
						//Starts the client thread connected to the server.
						myClient.start();
						
						//wait and see if connected and thread is successful.
						while(myClient.isConnected == 0){System.out.print("");}
						if(myClient.isConnected == 1){
							throw new Exception();
						}

						//If connection to server is succesful, then chage scene to the tic tac toe game scene.
						sceneMap.put("gameScreen", genGameScreen());
						primaryStage.setScene(sceneMap.get("gameScreen"));
						System.out.println("here");
					}
					//If fails to connect to the server, the user is alerted and must try inputting a different IP and port number.
					catch(Exception E) {
						alert.setTitle("Error");
						alert.setHeaderText("Error: Unable to connect to Server!");
						alert.setContentText("Check if server is running, then try again.");
						alert.showAndWait();
						userPortNumber.clear();
						userIpAddress.clear();
					}
				}
				//If port number and IP address are invalid strings, then alert the user to try again.
				else {					
					alert.setTitle("Error");
					alert.setHeaderText("Error: Enter a valid Port Number and IP Address.");
					alert.setContentText("Click OK and enter a valid Port Number and IP Address.");
					alert.showAndWait();
					userPortNumber.clear();
					userIpAddress.clear();					
				}
			}
		};//End of playGame event handler.


		//Set up the play again button event handler.
		playAgain = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//Reset all game board to blank and enable the board buttons.
				for(int i = 0; i < allButtons.size(); i ++)
				{
					allButtons.get(i).setText("");
					allButtons.get(i).setId("b");
					checkIfToEnable(allButtons.get(i));
				}
				//Set the play again button to be disabled. 
				playAgainBtn.setDisable(true);

				//Reset the GameInfo currentBoard member to be emtpy.
				myClient.gameInfo.currentBoard = null;

				//Declare the user has not made a move yet.
				myClient.gameInfo.playerHasMadeMove = false;

				//Declare the server has not made a move yet.
				myClient.gameInfo.serversNextMove = null;

				//Declare that there is no game winenr yet.
				myClient.gameInfo.winner = null;
				topThree.getItems().add("You have selected to play again!");
			}
		};//End of playAgain event handler.
		
		//Set up the quit button event handler.
		quit = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//Close the primary stage.
				primaryStage.close();
				//Exit the application.
				Platform.exit();
				try {	myClient.out.reset();
						myClient.socketClient.close();} catch (IOException e) {}
			}
		};
	}
//-------------------------------------------------------------------------------------------------//
//-------------------------------------End Of EventHandler Defin.----------------------------------//
//-----------------------------------------Unrelated Methods---------------------------------------//

	//Method to get the current game board.
	private String getcurrentBoard(){
		String temp = new String();
		//Get the state of all the game buttons.
		temp = allButtons.get(0).getId();
		for(int i = 1; i < allButtons.size(); i++) {
			temp = temp + " " + allButtons.get(i).getId();
		}
		//Return the game board string.
		return temp;
	}//End of getCurrentBoard() method.
	

	//Method to disable all the game buttons.
	public void disableAllButtons() {
		//Loop through buttons and disable.
		for(int i = 0; i < allButtons.size(); i++) {
			allButtons.get(i).setDisable(true);
			allButtons.get(i).setOpacity(1);
		}
	}//End of disableAllButtons() method.
	

	//Method to get the server's game move and display it on the board.
	public void buttonReableThread() {
		//Start a new thread for waiting for server's move.
		new Thread(new Runnable() {
		    @Override public void run() {
		    	//While the server has not made a move, just wait.
		    	while(myClient.gameInfo.serversNextMove == null) {
		    		try{Thread.sleep(10);}catch(Exception e){}
		    	}
		    	System.out.println("Found Move Form Server, it is " + myClient.gameInfo.serversNextMove);
		    	
		    	//Now that the sever has made a move, display the move on the clients game board.
		    	Platform.runLater(() -> {
		    		if(myClient.gameInfo.serversNextMove != null)
		    		{
			    		displayServerMove();
			    		for(int i = 0; i < allButtons.size(); i++) {
			    			checkIfToEnable(allButtons.get(i));
			    		}	
		    		}
		    		myClient.gameInfo.serversNextMove = null;
		    	});
		    }
		}).start();
	}//End of buttonReableThread().
	

	//Method to display the server's move on the client's board.
	private void displayServerMove() {
		//Get hte move from the GameInfo object.
		int move = Integer.parseInt(myClient.gameInfo.serversNextMove);
		
		//Set all the X's for where the server has made its move.
		for(int i = 0; i < allButtons.size(); i++) {
			if(move == i + 1) {
				System.out.println("In " + (i+1));
				allButtons.get(i).setId("X");
				allButtons.get(i).setText("X");
			}
		}
	}//End of displayServerMove() method.
	

	//Method to check if a board square is blank.
	private void checkIfToEnable(Button b){
		if(b.getId() == "b") {
			b.setDisable(false);
		}
	}//End of checkIfToEnable() method.
	

	//Method to check if there is winner.
	public void checkForWinner(){
		//Start a new thread to wait for game winner results.
		new Thread(new Runnable() {
		    @Override public void run() {
		    	//While the winner is not declared, just wait.
		    	while(myClient.gameInfo.winner == null) {
		    		try{Thread.sleep(10);}catch(Exception e){}
		    	}
		    	
		    	//Now that the winner has been declared....

		    	//If the palyer has won...
		    	if(myClient.gameInfo.winner.equals("Player")){
		    		intNumberOfWins = intNumberOfWins + 1;
					//Send an alert to the player.
					Platform.runLater(() -> alert3.setTitle("Congratulations!"));
					Platform.runLater(() -> alert3.setHeaderText("You beat the server!"));
					Platform.runLater(() -> alert3.setContentText("If you want to play again, please press play again!"));
					Platform.runLater(() -> alert3.showAndWait());
					
					//Update the number of wins player has.
					Platform.runLater(() -> numberOfWins.setText("Player Number of Wins: " + (intNumberOfWins)));
					
					//Enable the play again button.
					Platform.runLater(() -> playAgainBtn.setDisable(false));
					
					//Disable all the other game buttons.
					Platform.runLater(() -> disableAllButtons());
					
					//Add message to the list. 
					Platform.runLater(() -> topThree.getItems().add("You have beat the server!"));
				}
				//If the Computer won...
				else if (myClient.gameInfo.winner.equals("Computer")){
					//Alert the player that they are a loser.
					Platform.runLater(() -> alert3.setTitle("Loser!"));
					Platform.runLater(() -> alert3.setHeaderText("You have lost against the server!"));
					Platform.runLater(() -> alert3.setContentText("If you want to play again, please press play again!"));
					Platform.runLater(() -> alert3.showAndWait());

					//Enable the play again button,
					Platform.runLater(() -> playAgainBtn.setDisable(false));
					
					//Disable all other game buttons.
					Platform.runLater(() -> disableAllButtons());

					//Add message to the list.
					Platform.runLater(() -> topThree.getItems().add("You have lost against the server!"));
				}

				//If the computer and client tie...
				else if (myClient.gameInfo.winner.equals("Tie")){
					//Alert the player that they have tied the computer.
					Platform.runLater(() -> alert3.setTitle("Tie!"));
					Platform.runLater(() -> alert3.setHeaderText("You have Tied against the server!"));
					Platform.runLater(() -> alert3.setContentText("If you want to play again, please press play again!"));
					Platform.runLater(() -> alert3.showAndWait());

					//Enable the play again button.
					Platform.runLater(() -> playAgainBtn.setDisable(false));

					//Disable all other game buttons.
					Platform.runLater(() -> disableAllButtons());

					//Add message to the list.
					Platform.runLater(() -> topThree.getItems().add("You have tied against the server!"));
				}
		    	//Reset the GameInfo winner to null.
		    	myClient.gameInfo.winner = null;
		    }
		}).start();
	}//End of checkForWinner() method.
}//End of ClientGUI class.