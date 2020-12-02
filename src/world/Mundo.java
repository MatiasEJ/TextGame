package world;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Mundo extends GRAFICOS{
	private static Mundo instancia;
	public int                      posicionActual = 1;
	public static Jugador                  heroe ;
	public Map<Integer, Habitacion> habitaciones   = new HashMap<>();
	//TODO Diccionario fuera de la clase?
	List<String> comandos = List.of(
			"este", "oeste", "norte", "sur", "agarrar", "atacar", "comandos", "mirar",
			"mochila", "stats", "equipar", "salir", "escapar");


	private Mundo() {
	}

	public static Mundo dameInstancia() {
		if(instancia == null){
			instancia = new Mundo();
		}
		return instancia;
	}

	public void agregarHabitacion(int codHabitacion, Habitacion habitacion) {
		this.habitaciones.put(codHabitacion, habitacion);
	}

	public int getPosicionActual() {
		return posicionActual;
	}

	public Habitacion habitacionActual() {
		return this.habitaciones.get(this.posicionActual);
	}

	public void mostrarContenidoHabitacion() throws InterruptedException {
		System.out.println("Te encuentras en: ");

		mostrarSalidas();
		mostrarItems();
		System.out.println();
		if (getMonster().isInteractuable()) {
			delay(1);
			System.out.println("V^V^V^V^V^V^V^V^V^V^V^V^V^V^V^V");
			System.out.println();
			System.out.println(">-> !Un " + enNegrita(habitacionActual().getNombreMonster()) +"(" +habitacionActual().getMonster().getVida()+")" + " CUIDADO!");
			System.out.println();
			System.out.println("V^V^^V^V^V^V^V^V^V^V^V^V^V^V^V^");
		}
		linea();
	}

	public Monster getMonster() {
		return habitacionActual().getMonster();
	}

	public void mostrarSalidas() {
		mapaUbicacionActual(habitacionActual().getDescription());

		System.out.println("Frente a ti puedes ver: " + (habitacionActual().getSalidas().size()-1) + " salidas.");
		for (String salida : habitacionActual().getSalidas()) {
			if (!salida.equals("salir")){
			    System.out.print(enNegrita(salida) + " ");

			}

		}
		System.out.println();
	}

	public void queQuieresHacer() throws Exception {
		//TODO REFACTOREAR!!!
		Scanner scanner  = new Scanner(System.in);
		int     intentos = 0;
		while (true) {

			System.out.print("Que quieres hacer?: ");
			String   scanLinea       = scanner.nextLine().toLowerCase();
			String[] cadena          = scanLinea.split(" ");
			String[] cadenaItem      = scanLinea.split("equipar |agarrar ");
			String[] cadenaAdevolver = new String[2];


			if (scanLinea.length() > 0) {
				//  reviso la cadena
				for (String comando : cadena) {
					if (this.comandos.contains(comando)) {
						cadenaAdevolver[0] = comando;
						if (comando.equals("agarrar") || comando.equals("equipar")) {
							if (cadenaItem.length > 1) {
								cadenaAdevolver[1] = cadenaItem[1];
							}
						}
					}

				}
			}

			if (cadenaAdevolver[0] != null ) {
				accionar(cadenaAdevolver);
				break;
			} else {
				System.out.println("Comando incorrecto, intenta de nuevo.");
				intentos++;
			}
			if (intentos > 2) {
				mostrarComandos();
				intentos = 0;
			}
		}
	}



	private void accionar(String[] palabra) throws Exception {
		//TODO refactorear switch
		String original = palabra[0];
		String item = !existeItem(palabra[1]) ? "" : palabra[1];

		if (List.of("este", "oeste", "norte", "sur").contains(palabra[0])) {
			palabra[0] = "moverse";
		}
		if (List.of("mirar", "investigar", "observar", "ver").contains(palabra[0])) {
			palabra[0] = "mirar";
		}


		switch (palabra[0]) {
			case "atacar":
				heroe.atacar(getMonster());
				getMonster().atacar(heroe);
				break;
			case "moverse":
				moverse(original);
				break;
			case "agarrar":
				heroe.guardarEnMochila(itemAheroe(item));
				break;
			case "mirar":
				mostrarContenidoHabitacion();
				break;
			case "salir":
				heroe.salir();
				break;
			case "comandos":
				mostrarComandos();
				break;
			case "mochila":
				heroe.mostarContenidoMochila();
				break;
			case "stats":
				heroe.stats();
				break;
			case "equipar":
				heroe.equipar(itemAheroe(palabra[1]));
				break;
			case "escapar":
				heroe.escapar();
				break;
			default:
				System.out.println("no encontrado el comando");
		}
	}

	public boolean existeItem(String nombreItem){
		for (Item item: habitacionActual().getItems()){
			if (item.getNombre().equals(nombreItem)){
				return true;
			}
		}
		return false;
	}
	public void moverse(String direccion) {
		int posicionSalida = posicionSalida(direccion);

		if (existeHabitacion(posicionSalida)) {
			heroe.pierdeVidaAlCaminar();
			System.out.println("Te mueves lentamente al " + direccion);
			cambiarDireccion(direccion);
		} else {
			if (!existeSalida(direccion)) {
				System.out.println("No puedes ir en esa direccion");
			} else {
				System.out.println("No existe esa habitacion.");
			}
		}
	}

	public void cambiarDireccion(String direccion) {
		for (String salida : mapaSalidas().keySet()) {
			if (salida.equals(direccion)) {
				this.posicionActual = mapaSalidas().get(salida);
				break;
			}
		}
	}

	public int posicionSalida(String direccion) {
		for (String direccionSalida : mapaSalidas().keySet()) {
			if (direccionSalida.equals(direccion)) {
				return mapaSalidas().get(direccionSalida);
			}
		}
		return 0;
	}

	public boolean existeHabitacion(int codigo) {
		for (int codHab : habitaciones.keySet()) {
			if (codHab==codigo) {
				return true;
			}
		}

		return false;
	}

	public boolean existeSalida(String direccion) {
		return mapaSalidas().keySet().contains(direccion);
	}

	public Map<String, Integer> mapaSalidas() {
		return this.habitaciones.get(this.posicionActual).getMapSalidas();
	}


	public Item itemAheroe(String nombreItem) {
		for (Item item : habitacionActual().getItems()) {
			if (item.getNombre().equals(nombreItem)) {
				item.setInteractuable();
				return item;
			}
		}
		return null;
	}

	public void mostrarComandos() {
		System.out.println("Posibles comandos:");
		int cont = 0;
		for (String comando : this.comandos) {
			if (comando.equals("mirar")){
				System.out.println();
			}
			System.out.print(enNegrita(comando) + " ");
		}
		System.out.println();
		linea();
	}

	public void mostrarItems() {
		linea();
		System.out.println("En el piso ves: " + habitacionActual().getItems().size() + enNegrita(" Items!"));
		for (Item item : habitacionActual().getItems()) {
			if(!item.isInteractuable()){
				System.out.print(enNegrita("(*) ") + item.getNombre() + " ");
			}
		}
		System.out.println();
		linea();
	}


	public void crearPj() {
		Scanner scanner = new Scanner( System.in);
		introJuego();
		System.out.print("Tu nombre es : ");
		String nombre = scanner.nextLine();
		linea();
		mostrarComandos();
		heroe =  new Jugador(nombre);


	}
}