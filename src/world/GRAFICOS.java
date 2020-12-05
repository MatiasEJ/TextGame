package world;

abstract class GRAFICOS {

	public void clearScreen() {
		for (int i = 0; i<15;i++){
			System.out.println();
		}
	}

	public void delay(int tiempoSegundos) {
		tiempoSegundos = tiempoSegundos * 1000;
		try {
			Thread.sleep(tiempoSegundos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void barraDeVida(int vida) {

		System.out.print(" ");
		for (int i = 0; i < (vida / 2); i++) {
			System.out.print("___");
		}
		System.out.println();
		for (int i = 0; i < vida / 2; i++) {
			System.out.print("/__");
		}
		System.out.print("/ Puntos de vida: (" + vida + ")");


		System.out.println();
	}


	public static String enNegrita(String aConvertir) {
		return "\033[1m" + aConvertir + "\033[0m";
	}

	public static void linea() {
		System.out.println("====---------------------------------------====");
	}

	public void gameOverMen() {
		System.out.println("\n" +
				"   ______                                    ____                      \n" +
				"  / ____/  ____ _   ____ ___   ___          / __ \\ _   __  ___    _____\n" +
				" / / __   / __ `/  / __ `__ \\ / _ \\        / / / /| | / / / _ \\  / ___/\n" +
				"/ /_/ /  / /_/ /  / / / / / //  __/       / /_/ / | |/ / /  __/ / /    \n" +
				"\\____/   \\__,_/  /_/ /_/ /_/ \\___/        \\____/  |___/  \\___/ /_/     \n" +
				"                                                                       \n");
	}

	public void logo(){
		System.out.println("\n");
		System.out.println("\n" +
				"████████╗██╗  ██╗██╗███████╗    ██╗███████╗    ██╗  ██╗ ██████╗ ██╗    ██╗    ██╗   ██╗ ██████╗ ██╗  " +
				" ██╗    ██████╗ ██╗███████╗\n" +
				"╚══██╔══╝██║  ██║██║██╔════╝    ██║██╔════╝    ██║  ██║██╔═══██╗██║    ██║    ╚██╗ ██╔╝██╔═══██╗██║  " +
				" ██║    ██╔══██╗██║██╔════╝\n" +
				"   ██║   ███████║██║███████╗    ██║███████╗    ███████║██║   ██║██║ █╗ ██║     ╚████╔╝ ██║   ██║██║  " +
				" ██║    ██║  ██║██║█████╗  \n" +
				"   ██║   ██╔══██║██║╚════██║    ██║╚════██║    ██╔══██║██║   ██║██║███╗██║      ╚██╔╝  ██║   ██║██║  " +
				" ██║    ██║  ██║██║██╔══╝  \n" +
				"   ██║   ██║  ██║██║███████║    ██║███████║    ██║  ██║╚██████╔╝╚███╔███╔╝       ██║   " +
				"╚██████╔╝╚██████╔╝    ██████╔╝██║███████╗\n" +
				"   ╚═╝   ╚═╝  ╚═╝╚═╝╚══════╝    ╚═╝╚══════╝    ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝        ╚═╝    ╚═════╝  " +
				"╚═════╝     ╚═════╝ ╚═╝╚══════╝\n" +
				"                                                                                                     " +
				"                           \n");
	}


	public void mapaUbicacionActual(String lugar) {

		System.out.println();

		switch (lugar) {
			case "casa":
				linea();
				System.out.println(lugar);
				linea();

				System.out.print(
								"      ^  ^\n" +
								"  +---+  +---+\n" +
								"<-+            +->\n" +

								"<-+            +->\n" +
								"  +---+  +---+\n" +
								"      v  v\n");
				break;
			case "cementerio":
				linea();
				System.out.println(lugar);
				linea();
				System.out.print(
								"    ^  ^\n" +
								"+---+  +---+\n" +
								"|          |\n" +
								"|          |\n" +
								"|          |\n" +
								"+----------+\n");
				break;
			case "escuela":
				linea();
				System.out.println(lugar);
				linea();
				System.out.print(
								"  +--------+\n" +
								"<-+        |\n" +
								"           |\n" +
								"<-+        |\n" +
								"  +--------+\n");
				break;
			case "lago":
				linea();
				System.out.println(lugar);
				linea();
				System.out.print(
								"      ^  ^\n" +
								"  +---+  +---+\n" +
								"<-+          |\n" +
								"             |\n" +
								"<-+          |\n" +
								"  +----------+\n");
				break;
			case "hospital":
				linea();
				System.out.println(lugar);
				linea();
				System.out.print(
					        	"  +----------+\n" +
								"<-+          |\n" +
								"             |\n" +
								"<-+          |\n" +
								"  +---+  +---+\n" +
								"      v  v\n");
				break;
			default:
				System.out.println("No encontrado el lugar");
				break;

		}

		linea();
	}


	public void muerte(){
		System.out.println("___________________|");
		System.out.println("|Woops, te moriste.|");
		System.out.println("|__________________|");
		System.out.println(
				"      .-.\n" +
				"     (o.o)\n" +
				"      |=|\n" +
				"     __|__\n" +
				"   //.=|=.\\\\\n" +
				"  // .=|=. \\\\\n" +
				"  \\\\ .=|=. //\n" +
				"   \\\\(_=_)//\n" +
				"    (:| |:)\n" +
				"     || ||\n" +
				"     () ()\n" +
				"     || ||\n" +
				"     || ||\n" +
				"    ==' '==");
		System.out.println("Eso no salio muy bien ^_^");

	}

	public void introJuego(){
		logo();
		linea();
		System.out.println("...es esto una pesadilla?");
		System.out.println("Bienvenido al 2021, te tenemos malas noticias: "+ enNegrita("apocalipsis zombie."));
		System.out.println("Estas herido. No tienes como protegerte. Estas rodeado. Y mañana es Lunes.");
		System.out.println("Tu objetivo? "+enNegrita("sobrevivir"));
	}

	public void todoEnNegrita(String frase){
		System.out.println(enNegrita(frase));
	}

}
