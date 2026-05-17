package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	
	@Override
	public void setEnergy(int energy) {
		super.setEnergy(energy + Constants.SCHEMER_STEAL);
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
	    System.out.println(getName() + " uses Chain Attack!");
	    int totalStolen = stealEnergyFrom(opponentMonster);

	    for (Monster target : Board.getStationedMonsters()) {
	        totalStolen += stealEnergyFrom(target);
	        System.out.println("  -> Stole from " + target.getName());
	    }

	    this.setEnergy(this.getEnergy() + totalStolen);
	    System.out.println("Total stolen: " + totalStolen + " energy!");
	}
	
	private int stealEnergyFrom(Monster target) {
	    int stolen = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
	    target.setEnergy(target.getEnergy() - stolen);
	    return stolen;
	}

}
