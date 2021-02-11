package dostercios;

import java.util.List;

public class Monster extends Humanoide {
	private int id;
	private String descripcion;
	private int ataque;
	private List<String> universos;


	public Monster(int id, String nombre, int vida, String descripcion, int ataque, List<String> universos) {
		super(nombre, vida);
		this.id = id;
		this.descripcion = descripcion;
		this.ataque = ataque;
		this.universos = universos;
	}

	public List<String> getUniversos() {
		return universos;
	}

	public String getNombre() {
		return super.getNombre();
	}

	public void randomStats(){

	}
}