package domain;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import world.Habitacion;
import world.Item;
import world.Mundo;
import world.errorHandling.ItemException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Juego {
	public static Scanner      scanner     = new Scanner(System.in);
	public static Mundo        mundo       = Mundo.dameInstancia();
	public static List<String> ubicaciones = new ArrayList<>();

	//TODO fix compilacion, toma de args.
	public static void main(String[] args) throws Exception, ItemException {
		mundo.logo();
		parseoMundo();
		parseoHabitaciones(parseoItems());

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

	private static void parseoMundo() throws IOException, ParseException {
		//LEO mundo1
		while (true) {
			try {

				mundo.linea();
				System.out.print("Elige tu mundo: ");
				String selectMundo = scanner.nextLine().toLowerCase().trim().concat(".json");
				mundo.linea();

				FileReader mundo1    = new FileReader(selectMundo);
				JSONParser jsonMundo = new JSONParser();
				JSONArray  jsonArray = (JSONArray) jsonMundo.parse(mundo1);
				for (Object obj : jsonArray) {
					JSONObject datosHabitacion  = (JSONObject) obj;
					String     nombreHabitacion = (String) datosHabitacion.get("habitacion");
					ubicaciones.add(nombreHabitacion);

				}
				mundo1.close();

				System.out.println(selectMundo + " CARGADO");
				mundo.clearScreen();
				mundo.linea();
				break;
			} catch (FileNotFoundException fileNotFoundException) {
				System.out.println("Archivo no encontrado. Intentalo de nuevo.");
			}
		}

	}

	private static void parseoHabitaciones(List<Item> listaItems) throws IOException, ParseException {
		//PARSEO habitaciones
		FileReader fl                = new FileReader("habitaciones.json");
		JSONParser jsonParser        = new JSONParser();
		JSONArray  arrayHabitaciones = (JSONArray) jsonParser.parse(fl);
		List<Item> randomItems       = new ArrayList<>();
		for (Object obj : arrayHabitaciones) {
			JSONObject           habitaciones     = (JSONObject) obj;
			String               nombreHabitacion = (String) habitaciones.get("nombreHabitacion");
			int                  codHab           = (int) (long) habitaciones.get("codHab");
			JSONArray            arraySalidas     = (JSONArray) habitaciones.get("salidas");
			Map<String, Integer> salidas          = new HashMap<>();
			for (Object salida : arraySalidas) {
				JSONObject sal             = (JSONObject) salida;
				String     direccionSalida = (String) sal.get("direccionSalida");
				int        codSalida       = (int) (long) sal.get("codSalida");

				salidas.put(direccionSalida, codSalida);
			}

			randomItems = randomItems(listaItems);

			if (ubicaciones.contains(nombreHabitacion)) {
				mundo.agregarHabitacion(
						codHab,
						new Habitacion(codHab, nombreHabitacion, salidas, randomItems(randomItems)));

			}

		}
		fl.close();
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
			listaItems.add(new Item(descripcionItem, danio, durabilidad));


		}


		frItems.close();
		return listaItems;

	}


	public static List<Item> randomItems(List<Item> listaItems) {
		int        cantItemsAremover;
		int        itemAAgregar;
		List<Item> nuevaLista = new ArrayList<>(listaItems);
		while (true) {
			cantItemsAremover = (int) Math.round(Math.random() * listaItems.size() / 2);
			if (cantItemsAremover!=listaItems.size()) break;
		}

		for (int i = 0; i < cantItemsAremover; i++) {
			itemAAgregar = (int) Math.round(Math.random() * nuevaLista.size());
			System.out.println(itemAAgregar);

			nuevaLista.remove(listaItems.get(itemAAgregar));
		}
		return nuevaLista;

	}

}
