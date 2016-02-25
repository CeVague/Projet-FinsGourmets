
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvFile {
	public final static String SEPARATOR = ",";
	public final static int nb_restau = 1751;
	public final static int nb_clients = 3965;

	
	/**
	 * Systheme de lecture ligne par ligne de fichiers pour en retourner la
	 * liste.
	 * 
	 * @param file le fichier concern�
	 * @return une List<String> contenant chaque ligne du fichier (sauf la
	 *         premi�re) dans l'ordre.
	 */
	private static List<String> readFileLines(File file) {
		List<String> result = new ArrayList<String>();
		
		try{
	
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
	
			br.readLine(); // Pour sauter juste la 1�re ligne ("user,restaurants,stars")
	
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				result.add(line);
			}
	
			br.close();
			fr.close();
		}catch (Exception e) {
			System.out.println("Erreur lors de l'ouverture du fichier.");
		}
		
		return result;
	}

	
	/**
	 * Fonction de chargement des fichiers d'entrainements (client, restaurant, note)
	 * en un tableau de notes.
	 * 
	 * Pas de note = 0.
	 * 
	 * @param chemin le chemin vers le fichier
	 * 			(ou son nom si il est � la racine du projet java)
	 * @return un int[][] o� le premier param�tre est le num client et le
	 * 			deuxieme est le num du restaurant.
	 * 			Seul la derni�re note du fichier est prise en compte
	 * 			(en cas de doublons).
	 */
	public static int[][] chargeTrain(String chemin) {
		// tab repr�sente notre matrice, les clients en lignes et les notes par restaurants en colonnes
		// Elle est initialis�e par d�faut avec des zeros partout
		int[][] tab = new int[nb_clients][nb_restau];

		// Dans ce tableau on va stocker le num client , restau et la note mais en String
		String[] colonnes_split = new String[3];
		// Dans ce tableau on va stocker le num client , restau et la note mais en Entier
		int[] colonnes = new int[3];
		// Dans cette liste on va stocker toutes les lignes que nous allons lire � partir de notre Csv
		File myfile = new File(chemin);
		List<String> lignes = readFileLines(myfile);
		
		// On parcourt les lignes qu'on r�cup�re une par une
		for (String ligne : lignes) {
			// On fait un split pour r�cuperer chaque valeur � part (user, restau, note)
			colonnes_split = ligne.split(SEPARATOR);
			// On convertit les valeurs qu'on eu gr�ce en entiers
			for (int k = 0; k <= 2; k++) {
				colonnes[k] = Integer.parseInt(colonnes_split[k]);
			}
			// System.out.println(colonnes[1]);
			// Remplissage de la matrice
			// Si la ligne qu'on r�cup�r�e est "3,0,5"
			// Gr�ce au split elle stock� dans colonnes_split {"3","0","5"}
			// Gr�ce � la conversion , elle est stock� dans colonnes {3,0,5}
			// donc on fait cette affecttaion tab[3][0]=5;
			tab[colonnes[0]][colonnes[1]] = colonnes[2];
		}

		return tab;
	}
	
	
	/**
	 * Fonction de chargement des fichier de test (dev et test) (client, restaurant, ?)
	 * 
	 * @param chemin le chemin vers le fichier
	 * 			(ou son nom si il est � la racine du projet java)
	 * @return une List<int[]> donc chaque element est un couple
	 * 			(client, restaurant).
	 */
	public static List<int[]> chargeTest(String chemin) {
		// Dans cette liste on va stocker toutes les lignes que nous allons lire � partir de notre Csv
		List<String> lignes = readFileLines(new File(chemin));
	
		// Initialisation de la liste de retour
		List<int[]> liste = new ArrayList<int[]>();

		// Dans ce tableau on va stocker le couple (num client, restau) en int
		int[] couple = new int[2];
		
		// On r�cup�re les lignes une par une
		for (String ligne : lignes) {
			// On fait un split pour r�cuperer chaque valeur � part (user, restau, ?)
			String[] temp = ligne.split(SEPARATOR);
			// On convertit les valeurs qu'on eu gr�ce en entiers
			for (int k = 0; k < 2; k++) {
				couple[k] = Integer.parseInt(temp[k]);
			}
			// Et on ajoute ce couple � la liste
			liste.add(couple.clone());
		}

		return liste;
	}

	// Pour afficher trois collones de la matrice
	public static void affiche_mat(int[][] tab) {
		for (int i = 0; i < tab.length; i++) {
			System.out.println(tab[i][0] + "," + tab[i][1] + "," + tab[i][2]);

		}
	}

	public static void main(String[] args) {
		int[][] tab = chargeTrain("train.csv");
		System.out.println("Hello!");
		affiche_mat(tab);
	}
}
