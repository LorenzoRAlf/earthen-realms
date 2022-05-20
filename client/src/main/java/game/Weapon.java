package game;
import java.io.Serializable;

public class Weapon implements Serializable{
	private String Name, damageType;
	private int Damage;
	private String id;

	public Weapon(String name, int damage, String damageType, String id) {
		super();
		Name = name;
		Damage = damage;
		this.damageType = damageType;
		this.id = id;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getDamage() {
		return Damage;
	}
	public void setDamage(int damage) {
		Damage = damage;
	}
	public String getDamageType() {
		return damageType;
	}
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
