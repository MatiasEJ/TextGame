package world;


import world.errorHandling.ItemException;

import java.util.*;

public class Mundo extends GRAFICOS {
	private static Mundo                    instancia;
	private        int                      posicionActual = 1;
	private        Jugador                  heroe;
	public         Map<Integer, Habitacion> habitaciones   = new HashMap<>();
	//TODO Diccionario fuera de la clase?
	List<String> comandos = List.of(
			"este", "oeste", "norte", "sur", "agarrar", "atacar", "comandos", "mirar",
			"mochila", "stats", "equipar", "salir", "escapar", "investigar");

	// ==- CONSTRUCTORES
	private Mundo() {
	}

	public static Mundo dameInstancia() {
		if (instancia==null) {
			instancia = new Mundo();
		}
		return instancia;
	}

	public void agregarHabitacion(int codHabitacion, Habitacion habitacion) {
		this.habitaciones.put(codHabitacion, habitacion);
	}


	// ==- Getters&Setters

	public Jugador getHeroe() {
		return heroe;
	}

	public int getPosicionActual() {
		return posicionActual;
	}

	public Habitacion getHabitacionActual() {
		return this.habitaciones.get(this.posicionActual);
	}

	public void mostrarContenidoHabitacion() throws InterruptedException {
		System.out.println("Te encuentras en: ");
		mostrarSalidas();
		mostrarItems();
		System.out.println();
		if (getMonster().getVida() > 0) {
			delay(1);
			System.out.println("V^V^V^V^V^V^V^V^V^V^V^V^V^V^V^V");
			System.out.println();
			System.out.println(">-> !Un " + enNegrita(getHabitacionActual().getNombreMonster()) + "(" + getHabitacionActual().getMonster().getVida() + ")" + " CUIDADO!");
			System.out.println();
			System.out.println("V^V^^V^V^V^V^V^V^V^V^V^V^V^V^V^");
		}
		linea();
	}

	public Monster getMonster() {
		return getHabitacionActual().getMonster();
	}

	public void mostrarSalidas() {
		linea();
		mapaUbicacionActual(getHabitacionActual().getDescription());
		System.out.println("Frente a ti puedes ver: " + (getHabitacionActual().getSalidas().size() - 1) + " salidas.");
		for (String salida : getHabitacionActual().getSalidas()) {
			if (!salida.equals("salir")) {
				System.out.print(enNegrita(salida) + " ");

			}

		}
		System.out.println();
	}


	// =- Metodos

	public void queQuieresHacer(String scanLinea) throws InterruptedException {
		//TODO REFACTOREAR!!!
		try {
			String[] cadenaAdevolver = new String[2];
			String   comando         = deserializarComando(scanLinea);
			String   item            = asignarItem(scanLinea, comando);
			item = existeItem(item) ? item:null;


			if (scanLinea.length() > 0) {
				cadenaAdevolver[0] = comando;
				cadenaAdevolver[1] = item;
			}

			if (cadenaAdevolver[0]!=null) {
				accionar(cadenaAdevolver);
			} else {
				System.out.println("Comando incorrecto, intenta de nuevo. ");
				todoEnNegrita("Prueba con: comandos");
			}

		} catch (ItemException itemException) {
			itemException.ItemNoEncontrado();
		}
	}

	private String asignarItem(String scanLinea, String comando) throws NullPointerException {
		String[] cadenaItem = scanLinea.split("equipar |agarrar ");
		if (cadenaItem.length > 1) {
			return deserializarItem(comando, cadenaItem).trim().toLowerCase();
		}
		return null;

	}

	private String deserializarComando(String scanLinea) {
		String[] cadena = scanLinea.split(" ");
		return chequearCadena(cadena);
	}

	private String chequearCadena(String[] cadena) {
		for (String comando : cadena) {
			if (this.comandos.contains(comando)) {
				return comando;
			}
		}
		return null;
	}

	private String deserializarItem(String comando, String[] cadenaItem) {

		if (comando.equals("agarrar") || comando.equals("equipar")) {
			if (cadenaItem.length > 1) {
				return cadenaItem[1];
			}
		}
		return null;
	}

	private void accionar(String[] palabra) throws ItemException, InterruptedException {
		String direccionAmoverse = palabra[0];
		String item              = !existeItem(palabra[1]) ? "":palabra[1];
		if (List.of("este", "oeste", "norte", "sur").contains(palabra[0])) {
			palabra[0] = "moverse";
		}
		if (List.of("mirar", "observar", "ver").contains(palabra[0])) {
			palabra[0] = "mirar";
		}
		switchComandos(palabra, direccionAmoverse, item);
	}

	public boolean existeItem(Item item) {
		return getHabitacionActual().getItems().contains(item);
	}

	private boolean existeItem(String nombreItem) {

		for (Item item : getHabitacionActual().getItems()) {

			if (item.getNombre().toLowerCase().equals(nombreItem)) {
				return true;
			}
		}
		return false;
	}

	private void switchComandos(String[] palabra, String direccionAmoverse, String item) throws ItemException,
			InterruptedException {
		switch (palabra[0]) {
			case "atacar":
				combate();
				break;
			case "moverse":
				moverse(direccionAmoverse);
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
			case "investigar":
				investigar();
				break;
			default:
				System.out.println("no encontrado el comando");
		}
	}


	public void moverse(String direccion) {
		int posicionSalida = posicionSalida(direccion);
		heroe.pierdeVidaAlCaminar();

		if (existeHabitacion(posicionSalida)) {
			System.out.println("Te mueves lentamente al " + direccion);
			cambiarDireccion(direccion);
		} else {
			if (!existeSalida(direccion)) {
				System.out.println("No puedes ir en esa direccion!! Tendrias que " + enNegrita("investigar") + " un " +
						"poco mas.");
			} else {
				System.out.println("No existe esa habitacion.");
			}
		}
	}

	private void cambiarDireccion(String direccion) {
		for (String salida : mapaSalidas().keySet()) {
			if (salida.equals(direccion)) {
				this.posicionActual = mapaSalidas().get(salida);
				break;
			}
		}
	}

	private int posicionSalida(String direccion) {
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

		return mapaSalidas().containsKey(direccion);
	}


	public Map<String, Integer> mapaSalidas() {
		return this.habitaciones.get(this.posicionActual).getMapSalidas();
	}


	public Item itemAheroe(String nombreItem) {
		for (Item item : getHabitacionActual().getItems()) {
			System.out.println(item.getNombre());
			if (item.getNombre().toLowerCase().equals(nombreItem.toLowerCase())) {
				item.setInteractuable();
				//
				this.habitaciones.get(this.posicionActual).getItems().remove(item);
				return item;
			}
		}

		return null;
	}

	public void mostrarComandos() {
		System.out.println("Posibles comandos:");
		for (String comando : this.comandos) {
			if (comando.equals("mirar")) {
				System.out.println();
			}
			System.out.print(enNegrita(comando) + " ");
		}
		System.out.println();
		linea();
	}

	public void mostrarItems() {
		linea();
		System.out.println("En el piso ves: " + getHabitacionActual().getItems().size() + enNegrita(" Items!"));
		for (Item item : getHabitacionActual().getItems()) {
			// if (!item.isInteractuable()) {
				System.out.print(enNegrita("(*) ") + item.getNombre() + " ");
			// }
		}
		System.out.println();
		linea();
	}

	public void crearPj() {
		Scanner scanner = new Scanner(System.in);
		introJuego();
		System.out.print("Tu nombre es : ");
		String nombre = scanner.nextLine();
		linea();
		mostrarComandos();
		heroe = new Jugador(nombre);


	}

	private void combate() {
		getMonster().recibeDanio(getHeroe().atacar(getMonster()));
		heroe.recibeDanio(getMonster().atacar(heroe));
	}

	public Collection<Integer> codigosSalidasHabitacion() {
		return getHabitacionActual().getMapSalidas().values();
	}

	//TODO reworkear
	public void investigar() {
		todoEnNegrita("No todo es lo que parece aqui...");
		delay(1);
		System.out.println("Encuentras las verdaderas salidas!: ");
		int cont = 0;
		for (Integer codigo : codigosSalidasHabitacion()) {
			if (existeHabitacion(codigo)) {
				for (String sal : mapaSalidas().keySet()) {
					if (mapaSalidas().get(sal).equals(codigo)) {
						todoEnNegrita(sal + " ");

					}

				}
				cont++;
			}
		}
		System.out.println();
		System.out.println("Solo existen " + (cont) + " habitaciones verdaderas...");


	}


	//TODO implementar mapa autoGenerado.
	private void crearMapa() {
		for (String salida : getHabitacionActual().getMapSalidas().keySet()) {
			if (existeSalida(salida)) {
				System.out.println();
				if (salida.equals("sur")) {
					System.out.println(
							"  +---+  +---+\n" +
									"      v  v\n");
				} else {
					System.out.println("  +---------+\n");
				}
				if (salida.equals("este")) {
					System.out.println("" +
							"   \n" +
							"  \n" +
							"<-+\n" +

							"<-+\n" +
							"  +\n" +
							"  \n");

				}
			}
		}
	}

}