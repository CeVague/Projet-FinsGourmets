
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Petite classe pour faire des test sur nos algos en utilisant une partie de
 * l'ensemble d'entrainement. Ca évite de toujours tout envoyer en ligne,
 * surtout pour les ajustements d'algos
 * 
 * @author Cédric
 */
public class ValidationCroisee {

	private final int[][] train_data;
	private final List<int[]> listeTrainTotal;
	private final int tailleDeK;

	private int k;
	private List<int[]> listeEvalTemp;
	private List<Integer> listeNotes;

	/*
	 * Initialisation de l'objet.
	 * 
	 * @param k en combien de morceau il faut découper l'ensemble de départ
	 * @param chemin le chemin vers le fichier CSV d'entrainement
	 */
	public ValidationCroisee(int k, String chemin) {
		// On charge la matrice de base
		this.train_data = CsvFile.chargeTrain(chemin);

		// On charge la liste de valeur
		this.listeTrainTotal = CsvFile.chargeTrainList(chemin);

		// Mélange les valeurs
		Collections.shuffle(listeTrainTotal);

		// On initialise le nombre de découpe et l'emplacement actuel
		this.tailleDeK = listeTrainTotal.size() / k;
		this.k = 0;

		// Initialise la liste de notes prédites
		listeNotes = new ArrayList<Integer>();
	}

	/*
	 * Prévient que vous passez au morceau de donnée suivant
	 */
	public void next() {
		this.k++;
	}

	/*
	 * Renvoie les données d'entrainement sans la partie qui servira aux test
	 * sous forme d'une matrice
	 */
	public int[][] trainData() {
		int[][] retour = new int[train_data.length][train_data[0].length];

		for (int i = 0; i < train_data.length; i++) {
			for (int j = 0; j < train_data[0].length; j++) {
				if (train_data[i][j] != 0) {
					retour[i][j] = train_data[i][j];
				}
			}
		}

		for (int i = (k * tailleDeK); i < ((k + 1) * tailleDeK); i++) {
			int[] temp = listeTrainTotal.get(i);
			retour[temp[0]][temp[1]] = 0;
		}

		return retour;
	}

	/*
	 * Renvoie la liste des couples dont il faudra prédire les notes
	 */
	public List<int[]> trainEvaluation() {
		listeEvalTemp = new ArrayList<int[]>();

		for (int i = (k * tailleDeK); i < ((k + 1) * tailleDeK); i++) {
			int[] temp = listeTrainTotal.get(i);
			listeEvalTemp.add(new int[] { temp[0], temp[1] });
		}

		return listeEvalTemp;
	}

	/*
	 * Pour envoyer la note prédite à l'objet (à renvoyer dans le même ordre que
	 * la liste de trainEvaluation
	 */
	public void add(int note) {
		listeNotes.add(note);
	}

	/*
	 * Renvoie le score RMSE obtenue selon les notes prédites
	 */
	public double trainScore() {
		double resultat = 0;

		for (int i = 0; i < listeNotes.size(); i++) {
			int[] temp = listeEvalTemp.get(i);
			resultat += Math.pow(listeNotes.get(i) - train_data[temp[0]][temp[1]], 2);
		}

		resultat = resultat / listeNotes.size();

		listeNotes = new ArrayList<Integer>();

		return Math.sqrt(resultat);
	}

}
