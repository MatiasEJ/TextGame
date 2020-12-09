package dostercios;

import dostercios.errorHandling.ItemException;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Humanoide {
	private Item       itemEquipado  = null;
	private List<Item> mochila;
	private long       tiempoConVida = 0;
	private int        maxVida       = 0;


	public Jugador(String nombre) {
		//cuanta vida deberia tener?
		super(nombre, 20);
		this.mochila = new ArrayList<>();
	}


	// ==- GyS
	public void stats() {
		System.out.println();
		clearScreen();

		System.out.println("============ STATS ACTUALES ============");
		barraDeVida(this.vida);
		mostarContenidoMochila();
		isItemEquipado();
		System.out.println("============ STATS ACTUALES ============");
	}

	//STATS
	public void pierdeVidaAlCaminar() {
		this.vida--;
		System.out.println("Tus heridas son graves, pierdes vida al moverte, te quedan (" + getVida() + ")");
		if (this.vida < 0) {
			muerte();
		}
	}

	@Override
	public void recibeDanio(int danioRecibido) {
		super.recibeDanio(danioRecibido);
		if (this.getVida() < 1) {
			this.vida = 0;
			setAtacable(false);
			muerte();


		}
	}

	public List<Item> getMochila() {
		return mochila;
	}

	public boolean isMochilaVacia() {
		if (this.getMochila().size() < 1) {
			System.out.print("Esta vacia (>T_T)> ");
			return true;
		}
		return false;
	}

	public void guardarEnMochila(Item item) {
		if (item!=null) {

			System.out.println("(*)" + item.getNombre() + " fue guardado en la mochila");
			item.setInMochila(true);
			mochila.add(item);
		} else {
			System.out.println("Nada para guardar T_T");
		}

	}

	public void desequipar() {
		if (this.itemEquipado!=null) {
			System.out.println("(*)" + this.itemEquipado + " fue desequipado.");
			this.itemEquipado.setEnMano(false);
			guardarEnMochila(this.itemEquipado);
		}

	}

	public void imprimirMochila() {
		for (Item item : getMochila()) {
			System.out.print(item.getNombre() + ", ");
		}
	}

	public void mostarContenidoMochila() {
		linea();
		System.out.println("Abres tu mochila:");
		System.out.print("=> ");
		if (!isMochilaVacia()) {
			imprimirMochila();
		}
		System.out.println("<=");
		linea();
	}

	private void isItemEquipado() {
		linea();
		if (itemEquipado!=null && itemEquipado.getDanio() > 0) {
			System.out.println("Llevas equipado: " + this.itemEquipado.getNombre());
		} else {
			System.out.println("No llevas nada equipado");
		}
		linea();
	}

	public void escapar() {
		System.out.println("Intentas escapar...");
		int dados = pruebaSuerte();
		if (dados < 4) {
			this.vida = this.vida - 4;
			System.out.println("Pero fallas, te rompes el craneo por " + 4 + " de danio");
		} else {
			System.out.println(enNegrita("Logras escapar!!") + "La esperanza te regenera vida!");
			this.vida = this.vida + 7;
		}

	}

	public boolean isInMochila(Item item) {
		return getMochila().contains(item);
	}

	public void equipar(Item item) throws ItemException {
		desequipar();
		if (!isInMochila(item)) {
			System.out.println("Item: " + item.getNombre() + " equipado.");
			System.out.println("Recibes bonus de " + item.getDanio());
			item.setEnMano(true);
			this.itemEquipado = item;
		} else {
			equiparDesdeMochila(item);
		}

	}


	public void equiparDesdeMochila(Item itemAequipar) {

		if (isInMochila(itemAequipar)) {
			System.out.println("Item: " + itemAequipar.getNombre() + " equipado desde la mochila.");
			this.itemEquipado = itemAequipar;
			getMochila().remove(itemAequipar);

		}
	}


	public void salir() {
		this.vida = 0;
		System.out.println();
		System.out.println("Abres los ojos, estas frente a la compu.\nHay tarea de Analisis, ya quisieras estar en " +
				"ese" +
				" " +
				"apocalipsis zombie");
	}

	//TODO ranking de supervivencia
	public void tiempoSupervivencia(long milisec) {
		this.tiempoConVida = milisec / 1000;
	}

	public void statsFinales() {
		System.out.println("============ STATS FINALES ============");
		System.out.println("Mejor Daño: " + super.getMejorDanioCritico());
		System.out.println("Sobreviviste: " + tiempoConVida + " segundos.");
		System.out.println("============ STATS FINALES ============");
	}


}