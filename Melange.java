import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Classe qui exploitera chacun des algorithmes que nous avons implémentés. Elle
 * contient donc le "Main" principal qui sera executé pour avoir le résultat
 * final.
 */
public class Melange {

	private static int h, l;

	/**
	 * Fonction pour faire la copie d'un tableau
	 * 
	 * @param m la matrice qu'il faut borner
	 * @return une nouvelle matrice bornée
	 */
	private static double[][] copie(double[][] m) {
		double[][] mNew = new double[h][l];

		// Parcourt de toute la matrice
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < l; j++) {
				mNew[i][j] = m[i][j];
			}
		}

		return mNew;
	}

	/**
	 * Replace les valeurs d'entrainement dans la matrice
	 * 
	 * @param m la matrice de base
	 * @param l la liste des valeurs d'entrainement
	 */
	private static void reRemplissage(double[][] m, List<int[]> l) {
		for (int[] i : l) {
			m[i[0]][i[1]] = i[2];
		}
	}

	/**
	 * Fonction pour décider des valeurs les plus fiables de la matrice. Pour
	 * cela on supposera que plus nbNoteClient * nbNoteRestaurant est grand,
	 * plus la note prédite sera probablement juste
	 * 
	 * @param m notre matrice d'apprentissage
	 * @param min le nombre de valeurs minimum à classer
	 * @return une matrice dont chaque case représente si c'est une case sûr ou
	 *         non (0 si très faible, puis 1 pour la plus sûr, puis 2 etc...)
	 */
	private static int[][] validite(int[][] m, int min) {
		int[][] f = new int[h][l]; // matrice des nbNoteClient *
									// nbNoteRestaurant
		int[] nbC = new int[h];
		int[] nbR = new int[l];

		// Calcul du nombre de notes de chaque client
		for (int i = 0; i < h; i++) {
			int nb = 0;
			for (int j = 0; j < l; j++) {
				if (m[i][j] != 0)
					nb++;
			}
			nbC[i] = nb;
		}

		// Calcul du nombre de notes de chaque restaurant
		for (int i = 0; i < l; i++) {
			int nb = 0;
			for (int j = 0; j < h; j++) {
				if (m[j][i] != 0)
					nb++;
			}
			nbR[i] = nb;
		}

		// Multiplication des deux
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < l; j++) {
				f[i][j] = nbC[i] * nbR[j];
			}
		}

		// Remise à 0 quand une note est présente à cet emplacement dans
		// l'ensemble d'entrainement
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < l; j++) {
				if (m[i][j] != 0) {
					f[i][j] = 0;
				}
			}
		}

		// Matrice finale à renvoyer
		int[][] fiable = new int[h][l];

		int n = 0; // Nombre de cases remplis
		int rang = 1; // Rang de fiabilitée

		// Rangement par ordre croissant de fiabilité
		while (n < min) {

			// On recherche la valeur maximum de f[][]
			int max = 0;

			for (int i = 0; i < h; i++) {
				for (int j = 0; j < l; j++) {
					if (max < f[i][j])
						max = f[i][j];
				}
			}

			// On note toute les valeurs ==max dans fiable
			// Et on les enlève de f
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < l; j++) {
					if (max == f[i][j]) {
						n++;
						f[i][j] = 0;
						fiable[i][j] = rang;
					}
				}
			}
			rang++;
		}

		return fiable;
	}

	private static void image(double[][] tab, String nom){
		try {
		    BufferedImage bi = new BufferedImage(h, l, BufferedImage.TYPE_BYTE_GRAY);
		    WritableRaster raster = bi.getRaster();
	
		    for(int i=0; i<h; i++)
		        for(int j=0; j<l; j++)
		            raster.setSample(i,j,0,tab[i][j]*36);

			ImageIO.write(bi, "png", new File( nom + " non arrondi.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	private static double[][] remplissageFiable(double[][] mat, int[][] base, int[][] fiabilite, int nb){
		double[][] fiable = new double[h][l];
		
		for(int i=0;i<h;i++){
			for(int j=0;j<l;j++){
				if(base[i][j]!=0){
					fiable[i][j] = base[i][j];
				}
			}
		}
		
		int nbTot = 0;
		int rang = 1;
		boolean ok = true;
		
		while(ok){
			for(int i=0;i<h;i++){
				for(int j=0;j<l;j++){
					if(nbTot>nb){
						ok = false;
						break;
					}
					
					if(rang==fiabilite[i][j]){
						fiable[i][j] = mat[i][j];
						nbTot++;
					}
				}
				
				if(!ok)	break;
			}
			rang++;
		}
		
		return fiable;
	}
	*/

	public static void main(String[] args) {

		System.out.print("Chargement des données d'entrainement...");

		// Chargement des données d'entrainement
		int[][] entrainement = CsvFile.chargeTrain("train.csv");
		List<int[]> donneeValides = CsvFile.chargeTrainList("train.csv");

		// Sauvegarde des dimentions de la matrice
		h = entrainement.length;
		l = entrainement[0].length;

		System.out.println("Fait");


		System.out.print("Calcul des valeurs les plus fiables...");

		// Tableau de fiabilitée (plus la valeur est proche de 1, plus la
		// prédiction est juste)
		//int nbFiable = 38000;
		//int[][] fiabilite = validite(entrainement, nbFiable);

		System.out.println("Fait");


		/**************** Initialisation des Algorithmes *****************/

		
		System.out.println("Lancement des différents algorythmes :");


		System.out.print("Moyenne...");

		Moyenne.initialiser(entrainement);

		double[][] resultatMoyenne = Moyenne.matrix();

		System.out.println("Fait");


		System.out.print("Pearson...");

		Pearson.initialiser(entrainement);

		double[][] resultatPearson = Pearson.matrix();

		System.out.println("Fait");


		System.out.print("MoyenneRegularise...");

		MoyenneRegularise.initialiser(entrainement, 14, 8);

		double[][] resultatMoyenneRegularise = MoyenneRegularise.matrix();

		System.out.println("Fait");


		System.out.print("SVD...");
		// Création de la matrice pour la SVD
		// On reprend la matrice (que l'on a borné)
		double[][] entrainementSVD = copie(resultatMoyenneRegularise);
		// On y remet les valeurs de l'ensemble d'entrainement
		reRemplissage(entrainementSVD, donneeValides);
		// Et on lance l'algo
		SVD.initialiser(entrainementSVD);

		SVD.decomposer(3);

		System.out.println("Fait");


		System.out.print("SGD...");

		double[][] entrainementSGD = new double[h][l];
		reRemplissage(entrainementSGD, donneeValides);

		// Remplissage random car la matrice est très vide
		for (int a = 0; a < 15000; a++) {
			int i = (int) (Math.random() * h);
			int j = (int) (Math.random() * l);

			if (entrainementSGD[i][j] == 0) {
				entrainementSGD[i][j] = resultatPearson[i][j];
			} else {
				a--;
			}
		}

		SGD.facteurs(0.0002, 0.1, 20, 40, 300);

		double[][] resultatSGD = SGD.lance(entrainementSGD);

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
				matriceFinale[i][j] = (1 * resultatMoyenne[i][j] + 2 * resultatPearson[i][j] + 2 * resultatMoyenneRegularise[i][j] + 4 * SVD.get(i, j) + 4 * resultatSGD[i][j]) / 13;
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
		PredictFile.zip("Melange 12244 pers -40 b.zip");

		System.out.println("Travail accomplit.");
	}
}
