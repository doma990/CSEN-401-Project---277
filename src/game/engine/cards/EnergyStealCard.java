package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}

	@Override
	public void performAction(Monster player, Monster opponent) {
		int opponentEnergyBefore = opponent.getEnergy();
		
	    int toSteal = Math.min(this.getEnergy(), opponentEnergyBefore);

	    modifyCanisterEnergy(opponent, -toSteal);

	    if (opponent.getEnergy() == opponentEnergyBefore) {
	        System.out.println(opponent.getName() + "'s shield blocked the energy steal!");
	        return;
	    }

	    modifyCanisterEnergy(player, toSteal);
	    System.out.println(player.getName() + " stole " + toSteal + " energy from " + opponent.getName() + "!");
	}
	
	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(canisterValue);
	}
	
}
