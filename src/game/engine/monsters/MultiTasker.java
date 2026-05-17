package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class MultiTasker extends Monster {
	private int normalSpeedTurns;
	
	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}
	
	public int getNormalSpeedTurns() {
		return normalSpeedTurns;
	}
	
	public void setNormalSpeedTurns(int normalSpeedTurns) {
		this.normalSpeedTurns = normalSpeedTurns;
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		this.setNormalSpeedTurns(2);
		System.out.println(getName() + " activated Focus Mode! Normal speed for 2 turns!");
	}
	
	@Override
	public void setEnergy(int energy) {
		super.setEnergy(energy + Constants.MULTITASKER_BONUS);
	}

	@Override
	public void move(int distance) {
		if (getNormalSpeedTurns() > 0) {
			System.out.println(getName() + " using Focus Mode! (" + normalSpeedTurns + " turns left)");
            setNormalSpeedTurns(getNormalSpeedTurns()-1);
	    } 
		
		else 
	        distance /= 2;
	    
	    super.move(distance);
	}
}