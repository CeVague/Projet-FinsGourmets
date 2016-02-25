
import java.util.List;

public class Exerimentation {
	private final static int nb_restau = 1751;
	private final static int nb_clients = 3962;

	public static void main(String[] args){

		// Chargement du fichier d'entrainement
		int[][] tab = CsvFile.chargeTrain("train.csv");

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
		List<int[]> fileLignes = CsvFile.chargeTest("dev.csv");

		Predict ff = new Predict("dev.predict");
		for (int[] binome : fileLignes)
			Predict.add(moy_rest[binome[1]]);
		ff.ecrit();
		
		
		
		// Creation des prédictions pour le fichier test
		fileLignes = CsvFile.chargeTest("test.csv");

		ff = new Predict("test.predict");
		for (int[] binome : fileLignes)
			Predict.add(Integer.toString(moy_rest[binome[1]]));
		ff.ecrit();

		System.out.println("Fini :)");
	}
}
