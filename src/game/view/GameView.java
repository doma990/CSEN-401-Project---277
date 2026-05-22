package game.view;

import java.util.HashMap;
import java.util.List; 
import java.util.Map;

import game.engine.Constants;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cards.EnergyStealCard;
import game.engine.cards.ShieldCard;
import game.engine.cards.StartOverCard;
import game.engine.cards.SwapperCard;
import game.engine.cells.*;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Schemer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameView {
	
	private final int CELL_SIZE = 80;
	private Map<String, Image> imageCache = new HashMap<>();
	
//	Welcome Screen Components
	private Label gameTitle;
	private Button scarerButton;
	private Button laugherButton;
	private Button instructionsButton;
	private Button muteButton;
	private ImageCursor imageCursor;
	private BorderPane mainMenuLayout;
	
//	The stack centered in the borderpane, which has the board and the diagonal transport on top of the board
	private StackPane rootContainer;
	private BorderPane gameWindow;
	private Pane cardPileLayer;
	private StackPane mainBoardContainer;
	private GridPane boardGrid;
	private Pane overlayPane;
	private Label headerLabel;
	
//	Bottom Panel Components
//	Left HBox
	private Button rollDiceButton;
	private Button usePowerupButton;
	private Label diceRollLabel;
	
//	Middle Textarea
	private TextArea actionLog;
	
//	Right VBox
	private VBox cardViewer;
	private Label cardNameLabel;
	private Label cardEffectLabel;
	
//	Back Button at Winner Layout
	private Button backButton;
	
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
			    "-fx-text-fill: #9ACD32; " +
			    "-fx-stroke: #2E8B57; " +
			    "-fx-stroke-width: 2px;"
		);
		
		ScaleTransition titleZoomIn = new ScaleTransition(Duration.millis(200), gameTitle);
		titleZoomIn.setToX(1.15);
		titleZoomIn.setToY(1.15);

		ScaleTransition titleZoomOut = new ScaleTransition(Duration.millis(200), gameTitle);
		titleZoomOut.setToX(1.0);
		titleZoomOut.setToY(1.0);

		DropShadow titleGlow = new DropShadow();
		titleGlow.setColor(Color.web("#ff4000")); // 66ff66
		titleGlow.setRadius(25);
		titleGlow.setSpread(0.5);

		gameTitle.setOnMouseEntered(e -> {
		    titleZoomIn.playFromStart();
		    gameTitle.setEffect(titleGlow);
		});

		gameTitle.setOnMouseExited(e -> {
		    titleZoomOut.playFromStart();
		    gameTitle.setEffect(null); 
		});
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0, 0, 0, 0.5));
		gameTitle.setEffect(dropShadow);
		
		gameTitle.setPadding(new Insets(200,0,0,0));
		
		Image cursorImage = getCachedImage("/resources/cursor-icon.png");
		this.imageCursor = new ImageCursor(cursorImage);
	}
	
	public StackPane placeUIComponents() {
	    StackPane rootPane = new StackPane();
	    this.mainMenuLayout = new BorderPane();
	    
	    ImageView bgImageView = new ImageView(getCachedImage("/resources/Welcome-Screen.jpeg"));
	    
	    bgImageView.fitWidthProperty().bind(rootPane.widthProperty());
	    bgImageView.fitHeightProperty().bind(rootPane.heightProperty());
	    bgImageView.setPreserveRatio(false);

	    FadeTransition bgTransition = new FadeTransition(Duration.seconds(6), bgImageView);
	    bgTransition.setFromValue(0.5);
	    bgTransition.setToValue(1.0);
	    bgTransition.setAutoReverse(false);
	    bgTransition.play();
	    
	    bgImageView.setScaleX(1.05);
	    bgImageView.setScaleY(1.05);

	    rootPane.setOnMouseMoved(event -> {
	    	
	        double windowWidth = rootPane.getWidth();
	        double windowHeight = rootPane.getHeight();
	        
	        double centerX = windowWidth / 2;
	        double centerY = windowHeight / 2;
	        
	        double mouseX = event.getX();
	        double mouseY = event.getY();
	        
	        double intensity = 0.05; 
	        
	        double shiftX = (centerX - mouseX) * intensity;
	        double shiftY = (centerY - mouseY) * intensity;
	        
	        bgImageView.setTranslateX(shiftX);
	        bgImageView.setTranslateY(shiftY);
	        
	    });
	    
	    muteButton = new Button();
	    try {
	    	
	        ImageView muteIcon = new ImageView(getCachedImage("/resources/mute-icon.png"));
	        muteIcon.setFitWidth(30);
	        muteIcon.setFitHeight(30);
	        muteButton.setGraphic(muteIcon);
	        
	    } catch (Exception e) {
	    	
	        muteButton.setText("Mute");
	        
	    }
	    
	    muteButton.setStyle("-fx-background-color: white;");
	    addHoverAnimation(muteButton);

	    StackPane.setAlignment(muteButton, Pos.TOP_RIGHT);
	    StackPane.setMargin(muteButton, new Insets(20));
	    
	    VBox allButtons = new VBox(30);
	    allButtons.setAlignment(Pos.CENTER);
	    HBox scarerAndLaugherButtons = new HBox(30);
	    scarerAndLaugherButtons.setAlignment(Pos.CENTER);
	    
	    addHoverAnimation(scarerButton);
	    addHoverAnimation(laugherButton);
	    addHoverAnimation(instructionsButton);

	    scarerAndLaugherButtons.getChildren().addAll(scarerButton, laugherButton);
	    allButtons.getChildren().addAll(scarerAndLaugherButtons, instructionsButton);
	    
	    mainMenuLayout.setTop(gameTitle);
	    mainMenuLayout.setCenter(allButtons);
	    BorderPane.setAlignment(gameTitle, Pos.CENTER);
	    
	    rootPane.getChildren().addAll(bgImageView, mainMenuLayout, muteButton);
	    
	    return rootPane;
	}

	private void addHoverAnimation(Button btn) {
	    ScaleTransition scaleIn = new ScaleTransition(Duration.millis(150), btn);
	    scaleIn.setToX(1.1);
	    scaleIn.setToY(1.1);

	    ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), btn);
	    scaleOut.setToX(1.0);
	    scaleOut.setToY(1.0);

	    btn.setOnMouseEntered(e -> {
	        scaleIn.playFromStart();
	        btn.setEffect(new DropShadow(15, Color.WHITE)); 
	    });
	    
	    btn.setOnMouseExited(e -> {
	        scaleOut.playFromStart();
	        btn.setEffect(null); 
	    });
	}
	
	public void changeMuteButtonIcon(String path) {
		
	    try {
	    	
	        ImageView muteIcon = new ImageView(getCachedImage(path));
	        muteIcon.setFitWidth(30);
	        muteIcon.setFitHeight(30);
	        muteButton.setGraphic(muteIcon);
	        
	    } catch (Exception e) {
	    	
	        this.muteButton.setText("Mute");
	        
	    }
	}
	

	private Image getCachedImage(String path) {
		
	    if (!imageCache.containsKey(path)) {
	    	
	        imageCache.put(path, new Image(getClass().getResource(path).toExternalForm())); 
	    } 
	    return imageCache.get(path);
	}
	
	public void loadTheBoard(Cell[][] boardCells, String playerName, String opponentName) {

//		Root Node
		this.rootContainer = new StackPane();
		rootContainer.setStyle("-fx-background-color: #2c3e50;");
		
		this.gameWindow = new BorderPane();
		this.gameWindow.setStyle("-fx-background-color: white;");
		
		this.cardPileLayer = new Pane();
		
//		A stack which has both the board gridpane and above it the overlay pane for diagonal extensions
		this.mainBoardContainer = new StackPane();
		mainBoardContainer.setMaxSize(10*CELL_SIZE, 10*CELL_SIZE);
//		GridPane to hold the board's cells
		this.boardGrid = new GridPane();
//		Pane to hold diagonal extensions of contamination sock and conveyor belt above the board grid
		this.overlayPane = new Pane();
		overlayPane.setMouseTransparent(true);
		
		boardGrid.setAlignment(Pos.CENTER);
				
		ImageView playerImage = new ImageView();
		ImageView opponentImage = new ImageView();
		
		playerImage.setFitWidth(35);
		playerImage.setFitHeight(35);
		opponentImage.setFitWidth(35);
		opponentImage.setFitHeight(35);
		
		playerImage.setId(playerName);
		playerImage.setImage(getCachedImage(returnMonsterImagePath(playerName)));
		opponentImage.setId(opponentName);
		opponentImage.setImage(getCachedImage(returnMonsterImagePath(opponentName)));
						
		Cell currentCell;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				
				currentCell = boardCells[i][j];
				
				StackPane cellStackPane = new StackPane();
				
				Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
				background.setArcWidth(15);
				background.setArcHeight(15);
				background.setStroke(Color.web("#2c3e50"));
				background.setStrokeWidth(2.0);
				
				int cellNumber = (i % 2 == 0) ? (i * 10) + j : (i * 10) + (9 - j);
				Label cellIndex = new Label(String.valueOf(cellNumber));
				
				ImageView cellIcon = new ImageView();
				cellIcon.setFitHeight(60);
				cellIcon.setFitWidth(60);
				
				HBox monstersContainer = new HBox(5);
				monstersContainer.setAlignment(Pos.CENTER);
				
				Label doorEnergy = new Label();
				doorEnergy.setTextFill(Color.BLACK);
				
//					Card cell
				if (currentCell instanceof CardCell) {
					
					background.setFill(createTileGradient("#8E2DE2", "#4A00E0"));
					cellIcon.setImage(getCachedImage("/resources/cards/card.png"));
					
//					Contamination Sock
				} else if (currentCell instanceof ContaminationSock) {
					
					background.setFill(createTileGradient("#ff9966", "#ff5e62"));
					Image image = getCachedImage("/resources/transport/sock.png");
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
					Image image = getCachedImage("/resources/transport/belt.png");
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
						cellIcon.setImage(getCachedImage("/resources/door/scarer-door.png"));
						cellIcon.setId("scarerDoor");
					} else {						
						cellIcon.setImage(getCachedImage("/resources/door/laugher-door.png"));
						cellIcon.setId("laugherDoor");
					}
					doorEnergy.setText("" + ((DoorCell) currentCell).getEnergy());
					cellIcon.setFitHeight(80);
					
//					Monster Cell
				} else if (currentCell instanceof MonsterCell) {
					background.setFill(createTileGradient("#00c6ff", "#0072ff"));
					
					String monsterCellName = ((MonsterCell) currentCell).getCellMonster().getName();
					cellIcon.setImage(getCachedImage(returnMonsterImagePath(monsterCellName)));
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
				boardGrid.add(cellStackPane, j, 9 - i);
			}
		}
		
		mainBoardContainer.getChildren().addAll(boardGrid, overlayPane);
		
		double boardBaseSize = 10 * CELL_SIZE;
		mainBoardContainer.setMinSize(boardBaseSize, boardBaseSize);
		mainBoardContainer.setPrefSize(boardBaseSize, boardBaseSize);
		mainBoardContainer.setMaxSize(boardBaseSize, boardBaseSize);

		Group boardGroup = new Group(mainBoardContainer);
		
		StackPane centerWrapper = new StackPane(boardGroup);
		
		centerWrapper.setMinSize(0, 0); 
		
		DoubleBinding scaleBinding = (DoubleBinding) Bindings.min(
			centerWrapper.widthProperty().divide(boardBaseSize),
			centerWrapper.heightProperty().divide(boardBaseSize)
		);
		
		mainBoardContainer.scaleXProperty().bind(scaleBinding);
		mainBoardContainer.scaleYProperty().bind(scaleBinding);
		
		this.gameWindow.setCenter(centerWrapper);
		
		this.headerLabel = new Label("Player 1 : " + playerName);
		this.gameWindow.setTop(headerLabel);
		
		headerLabel.setPadding(new Insets(30, 0, 30, 0)); 
		headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		centerWrapper.setPadding(new Insets(10, 20, 30, 20));

		StackPane.setAlignment(boardGroup, Pos.CENTER);
		
		BorderPane.setAlignment(headerLabel, Pos.CENTER);
		
		rootContainer.getChildren().addAll(cardPileLayer, gameWindow);
	}
	
	public void animateCardDraw(Card card, int remainingCards, Runnable onFinished) {
		String cardImagePath = getCardImagePath(card);
		
	    BorderPane mainWindow = this.getGameWindow();
	    Pane pileLayer = this.getCardPileLayer();
	    pileLayer.getChildren().clear(); // Not sure about this line
	    
	    int visualCardsCount = (remainingCards > 18) ? 5 : (remainingCards > 9) ? 3 : 1;
	    
	    StackPane deckVisual = new StackPane();
	    deckVisual.setLayoutX(400);
	    deckVisual.setLayoutY(50);
	    
	    Image cardBackImg = getCachedImage("/resources/cards/card-back.png");
	    
	    for (int i = 0; i < visualCardsCount; i++) {
	    	
	        ImageView stackedCard = new ImageView(cardBackImg);
	        stackedCard.setFitWidth(150);
	        stackedCard.setFitHeight(220);
	        
	        // Offset each card slightly to look like a pile
	        stackedCard.setTranslateX(i * -2); 
	        stackedCard.setTranslateY(i * -2);
	        deckVisual.getChildren().add(stackedCard);
	    }
	    pileLayer.getChildren().add(deckVisual);

	    ImageView drawnCard = new ImageView(cardBackImg);
	    drawnCard.setFitWidth(200);
	    drawnCard.setPreserveRatio(true);;
	    deckVisual.getChildren().add(drawnCard);

	    // A. Slide the main window down by 350 pixels
	    TranslateTransition slideDown = new TranslateTransition(Duration.seconds(0.8), mainWindow);
	    slideDown.setToY(350);
	    slideDown.setInterpolator(Interpolator.EASE_OUT);
	    
	    // B. Move the drawn card up and out of the pile
	    TranslateTransition pullCard = new TranslateTransition(Duration.seconds(0.5), drawnCard);
	    pullCard.setByY(-50);
	    pullCard.setByX(200);
	    
	    // C. The Flip Trick (Scale X from 1 to 0, swap image, Scale X from 0 to 1)
	    ScaleTransition flipPart1 = new ScaleTransition(Duration.millis(250), drawnCard);
	    flipPart1.setToX(0);
	    flipPart1.setOnFinished(e -> drawnCard.setImage(getCachedImage(cardImagePath)));
	    
	    ScaleTransition flipPart2 = new ScaleTransition(Duration.millis(250), drawnCard);
	    flipPart2.setToX(1);
	    
	    // D. Pause so the player can read the card
	    PauseTransition readingPause = new PauseTransition(Duration.seconds(2));
	    
	    // E. Fade the card out
	    FadeTransition fadeCard = new FadeTransition(Duration.millis(400), drawnCard);
	    fadeCard.setToValue(0);
	    
	    // F. Slide the main window back up
	    TranslateTransition slideUp = new TranslateTransition(Duration.seconds(0.8), mainWindow);
	    slideUp.setToY(0);
	    slideUp.setInterpolator(Interpolator.EASE_IN);

	    // --- CHAIN THEM TOGETHER ---
	    SequentialTransition fullSequence = new SequentialTransition(
	        slideDown,
	        pullCard,
	        flipPart1,
	        flipPart2,
	        readingPause,
	        fadeCard,
	        slideUp
	    );
	    
	    // When everything is done, trigger the game logic to continue!
	    fullSequence.setOnFinished(e -> {
	        pileLayer.getChildren().clear();
	        if (onFinished != null) onFinished.run();
	    });
	    
	    fullSequence.play();
	}
	
	private String getCardImagePath(Card card) {
		if (card instanceof SwapperCard) return "/resources/cards/swap.png";
	    if (card instanceof ShieldCard) return "/resources/cards/shield.png";
	    if (card instanceof StartOverCard) return "/resources/cards/start-over.png";
	    if (card instanceof ConfusionCard) return "/resources/cards/confusion.png";
	    if (card instanceof EnergyStealCard) return "/resources/cards/steal.png";
	    return "/resources/cards/card.png";
	}
	
	public void updateCardVBox(Card card) {
		
		String cardName = card.getName();
		String cardDescription = card.getDescription();
		
		this.cardNameLabel.setText(cardName);
		this.cardEffectLabel.setText(cardDescription);
		
	}
	
	private double[] getPixelsFromRowAndCol(int row, int col) {
		
	    int visualRow = 9 - row; 

	    double x = (col * CELL_SIZE) + (CELL_SIZE / 2);
	    double y = (visualRow * CELL_SIZE) + (CELL_SIZE / 2);
	    
	    return new double[]{x,y};

	}
	
	private Line drawDiagonalItem(int startRow, int startCol, int endRow, int endCol) {
		
		double[] start = getPixelsFromRowAndCol(startRow, startCol);
		double[] end = getPixelsFromRowAndCol(endRow, endCol);
				
	    Line line = new Line(start[0], start[1], end[0], end[1]);
	    
	    return line;
	}
	
	private int[] getEndRowAndColForTransport(int effect, int i, int j) {
		int startIndex = (i % 2 == 0) ? (i * 10) + j : (i * 10) + (9 - j);
		int endIndex = startIndex + effect;		
		return indexToRowCol(endIndex);
	}
	
	public void updateBoardAfterRoll(String currentName, int oldPosition, int newPosition) {
//		Calculating indices of start and end positions
		int[] startRowAndCol = indexToRowColRespectingGridPane(oldPosition);
		int[] endRowAndCol = indexToRowColRespectingGridPane(newPosition);
		
//		Retrieving cell stack for old and new cells
		StackPane oldStack = getNodeFromGridPane(this.boardGrid, startRowAndCol[1], startRowAndCol[0]);
		StackPane newStack = getNodeFromGridPane(this.boardGrid, endRowAndCol[1], endRowAndCol[0]);
		
//		Retrieving the HBox container of the monsters for both old and new cells
		HBox oldHBox = (HBox) oldStack.getChildren().get(oldStack.getChildren().size() - 1);
		HBox newHBox = (HBox) newStack.getChildren().get(newStack.getChildren().size() - 1);
		
//		Retrieving ImageView object for the monster using its name which was set previously as an ID
		ImageView imageView = (ImageView) getNodeById(oldHBox, currentName);
		
		if (imageView == null) {
			imageView = findMonsterImageViewOnBoard(currentName);
		}
		if (imageView != null) {
			animateMonsterMovement(imageView, oldPosition, newPosition, oldHBox, newHBox);
		}		
	}
	
	private ImageView findMonsterImageViewOnBoard(String monsterId) {
		 for (Node node : boardGrid.getChildren()) {
			 if (node instanceof StackPane) {
			 StackPane cell = (StackPane) node;
			 // The last child is always the HBox with monster avatars
			 Node last = cell.getChildren().get(cell.getChildren().size() - 1);
			 if (last instanceof HBox) {
				 Node found = getNodeById((HBox) last, monsterId);
			 if (found instanceof ImageView) return (ImageView) found;
			 }
		 }
		 }
		 return null;
	}
	
	public void animateMonsterMovement(ImageView realMonsterImg, int startPos, int endPos, HBox oldHBox, HBox newHBox) {
	    
	    oldHBox.getChildren().remove(realMonsterImg);

	    ImageView ghostImg = new ImageView(realMonsterImg.getImage());
	    ghostImg.setFitWidth(realMonsterImg.getFitWidth());
	    ghostImg.setFitHeight(realMonsterImg.getFitHeight());

	    this.overlayPane.getChildren().add(ghostImg);

	    Path path = new Path();

	    double halfCell = CELL_SIZE / 2.0;

	    int[] startRc = indexToRowColRespectingGridPane(startPos); 
	    double startX = (startRc[1] * CELL_SIZE) + halfCell;
	    double startY = (startRc[0] * CELL_SIZE) + halfCell;
	    
	    path.getElements().add(new MoveTo(startX, startY));

	    int distance = Math.abs(startPos - endPos);
	    int step = (startPos < endPos) ? 1 : -1; // Works for moving forward AND backward!

	    // If the movement is 6 or less, it's a dice roll. Walk step-by-step to follow the zigzag.
	    if (distance <= 6) {
	        for (int i = startPos + step; i != endPos + step; i += step) {
	            int[] rc = indexToRowColRespectingGridPane(i);
	            double px = (rc[1] * CELL_SIZE) + halfCell;
	            double py = (rc[0] * CELL_SIZE) + halfCell;
	            path.getElements().add(new LineTo(px, py));
	        }
	    }
	    
	    // If distance > 6, it's a Transport Cell or a Cheat. Draw a direct diagonal line.
	    else {
	        int[] endRc = indexToRowColRespectingGridPane(endPos);
	        double px = (endRc[1] * CELL_SIZE) + halfCell;
	        double py = (endRc[0] * CELL_SIZE) + halfCell;
	        path.getElements().add(new LineTo(px, py));
	    }

	    // --- 5. Configure the Path Transition ---
	    PathTransition pathTransition = new PathTransition();
	    
	    // Calculate duration: 300ms per cell moved for dice rolls, or a flat 1 second for massive transports
	    double durationMs = (distance <= 6) ? (distance * 300) : 1000; 
	    pathTransition.setDuration(Duration.millis(durationMs));
	    
	    pathTransition.setPath(path);
	    pathTransition.setNode(ghostImg);

	    // --- 6. On Finish: Clean up and drop the real monster ---
	    pathTransition.setOnFinished(event -> {
	        // Remove the ghost from the overlay
	        this.overlayPane.getChildren().remove(ghostImg);
	        // Put the real image back into the grid layout safely
	        newHBox.getChildren().add(realMonsterImg);
	    });

	    // --- 7. Play! ---
	    pathTransition.play();
	}		
	private Node getNodeById(Pane parentNode, String id) {
	    for (Node node : parentNode.getChildren()) {
	        if (id.equals(node.getId())) {
	            return node;
	        }
	    }
	    return null;
	}
	
	public VBox createVictoryScreen(Monster winner, Monster loser) {
		
	    VBox root = new VBox(40);
	    
	    root.setAlignment(Pos.CENTER);
	    root.setStyle("-fx-background-color: #2c3e50; -fx-padding: 50px;");

	    Label titleLabel = new Label(winner.getName() + " WINS!");
	    titleLabel.setStyle("-fx-font-family: 'Impact'; -fx-font-size: 80px; -fx-text-fill: #f1c40f;");
	    
	    DropShadow shadow = new DropShadow();
	    shadow.setRadius(10);
	    shadow.setColor(Color.BLACK);
	    titleLabel.setEffect(shadow);

	    Label roleLabel = new Label("The Ultimate " + winner.getRole() + "!");
	    roleLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold;");

	    HBox statsBox = new HBox(80);
	    statsBox.setAlignment(Pos.CENTER);

	    VBox winnerStats = new VBox(10);
	    winnerStats.setAlignment(Pos.CENTER);
	    Label wName = new Label("Winner: " + winner.getName());
	    Label wEnergy = new Label("Final Energy: " + winner.getEnergy());
	    wName.setStyle("-fx-font-size: 24px; -fx-text-fill: #2ecc71; -fx-font-weight: bold;");
	    wEnergy.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
	    winnerStats.getChildren().addAll(wName, wEnergy);

	    VBox loserStats = new VBox(10);
	    loserStats.setAlignment(Pos.CENTER);
	    Label lName = new Label("Runner-up: " + loser.getName());
	    Label lEnergy = new Label("Final Energy: " + loser.getEnergy());
	    lName.setStyle("-fx-font-size: 24px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
	    lEnergy.setStyle("-fx-font-size: 20px; -fx-text-fill: #bdc3c7;");
	    loserStats.getChildren().addAll(lName, lEnergy);

	    statsBox.getChildren().addAll(winnerStats, loserStats);

	    this.backButton = new Button("Return to Main Menu");
	    backButton.setStyle("-fx-font-size: 20px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 15px 30px;");

	    root.getChildren().addAll(titleLabel, roleLabel, statsBox, backButton);

	    return root; 
	}

	private StackPane getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return (StackPane)node;
	        }
	    }
	    return null;
	}
	
	public int[] indexToRowCol(int index) {
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
	    
	    ImageView monsterImage = new ImageView();
	    String monsterImgPath = returnMonsterImagePath(monster.getName());
	    monsterImage.setImage(getCachedImage(monsterImgPath));

	    monsterImage.setFitWidth(120);
	    monsterImage.setFitHeight(120);
	    monsterImage.setPreserveRatio(true);

	    DropShadow dropShadow = new DropShadow();
	    dropShadow.setRadius(8.0);
	    dropShadow.setOffsetY(3.0);
	    dropShadow.setColor(Color.color(0, 0, 0, 0.4));
	    monsterImage.setEffect(dropShadow);
	    
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
	    nameLabel.setMaxWidth(isMainPlayer ? 180 : 130);
	    nameLabel.setMinHeight(Region.USE_PREF_SIZE);
	    
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
	    
	    StringBuilder description = new StringBuilder("");

	    if (monster.isShielded()) {
	    	description.append("Blocks the next negative energy loss effect to the entire team\n");
	    	description.append("Doesn’t protect from schemer’s steal power\n");
	        statusBox.getChildren().add(createStatusBadge("🛡️", "#2980b9", 0, description.toString())); 
	    }
	    
	    description.setLength(0);

	    if (monster.isFrozen()) {
	    	description.append("Freezes a monster for 1 turn, Making them skip their entire next turn.");
	        statusBox.getChildren().add(createStatusBadge("❄️", "#3498db", 0, description.toString()));
	    }

	    description.setLength(0);
	    
	    int confusedTurns = monster.getConfusionTurns();
	    if (confusedTurns > 0) {
	    	description.append("Confused: Monster swap roles for ");
	    	description.append(confusedTurns + " turns.");
	        statusBox.getChildren().add(createStatusBadge("😵", "#e74c3c", confusedTurns, description.toString()));
	    }

	    description.setLength(0);
	    
	    if (monster instanceof Dasher) {
	    	int momentumTurns = ((Dasher) monster).getMomentumTurns(); // Example
	    	if (momentumTurns > 0) {
	    		description.append("Gain 3x movement speed for the next ");
	    		description.append(momentumTurns + " turns.");
	    		statusBox.getChildren().add(createStatusBadge("⚡", "#f1c40f", momentumTurns, description.toString()));
	    	}	    	
	    }
	    
	    description.setLength(0);
	    
	    if (monster instanceof MultiTasker) {
	    	int normalSpeedTurns = ((MultiTasker) monster).getNormalSpeedTurns();
	    	if (normalSpeedTurns > 0) {	    		
	    		description.append("Move at normal speed (not halved) for the next ");
	    		description.append(normalSpeedTurns + " turns.");
	    		statusBox.getChildren().add(createStatusBadge("👌", "#f1c40f", normalSpeedTurns, description.toString()));
	    	}
	    }

	    card.getChildren().addAll(monsterImage, nameLabel, roleLabel, positionLabel, energyBar, energyText);
	    
	    // Only add the statusBox to the card if there are actually active effects!
	    if (!statusBox.getChildren().isEmpty()) {
	        card.getChildren().add(statusBox);
	    }
	    
	    return card;
	}
	
	private Label createStatusBadge(String icon, String color, int duration, String description) {
		
	    String text = (duration > 0) ? icon + " " + duration : icon;
	    
	    Label badge = new Label(text);
	    badge.setStyle(
	        "-fx-background-color: " + color + "; " +
	        "-fx-text-fill: white; " +
	        "-fx-font-weight: bold; " +
	        "-fx-padding: 3px 8px; " +
	        "-fx-background-radius: 12px; " + // Gives it a pill/badge shape
	        "-fx-font-size: 11px;"
	        
	    );
	    
	    Tooltip tooltip = new Tooltip(description);
	    tooltip.setStyle("-fx-font-size: 12px; -fx-background-color: #34495e;");
	    
	    badge.setTooltip(tooltip);
	    
	    return badge;
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
	
	public void deactivateDoorOnTheBoard(int doorPosition) {
//		Calculating cell row and column from its index
		int[] doorRowAndCol = indexToRowColRespectingGridPane(doorPosition);
//		Extracting the cell stack from the board using row and column indices
		StackPane doorCellStack = getNodeFromGridPane(this.boardGrid, doorRowAndCol[1], doorRowAndCol[0]);
//		Extracting the cell's image view
		ImageView scarerDoor = (ImageView) getNodeById(doorCellStack, "scarerDoor");
		ImageView laugherDoor = (ImageView) getNodeById(doorCellStack, "laugherDoor");
//		Checks whether the door is deactivated first or not
		if (getNodeById(doorCellStack, "deactivated") == null) {			
			if (scarerDoor != null) {
				String path = "/resources/door/black-door-open.png";
				scarerDoor.setImage(getCachedImage(path));
				scarerDoor.setId("deactivated");
			}
			else if (laugherDoor != null) {
				String path = "/resources/door/blue-open-door.png";
				laugherDoor.setImage(getCachedImage(path));
				laugherDoor.setId("deactivated");
			}
		}
	}
	
	public HBox generateBottomSection() {		
//		Main Container for the bottom panel
	    HBox bottomPanel = new HBox(20);
	    bottomPanel.setAlignment(Pos.CENTER);
	    bottomPanel.setPadding(new Insets(15));
	    bottomPanel.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 2 0 0 0;");
	    bottomPanel.setMinWidth(0.0);

	    // Left Section : Buttons
	    VBox leftSection = new VBox(10);
	    leftSection.setAlignment(Pos.CENTER);
	    
	    this.rollDiceButton = new Button("Roll Dice");
	    this.usePowerupButton = new Button("Use Power Up");
	    rollDiceButton.setStyle("-fx-font-size: 14px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
	    usePowerupButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
	    
	    this.diceRollLabel = new Label("Dice: -");
	    diceRollLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    leftSection.getChildren().addAll(rollDiceButton, usePowerupButton, diceRollLabel);
	    
	    leftSection.setMinWidth(Region.USE_PREF_SIZE);

//	    Middle Section : Text area to log actions done
	    this.actionLog = new TextArea();
	    actionLog.setEditable(false);
	    actionLog.setWrapText(true);
	    actionLog.setPrefHeight(100);
	    actionLog.setStyle("-fx-control-inner-background: #2c3e50; -fx-text-fill: #ecf0f1; -fx-font-family: 'Consolas';");
	    actionLog.setMinWidth(0.0);
	    
	    actionLog.setMinWidth(0.0);
		actionLog.setPrefWidth(10.0);
	    
	    HBox.setHgrow(actionLog, Priority.ALWAYS);

//	    Right Section : Card Placeholder
	    this.cardViewer = new VBox(5);
	    cardViewer.setAlignment(Pos.CENTER);
	    cardViewer.setPrefWidth(200);
	    cardViewer.setStyle("-fx-background-color: white; -fx-border-color: #e74c3c; -fx-border-radius: 5px; -fx-border-width: 2px; -fx-padding: 10px;");
	    
	    cardViewer.setMinWidth(Region.USE_PREF_SIZE);
		cardViewer.setMaxWidth(Region.USE_PREF_SIZE);
	    
	    Label cardHeader = new Label("Last Card Drawn");
	    cardHeader.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
	    
	    this.cardNameLabel = new Label("None");
	    cardNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #c0392b;");
	    
	    this.cardEffectLabel = new Label("No effect");
	    cardEffectLabel.setWrapText(true);
	    cardEffectLabel.setAlignment(Pos.CENTER);
	    cardEffectLabel.setMinHeight(Region.USE_PREF_SIZE);
	    cardEffectLabel.setTextAlignment(TextAlignment.CENTER);
	    
	    cardViewer.getChildren().addAll(cardHeader, cardNameLabel, cardEffectLabel);

	    bottomPanel.getChildren().addAll(leftSection, actionLog, cardViewer);
	    
	    return bottomPanel;
	}
		
	public void logAction(String message) {
	    actionLog.appendText(message + "\n");
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
	
	public Label getDiceLabel() {
		return diceRollLabel;
	}
	
	public Button getBackButton() {
		return backButton;
	}
	
	public BorderPane getMainMenuLayout() {
		return mainMenuLayout;
	}
	
	public Button getMuteButton() {
		return muteButton;
	}
	
	public ImageCursor getImageCursor() {
		return imageCursor;
	}
	
	public BorderPane getGameWindow() {
		return gameWindow;
	}
	
	public Label getHeaderLabel() {
		return headerLabel;
	}
	
	public StackPane getRootContainer() {
		return rootContainer;
	}
	
	public Pane getCardPileLayer() {
		return cardPileLayer;
	}
}




