package game.engine.cards;

import game.engine.monsters.Monster;

public class SwapperCard extends Card {

	public SwapperCard(String name, String description, int rarity) {
		super(name, description, rarity, true);
	}

	@Override
	public void performAction(Monster player, Monster opponent) {
		if (player.compareTo(opponent) < 0) {
			int playerPosition = player.getPosition();
			player.setPosition(opponent.getPosition());
			opponent.setPosition(playerPosition);
			System.out.println("Swapped positions! " + player.getName() + " and " + opponent.getName());
		}
	}

}
