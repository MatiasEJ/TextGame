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
	public static Scanner    scanner = new Scanner(System.in);
	public static Mundo      mundo   = Mundo.dameInstancia();
	public static Habitacion habitacion;

	//TODO fix compilacion, toma de args.
	public static void main(String[] args) throws Exception, ItemException {
		System.out.print("Elige tu mundo (vampiros/zombies): ");
		String universo = scanner.nextLine().toLowerCase();
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

	public static Map<String, Integer> jsonObjToMap(JSONObject salidas){
		Map<String, Integer> salidasTemp = new HashMap<>();

		for (Object key : salidas.keySet())	{
				String kei = (String) key;
				int valor = (int) (long) salidas.get(key) ;
			salidasTemp.put(kei,valor);

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
			cantItemsAremover = (int) Math.round(Math.random() *  (listaItems.size() / 2 ));
			if (cantItemsAremover > 1 && cantItemsAremover < listaItems.size()){
			break;
			}
		}
		for (int i = 0; i < cantItemsAremover; i++) {
			itemAAgregar = (int) Math.round(Math.random() * listaItems.size());
			if (itemAAgregar < listaItems.size()){
				nuevaLista.add(listaItems.get(itemAAgregar));

			}
			// nuevaLista.remove(listaItems.get(itemAAgregar));
		}
		return nuevaLista;

	}

	public static List<Item> filtroItems(List<Item> listaItems, String filtro) {
		List<Item> listaFiltrada = new ArrayList<>();
		for (Item item : listaItems) {
			for (String nombre : item.getMundos()){
				if (nombre.equals(filtro)){
					listaFiltrada.add(item);
				}
			}
		}
		return listaFiltrada;
	}

	public static void parseoMundo(String universo) throws IOException, ParseException {
		String selectMundo = universo.trim().concat(".json");

		FileReader           fl                = new FileReader(selectMundo);
		JSONParser           jsonParser        = new JSONParser();
		JSONArray            arrayHabitaciones = (JSONArray) jsonParser.parse(fl);
		List<Item> listaItems = new ArrayList<>();

		for (Object obj : arrayHabitaciones) {
			JSONObject habitaciones     = (JSONObject) obj;
			int        codHab           = (int) (long)habitaciones.get("codHab");
			String     nombreHabitacion = (String) habitaciones.get("nombreHabitacion");
			JSONObject salidas = (JSONObject) habitaciones.get("salidas");

			listaItems = randomItems( filtroItems(parseoItems(), universo) );


			habitacion = new Habitacion(codHab, nombreHabitacion, jsonObjToMap(salidas), listaItems);


			mundo.agregarHabitacion(codHab, habitacion);

		}
	}

}
