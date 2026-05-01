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
		
		if(this.getName().equals("Mind Scramble")) {
			player.setConfusionTurns(2);
			opponent.setConfusionTurns(2);
		} else if (this.getName().equals("Total Confusion")){
			player.setConfusionTurns(3);
			opponent.setConfusionTurns(3);
		} else {			
			player.setConfusionTurns(1);
			opponent.setConfusionTurns(1);
		}
		
	}

	public int getDuration() {
		return duration;
	}

}
