package game.engine.cells;

import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}
	
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		if (landingMonster.getRole() == cellMonster.getRole())
			landingMonster.executePowerupEffect(landingMonster);
		else if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
			int cellMonsterOriginalEnergy = cellMonster.getEnergy();
			cellMonster.setEnergy(landingMonster.getEnergy());
			if (landingMonster.isShielded())
				landingMonster.setShielded(false);
			else
				landingMonster.setEnergy(cellMonsterOriginalEnergy);
		}
	}
}
