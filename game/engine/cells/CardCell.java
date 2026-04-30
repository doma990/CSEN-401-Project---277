package game.engine.cells;

import game.engine.monsters.Monster;
import game.engine.cards.Card;
import game.engine.*;

public class CardCell extends Cell {
	
	public CardCell(String name) {
        super(name);
    }
	
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.setMonster(landingMonster);
		Card drawnCard = Board.drawCard();
		drawnCard.performAction(landingMonster, opponentMonster);
	}

}
