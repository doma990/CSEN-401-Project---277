package game.engine.monsters;

import game.engine.Role;

abstract public class Monster implements Comparable<Monster> {
	
	private String name;
	private String description;
	private Role role;
	private Role originalRole;
	private int energy;
	private int position;
	private boolean frozen;
	private boolean shielded;
	private int confusionTurns;
	
	public Monster(String name, String description, Role originalRole, int energy) {
		this.name = name;
		this.description = description;
		this.originalRole = originalRole;
		this.role = originalRole;
		setEnergy(energy);
		setFrozen(false);
		setShielded(false);
		setPosition(0);
		setConfusionTurns(0);
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int energy) {
		this.energy = (energy >= 0) ? energy : 0;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		if (position <= 99 && position >= 0)
			this.position = position;
		else if (position > 99)
//			Wrapping positions greater than 99 to their value in the range [0-99]
			this.position = position - (((int) position / 100) * 100);
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public boolean isShielded() {
		return shielded;
	}
	
	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}
	
	public int getConfusionTurns() {
		return confusionTurns;
	}
	
	public void setConfusionTurns(int confusionTurns) {
		this.confusionTurns = confusionTurns;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Role getOriginalRole() {
		return originalRole;
	}
	
	public int compareTo(Monster o) {
		return this.position - o.position;
	}
}
