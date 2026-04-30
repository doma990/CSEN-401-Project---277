package game.engine.monsters;

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

	public void executePowerupEffect(Monster opponentMonster) {
		this.normalSpeedTurns = 2;	
	}
	
	public void move(int distance) {
		int finaldistance = 0;
		
		if(this.normalSpeedTurns > 0){
			finaldistance = distance;
			normalSpeedTurns--;
		}
		else
			finaldistance = (distance / 2);
		
		super.move(finaldistance);
	}
	
	public void setEnergy(int newEnergy) {
		super.setEnergy(newEnergy + 200);
	}
}









