package world;

import java.util.*;

public class Habitacion  {
	//ATRIBUTOS
	private final int                 locationId;
	private final String              description;
	private       Map<String,Integer> salidas;
	private final List<Item>          items;
	private final Monster monster;

	//CONSTRUCTOR
	public Habitacion(int locationId, String description, Map <String, Integer> salidas,  List<Item> items) {
		this.locationId = locationId;
		this.description = description;
		this.salidas = new HashMap<String, Integer>(salidas);
		this.salidas.put("salir",0);
		this.items = new ArrayList<>(items);
		this.monster = new Monster();
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

	public String getDescription() {
		return description;
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

}
