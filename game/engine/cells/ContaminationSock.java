package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}
	
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		this.transport(landingMonster);
		modifyCanisterEnergy(landingMonster,
				Math.min(Constants.MIN_ENERGY, landingMonster.getEnergy() - Constants.SLIP_PENALTY));
	}
	
	public void transport(Monster monster) {
		monster.setPosition(monster.getPosition() - getEffect());
	}
	
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.setEnergy(canisterValue);
	}
}

