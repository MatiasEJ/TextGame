package world;

public class Monster extends Humanoide {
	//TODO Implementacion arma Monster
	private String  arma     = "palo";

	public Monster() {
		super("zombie",15);
	}

	// ==- GyS
	public String getNombre() {
		return nombre;
	}

	// ==- Metodos


	@Override
	public void recibeDanio(int danioRecibido) {
		super.recibeDanio(danioRecibido);
		if (!isAlive()) {
			this.vida = 0;
			setNotAtacable();
			System.out.println("El " + this.nombre + " se murio bien muerto.");
		}
	}

	private boolean isAlive() {
		return this.vida > 0;
	}

	@Override
	public boolean isInteractuable() {
		return atacable;
	}




}