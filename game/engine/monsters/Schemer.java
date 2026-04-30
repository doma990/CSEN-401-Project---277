package game.engine.monsters;

import java.util.ArrayList;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	
	private int stealEnergyFrom(Monster target){	
		int stolen = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
		target.setEnergy(target.getEnergy() - stolen );
		return stolen;
	}
	
	public void setEnergy(int newEnergy) {
		super.setEnergy(newEnergy + 10);
	}

	public void executePowerupEffect(Monster opponentMonster) {
		int total =0;
		ArrayList<Monster> stationed = Board.getStationedMonsters(); 
		for (Monster monster : stationed) {
			total += stealEnergyFrom(monster);
		}
		total += stealEnergyFrom(opponentMonster);
		
		this.setEnergy(this.getEnergy() + total);
	}
}
