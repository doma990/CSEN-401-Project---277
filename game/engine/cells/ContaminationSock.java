package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}
	
//	public void onLand(Monster landingMonster, Monster opponentMonster) {
//		this.transport(landingMonster);
//		if (!landingMonster.isShielded())
//			modifyCanisterEnergy(landingMonster, -Constants.SLIP_PENALTY);
//		else
//			landingMonster.setShielded(false);
//	}
	
	public void transport(Monster monster) {
		monster.setPosition(monster.getPosition() + getEffect());
		 if (monster.isShielded()) {
            monster.setShielded(false);
         } else {
            monster.alterEnergy(-Constants.SLIP_PENALTY);
         }
	}
	
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(monster.getEnergy() + canisterValue);
	}
}

