package game.engine;

import java.util.ArrayList;
import java.util.Collections;


import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
import game.engine.monsters.Monster;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
		setCardsByRarity();
		reloadCards();
	}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}
	
	private int[] indexToRowCol(int index) {
		int row = index / 10;
		int column = (row % 2 == 0) ? (index % 10) : (9 - (index % 10));
		return new int[]{row,column};
	}
	
	private Cell getCell(int index) {
		int[] coordinates = indexToRowCol(index);
		return boardCells[coordinates[0]][coordinates[1]];
	}
	
	private void setCell(int index, Cell cell) {
		int[] coordinates = indexToRowCol(index);
		boardCells[coordinates[0]][coordinates[1]] = cell;
	}
	
	public void initializeBoard(ArrayList<Cell> specialCells) {
		int doorIndex = 1, beltIndex = 0, sockIndex = 0;
//		Assigning all Door, Belt, and Sock cells
		for (Cell cell : specialCells) {
			int index = 0;
//			Door Cell
			if (cell instanceof DoorCell) {
				index = doorIndex;
				doorIndex += 2;
			}
//			Conveyor Belt Cell
			else if (cell instanceof ConveyorBelt)
				index = Constants.CONVEYOR_CELL_INDICES[beltIndex++];
//			Contamination Sock Cell
			else if (cell instanceof ContaminationSock) 
				index = Constants.SOCK_CELL_INDICES[sockIndex++];
//			Setting the cell
			this.setCell(index, cell);
		}
//		Assigning Card Cells
		for (int index : Constants.CARD_CELL_INDICES)
			setCell(index, new CardCell("CardCell"));
//		Assigning Monster Cells
		for (int i = 0; i < stationedMonsters.size(); i++) {			
			int index = Constants.MONSTER_CELL_INDICES[i];
			Monster monster = stationedMonsters.get(i);
			monster.setPosition(index);
			setCell(index, new MonsterCell("MonsterCell", monster));
		}
//		Assigning Normal Cells
		for (int i = 0; i < 100; i += 2) {
			if (getCell(i) == null)
				setCell(i, new Cell("NormalCell"));
		}
	}
	
	private void setCardsByRarity() {
		ArrayList<Card> temp = new ArrayList<Card>();
		for (Card card : originalCards) {
			for (int i = 0; i < card.getRarity(); i++)
				temp.add(card);
		}
		Board.originalCards = temp;
	}
	
	public static void reloadCards() {
		ArrayList<Card> temp = new ArrayList<Card>();
		// Copying
		for (Card card : originalCards)
			temp.add(card);
		
		// Shuffling
		Collections.shuffle(temp);
		
		// Assigning
		Board.cards = temp;
	}
	
//	Adam : I changed the access modifier to public to be visible inside CardCell in onLand(M1, M2)
	public static Card drawCard() {
		if (Board.cards.isEmpty())
			reloadCards();
		
		return Board.cards.remove(0);
	}
	
	void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
		int newPosition = currentMonster.getPosition() + roll;
		if (newPosition > 99)
			newPosition -= 100;
		if (newPosition == opponentMonster.getPosition())
			throw new InvalidMoveException();
		else {
			currentMonster.setPosition(newPosition);
			getCell(newPosition).onLand(currentMonster, opponentMonster);
			if (currentMonster.isConfused()) {
				currentMonster.decrementConfusion();
				opponentMonster.decrementConfusion();				
			}
			updateMonsterPositions(currentMonster, opponentMonster);
		}
	}
//	Moves Monsters on the board
	private void updateMonsterPositions(Monster player, Monster opponent) {
		int[] playerRowCol = indexToRowCol(player.getPosition());
		int[] opponentRowCol = indexToRowCol(opponent.getPosition());
//		Clearing all cell monster references across the board
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				boardCells[i][j].setMonster(null);
		boardCells[playerRowCol[0]][playerRowCol[1]].setMonster(player);
		boardCells[opponentRowCol[0]][opponentRowCol[1]].setMonster(opponent);
	}
}









