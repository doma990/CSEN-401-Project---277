package game.engine;

import java.util.ArrayList;
import java.io.IOException;

import game.engine.monsters.Monster;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters;
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
//		Initializing the board
		board = new Board(game.engine.dataloader.DataLoader.readCards());
//		Reading monsters.csv and initializing stationed monsters on the board
		Board.setStationedMonsters(game.engine.dataloader.DataLoader.readMonsters());
//		Random selection of monsters for player
		player = selectRandomMonsterByRole(playerRole);
//		Random selection of monsters for opponent
		Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
		opponent = selectRandomMonsterByRole(opponentRole);		
//		Setting the current with the player
		current = player;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		ArrayList<Monster> temp = Board.getStationedMonsters();
		Monster result;
		do {
			int index = (int) Math.random() * temp.size();
			result = temp.get(index);
		} while (result.getRole() != role);
		return result;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters;
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	
}
