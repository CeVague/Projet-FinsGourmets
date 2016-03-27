import weka.core.matrix.Matrix;
import weka.core.matrix.SingularValueDecomposition;

public class SVD {

	private static int phase = 0; // 0, 1 si table est init, 2 si USV sont init
	private static Matrix MInit;
	private static Matrix U;
	private static Matrix S;
	private static Matrix V;
	private static Matrix MFinal;

	/**
	 * Initialise les variables intèrnes de la classe SVD. Toutes les autres
	 * oppérations se feront sur cette nouvelle matrice.
	 * 
	 * @param tab un tableau de notes sans trou (il faut la remplir avant)
	 */
	public static void initialiser(double[][] tab) {
		MInit = new Matrix(tab);
		phase = 1;
	}

	/**
	 * Applique la SVD à la matrice déjà initialisée auparavent (cette étape est
	 * longue, c'est normal).
	 */
	private static void decomposer() {
		// Gestion des exeptions
		if (phase == 0) {
			System.out.println("La table n'est pas initialisée");
			return;
		}

		// On fait la décomposition
		SingularValueDecomposition SVD = new SingularValueDecomposition(MInit);

		// Et on récupère chaque matrices
		U = SVD.getU();
		S = SVD.getS();
		V = SVD.getV().transpose();

		// On prévient que l'on a bien fait la décomposition
		phase = 2;
	}

	/**
	 * Applique la SVD à la matrice déjà initialisée (uniquement si ce n'est pas
	 * déjà fait), puis créée la nouvelle matrice selon k. Cette étape est donc
	 * longue uniquement au premier lancement.
	 * 
	 * @param k le nombre de valeur singulière à garder (mettez 0 pour utiliser
	 *            une valeur par défaut)
	 */
	public static void decomposer(int k) {
		// On décompose la matrice si ce n'est pas déjà fait
		if (phase != 2) {
			decomposer();
		}

		// Dimention minimum de S
		int min = Math.min(MInit.getColumnDimension(), MInit.getRowDimension());

		// Vérification des erreurs
		if (k > min) {
			System.out.println("K est trop grand");
			return;
		} else if (k == 0)
			k = 10; // Met la valeur par défaut

		// Multiplication (optimisée) U*S
		MFinal = new Matrix(U.getRowDimension(), k);
		for (int i = 0; i < MFinal.getRowDimension(); i++) {
			for (int j = 0; j < k; j++) {
				MFinal.set(i, j, U.get(i, j) * S.get(j, j));
			}
		}

		// Multiplication (U*S)*V
		Matrix VTemp = new Matrix(k, V.getColumnDimension());
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < V.getColumnDimension(); j++) {
				VTemp.set(i, j, V.get(i, j));
			}
		}
		MFinal = MFinal.times(VTemp);
	}

	/**
	 * Renvoie les prédictions de notes une fois la SVD appliquée
	 * 
	 * @param i le numéro du client
	 * @param j le numéro du restaurant
	 * @return la note prédite (double à arrondir)
	 */
	public static double get(int i, int j) {
		if (phase != 2) {
			System.out.println("U, S et V ne sont pas initialisés");
			return 0;
		}

		return MFinal.get(i, j);
	}
	
	public static double[][] matrix() {
		return MFinal.getArray();
	}
}
