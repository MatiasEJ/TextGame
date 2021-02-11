package dostercios;

import java.util.*;

public class Habitacion  {
	//ATRIBUTOS
	private final int    locationId;
	private final String nombre;
	private final String descripcionHabitacion;
	private       Map<String,Integer> salidas;
	private final List<Item>          items;
	private final Monster monster;

	//CONSTRUCTOR
	public Habitacion(int locationId, String nombre, String descripcionHabitacion, Map<String, Integer> salidas, List<Item> items, Monster monster) {
		this.locationId = locationId;
		this.nombre = nombre;
		this.descripcionHabitacion = descripcionHabitacion;
		this.salidas = new HashMap<String, Integer>(salidas);
		this.salidas.put("salir",0);
		this.items = new ArrayList<>(items);
		this.monster = monster;
	}

	public String getNombreMonster() {
		return monster.getNombre();
	}

	public Monster getMonster() {
		return monster;
	}


	public List<Item> getItems() {
		return items;
	}

	public int getLocationId() {
		return locationId;
	}

	public String getNombre() {
		return nombre;
	}

	public Set<String> getSalidas() {
		return salidas.keySet();
	}

	public Map<String, Integer> getMapSalidas(){
		return  salidas;
	}

	@Override
	public boolean equals(Object obj){
		Habitacion loc = (Habitacion) obj;
		return this.locationId == loc.locationId;
	}

	@Override
	public int hashCode(){
		return this.locationId;
	}

	public String getDescripcionHabitacion() {
		return descripcionHabitacion;
	}
}
