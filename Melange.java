import java.util.List;

/**
 * Classe qui exploitera chacun des algorithmes que nous avons implémentés. Elle
 * contient donc le "Main" principal qui sera executé pour avoir le résultat
 * final.
 */
public class Melange {

	public static void main(String[] args) {

		System.out.print("Chargement des données d'entrainement...");

		// Chargement des données d'entrainement
		int[][] entrainement = CsvFile.chargeTrain("train.csv");

		// Sauvegarde des dimentions de la matrice
		int h = entrainement.length;
		int l = entrainement[0].length;

		System.out.println("Fait");


		/**************** Initialisation des Algorithmes *****************/

		System.out.println("Lancement des différents algorythmes :");


		System.out.print("SVD...");
		// Création de la matrice pour la SVD
		double[][] entrainementSVD = new double[h][l];


		// Remplissage de la matrice avec les moyennes restaurants (l'algo sera
		// bientot placé dans une classe à part)
		double[] moy_rest = new double[l];
		for (int i = 0; i < l; i++) {
			double total = 0;
			double compt = 0;
			for (int j = 0; j < h; j++)
				if (entrainement[j][i] != 0) {
					compt++;
					total += entrainement[j][i];
				}
			if (compt > 0)
				total = total / compt;
			moy_rest[i] = total;
		}
		for (int i = 0; i < h; i++)
			for (int j = 0; j < l; j++) {
				if (entrainement[i][j] == 0)
					entrainementSVD[i][j] = moy_rest[j];
				else
					entrainementSVD[i][j] = entrainement[i][j];
			}


		SVD.initialiser(entrainementSVD);

		SVD.decomposer(10);

		System.out.println("Fait");


		// 300 -> 250
		// 10000 -> 500000
		// 10000 -> 20000
		// 20000 -> 40000
		// 20, 20 -> 18, 20
		// 18, 20 -> 22, 20
		// 22, 20 -> 20, 20
		// 40000 -> 30000
		// 30000 -> 20000
		// 20, 20 -> 20, 18


		System.out.print("SGD...");
		for (int t = 0; t < 20000; t++) {
			int i = (int) (Math.random() * h);
			int j = (int) (Math.random() * l);
			if (entrainement[i][j] == 0)
				entrainement[i][j] = (int) Math.round(SVD.get(i, j));
		}


		SGD.facteurs(0.0002, 0.1, 20, 20, 250);
		double[][] fin = SGD.lance(entrainement);


		System.out.println("Fait");


		System.out.println("Tous les algorythmes ont été initialisés.");


		/********************* Prédiction des notes **********************/

		System.out.print("Création matrice finale");

		// Matrice de combinaison finale des prédictions
		double[][] matriceFinale = new double[h][l];

		for (int i = 0; i < h; i++) {
			if (i % 150 == 0)
				System.out.print(".");

			for (int j = 0; j < l; j++) {
				matriceFinale[i][j] = fin[i][j];
			}
		}

		System.out.println("Fait");

		/**************** Enregistrement des prédictions *****************/

		System.out.println("Création des fichiers de sortie :");

		// Maintenant que l'on a initialiser tous nos algorithmes, on va prédire
		// toutes les données demandées dans dev.csv et test.csv
		for (String nom : new String[] { "dev", "test" }) {
			// Chargement de la liste des emplacements des notes à trouver
			List<int[]> entree = CsvFile.chargeTest(nom + ".csv");

			// Initialisation du .predict de sortie
			PredictFile sortie = new PredictFile(nom + ".predict");

			// Et on prédit les notes demandées
			for (int[] couple : entree) {
				sortie.add(matriceFinale[couple[0]][couple[1]]);
			}

			// Fermeture du .predict
			sortie.close();

			System.out.println("- " + nom + ".predict fait");
		}

		// Zippage des deux fichiers
		PredictFile.zip("Melange.zip");

		System.out.println("Travail accomplit.");
	}
}
