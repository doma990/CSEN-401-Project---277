package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster {
	private int momentumTurns;

	public Dasher(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.momentumTurns = 0;
	}
	
	public int getMomentumTurns() {
		return momentumTurns;
	}
	
	public void setMomentumTurns(int momentumTurns) {
		this.momentumTurns = momentumTurns;
	}
	
	public void move(int distance) {
		int finaldistance = 0;
		if(this.momentumTurns > 0) {
			finaldistance = distance * 3;
			momentumTurns--;
		}
		else
			finaldistance = distance * 2;
		super.move(finaldistance);
	}
	
	public void executePowerupEffect(Monster opponentMonster) {
		this.momentumTurns = 3;
	}
}