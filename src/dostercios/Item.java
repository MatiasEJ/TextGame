package dostercios;

import java.util.Objects;

public class Item {
	private String  nombre;
	private int     danio;
	private int     durabilidad = 1;
	private boolean interactuable = false;
	private boolean inMochila = false;
	private boolean enMano = false;


	public void setInMochila(boolean inMochila) {
		this.inMochila = inMochila;
	}

	public void setEnMano(boolean enMano) {
		this.enMano = enMano;
	}

	public int getDurabilidad() {
		return durabilidad;
	}

	public Item(String nombre, int danio, int durabilidad ) {
		this.nombre = nombre;
		this.danio = danio;
		this.durabilidad = durabilidad;

	}

	public String getNombre() {
		return nombre;
	}

	public int getDanio() {
		return danio;
	}

	//TODO Falta implementacion de usar item.
	public void usoItem(){
		if(durabilidad > 1){
			this.durabilidad--;
		}else{
			System.out.println(this.nombre + "se rompio!");
		}
	}

	public boolean isInteractuable() {
		return interactuable;
	}

	void setInteractuable(){
		this.interactuable = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this==o) return true;
		if (!(o instanceof Item)) return false;

		Item item = (Item) o;

		if (durabilidad!=item.durabilidad) return false;
		return Objects.equals(nombre, item.nombre);
	}

	@Override
	public int hashCode() {
		int result = nombre!=null ? nombre.hashCode():0;
		result = 31 * result + durabilidad;
		return result;
	}


	@Override
	public String toString() {
		return this.nombre;
	}
}
