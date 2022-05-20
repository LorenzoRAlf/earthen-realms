package game;

import java.io.Serializable;

public class Armor implements Serializable{
	private String name, type, id;
	private int speed, defense;
	
	public Armor(String name, String type, int speed, int defense, String id) {
		super();
		this.name = name;
		this.type = type;
		this.speed = speed;
		this.defense = defense;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
}
