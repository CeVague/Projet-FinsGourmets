
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.io.File;

public class Exerimentation {
	private final static int nb_restau = 1751;
	private final static int nb_clients = 3962;

	public static void main(String[] args) throws IOException {

		// Chargement du fichier d'entrainement
		File myfile = new File("train.csv");
		Integer[][] tab = CsvFile.readFile(myfile);

		// Initialisation de la liste des moyennes obtenues pour chaque restaurants
		int[] moy_rest = new int[nb_restau];

		// Remplissage de la liste :
		// Pour chaque restaurant
		for (int i = 0; i < nb_restau; i++) {
			int total = 0;
			int compt = 0;

			// On additionne les notes de tous les clients qui en ont donnée
			for (int j = 0; j < nb_clients; j++) {
				if (tab[j][i] != 0) {
					compt++;
					total += tab[j][i];
				}
			}

			// On fait la moyenne (arrondi scientifique)
			if (compt > 0) {
				total = (total + (compt / 2)) / compt;
			}

			// Et on place la moyenne dans notre liste des moyennes
			moy_rest[i] = total;
		}

		
		
		
		// Creation des prédictions pour le fichier dev
		myfile = new File("dev.csv");
		List<String> fileLignes = CsvFile.readFileline(myfile);

		try {
			File ff = new File("dev.predict");
			ff.createNewFile();
			FileWriter ffw = new FileWriter(ff);

			// Pour chaque ligne du fichier dev.csv
			for (String binome : fileLignes) {
				// On récupère le moyenne du restaurant que l'on écrit dans le fichier
				String[] temp_bis = binome.split(",");
				ffw.write(Integer.toString(moy_rest[Integer.parseInt(temp_bis[1])]) + "\n");
			}

			ffw.close();
		} catch (Exception e) {

		}

		// Creation des prédictions pour le fichier test
		myfile = new File("test.csv");
		fileLignes = CsvFile.readFileline(myfile);

		try {
			File ff = new File("test.predict");
			ff.createNewFile();
			FileWriter ffw = new FileWriter(ff);

			// Pour chaque ligne du fichier test.csv
			for (String binome : fileLignes) {
				// On récupère le moyenne du restaurant que l'on écrit dans le fichier
				String[] temp_bis = binome.split(",");
				ffw.write(Integer.toString(moy_rest[Integer.parseInt(temp_bis[1])]) + "\n");
			}

			ffw.close();
		} catch (Exception e) {

		}

		System.out.println("Fini :)");
	}
}
