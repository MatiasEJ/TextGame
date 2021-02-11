package dostercios;


import dostercios.errorHandling.ItemException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Juego {
	public static Scanner       scanner = new Scanner(System.in);
	public static Mundo         mundo   = Mundo.dameInstancia();
	public static Habitacion    habitacion;
	public static List<Monster> enemigos;

	//TODO fix compilacion, toma de args.
	public static void main(String[] args) throws Exception, ItemException {
		System.out.print("Elige tu mundo (vampiros/zombies): ");
		String universo = scanner.nextLine().toLowerCase();
		enemigos = parseoEnemigos(universo);


		parseoMundo(universo);
		mundo.logo();
		mundo.introJuego(universo);
		mundo.crearPj();

		long millis_startTime = System.currentTimeMillis();
		while (mundo.getHeroe().getVida() > 0 && mundo.getPosicionActual() > 0) {
			System.out.print("Que quieres hacer?: ");
			String scanLinea = scanner.nextLine().toLowerCase();
			mundo.queQuieresHacer(scanLinea);
		}
		long millis_endTime = System.currentTimeMillis();
		mundo.delay(2);
		mundo.gameOverMen();
		mundo.getHeroe().tiempoSupervivencia(millis_endTime - millis_startTime);
		mundo.getHeroe().statsFinales();
	}

	public static Map<String, Integer> jsonObjToMap(JSONObject salidas) {
		Map<String, Integer> salidasTemp = new HashMap<>();

		for (Object key : salidas.keySet()) {
			String kei   = (String) key;
			int    valor = (int) (long) salidas.get(key);
			salidasTemp.put(kei, valor);

		}

		return salidasTemp;
	}

	private static List<Item> parseoItems() throws IOException, ParseException {
		// PARSEO items
		FileReader frItems     = new FileReader("items.json");
		JSONParser parserItems = new JSONParser();
		JSONArray  itemsJson   = (JSONArray) parserItems.parse(frItems);
		List<Item> listaItems  = new ArrayList<>();
		int        cantItems;
		for (Object obj : itemsJson) {
			JSONObject item            = (JSONObject) obj;
			String     descripcionItem = (String) item.get("descripcionItem");
			int        danio           = (int) (long) item.get("danio");
			int        durabilidad     = (int) (long) item.get("durabilidad");
			JSONArray  mundos          = (JSONArray) item.get("mundos");
			listaItems.add(new Item(descripcionItem, danio, durabilidad, mundos));
		}
		frItems.close();
		return listaItems;
	}


	public static List<Item> randomItems(List<Item> listaItems) {
		int        cantItemsAremover;
		int        itemAAgregar;
		List<Item> nuevaLista = new ArrayList<>();
		while (true) {
			cantItemsAremover = (int) Math.round(Math.random() * (listaItems.size() / 2));
			if (cantItemsAremover > 1 && cantItemsAremover < listaItems.size()) {
				break;
			}
		}
		for (int i = 0; i < cantItemsAremover; i++) {
			itemAAgregar = (int) Math.round(Math.random() * listaItems.size());
			if (itemAAgregar < listaItems.size()) {
				nuevaLista.add(listaItems.get(itemAAgregar));

			}
			// nuevaLista.remove(listaItems.get(itemAAgregar));
		}
		return nuevaLista;

	}

	public static List<Item> filtroItems(List<Item> listaItems, String filtro) {
		List<Item> listaFiltrada = new ArrayList<>();
		for (Item item : listaItems) {
			for (String nombre : item.getMundos()) {
				if (nombre.equals(filtro)) {
					listaFiltrada.add(item);
				}
			}
		}
		return listaFiltrada;
	}

	public static void parseoMundo(String universo) throws IOException, ParseException {
		String selectMundo = universo.trim().concat(".json");

		FileReader fl                = new FileReader(selectMundo);
		JSONParser jsonParser        = new JSONParser();
		JSONArray  arrayHabitaciones = (JSONArray) jsonParser.parse(fl);
		List<Item> listaItems        = new ArrayList<>();

		for (Object obj : arrayHabitaciones) {
			JSONObject habitaciones          = (JSONObject) obj;
			int        codHab                = (int) (long) habitaciones.get("codHab");
			String     nombreHabitacion      = (String) habitaciones.get("nombreHabitacion");
			String     descripcionHabitacion = (String) habitaciones.get("descripcionHabitacion");
			JSONObject salidas               = (JSONObject) habitaciones.get("salidas");

			listaItems = randomItems(filtroItems(parseoItems(), universo));
			habitacion = new Habitacion(
					codHab,
					nombreHabitacion,
					descripcionHabitacion, jsonObjToMap(salidas),
					listaItems, randomMonster(enemigos, universo));
			mundo.agregarHabitacion(codHab, habitacion);

		}
	}


	public static List<Monster> parseoEnemigos(String universo) throws IOException, ParseException {
		FileReader    frItems        = new FileReader("enemigos.json");
		JSONParser    parserItems    = new JSONParser();
		JSONArray     itemsJson      = (JSONArray) parserItems.parse(frItems);
		List<Monster> listaMonstruos = new ArrayList<>();
		for (Object obj : itemsJson) {
			JSONObject item        = (JSONObject) obj;
			int        id          = (int) (long) item.get("id");
			String     nombre      = (String) item.get("nombre");
			int        vida        = (int) (long) item.get("vida");
			String     descripcion = (String) item.get("descripcion");
			int        ataque      = (int) (long) item.get("ataque");
			JSONArray  universos   = (JSONArray) item.get("universo");
			listaMonstruos.add(new Monster(id, nombre, vida, descripcion, ataque, universos));
		}
		frItems.close();


		return listaMonstruos;

	}


	public static Monster randomMonster(List<Monster> listaMonstruos, String universo) {
		List<Monster> nuevaLista = new ArrayList<>(listaMonstruos);

		for (Monster monster : listaMonstruos) {
			for (String univ : monster.getUniversos()) {
				if (!univ.equals(universo)) {
					nuevaLista.remove(monster);
				}
			}
		}

		int posicionMonstruo;
		while (true) {
			posicionMonstruo = (int) Math.round(Math.random() * nuevaLista.size() - 1);
			if (posicionMonstruo >= 0 && posicionMonstruo < nuevaLista.size()) {
				break;
			}
		}


		return nuevaLista.get(posicionMonstruo);
	}


}
