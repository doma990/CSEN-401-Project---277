package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import org.omg.CORBA.DynAnyPackage.Invalid;

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
	
	void initializeBoard(ArrayList<Cell> specialCells) {
		int doorIndex = 1, beltIndex = 0, sockIndex = 0, cardIndex = 0, monsterIndex = 0;
//		Assigning all special cells
		for (Cell cell : specialCells) {
			int[] temp = new int[2];

//			Door Cell
			if (cell instanceof DoorCell) {
				temp = indexToRowCol(doorIndex);
				doorIndex += 2;
			}
			
//			Conveyor Belt Cell
			else if (cell instanceof ConveyorBelt)
				temp = indexToRowCol(Constants.CONVEYOR_CELL_INDICES[beltIndex++]);
			
//			Contamination Sock Cell
			else if (cell instanceof ContaminationSock) 
				temp = indexToRowCol(Constants.SOCK_CELL_INDICES[sockIndex++]);

//			Card Cell
			else if (cell instanceof CardCell)
				temp = indexToRowCol(Constants.CARD_CELL_INDICES[cardIndex++]);

//			Monster Cell
			else if (cell instanceof DoorCell)
				temp = indexToRowCol(Constants.MONSTER_CELL_INDICES[monsterIndex++]);
			
			boardCells[temp[0]][temp[1]] = cell;
		}
		
//		Assigning Normal Cells
		for (int i = 0; i < 100; i += 2) {
			int[] temp = indexToRowCol(i);
			if ( !(boardCells[temp[0]][temp[1]] instanceof Cell) )
				boardCells[temp[0]][temp[1]] = new Cell("NormalCell");
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
	
	static void reloadCards() {
		ArrayList<Card> temp = new ArrayList<Card>();
		// Copying
		for (Card card : originalCards)
			temp.add(card);
		
		// Shuffling
		Collections.shuffle(temp);
		
		// Assigning
		Board.cards = temp;
	}
	
	static Card drawCard() {
		if (Board.cards.isEmpty())
			reloadCards();
		
		return Board.cards.remove(0);
	}
	
	void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
		int newPostion = currentMonster.getPosition() + roll;
		int rowcol[] = indexToRowCol(newPostion);
		if (newPostion == opponentMonster.getPosition())
			throw new InvalidMoveException();
		else {
			currentMonster.setPosition(newPostion);
			boardCells[rowcol[0]][rowcol[1]].onLand(currentMonster, opponentMonster);
			currentMonster.decrementConfusion();
			opponentMonster.decrementConfusion();
			updateMonsterPositions(currentMonster, opponentMonster);
		}
	}
	
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









