package game.engine.cells;

import java.util.ArrayList;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;
	
	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
	}
	
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		if (!isActivated()) {
			ArrayList<Monster> stationed = Board.getStationedMonsters();
			if (landingMonster.getRole() == this.getRole()) {
//				TODO : Increase energy of landing monster and stationed ones of the same role by the cell's energy 
				modifyCanisterEnergy(landingMonster, this.getEnergy());
				for (Monster monster : stationed)
					if (monster.getRole() == landingMonster.getRole())
						modifyCanisterEnergy(monster, this.getEnergy());	
				setActivated(true);
			}
			else if (!landingMonster.isShielded()) {
//				TODO : Decrease energy of landing monster and stationed ones of the same role by the cell's energy 				
				modifyCanisterEnergy(landingMonster, -this.getEnergy());
				for (Monster monster : stationed)
					if (monster.getRole() == landingMonster.getRole())
						modifyCanisterEnergy(monster, -this.getEnergy());
				setActivated(true);
			}
			else
				landingMonster.setShielded(false);
		}
	}

	public Role getRole() {
		return role;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		int change = (monster.getRole() == this.getRole()) ? canisterValue : -canisterValue;
		monster.alterEnergy(change);
	}
}
