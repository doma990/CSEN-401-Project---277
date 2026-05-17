package game.view;

import java.util.List;

import game.engine.Constants;
import game.engine.Role;
import game.engine.cells.*;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Schemer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class GameView {
	
	private final int CELL_SIZE = 80;
	
	private Label gameTitle;
	private Button scarerButton;
	private Button laugherButton;
	private Button instructionsButton;
	private Button rollDiceButton;
	private Button usePowerupButton;
	private HBox actionBar;
	
	public GameView() {
		gameTitle = new Label("DooRDasH: Scare vs Laugh Touchdown");
		scarerButton = new Button("Play as SCARER");
		laugherButton = new Button("Play as LAUGHER");
		instructionsButton = new Button("Instructions");
		
		scarerButton.setStyle("-fx-font-size: 18px; -fx-base: #4a235a; -fx-text-fill: white; -fx-font-weight: bold;");
		laugherButton.setStyle("-fx-font-size: 18px; -fx-base: #f1c40f; -fx-text-fill: black; -fx-font-weight: bold;");
		instructionsButton.setStyle("-fx-font-size: 18px; -fx-base: #b3b3b3; -fx-text-fill: black; -fx-font-weight: bold;");
		
		gameTitle.setStyle(
			    "-fx-font-family: 'Impact'; " + 
			    "-fx-font-size: 60px; " + 
			    "-fx-text-fill: #9ACD32; " + // YellowGreen
			    "-fx-stroke: #2E8B57; " + // SeaGreen outline effect
			    "-fx-stroke-width: 2px;"
		);
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0, 0, 0, 0.5));
		gameTitle.setEffect(dropShadow);
		
		gameTitle.setPadding(new Insets(200,0,0,0));
	}
	
	public BorderPane placeUIComponents() {
		BorderPane root = new BorderPane();
		String imagePath = getClass().getResource("/resources/Welcome-Screen.jpeg").toExternalForm();
		Image bgImg = new Image(imagePath); 
		BackgroundImage background = new BackgroundImage(
				bgImg, 
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, 
				BackgroundPosition.CENTER,
				new BackgroundSize(100, 100, true, true, false, true)
		);
		
		root.setBackground(new Background(background));
		
//		A containter for "Play as Laugher" , "Play as scarer" , and "Instructions" buttons
		VBox allButtons = new VBox(30);
		allButtons.setAlignment(Pos.CENTER);
		
//		A container for Scarer and Laugher buttons
		HBox scarerAndLaugherButtons = new HBox(30);
		scarerAndLaugherButtons.setAlignment(Pos.CENTER);
		
		scarerAndLaugherButtons.getChildren().addAll(scarerButton, laugherButton);
		allButtons.getChildren().addAll(scarerAndLaugherButtons, instructionsButton);
		
		root.setTop(gameTitle);
		root.setCenter(allButtons);
		
		BorderPane.setAlignment(gameTitle, Pos.CENTER);

		return root;
	}
	
	public BorderPane loadTheBoard(Cell[][] boardCells, String playerName, String opponentName) {
		
//		Root Node
		BorderPane entireWindow = new BorderPane();
//		A stack which has both the board gridpane and above it the overlay pane for diagonal extensions
		StackPane mainBoardContainer = new StackPane();
		mainBoardContainer.setMaxSize(10*CELL_SIZE, 10*CELL_SIZE);
//		GridPane to hold the board's cells
		GridPane board = new GridPane();
//		board.setGridLinesVisible(true);
//		Pane to hold diagonal extensions of contamination sock and conveyor belt above the board grid
		Pane overlayPane = new Pane();
		overlayPane.setMouseTransparent(true);
		
		board.setAlignment(Pos.CENTER);
				
		ImageView playerImage = new ImageView();
		ImageView opponentImage = new ImageView();
		
		playerImage.setFitWidth(35);
		playerImage.setFitHeight(35);
		opponentImage.setFitWidth(35);
		opponentImage.setFitHeight(35);
		
		playerImage.setId(playerName);
		playerImage.setImage(new Image(getClass().getResource(returnMonsterImagePath(playerName)).toExternalForm()));
		opponentImage.setId(opponentName);
		opponentImage.setImage(new Image(getClass().getResource(returnMonsterImagePath(opponentName)).toExternalForm()));
						
		Cell currentCell;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				
				currentCell = boardCells[i][j];
				
				HBox monstersContainer = new HBox(5);
				monstersContainer.setAlignment(Pos.CENTER);
				
				StackPane cellStackPane = new StackPane();
				
				Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
				background.setArcWidth(15);
				background.setArcHeight(15);
				background.setStroke(Color.web("#2c3e50"));
				background.setStrokeWidth(2.0);
				
				ImageView cellIcon = new ImageView();
				cellIcon.setFitHeight(60);
				cellIcon.setFitWidth(60);
				
				Label doorEnergy = new Label();
				doorEnergy.setTextFill(Color.BLACK);
				
				int cellNumber = (i % 2 == 0) ? (i * 10) + j : (i * 10) + (9 - j);
				Label cellIndex = new Label(String.valueOf(cellNumber));
				
//					Card cell
				if (currentCell instanceof CardCell) {
					
					background.setFill(createTileGradient("#8E2DE2", "#4A00E0"));
					cellIcon.setImage(new Image(getClass().getResource("/resources/card.png").toExternalForm()));
					
//					Contamination Sock
				} else if (currentCell instanceof ContaminationSock) {
					
					background.setFill(createTileGradient("#ff9966", "#ff5e62"));
					Image image = new Image(getClass().getResource("/resources/transport/sock.png").toExternalForm());
					cellIcon.setImage(image);
					int effect = ((ContaminationSock) currentCell).getEffect();
					int[] endCoordinates = getEndRowAndColForTransport(effect, i, j);
					Line sock = drawDiagonalItem(i, j, endCoordinates[0], endCoordinates[1]);
					
					sock.setStroke(Color.RED);
					sock.setStrokeWidth(8);
					sock.getStrokeDashArray().addAll(10d, 5d);
					
					overlayPane.getChildren().add(sock);
					
//					Conveyor Belt
				} else if (currentCell instanceof ConveyorBelt) {
					
					background.setFill(createTileGradient("#11998e", "#38ef7d"));
					Image image = new Image(getClass().getResource("/resources/transport/belt.png").toExternalForm());
					cellIcon.setImage(image);					
					int effect = ((ConveyorBelt) currentCell).getEffect();
					int[] endCoordinates = getEndRowAndColForTransport(effect, i, j);
					Line belt = drawDiagonalItem(i, j, endCoordinates[0], endCoordinates[1]);
					
					belt.setStroke(Color.BLUE);
					belt.setStrokeWidth(8);
					belt.getStrokeDashArray().addAll(10d, 5d);
					
					overlayPane.getChildren().add(belt);
					
//					DoorCell
				} else if (currentCell instanceof DoorCell) {
					
					background.setFill(createTileGradient("#F2C94C", "#F2994A"));
					if (((DoorCell) currentCell).getRole() == Role.SCARER) {						
						cellIcon.setImage(new Image(getClass().getResource("/resources/door/scarer-door.png").toExternalForm()));					
						cellIcon.setId("scarerDoor");
					} else {						
						cellIcon.setImage(new Image(getClass().getResource("/resources/door/laugher-door.png").toExternalForm()));					
						cellIcon.setId("laugherDoor");
					}
					doorEnergy.setText("" + ((DoorCell) currentCell).getEnergy());
					cellIcon.setFitHeight(80);
					
//					Monster Cell
				} else if (currentCell instanceof MonsterCell) {
					background.setFill(createTileGradient("#00c6ff", "#0072ff"));
					
					String monsterCellName = ((MonsterCell) currentCell).getCellMonster().getName();
					cellIcon.setImage(new Image(getClass().getResource(returnMonsterImagePath(monsterCellName)).toExternalForm()));
				}
				
//				Normal Cell
				else if (currentCell instanceof Cell) {
					background.setFill(createTileGradient("#e0e0e0", "#bdc3c7"));
				}
				
				if (i == 0 && j == 0) {
					monstersContainer.getChildren().addAll(playerImage, opponentImage);					
				}
				
				cellStackPane.getChildren().addAll(background, cellIndex, cellIcon, doorEnergy, monstersContainer);
				StackPane.setAlignment(doorEnergy, Pos.BOTTOM_LEFT);					
				StackPane.setAlignment(cellIndex, Pos.TOP_RIGHT);
				board.add(cellStackPane, j, 9 - i);
			}
		}
		
		mainBoardContainer.getChildren().addAll(board, overlayPane);
		entireWindow.setCenter(mainBoardContainer);
		
		Label header = new Label("Player 1 : " + playerName);
		entireWindow.setTop(header);
		BorderPane.setAlignment(header, Pos.CENTER);
		
		return entireWindow;
	}
	
	private int[] getEndRowAndColForTransport(int effect, int i, int j) {
		int startIndex = (i % 2 == 0) ? (i * 10) + j : (i * 10) + (9 - j);
		int endIndex = startIndex + effect;		
		return indexToRowCol(endIndex);
	}
	
	private Line drawDiagonalItem(int startRow, int startCol, int endRow, int endCol) {
				
		int visualStartRow = 9 - startRow;
	    int visualEndRow = 9 - endRow;

	    double startX = (startCol * CELL_SIZE) + (CELL_SIZE / 2);
	    double startY = (visualStartRow * CELL_SIZE) + (CELL_SIZE / 2);
	    
	    double endX = (endCol * CELL_SIZE) + (CELL_SIZE / 2);
	    double endY = (visualEndRow * CELL_SIZE) + (CELL_SIZE / 2);
	    
	    Line line = new Line(startX, startY, endX, endY);
	    
	    return line;
	}
	
	public void updateBoardAfterRoll(Monster current, int oldPosition, int newPosition, BorderPane gameWindow) {
//		Calculating indices of start and end positions
		int[] startRowAndCol = indexToRowColRespectingGridPane(oldPosition);
		int[] endRowAndCol = indexToRowColRespectingGridPane(newPosition);
		
//		Board's grid
		GridPane board = ((GridPane) ((StackPane) gameWindow.getCenter()).getChildren().get(0));
		
//		Retrieving cell stack for old and new cells
		StackPane oldStack = getNodeFromGridPane(board, startRowAndCol[1], startRowAndCol[0]);
		StackPane newStack = getNodeFromGridPane(board, endRowAndCol[1], endRowAndCol[0]);
		
//		Retrieving the HBox container of the monsters for both old and new cells
		HBox oldHBox = (HBox) oldStack.getChildren().get(oldStack.getChildren().size() - 1);
		HBox newHBox = (HBox) newStack.getChildren().get(newStack.getChildren().size() - 1);
		
//		Retrieving ImageView object for the monster using its name which was set previously as an ID
		ImageView imageView = (ImageView) getNodeById(oldHBox, current.getName());
		
//		Removing monster from old cell and adding it to the new cell
		if (imageView != null) {
			oldHBox.getChildren().remove(imageView);
			newHBox.getChildren().add(imageView);
		}
	}
		
	private Node getNodeById(Pane parentNode, String id) {
	    for (Node node : parentNode.getChildren()) {
	        if (id.equals(node.getId())) {
	            return node;
	        }
	    }
	    return null;
	}

	private StackPane getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return (StackPane)node;
	        }
	    }
	    return null;
	}
	
	private int[] indexToRowCol(int index) {
		int cols = Constants.BOARD_COLS;
		
		int row = index / cols;
		int col = index % cols;
		
		if (row % 2 == 1)
			col = cols - 1 - col;
		
		return new int[]{row, col};
	}
	
	private int[] indexToRowColRespectingGridPane(int index) {
	    int cols = Constants.BOARD_COLS;

	    int row = index / cols;
	    int col = index % cols;

	    if (row % 2 == 1)
	        col = cols - 1 - col;

	    return new int[]{(9-row), col};
	}
	
	private LinearGradient createTileGradient(String topColorHex, String bottomColorHex) {
		
	    Stop[] stops = new Stop[] {
	        new Stop(0, Color.web(topColorHex)),
	        new Stop(1, Color.web(bottomColorHex))
	    };
	    
	    return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
	}
	
	private String returnMonsterImagePath (String monsterCellName) {
		String path = "";
		if (monsterCellName.equals("James P. Sullivan"))
			path = "/resources/monsters/james-p-sullivan.png";
		
		else if (monsterCellName.equals("Mike Wazowski"))
			path = "/resources/monsters/Mike-Wazowski.png";
		
		else if (monsterCellName.equals("Randall Boggs"))
			path = "/resources/monsters/randall-boggs.png";
		
		else if (monsterCellName.equals("Celia Mae"))
			path = "/resources/monsters/celia-mae.png";
		
		else if (monsterCellName.equals("Roz"))
			path = "/resources/monsters/Roz.png";
		
		else if (monsterCellName.equals("Fungus"))
			path = "/resources/monsters/Fungus.png";
		
		else if (monsterCellName.equals("Henry J. Waternoose"))
			path = "/resources/monsters/Henry J.Waternoose.png";
		
		else if (monsterCellName.equals("Yeti"))
			path = "/resources/monsters/Yeti.png";
		return path;
	}
		
	public VBox constructPanel(String mainCardLabel, VBox mainCard, List<VBox> stationedMonstersCards) {
		
		VBox panel = new VBox(15);
		panel.setPadding(new Insets(15));
		panel.setPrefWidth(250);

		VBox stationedMonsters = new VBox(10);
		
		for (VBox card : stationedMonstersCards)
		    stationedMonsters.getChildren().add(card);

		ScrollPane scroll = new ScrollPane(stationedMonsters);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
		scroll.setPrefHeight(400);

		panel.getChildren().addAll(new Label(mainCardLabel), mainCard, new Label("STATIONED MONSTERS"), scroll);
		
		return panel;
	}
	
	public VBox buildMonsterCard(Monster monster, boolean isMainPlayer) {
		
	    VBox card = new VBox(8);
	    
	    card.setPadding(new Insets(10));
	    card.setStyle(
	        "-fx-background-color: white; " +
	        "-fx-border-color: #bdc3c7; " +
	        "-fx-border-radius: 8px; " +
	        "-fx-background-radius: 8px; " +
	        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
	    );

	    Label nameLabel = new Label(monster.getName() + " (" + getMonsterType(monster) + ")");
	    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: " + (isMainPlayer ? "16px;" : "12px;"));
	    nameLabel.setWrapText(true);
	    
	    Label roleLabel = new Label("Role: " + monster.getRole());
	    
	    Label positionLabel = new Label("Current Position: " + monster.getPosition());

	    double energyPercentage = Math.min(1.0, monster.getEnergy() / 1000.0);
	    ProgressBar energyBar = new ProgressBar(energyPercentage);
	    energyBar.setPrefWidth(isMainPlayer ? 180 : 130);
	    
	    if (monster.getRole() == Role.SCARER) {
	        energyBar.setStyle("-fx-accent: #9b59b6;");
	    } else {
	        energyBar.setStyle("-fx-accent: #f1c40f;");
	    }
	    
	    Label energyText = new Label("Energy: " + monster.getEnergy());

	    HBox statusBox = new HBox(10);
	    statusBox.setAlignment(Pos.CENTER_LEFT);
	    
	    if (monster.isShielded()) {
	        Label shieldIcon = new Label("Shielded");
	        shieldIcon.setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold;");
	        statusBox.getChildren().add(shieldIcon);
	    }
	    if (monster.isFrozen()) {
	        Label freezeIcon = new Label("Frozen");
	        freezeIcon.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
	        statusBox.getChildren().add(freezeIcon);
	    }
	    if (monster.isConfused()) {
	        Label confusedIcon = new Label("Confused");
	        confusedIcon.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
	        statusBox.getChildren().add(confusedIcon);
	    }

	    card.getChildren().addAll(nameLabel, roleLabel, positionLabel, energyBar, energyText, statusBox);
	    return card;
	}
	
	private String getMonsterType(Monster monster) {
		String result = "";
		if (monster instanceof Dynamo)
			result = "Dynamo";
		else if (monster instanceof Dasher)
			result = "Dasher";
		else if (monster instanceof MultiTasker)
			result = "MultiTasker";
		else if (monster instanceof Schemer)
			result = "Schemer";
		return result;
	}
	
	public void deactivateDoorOnTheBoard(BorderPane gameWindow, int doorPosition) {
//		Extracting the board
		GridPane board = ((GridPane) ((StackPane) gameWindow.getCenter()).getChildren().get(0));
//		Calculating cell row and column from its index
		int[] doorRowAndCol = indexToRowColRespectingGridPane(doorPosition);
//		Extracting the cell stack from the board using row and column indices
		StackPane doorCellStack = getNodeFromGridPane(board, doorRowAndCol[1], doorRowAndCol[0]);
//		Extracting the cell's image view
		ImageView scarerDoor = (ImageView) getNodeById(doorCellStack, "scarerDoor");
		ImageView laugherDoor = (ImageView) getNodeById(doorCellStack, "laugherDoor");
//		Checks whether the door is deactivated first or not
		if (getNodeById(doorCellStack, "deactivated") == null) {			
			if (scarerDoor != null) {
				String path = "/resources/door/black-door-open.png";
				scarerDoor.setImage(new Image(getClass().getResource(path).toExternalForm()));
				scarerDoor.setId("deactivated");
			}
			else if (laugherDoor != null) {
				String path = "/resources/door/blue-open-door.png";
				laugherDoor.setImage(new Image(getClass().getResource(path).toExternalForm()));
				laugherDoor.setId("deactivated");
			}
		}
	}
	
	public HBox generateActionBar() {
		HBox actionBar = new HBox(20);
		actionBar.setAlignment(Pos.CENTER);
		actionBar.setPadding(new Insets(20));
		
		Button rollDiceButton = new Button("Roll Dice");
		Button powerUpButton = new Button("Use Power Up");
		
		rollDiceButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		powerUpButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2196F3; -fx-text-fill: white;");
		
		this.rollDiceButton = rollDiceButton;
		this.usePowerupButton = powerUpButton;
		this.actionBar = actionBar;
		
		actionBar.getChildren().addAll(rollDiceButton, powerUpButton, new Label());
		
		return actionBar;
	}
	
	public VBox generateInstructionsPopup() {
	    VBox popupLayout = new VBox(20);
	    
//	    20 Pixels padding all around the instruction window
	    popupLayout.setPadding(new Insets(20));
	    popupLayout.setPrefWidth(700);
	    
	    Label turnHeader = new Label("Turn Sequence");
	    turnHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
	    
	    Label howToWinHeader = new Label("How to Win");
	    howToWinHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
	    
	    Label step1 = new Label("• Power Up (Optional): Before moving, you can choose to activate your special powerup if you have at least 500 energy.");
	    Label step2 = new Label("• Roll the Dice: Roll a 6-sided dice to determine how many cells you will move forward.");
	    Label step3 = new Label("• Move: Attempt to move to your new destination. If your final destination is currently occupied by your opponent, the move is invalid, you stay in your current position, and you must roll again.");
	    Label step4 = new Label("• Cell Action: If the destination cell is not occupied, you land on it and trigger any special effects the cell might have.");
	    Label step5 = new Label("• Turn Switch: Unless the win condition is met, your turn ends and it becomes the other player's turn.");
	    Label step6 = new Label("• To win the game, you must fulfill two conditions at the exact same time: you must land exactly on cell 99 (Boo's Door), and you must have 1000 energy or more in your canister.");
	    
	    Insets indent = new Insets(0, 0, 0, 20);
	    step1.setPadding(indent);
	    step1.setWrapText(true);
	    step2.setPadding(indent);
	    step2.setWrapText(true);
	    step3.setPadding(indent);
	    step3.setWrapText(true);
	    step4.setPadding(indent);
	    step4.setWrapText(true);
	    step5.setPadding(indent);
	    step5.setWrapText(true);
	    step6.setPadding(indent);
	    step6.setWrapText(true);
	    
	    popupLayout.getChildren().addAll(turnHeader, step1, step2, step3, step4, step5, howToWinHeader, step6);
	    return popupLayout;
	    
	}
	
	public Button getScarerButton() {
		return scarerButton;
	}

	public Button getLaugherButton() {
		return laugherButton;
	}
	
	public Button getInstructionsButton() {
		return instructionsButton;
	}
	
	public Button getRollDiceButton() {
		return rollDiceButton;
	}
	
	public Button getUsePowerupButton() {
		return usePowerupButton;
	}
	
	public HBox getActionBar() {
		return actionBar;
	}
}
