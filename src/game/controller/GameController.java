package game.controller;

import java.io.IOException;
import java.util.ArrayList;

import game.engine.Board;
import game.engine.Game;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class GameController extends Application {

	private Game game;
	private GameView gameView;
	
	public void start(Stage primaryStage) throws Exception {
		
		gameView = new GameView();
		
		StackPane root = gameView.placeUIComponents();
		
		String audioPath = getClass().getResource("/resources/audio/monsters-inc-theme.mp3").toExternalForm();
		Media media = new Media(audioPath);
	    MediaPlayer startAudio = new MediaPlayer(media);
	    
	     startAudio.setCycleCount(MediaPlayer.INDEFINITE); 
	     startAudio.play();
	     
		gameView.getScarerButton().setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				try {
					startAudio.stop();
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
					startAudio.stop();
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
		
		final boolean[] isMuted = {false};

	    gameView.getMuteButton().setOnAction(e -> {
	    	
	        isMuted[0] = !isMuted[0];
	        
	        startAudio.setMute(isMuted[0]); 
	        
	        if (isMuted[0]) {
	        	
	            gameView.changeMuteButtonIcon("/resources/unmute-icon.png");
	            
	        } else {
	        	
	            gameView.changeMuteButtonIcon("/resources/mute-icon.png");
	            
	        }
	        
	    });
	    
		Scene scene = new Scene(root, 1000, 600);
		scene.setCursor(gameView.getImageCursor());
		primaryStage.setTitle("DooRDasH - Start Clocking In");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void startTheGame(Stage primaryStage) {
		
		BorderPane gameWindow = gameView.loadTheBoard(game.getBoard().getBoardCells(), game.getPlayer().getName(), game.getOpponent().getName());
		
//		Generating left and right panels
		generateMonstersCards(gameWindow);
		
//		Generating bottom panel
		gameWindow.setBottom(gameView.generateBottomSection());
		
		gameView.getRollDiceButton().setOnAction(new EventHandler<ActionEvent>(){
			
			public void handle(ActionEvent event) {
				
				Monster current = game.getCurrent();
				Monster notCurrent = game.getNotCurrent();
				String name = current.getName();
				
				int oldPos = current.getPosition();
				int oldPosNotCurrent = notCurrent.getPosition();
				int oldEnergy = current.getEnergy();
				Role oldRole = current.getRole();
				
				try {			
				    
					if (current.isFrozen()) {
						gameView.logAction(name + " is Frozen! Turn Skipped.");
						game.playTurn();						
						displayAlert("Ahh", "Your turn was skipped as you were frozen.", "OK");
						updateHeader((Label) gameWindow.getTop());
						generateMonstersCards(gameWindow);
						return;
					}
						
					game.playTurn();

			        int dice = game.getLastDiceRoll();
			        
			        int newPos = current.getPosition();
			        
			        int[] newPosRowAndCol = gameView.indexToRowCol(newPos);
			        Cell newCell = game.getBoard().getBoardCells()[newPosRowAndCol[0]][newPosRowAndCol[1]];
			        if (newCell instanceof CardCell)
			        	drawCardFromBoard(current, notCurrent);
			        
			        newPos = current.getPosition();
			        int newPosNotCurrent = notCurrent.getPosition();
			        int newEnergy = current.getEnergy();
			        Role newRole = current.getRole();
			        
			        gameView.logAction(name + " rolled a " + dice + ".");
			        
//				        Transport Cell
			        int posBeforeTransportInCaseATransport = oldPos + dice;
			        int[] rowColBeforeTransportInCaseATransport = gameView.indexToRowCol(posBeforeTransportInCaseATransport);
			        Cell cellBeforeTransportInCaseATransport = game.getBoard().getBoardCells()[rowColBeforeTransportInCaseATransport[0]][rowColBeforeTransportInCaseATransport[1]];
			        if (cellBeforeTransportInCaseATransport instanceof ContaminationSock || cellBeforeTransportInCaseATransport instanceof ConveyorBelt) {
			        	gameView.logAction(name + " was transported to cell " + newPos + "!");
			        }
			        
			        gameView.getDiceLabel().setText("Dice: " + dice);

			        gameView.logAction(name + " landed on a " + getCellName(newPos) + ".");
			        
//				        Energy Changes
			        if (newEnergy > oldEnergy) {
			            gameView.logAction(name + " gained " + (newEnergy - oldEnergy) + " energy.");
			        } else if (newEnergy < oldEnergy) {
			            gameView.logAction(name + " lost " + (oldEnergy - newEnergy) + " energy.");
			        }

//				        Role Swap
			        if (oldRole != newRole) {
			            gameView.logAction(name + "'s role was swapped to " + newRole + "!");
			        }

			        // updating the entire gamewindow
					updateViewAfterDiceRoll(current.getName(), oldPos, newPos);
					
					if (oldPosNotCurrent != newPosNotCurrent)
						updateViewAfterDiceRoll(notCurrent.getName(), oldPosNotCurrent, newPosNotCurrent);
					
					updateHeader((Label) gameWindow.getTop());
					generateMonstersCards(gameWindow);
					if (newPos % 2 == 1)
						deactivateDoorCell(newPos);
					
					if (newPosNotCurrent % 2 == 1)
						deactivateDoorCell(newPosNotCurrent);
					
					if (game.getWinner() != null) {
						declareWinner(primaryStage);
					}		
				}
				 catch (InvalidMoveException e) {
					gameView.logAction(name + " couldn't move: " + e.getMessage());
					displayAlert("Invalid Move", "Unable to move to this position, try again.", "Try Again");
				}
				
			}
			
		});
		
		gameView.getUsePowerupButton().setOnAction(new EventHandler<ActionEvent>(){
			
			public void handle(ActionEvent event) {
				
				try {
					game.usePowerup();
					gameView.logAction(game.getCurrent().getName() + " just used their Power Up!");
					generateMonstersCards(gameWindow);
					
				} catch (OutOfEnergyException e) {
					
					gameView.logAction(game.getCurrent().getName() + " tried to use a Power Up but didn't have enough energy.");
//					Might be unnecessary, check later
					displayAlert("Out of Energy", "Cannot Execute Powerup Effect Due to Low Canister Energy", "OK");
					
				}
			}
			
		});
		
		Scene gameScene = new Scene(gameWindow, 1000, 600);
		
		gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
		    Monster current = game.getCurrent();
		    int oldPos = current.getPosition();
		    boolean cheatUsed = false;

		    // --- CHEAT 1: Teleport to Cell 99 ---
		    if (event.getCode() == KeyCode.W) {
		        current.setPosition(99); 
		        cheatUsed = true;
		    } 
		    // --- CHEAT 2: Add 500 Energy ---
		    else if (event.getCode() == KeyCode.E) {
		        current.alterEnergy(500);; 
		        cheatUsed = true;
		    }

		    // --- IF A CHEAT WAS USED, UPDATE THE GAME ---
		    if (cheatUsed) {
		    	
		    	updateViewAfterDiceRoll(current.getName(), oldPos, current.getPosition());
		        
		    	generateMonstersCards(gameWindow);

		        Monster winner = game.getWinner();
		        if (winner != null) {
		        	declareWinner(primaryStage);
		        }
		        
		        // Consume the event so nothing else tries to process the key press
		        event.consume(); 
		    }
		});
		
		
		primaryStage.setScene(gameScene);
	}
	
	private void declareWinner(Stage primaryStage) {
		Monster winner = game.getWinner();
		Monster loser = (game.getPlayer().getName().equals(winner.getName())) ? game.getOpponent() : game.getPlayer();
		
//		Designing new layout and scene
		VBox root = gameView.createVictoryScreen(winner, loser);
		
		gameView.getBackButton().setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				
				Scene scene = new Scene(gameView.getMainMenuLayout(), 1000, 600);
				primaryStage.setScene(scene);
				primaryStage.setTitle("DooRDasH - Start Clocking In");
				primaryStage.show();
				
			}
			
		});
		
//		New Scene
		Scene victoryScene = new Scene(root, 1000, 600);
		primaryStage.setScene(victoryScene);
		primaryStage.setTitle("Congrats!!");
		primaryStage.show();
		
	}
	
	private void drawCardFromBoard(Monster current, Monster notCurrent) {
		
		Card card = Board.drawCard();
		card.performAction(current, notCurrent);
		gameView.animateCardPopup(card);
		gameView.updateCardVBox(card);
		
	}
	
	private String getCellName(int pos) {
		String result = "";
		Cell[][] boardCells = game.getBoard().getBoardCells();
		int[] posRowAndCol = gameView.indexToRowCol(pos);
		Cell cell = boardCells[posRowAndCol[0]][posRowAndCol[1]];
		if (cell instanceof CardCell)
			result = "CARD CELL";
		else if (cell instanceof ConveyorBelt)
			result = "CONVEYOR BELT";
		else if (cell instanceof ContaminationSock)
			result = "CONTAMINATION SOCK";
		else if (cell instanceof DoorCell) {
			if (((DoorCell) cell).getRole() == Role.SCARER)
				result = "SCARER DOOR";
			else
				result = "LAUGHER DOOR";
		}
		else if (cell instanceof MonsterCell) {
			String monsterName = cell.getName().toUpperCase();
			result = "MONSTER CELL : " + monsterName;
		}
		else
			result = "NORMAL CELL";
		return result;
		
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
	
	private void updateViewAfterDiceRoll(String name, int oldPosition, int newPosition) {
		gameView.updateBoardAfterRoll(name, oldPosition, newPosition);
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
	
	private void deactivateDoorCell(int doorPosition) {
		gameView.deactivateDoorOnTheBoard(doorPosition);
	}
	
	private void showInstructions() {
		VBox instructionsPopup = gameView.generateInstructionsPopup();
		
		Scene popupScene = new Scene(instructionsPopup);
		popupScene.setCursor(gameView.getImageCursor());
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
