package game.engine.cards;

import game.engine.monsters.Monster;

public class ConfusionCard extends Card {
	private int duration;
	
	public ConfusionCard(String name, String description, int rarity, int duration) {
		super(name, description, rarity, false);
		this.duration = duration;
	}
	
	public void performAction(Monster player, Monster opponent) {
		player.setRole(opponent.getOriginalRole());
		opponent.setRole(player.getOriginalRole());
		
		player.setConfusionTurns(getDuration());
		opponent.setConfusionTurns(getDuration());
		
	}

	public int getDuration() {
		return duration;
	}

}
