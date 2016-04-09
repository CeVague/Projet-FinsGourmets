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
	 * Fonction pour éviter que les valeurs sortent des bornes [0.5;5.5]
	 * 
	 * @param m la matrice à borner
	 */
	private static void borne(double[][] m) {
		// Parcourt de toute la matrice
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < l; j++) {
				if(m[i][j]>=5.5){
					m[i][j] = 5.4;
				}else if(m[i][j]<0.5){
					m[i][j] = 0.5;
				}
			}
		}
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
	 * Crée une image à partire de la matrice d'entrée
	 * 
	 * @param tab la matrice (avec des valeurs entre 0 et 6 inclus)
	 * @param nom le nom du fichier de sortie sans extension
	 */
	private static void image(double[][] tab, String nom){
		try {
		    BufferedImage bi = new BufferedImage(h, l, BufferedImage.TYPE_BYTE_GRAY);
		    WritableRaster raster = bi.getRaster();
	
		    for(int i=0; i<h; i++)
		        for(int j=0; j<l; j++)
		            raster.setSample(i,j,0,tab[i][j]*36);
            		//raster.setSample(i,j,0,Math.round(tab[i][j])*36);

			ImageIO.write(bi, "png", new File( nom + " non arrondi.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Crée une image à partire de la matrice d'entrée
	 * 
	 * @param tab la matrice (avec des valeurs entre 0 et 6 inclus)
	 * @param nom le nom du fichier de sortie sans extension
	 */
	@SuppressWarnings("unused")
	private static void image(int[][] tab, String nom){
		double[][] t = new double[h][l];
	
	    for(int i=0; i<h; i++)
	        for(int j=0; j<l; j++)
	            t[i][j] = tab[i][j];
	    
	    image(t, nom);

	}

	public static void main(String[] args) {

		System.out.print("Chargement des données d'entrainement...");

		// Chargement des données d'entrainement
		int[][] entrainement = CsvFile.chargeTrain("train.csv");
		List<int[]> donneeValides = CsvFile.chargeTrainList("train.csv");

		// Sauvegarde des dimentions de la matrice
		h = entrainement.length;
		l = entrainement[0].length;

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

		MoyenneRegularise.initialiser(entrainement, 13, 8);

		double[][] resultatMoyenneRegularise = MoyenneRegularise.matrix();

		System.out.println("Fait");


		System.out.print("SVD...");
		// Création de la matrice pour la SVD
		// On reprend la matrice de Pearson (que l'on a borné)
		double[][] entrainementSVD = copie(resultatPearson);
		// On y remet les valeurs de l'ensemble d'entrainement
		reRemplissage(entrainementSVD, donneeValides);
		// Et on lance l'algo
		SVD.initialiser(entrainementSVD);

		SVD.decomposer(3);

		System.out.println("Fait");


		System.out.print("SGD...");

		double[][] entrainementSGD = new double[h][l];
		
		reRemplissage(entrainementSGD, donneeValides);

		SGD.facteurs(0.0002, 0.1, 20, 48, 350);

		double[][] resultatSGD = SGD.lance(entrainementSGD);
		
		borne(resultatSGD);

		System.out.println("Fait");


		System.out.println("Tous les algorythmes ont été initialisés.");


		/********************* Prédiction des notes **********************/

		
		System.out.print("Création matrice finale");

		// Matrice de combinaison finale des prédictions
		double[][] matriceFinale = new double[h][l];

		for (int i = 0; i < h; i++) {
			// Pondération de chaque prédictions de chaque algorithme
			for (int j = 0; j < l; j++) {
				matriceFinale[i][j] = (1 * resultatMoyenne[i][j] + 2 * resultatPearson[i][j] +
						2 * resultatMoyenneRegularise[i][j] + 3 * SVD.get(i, j) + 4 * resultatSGD[i][j]) / 12;
			}
		}
		
		System.out.println("Fait");

		
		/**************** Enregistrement des prédictions *****************/

		
		System.out.println("Création des fichiers de sortie :");

		// Maintenant que l'on a notre matrice de prédictions, on va noter
		// toutes les données demandées dans dev.csv et test.csv
		for (String nom : new String[] { "dev", "test" }) {
			// Chargement de la liste des emplacements des notes à prédire
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
		PredictFile.zip("Melange clone Final.zip");

		System.out.println("Travail accomplit.");
	}
}
