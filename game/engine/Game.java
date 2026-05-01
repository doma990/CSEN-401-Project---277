package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());
		this.allMonsters = DataLoader.readMonsters();
		this.player = selectRandomMonsterByRole(playerRole);
		this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.current = player;
		Board.setStationedMonsters(getRemainingMonsters());
		this.board.initializeBoard(DataLoader.readCells());
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
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	
	private ArrayList<Monster> getRemainingMonsters() {
		ArrayList<Monster> temp = new ArrayList<Monster>();
		for (Monster monster : allMonsters)
			if (!monster.equals(player) && !monster.equals(opponent))
				temp.add(monster);
		return temp;
	}
	
//	Adam : I modified it to be == not .equals(player.getName())
	private Monster getCurrentOpponent() {
		if (this.current == this.player)
			return this.opponent;
		return this.player;
	}
	
//	private Monster getCurrentOpponent() {
//		if(current.getName().equals(player.getName()))
//			return opponent;
//		return player;
//	}
	
	private int rollDice() {
		return (int) (Math.random()*6 + 1);
	}
	
	void usePowerup() throws OutOfEnergyException {
		if (current.getEnergy() < Constants.POWERUP_COST)
			throw new OutOfEnergyException();
		else {
			current.alterEnergy(current.getEnergy() - Constants.POWERUP_COST);
			current.executePowerupEffect(current);
		}
	}

	void playTurn() throws InvalidMoveException {
		if (this.getCurrent().isFrozen())
			this.getCurrent().setFrozen(false);
		else
			board.moveMonster(current, rollDice(), getCurrentOpponent());
		this.switchTurn();
	}
	
	private void switchTurn() {
		if(this.current == this.player)
			this.setCurrent(this.getOpponent());
		else
			this.setCurrent(this.getPlayer());
	}
	
//	private void switchTurn() {
//		if(this.getCurrent().equals(this.getPlayer()) )
//			this.setCurrent(this.getOpponent());
//		else
//			this.setCurrent(this.getPlayer());
//	}	
	
	private boolean checkWinCondition(Monster monster) {
		return monster.getPosition() == Constants.WINNING_POSITION && 
				monster.getEnergy() >= Constants.WINNING_ENERGY;
	}
	
	public Monster getWinner() {
		if(this.checkWinCondition(this.getOpponent()))
			return this.getOpponent();
		
		if(this.checkWinCondition(this.getPlayer()))
			return this.getPlayer();
		
		return null;
	}
}










