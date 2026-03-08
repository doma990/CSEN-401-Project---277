package game.engine.cells;

import game.engine.monsters.*;

public class Cell {
	
	private String name;
	private Monster monster;
	
	public Cell (String name) {
		this.name = name;
		monster = null;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public String getName() {
		return name;
	}
	
}
