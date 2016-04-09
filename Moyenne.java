
public class Moyenne {

	public static double[] moy_restau;
	public static double[] moy_client;
	public static double moy_total;
	private static boolean init = false;

	/**
	 * Fonction qui calcule la note moyenne de toutes les notes
	 * 
	 * @param mat une matrice de donn�es fournis par chargeTrain de la classe
	 *            CsvFile.
	 * @return la moyenne calculée
	 */
	public static double moyenne_total(int[][] mat) {
		double count = 0;
		double sum = 0;

		// On parcourt toute la matrice
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				// Et on somme toutes les valeurs (pour les diviser ensuite)
				if (mat[i][j] != 0) {
					count++;
					sum += mat[i][j];
				}
			}
		}

		if (count != 0) {
			return sum / count;
		}

		return 0;
	}


	/**
	 * Fonction qui calcule la note moyenne qu'a obtenu un restaurant
	 * 
	 * @param restau l'indice indentifiant le restaurant dont on souhaite
	 *            calculer la moyenne.
	 * @param mat une matrice de donn�es fournis par chargeTrain de la classe
	 *            CsvFile.
	 * @return retourne la moyenne et 0 aucune note.
	 */
	public static double moyenne_restau(int restau, int[][] mat) {
		double count = 0;
		double sum = 0;

		// On parcourt tous les clients
		for (int i = 0; i < mat.length; i++) {
			// Et on somme toutes les valeurs (pour les diviser ensuite)
			if (mat[i][restau] != 0) {
				count++;
				sum += mat[i][restau];
			}
		}

		if (count != 0) {
			return sum / count;
		}
		
		return 0;
	}

	/**
	 * Fonction qui calcule la moyenne des notes qu'a donn� un client
	 * 
	 * @param client l'indice identifiant le client.
	 * @param mat une matrice de donn�es fournis par chargeTrain de la classe
	 *            CsvFile.
	 * @return retourne la moyenne et 0 aucune note.
	 */
	public static double moyenne_client(int client, int[][] mat) {
		double count = 0;
		double sum = 0;

		// On parcourt tous les restaurants
		for (int j = 0; j < mat[0].length; j++) {
			// Et on somme toutes les valeurs (pour les diviser ensuite)
			if (mat[client][j] != 0) {
				count++;
				sum += mat[client][j];
			}
		}

		if (count != 0) {
			return sum / count;
		}
		
		return 0;
	}

	/**
	 * Initialisation de la classe (indispensable pour utiliser completeMatrix()
	 * et get(int i, int j))
	 * 
	 * @param mat la matrice sur laquelle on souhaite travailler
	 */
	public static void initialiser(int[][] mat) {
		// On crée la liste des moyennes clients et restaurants
		moy_restau = new double[mat[0].length];
		moy_client = new double[mat.length];

		moy_total = moyenne_total(mat);

		// Et on les remplis
		
		// Enregistre la moyenne de chaque restaurant
		for (int i = 0; i < moy_restau.length; i++) {
			moy_restau[i] = moyenne_restau(i, mat);
		}

		// Enregistre la moyenne de chaque client
		for (int i = 0; i < moy_client.length; i++) {
			moy_client[i] = moyenne_client(i, mat);
		}

		// Dit que l'initialisation est effectué
		// Pour que le reste du programme de renvoie pas d'erreur
		init = true;
	}

	/**
	 * Crée la matrices des combinaisons des deux moyennes
	 * Il faut avoir préalablement initialisé la classe
	 * 
	 * @return la matrice toute remplis
	 */
	public static double[][] matrix() {
		// Vérifie que moy_client et moy_restau ont bien été initialisés
		if (!init) {
			System.out.println("Erreur : matrice non initialisée.");
			return null;
		}

		// On initialise la matrice finale
		double[][] mat = new double[moy_client.length][moy_restau.length];

		// Remplis la matrice en combinant les deux moyennes
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				double moyClient = moy_client[i];
				double moyRestau = moy_restau[j];

				if (moyClient != 0 && moyRestau != 0) { // Si c'est un cas normal
					mat[i][j] = (moyClient + moyRestau) / 2;
				} else if (moyClient == 0 && moyRestau == 0) { // Si ce client et restaurant n'ont aucune note
					mat[i][j] = Moyenne.moy_total;
				} else if (moyClient == 0) { // Si le client n'a pas donné de notes
					mat[i][j] = moyRestau;
				} else { // Si le restaurant n'a pas reçus de notes
					mat[i][j] = moyClient;
				}
			}
		}

		return mat;
	}

	/**
	 * Renvoie la valeur de la moyenne pour un client et un restaurant précis si
	 * la classe est bien initialisés
	 * 
	 * @param i l'id client
	 * @param j l'id restaurant
	 * @return la valeur moyenne
	 */
	public static double get(int i, int j) {
		if (!init) {
			System.out.println("Erreur : matrice non initialisée.");
			return 0;
		}

		double moyClient = moy_client[i];
		double moyRestau = moy_restau[j];


		if (moyClient != 0 && moyRestau != 0) { // Si c'est un cas normal
			return (moyClient + moyRestau) / 2;
		} else if (moyClient == 0 && moyRestau == 0) { // Si ce client et restaurant n'ont aucune note
			return Moyenne.moy_total;
		} else if (moyClient == 0) { // Si le client n'a pas donné de notes
			return moyRestau;
		} else { // Si le restaurant n'a pas reçus de notes
			return moyClient;
		}
	}

}