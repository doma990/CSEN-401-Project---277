package game.controller;

import java.io.IOException;
import java.util.ArrayList;

import game.engine.Board;
import game.engine.Game;
import game.engine.Role;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import game.view.GameView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameController extends Application {

	private Game game;
	private GameView gameView;
	
	public void start(Stage primaryStage) throws Exception {
		gameView = new GameView();
		BorderPane root = gameView.placeUIComponents();
		
		gameView.getScarerButton().setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				try {
					game = new Game(Role.SCARER);
					startTheGame(primaryStage);
				} catch (IOException e) {
					// TODO : Display an alert "Could not initialize the game" and button "Try again"
					displayAlert("Could Not Initialize the Game", "Error happened while starting, try again.", "Try Again");
				}
			}
			
		});
		
		gameView.getLaugherButton().setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				try {
					game = new Game(Role.LAUGHER);
					startTheGame(primaryStage);
				} catch (IOException e) {
					// TODO : Display an alert "Could not initialize the game" and button "Try again"
					displayAlert("Could Not Initialize the Game", "Error happened while starting, try again.", "Try Again");
				}
			}
			
		});

		gameView.getInstructionsButton().setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				
//				TODO : Show a pop up window with the intructions of the game
				showInstructions();
			}
			
		});
		
		Scene scene = new Scene(root, 1000, 600);
		primaryStage.setTitle("DooRDasH - Start Clocking In");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void startTheGame(Stage primaryStage) {
		
		BorderPane gameWindow = gameView.loadTheBoard(game.getBoard().getBoardCells(), game.getPlayer().getName(), game.getOpponent().getName());
		
//		Generating left and right panels
		generateMonstersCards(gameWindow);
		
		gameWindow.setBottom(gameView.generateActionBar());
		
		gameView.getRollDiceButton().setOnAction(new EventHandler<ActionEvent>(){
			
			public void handle(ActionEvent event) {
//				TODO : Roll the dice
				try {			
					Monster current = game.getCurrent();
					int oldPosition = current.getPosition();
//					If the monster is frozen turn will be skipped
					if (current.isFrozen()) {
						displayAlert("Ahh", "Your turn was skipped as you were frozen.", "OK");
						game.playTurn();						
					} else {						
						game.playTurn();
						
						int dice = game.getLastDiceRoll();
						gameView.getActionBar().getChildren().remove(2);
						gameView.getActionBar().getChildren().addAll(new Label("Dice: " + dice));
						
//						Starting updates
//						#1 Updating the monster's cell on the board after rolling the dice
						updateViewAfterDiceRoll(current, oldPosition, current.getPosition(), gameWindow);
						
//						#2 Updating the header (which player's turn it is)
						updateHeader((Label) gameWindow.getTop());
						
//						#3 Regenerating the monsters cards on the left and right panels
						generateMonstersCards(gameWindow);
						
//						#4 Deactivating the door
						if (current.getPosition() % 2 == 1)
							deactivateDoorCell(current.getPosition(), gameWindow);
					}
				} catch (InvalidMoveException e) {
					displayAlert("Invalid Move", "Unable to move to this position, try again.", "Try Again");
				}
				
			}
			
		});
		
		gameView.getUsePowerupButton().setOnAction(new EventHandler<ActionEvent>(){
			
			public void handle(ActionEvent event) {
//				TODO : Use the powerup
				try {
					game.usePowerup();
					generateMonstersCards(gameWindow);
				} catch (OutOfEnergyException e) {
					displayAlert("Out of Energy", "Cannot Execute Powerup Effect Due to Low Canister Energy", "OK");
				}
			}
			
		});
		
		Scene gameScene = new Scene(gameWindow, 1000, 600);
		primaryStage.setScene(gameScene);
	}
	
	private void generateMonstersCards(BorderPane gameWindow) {
		
		VBox playerCard = gameView.buildMonsterCard(game.getPlayer(), true);
		VBox opponentCard = gameView.buildMonsterCard(game.getOpponent(), true);
		
		ArrayList<VBox> stationedMonstersCards = new ArrayList<VBox>();
		for (Monster monster : Board.getStationedMonsters())
			stationedMonstersCards.add(gameView.buildMonsterCard(monster, false));
		
		VBox leftPanel = gameView.constructPanel("PLAYER", playerCard, stationedMonstersCards.subList(0, 3));
		VBox rightPanel = gameView.constructPanel("OPPONENT", opponentCard, stationedMonstersCards.subList(3, 6));
		
		gameWindow.setLeft(leftPanel);
		gameWindow.setRight(rightPanel);

	}
	
	private void updateViewAfterDiceRoll(Monster current, int oldPosition, int newPosition, BorderPane gameWindow) {
		gameView.updateBoardAfterRoll(current, oldPosition, newPosition, gameWindow);
	}
	
	private void updateHeader(Label label) {
		StringBuilder result = new StringBuilder("Player ");
		String currentName = game.getCurrent().getName();
		if (game.getPlayer().getName().equals(currentName)) {
			result.append("1 Turn: ").append(currentName);
		} else {			
			result.append("2 Turn: ").append(currentName);
		}
		label.setText(result.toString());
	}
	
	private void deactivateDoorCell(int doorPosition, BorderPane gameWindow) {
		gameView.deactivateDoorOnTheBoard(gameWindow, doorPosition);
	}
	
	private void showInstructions() {
		VBox instructionsPopup = gameView.generateInstructionsPopup();
		
		Scene popupScene = new Scene(instructionsPopup);
		Stage popupStage = new Stage();
		popupStage.setScene(popupScene);
		
		popupStage.sizeToScene();
		popupStage.setResizable(false);
	    popupStage.setTitle("How to Play");
	    popupStage.showAndWait();
	}
	
    private void displayAlert(String title, String message, String closeBtnMsg) {
        Stage alertStage = new Stage();
        alertStage.setTitle(title);

        Label label = new Label(message);
        Button closeButton = new Button(closeBtnMsg);
        closeButton.setOnAction(event -> alertStage.close());

        BorderPane pane = new BorderPane();
        pane.setTop(label);
        pane.setCenter(closeButton);

        Scene scene = new Scene(pane, 500, 100);
        alertStage.setScene(scene);
        alertStage.show();
    }
		
	public static void main(String[] args) {
		launch(args);
	}

}
