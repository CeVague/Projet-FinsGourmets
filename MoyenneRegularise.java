

public class MoyenneRegularise {
	public static double[] moy_restau;
	public static double[] moy_client;
	public static double moy_total;
	private static boolean init = false;
	private static double betaClient, betaRestau;

	public static double moyenne_total(int[][] mat) {
		double count = 0;
		double sum = 0;

		for (int i = 0; i < mat.length; i++) {
			for(int j=0;j<mat[0].length;j++){
				if (mat[i][j] != 0) {
					count++;
					sum += mat[i][j];
				}
			}
		}

		if (count != 0) { return sum / count; }

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

		for (int j = 0; j < mat[0].length; j++) {
			if (mat[client][j] != 0) {
				count++;
				sum += (mat[client][j] - moy_total);
			}
		}

		if (count != 0) { return sum / (count + betaClient); }

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

		for (int i = 0; i < mat.length; i++) {
			if (mat[i][restau] != 0) {
				count++;
				sum += (mat[i][restau] - moy_total - moy_client[i]);
			}
		}

		if (count != 0) { return sum / (count + betaRestau); }

		return 0;
	}

	/**
	 * Initialisation de la classe (indispensable pour utiilser completeMatrix()
	 * et get(int i, int j))
	 * 
	 * @param mat la matrice sur laquelle on souhaite travailler
	 */
	public static void initialiser(int[][] mat, double bC, double bR) {
		betaClient = bC;
		betaRestau = bR;
		
		moy_restau = new double[mat[0].length];
		moy_client = new double[mat.length];
		
		moy_total = moyenne_total(mat);

		// Enregistre la moyenne de chaque restaurant
		for (int i = 0; i < moy_restau.length; i++) {
			moy_restau[i] = moyenne_restau(i, mat);
		}

		// Enregistre la moyenne de chaque client
		for (int i = 0; i < moy_client.length; i++) {
			moy_client[i] = moyenne_client(i, mat);
		}

		// Dit que l'initialisation est effectué
		init = true;
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

		return (moy_total + moy_client[i] + moy_restau[j]);
	}

	/**
	 * Crée la matrices des combinaisons des deux moeyennes si la classe à bien
	 * été initialisées
	 * 
	 * @return la matrice toute remplis
	 */
	public static double[][] matrix() {
		// Vérifie que moy_client et moy_restau ont bien été initialisés
		if (!init) {
			System.out.println("Erreur : matrice non initialisée.");
			return new double[moy_client.length][moy_restau.length];
		}

		double[][] mat = new double[moy_client.length][moy_restau.length];

		// Remplis la matrice en combinant les deux moyennes
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				mat[i][j] = (moy_total + moy_client[i] + moy_restau[j]);
			}
		}

		return mat;
	}

	public static void main(String[] args) {

		int[][] mat = CsvFile.chargeTrain("train.csv");

		/*
		 * int[][] finalMat = completeMatrix(mat);
		 * System.out.print(finalMat[125][15]);
		 */

		initialiser(mat, 25, 25);
		matrix();

	}
}
