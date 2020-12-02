package domain;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import world.Habitacion;
import world.Item;
import world.Jugador;
import world.Mundo;

import java.io.FileReader;
import java.util.*;

public class Juego {

	public static final Scanner              scanner = new Scanner(System.in);
	public static       Mundo                mundo   = Mundo.dameInstancia();

	//TODO fix compilacion toma de args.
	public static void main(String[] args) throws Exception {
		List<String> ubicaciones = new ArrayList<>();

		//LEO mundo1
		FileReader   mundo1         = new FileReader("mundo1.json");
		JSONParser   jsonMundo = new JSONParser();
		JSONArray    jsonArray  = (JSONArray) jsonMundo.parse(mundo1);
		for (Object obj : jsonArray) {
			JSONObject datosHabitacion  = (JSONObject) obj;
			String     nombreHabitacion = (String) datosHabitacion.get("habitacion");
			ubicaciones.add(nombreHabitacion);

		}

		// PARSEO items
		FileReader frItems = new FileReader("items.json");
		JSONParser parserItems = new JSONParser();
		JSONArray itemsJson = (JSONArray) parserItems.parse(frItems);
		List<Item> listaItems = new ArrayList<>();
		for (Object obj: itemsJson){
			JSONObject item  = (JSONObject) obj;
			String descripcionItem = (String) item.get("descripcionItem");
			int danio = (int) (long) item.get("danio");
			int durabilidad = (int)(long) item.get("durabilidad");
			listaItems.add(new Item(descripcionItem,danio,durabilidad));
		}

		//PARSEO habitaciones
		FileReader   fl         = new FileReader("habitaciones.json");
		JSONParser   jsonParser = new JSONParser();
		JSONArray    arrayHabitaciones  = (JSONArray) jsonParser.parse(fl);

		for (Object obj: arrayHabitaciones){
		 	JSONObject habitaciones  = (JSONObject) obj;
		 	String nombreHabitacion = (String) habitaciones.get("nombreHabitacion");
		 	int codHab = (int) (long) habitaciones.get("codHab");
		 	JSONArray arraySalidas = (JSONArray) habitaciones.get("salidas");

			Map<String,Integer> salidas = new HashMap<>();
		 	for (Object salida: arraySalidas){
				JSONObject sal = (JSONObject) salida;
			    String direccionSalida = (String) sal.get("direccionSalida");
			    int codSalida = (int) (long) sal.get("codSalida");

			    salidas.put(direccionSalida,codSalida);
			}
		 	if (ubicaciones.contains(nombreHabitacion)){
		 	    mundo.agregarHabitacion(codHab,new Habitacion(codHab,nombreHabitacion,salidas,listaItems));

		    }

		}




		mundo.logo();
		mundo.crearPj();
		long millis_startTime = System.currentTimeMillis();
		while (mundo.heroe.getVida() > 0 && mundo.getPosicionActual() > 0) {
			mundo.queQuieresHacer();
		}
		long millis_endTime = System.currentTimeMillis();
		mundo.delay(2);
		mundo.gameOverMen();
		mundo.heroe.tiempoSupervivencia(millis_endTime-millis_startTime);
		mundo.heroe.stats();
		scanner.close();
	}



}
