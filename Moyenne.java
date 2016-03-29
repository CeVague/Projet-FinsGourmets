
public class Moyenne {

	public static double[] moy_restau;
	public static double[] moy_client;
	public static double moy_total;
	private static boolean init = false;

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
				sum += mat[i][restau];
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
				sum += mat[client][j];
			}
		}

		if (count != 0) { return sum / count; }

		return 0;
	}

	/**
	 * Fonction qui complete une matrice creuse en remplacant les 0 par la
	 * moyenne des notes recu par le restaurant et donn�es par le client.
	 * 
	 * @param mat une matrice creuse que l'on veux remplir.
	 * @return retourne la matrice remplie.
	 **/
	public static int[][] completeMatrix(int[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			double moyClient = moyenne_client(i, mat);

			for (int j = 0; j < mat[0].length; j++) {
				if (mat[i][j] == 0) {
					// System.out.println("["+i+"]"+"["+j+"] remplis");

					double moyRestau = moyenne_restau(j, mat);
					
					// Pour gérer en cas de ligne ou colonne vide
					if(moyClient==0 && moyRestau==0){
						mat[i][j] = 4;
					}else if(moyClient==0){
						mat[i][j] = (int) Math.round(moyRestau);
					}else if(moyRestau==0){
						mat[i][j] = (int) Math.round(moyClient);
					}else{
						mat[i][j] = (int) Math.round((moyClient + moyRestau) / 2);
					}
				}
			}

			if (i % 40 == 0)
				System.out.println((i * 100) / mat.length + " % complete");
		}

		System.out.println("100 % complete");

		return mat;
	}

	/**
	 * Fonction qui permet de récuperer la moyenne entre la moyenne d'un client
	 * et d'un restaurant
	 * 
	 * @param i l'incide du client
	 * @param j l'indice du restaurant
	 * @param mat la matrice à utiliser pour creer les valeurs
	 * @return la moyenne entre la moyenne client et restaurant
	 */
	public static double get(int i, int j, int[][] mat) {
		return (moyenne_client(i, mat) + moyenne_restau(j, mat)) / 2;
	}

	/**
	 * Initialisation de la classe (indispensable pour utiilser completeMatrix()
	 * et get(int i, int j))
	 * 
	 * @param mat la matrice sur laquelle on souhaite travailler
	 */
	public static void initialiser(int[][] mat) {
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
				double moyClient = moy_client[i];
				double moyRestau = moy_restau[j];
				
				// Pour gérer en cas de ligne ou colonne vide
				if(moyClient==0 && moyRestau==0){
					mat[i][j] = Moyenne.moy_total;
				}else if(moyClient==0){
					mat[i][j] = moyRestau;
				}else if(moyRestau==0){
					mat[i][j] = moyClient;
				}else{
					mat[i][j] = (moyClient + moyRestau) / 2;
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

		return (moy_client[i] + moy_restau[j]) / 2;
	}

	public static void main(String[] args) {

		int[][] mat = CsvFile.chargeTrain("train.csv");

		/*
		 * int[][] finalMat = completeMatrix(mat);
		 * System.out.print(finalMat[125][15]);
		 */

		initialiser(mat);
		matrix();

	}


}