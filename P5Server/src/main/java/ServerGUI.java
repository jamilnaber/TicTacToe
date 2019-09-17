import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType; 
import javafx.scene.control.ListView;

//Class for the Server front end GUI.
public class ServerGUI extends Application{

	//Declaring global class variables.
	HashMap<String, Scene> sceneMap;
	Text directionsText;
	String portNumberString; 
	int portNumberInteger;
	EventHandler<ActionEvent> getPortNumber;
	TextField enterPortNumber;
	Alert alert = new Alert(AlertType.ERROR);
	ListView listView = new ListView();
	Server server;

	//Method main().
	public static void main(String[] args) {
		launch(args);
	}//End of main method.

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Define the the scene hash map for switching scenes
		sceneMap = new HashMap<String,Scene>();
		//Set the window title
		primaryStage.setTitle("Tic Tac Toe Server");
		
		//Set the window size.
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.setResizable(false);
		
		//Method for the even handler for this stage. 
		eventHandlerArea(primaryStage);   
		
		//Add the server home screen to the scene hash map.
		sceneMap.put("homeScreen", serverHomeScene());
		
		//Launch ans show the server home screen.
		primaryStage.setScene(sceneMap.get("homeScreen"));
		alert.initOwner(primaryStage);
		primaryStage.show();
	}

	//Server home screen GUI
	private Scene serverHomeScene() {
		//GUI element declarations
		VBox vbox1,vbox2;
		HBox hbox1, hbox2, hbox3;
		BorderPane border;
		Button startServerButton;
		Image serverImage;
		ImageView serverView;
		Scene scene;

		//Text box set up for server instructions.
		directionsText = new Text("Enter the Server \nPort Number:");
		directionsText.setStyle("-fx-font-size: 18;" + "-fx-fill: BLACK;"+ "-fx-font-weight: BOLD;");

		//Server icon image setup.
		serverImage = new Image("server.png");
		serverView = new ImageView(serverImage);
		serverView.setFitHeight(75);
		serverView.setPreserveRatio(true);

		//Button set up to start the connection.
		startServerButton = new Button("Start server");  
		startServerButton.setStyle("-fx-font-size: 22;" + "-fx-background-color: GREEN;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: BOLD;");
		startServerButton.setMinSize(250, 50);
		startServerButton.setOnAction(getPortNumber); 
		
		//Text field set up to enter the port numeber for the server.
		enterPortNumber = new TextField();
		enterPortNumber.setFocusTraversable(false);
		enterPortNumber.setPromptText("Ex: 5555");
		enterPortNumber.setMinWidth(250);
		enterPortNumber.setPrefWidth(250);
   		enterPortNumber.setMaxWidth(250);

		//Boxes set up and GUI organization layout.
		vbox1 = new VBox(directionsText);
		vbox1.setPadding(new Insets(15,0,0,15));
		hbox2 = new HBox(serverView,vbox1);
		hbox2.setPadding(new Insets(0,0,10,0));
		vbox2 = new VBox(hbox2, enterPortNumber, startServerButton, getExitButton());
		vbox2.setSpacing(5);
		vbox2.setPadding(new Insets(140,10,0,0));
		hbox3 = new HBox(getSceneLogo(), vbox2 );

		//Boarder pane set up, space and organiztion.
		border = new BorderPane();
		border.setPadding(new Insets(0,0,20,20));
		border.setTop(getSceneTitle());
		border.setCenter(hbox3);
		border.setStyle("-fx-background-color: WHITE");
		
		//Scene creation.
		scene = new Scene(border);  
		return scene;    
	}//End of serverHomeScene method.


	//Server state of game GUI
	private Scene stateOfGame(){
		//GUI elements declaration.
		BorderPane border;
		VBox vbox;
		Scene scene;


		//Setting the list view size.
		listView.setPrefWidth(100);
		listView.setPrefHeight(300);
		
		//Setting up the VBox with the list view and exit button inside.
		vbox = new VBox(listView, getExitButton());
		vbox.setSpacing(5);
		
		//Defining the border pane.
		border = new BorderPane();
		border.setPadding(new Insets(0,20,20,20));
		
		//Setting positions of GUI elements inside the border pane
		border.setTop(getSceneTitle());
		border.setLeft(getSceneLogo());
		border.setRight(vbox);

		//Set background of winodw as white.
		border.setStyle("-fx-background-color: WHITE");
		
		//Defining the new scene with the border pane.
		scene = new Scene(border);  

		//Returning the new created scene.
		return scene;  
	}//End of stateOfGame method.

	//Method for the event handlers for the GUI elements.
	private void eventHandlerArea(Stage primaryStage) {
		//Event handler for getting the port number.
		getPortNumber = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				//Getting the text from the port text field.
				portNumberString = enterPortNumber.getText(); 
				
				//Checking port nmber validtiy.
				if (portNumberString != null && portNumberString.matches("[0-9]+") && portNumberString.length() > 0 && portNumberString.length() < 6) { 
					//If port a valid string, then convert it to an integer.
					portNumberInteger = Integer.parseInt(portNumberString);

					//Try-catch to try to connect to the server.
					try{
						//Defining the sever with local port number.
						server = new Server(data->{Platform.runLater( ()->{listView.getItems().add(data);});}, portNumberInteger);

						//Waiting if server creations suceeds or fails.
						while(server.isConnected == 0){
							Thread.sleep(10);
						}

						//If the server is created, then change the GUI to the stateOfGame scene.
						if(server.isConnected == 1){
							sceneMap.put("stateOfGame", stateOfGame());
							primaryStage.setScene(sceneMap.get("stateOfGame"));
						}
						//If the server fails to be created, then alert the user of the failure.
						else if (server.isConnected == -1){
							alert.setTitle("Connection Failed!");
							alert.setHeaderText("Failed to connect to Server Port.");
							alert.setContentText("Validate an unused Port and try again!");
							alert.showAndWait();
							enterPortNumber.clear();
						}
					}
					catch(Exception e){}
				}
				//If the port number string is invalid, then alert the user of the failure.
				else{
					alert.setTitle("Connection Failed!");
					alert.setHeaderText("Error: Only numerical values are valid Port Numbers.");
					alert.setContentText("Click OK and enter a valid Port Number.");
					alert.showAndWait();
					enterPortNumber.clear();
				}
			}
		};
	}//End of eventHandlerArea method.
	
	//Method to return a the scene title. 
	ImageView getSceneTitle(){
		Image pageTitle = new Image("title.png");
		ImageView titleView = new ImageView(pageTitle);
		titleView.setFitWidth(750);
		titleView.setPreserveRatio(true);
		return titleView;
	}//End of getSceneTitle method

	//Method to return the scene logo.
	ImageView getSceneLogo(){
		Image logoImage = new Image("logo.png");
		ImageView logoView = new ImageView(logoImage);
		logoView.setFitHeight(435);
		logoView.setPreserveRatio(true);
		return logoView;
	}//End of getSceneLogo method.

	//Method to return the exit button. 
	Button getExitButton(){
		Button exit = new Button("Exit");    
		exit.setOnAction((event)->{System.exit(0);});
		exit.setStyle("-fx-font-size: 22;" + "-fx-background-color: RED;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: BOLD;" + "-fx-pref-width: 200px;");
		exit.setMinSize(250, 50);
		return exit;
	}//End of getExitButton method.
}//End of ServerGUI class.