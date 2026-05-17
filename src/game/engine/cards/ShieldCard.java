package game.engine.cards;

import game.engine.monsters.Monster;

public class ShieldCard extends Card {
	
	public ShieldCard(String name, String description, int rarity) {
		super(name, description, rarity, true); // LUCKY - protects you!
	}

	@Override
	public void performAction(Monster player, Monster opponent) {
		if (opponent.isShielded())
			opponent.setShielded(false);
		
		player.setShielded(true);
		System.out.println(player.getName() + " is now protected by a shield!");
		System.out.println("The shield will block the next negative effect!");
	}
}
