package world;

abstract class Humanoide extends GRAFICOS implements Interactuable {
	String  nombre;
	int     vida;
	boolean atacable = true;
	private int mejorDanioCritico = 0;
	private int  conteoDeZombies = 0;
	private Item itemEquipado;
	private int maximaVida = 0;


	private Humanoide() {
	}

	public Humanoide(String nombre, int vida) {
		this.nombre = nombre;
		this.vida = vida;
	}

	// == GyS
	public String getNombre() {
		return nombre;
	}


	public int getMejorDanioCritico() {
		return mejorDanioCritico;
	}

	public int getConteoDeZombies() {
		return conteoDeZombies;
	}

	public Item getItemEquipado() {
		return itemEquipado;
	}
	public int getVida() {
		return this.vida;
	}

	private void statusVida() {
		System.out.println("A " + enNegrita(this.getNombre()) + " le queda (" + this.getVida() + ") de vida.");
	}

	@Override
	public boolean isInteractuable() {
		return this.atacable;
	}

	private void guardaDatos(int danio, Humanoide humanoide) {
		if (danio > this.mejorDanioCritico) {
			this.mejorDanioCritico = danio;
		}
		if (!humanoide.isInteractuable()) {
			this.conteoDeZombies++;
		}
	}

	public void setNotAtacable() {
		this.atacable = false;
	}

	private boolean isItemEquipado() {
		return itemEquipado!=null && itemEquipado.getDurabilidad() > 0;

	}

	// =- COMBATE
	public int pruebaSuerte() {
		//tirada dados
		int dados = (int) Math.round(Math.random() * 10);
		if (dados < 4) {
			System.out.println("...la suerte no esta de su lado.");
			return 1;
		} else if (dados > 8) {
			System.out.println("La suerte esta de su lado...danio aumentado (" + (dados + 5) + ")");
			return dados + 5;
		}
		return dados;

	}

	private int modPorArma() {
		if (!isItemEquipado()) {
			return 0;
		}
		itemEquipado.usoItem();
		return itemEquipado.getDanio();

	}

	public int modMonstruo(Humanoide humanoide, int danio) {
		if (humanoide.getClass()!=Jugador.class) {
			return chanceCritico(danio) + modPorArma();
		}
		return danio + modPorArma();
	}

	public void atacar(Humanoide humanoide) {
		linea();
		int danio = 1;
		if (this.getVida() > 0 && humanoide.isInteractuable() && humanoide.getVida() > 0) {
			danio = pruebaSuerte();
			System.out.println("==> Es el turno de " + this.getNombre() + " para atacar.");
			danio = modMonstruo(humanoide, danio);
			muestraAtaque(humanoide, danio);


			humanoide.recibeDanio(danio);

		} else {
			recompensaPorMatar(humanoide, danio);

		}
	}

	private void muestraAtaque(Humanoide humanoide, int danio) {
		System.out.println(enNegrita(this.getNombre()) + " ataca por (" + danio + ") de danio a (" + enNegrita(humanoide.getNombre()) + ")");
		linea();
	}

	private int chanceCritico(int ataque) {
		if ((int) Math.round(Math.random() * 10) > 8) {
			return ataque * 2;
		}
		return ataque;
	}

	public void recibeDanio(int danioRecibido) {
		this.vida = this.getVida() - danioRecibido;
		System.out.println(enNegrita(this.getNombre()) + " recibe (" + danioRecibido + ") de danio.");
		statusVida();

	}

	public void recompensaPorMatar(Humanoide humanoide, int danio) {

			System.out.println("Encuentras una pocion, ganas 2 de vida!");
			this.vida = this.vida + 2;
			guardaDatos(danio, humanoide);
			guardarMaximaVida();

	}

	public void guardarMaximaVida(){
		if (this.getVida() > maximaVida){
			this.maximaVida = this.getVida();
		}
	}

	public int getMaximaVida() {
		return maximaVida;
	}
}

