import java.util.List;

/**
 * Classe qui exploitera chacun des algorithmes que nous avons implémentés. Elle
 * contient donc le "Main" principal qui sera executé pour avoir le résultat
 * final.
 */
public class Melange {
	
	public static void main(String[] args) {
		
		System.out.println("Chargement des données d'entrainement.");
		
		// Chargement des données d'entrainement
		int[][] entrainement = CsvFile.chargeTrain("train.csv");

		// Sauvegarde des dimentions de la matrice
		int h = entrainement.length;
		int l = entrainement[0].length;
		
		
		/**************** Initialisation des Algorithmes *****************/

		System.out.println("Début des initialisations d'algorythmes :");
		
		
		
		
		System.out.print("SVD...");
		// Création de la matrice pour la SVD
		double[][] entrainementSVD = new double[h][l];
		
		SVD.initialiser(entrainementSVD);
		
		SVD.decomposer(10);
		
		System.out.println("OK");
		
		
		
		
		
		System.out.println("Tous les algorythmes ont été initialisés.");
		
		
		/********************* Prédiction des notes **********************/
		
		System.out.print("Création matrice finale");
		
		// Matrice de combinaison finale des prédictions
		double[][] matriceFinale = new double[h][l];
		
		for (int i = 0; i < h; i++) {
			if(i%150==0) System.out.print(".");
			
			for (int j = 0; j < l; j++) {
				matriceFinale[i][j] = SVD.get(i, j);
			}
		}

		System.out.println("OK");
		
		/**************** Enregistrement des prédictions *****************/
		
		System.out.println("Création des fichiers de sortie :");
		
		// Maintenant que l'on a initialiser tous nos algorithmes, on va prédire
		// toute les données demandées dans dev.csv et test.csv
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
		PredictFile.zip("Melange Final.zip");

		System.out.println("Travail accomplit.");
	}
}
