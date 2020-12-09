package dostercios;

abstract class Humanoide extends GRAFICOS {
	protected String  nombre;
	protected int     vida;
	protected Item    itemEquipado;
	protected boolean atacable          = true;
	protected int     mejorDanioCritico = 0;
	protected int     conteoDeZombies   = 0;
	protected int     maximaVida        = 0;

	// CONSTRUCTORES
	public Humanoide(String nombre, int vida) {
		this.nombre = nombre;
		this.vida = vida;
	}

	// G&S
	public boolean isAtacable() {
		return atacable;
	}

	public void setAtacable(boolean atacable) {
		this.atacable = atacable;
	}

	public void setMejorDanioCritico(int mejorDanioCritico) {
		this.mejorDanioCritico = mejorDanioCritico;
	}

	public String getNombre() {
		return nombre;
	}

	public int getMejorDanioCritico() {
		return mejorDanioCritico;
	}

	public int getConteoDeZombies() {
		return conteoDeZombies;
	}


	public void guardarMaximaVida() {
		if (this.getVida() > maximaVida) {
			this.maximaVida = this.getVida();
		}
	}

	public int getMaximaVida() {
		return maximaVida;
	}

	private boolean isMonstruo() {
		return this.getClass()!=Jugador.class;
	}

	public int getVida() {
		return this.vida;
	}


	private void guardaDatos(int danio) {
		if (danio > mejorDanioCritico) {
			setMejorDanioCritico(danio);
		}
	}

	private boolean isItemEquipado() {
		return itemEquipado!=null && itemEquipado.getDurabilidad() > 0;

	}

	// =- COMBATE
	public int pruebaSuerte() {
		//tirada dados
		int dados = (int) Math.round(Math.random() * 10);
		if (dados < 4) {
			System.out.println("...la suerte no esta de su lado...");
			return 1;
		} else if (dados > 8) {
			todoEnNegrita("La suerte esta de su lado...");
			return dados + 5;
		}
		return dados;

	}

	private int modPorArma() {
		if (!isItemEquipado()) {
			return 0;
		}
		itemEquipado.usoItem();
		todoEnNegrita(" Bonus: " + itemEquipado.getDanio());
		return itemEquipado.getDanio();

	}


	public int atacar(Humanoide humanoide) {
		linea();
		int danio;
		if (this.getVida() > 0 && (humanoide.isAtacable() || humanoide.getVida() > 0)) {
			System.out.println("==> Es el turno de " + this.getNombre() + " para atacar.");
			danio = pruebaSuerte();
			danio = modPorArma() + chanceCritico(danio);
			muestraAtaque(humanoide, danio);
			guardaDatos(danio);
			return danio;
		} else {
			if (isMonstruo()) {
				this.vida = 0;
				todoEnNegrita("No Le pegues mas, bien muerto esta.");
			}
			return 0;
		}

	}

	private void muestraAtaque(Humanoide humanoide, int danio) {
		System.out.println(enNegrita(this.getNombre()) + " ataca por (" + danio + ") de danio a (" + enNegrita(humanoide.getNombre()) + ")");
		linea();
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	private int chanceCritico(int ataque) {
		if ((int) Math.round(Math.random() * 10) > 8) {
			return ataque * 2;
		}
		return ataque;
	}

	public void recibeDanio(int danioRecibido) {
		if (this.vida > 0) {
			this.vida = this.vida - danioRecibido;
		} else {
			System.out.println(this.getNombre() + " no puede recibir mas daño");
		}

	}

	//TODO falta implementacion
	private void recompensaPorMatar() {
		System.out.println("Encuentras una pocion, ganas 2 de vida!");
		this.vida = this.vida + 2;
		guardarMaximaVida();

	}

}

