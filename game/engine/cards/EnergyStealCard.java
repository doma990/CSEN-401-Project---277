package game.engine.cards;

public class EnergyStealCard extends Card {

	private int energy;
	
	public int getEnergy() {
		return energy;
	}
	
	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
}
