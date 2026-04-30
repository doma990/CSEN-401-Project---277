package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public void performAction(Monster player, Monster opponent) {
		if (opponent.isShielded()) {
			opponent.setShielded(false);
			return;
		}
		int stolen = Math.min(this.energy, opponent.getEnergy());
		modifyCanisterEnergy(player, player.getEnergy() + stolen);
		modifyCanisterEnergy(opponent, opponent.getEnergy() - stolen);
	}

//	@Override
//	public void performAction(Monster player, Monster opponent) {
//		if(!opponent.isShielded()) {
//			if(player.getEnergy()<opponent.getEnergy()) {
//				player.setEnergy(player.getEnergy()+opponent.getEnergy());
//				opponent.setEnergy(0);
//			}else {
//				player.setEnergy(player.getEnergy()*2);
//				opponent.setEnergy(opponent.getEnergy()-player.getEnergy());
//			}
//		  
//		}
//		
//	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.setEnergy(canisterValue);
	}

	
	public int getEnergy() {
		return energy;
	}
}
