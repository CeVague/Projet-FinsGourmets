import java.util.List;

/**
 * Classe qui exploitera chacun des algorithmes que nous avons implémentés. Elle
 * contient donc le "Main" principal qui sera executé pour avoir le résultat
 * final.
 */
public class Melange {
	private static int h, l;
	
	private static double[][] bornage(double[][] m){
		double[][] mNew = new double[h][l];
		for (int i = 0; i < h; i++){
			for (int j = 0; j < l; j++) {
				if (m[i][j] >= 5.5){
					mNew[i][j] = 5;
				}else if(m[i][j] < 0.5){
					mNew[i][j] = 1;
				}else{
					mNew[i][j] = m[i][j];
				}
			}
		}
		return mNew;
	}
	
	private static void reRemplissage(double[][] m, List<int[]> l){
		for(int[] i : l){
			m[i[0]][i[1]] = i[2];
		}
	}
	
	private static int[][] validite(int[][] m){
		int[][] f = new int[h][l];
		int[] nbC = new int[h];
		int[] nbR = new int[l];
		
		for(int i=0;i<h;i++){
			int nb = 0;
			for(int j=0;j<l;j++){
				if(m[i][j]!=0) nb++;
			}
			nbC[i] = nb;
		}
		
		for(int i=0;i<l;i++){
			int nb = 0;
			for(int j=0;j<h;j++){
				if(m[j][i]!=0) nb++;
			}
			nbR[i] = nb;
		}
		
		for(int i=0;i<h;i++){
			for(int j=0;j<l;j++){
				f[i][j] = nbC[i] * nbR[j];
			}
		}
		
		
		// Rangement par ordre croissant de fiabilité
		// avec 0 pour les notes de train
		int[][] fiable = new int[h][l];
		
		for(int i=0;i<h;i++){
			for(int j=0;j<l;j++){
				if(m[i][j]!=0){
					f[i][j] = 0;
				}
			}
		}
		
		int n = 0;
		int rang = 1;
		
		while(n<40000){
			int max = 0;
			
			for(int i=0;i<h;i++){
				for(int j=0;j<l;j++){
					if(max<f[i][j]) max=f[i][j];
				}
			}
			
			for(int i=0;i<h;i++){
				for(int j=0;j<l;j++){
					if(max==f[i][j]){
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
		
		// Tableau de valeur (plus elle est grande, plus la prédiction est juste)
		int[][] fiabilite = validite(entrainement);

		System.out.println("Fait");
		

		/**************** Initialisation des Algorithmes *****************/

		System.out.println("Lancement des différents algorythmes :");

		
		System.out.print("Moyenne...");
		
		Moyenne.initialisation(entrainement);
		
		double[][] resultatMoyenne = Moyenne.matrix();
		
		System.out.println("Fait");

		
		System.out.print("Pearson...");
		
		Pearson.initialisation(entrainement);
		
		double[][] resultatPearson = Pearson.matrix();
		
		System.out.println("Fait");
		
		
		System.out.print("SVD...");
		// Création de la matrice pour la SVD
		// On reprend la matrice (que l'on a borné)
		double[][] entrainementSVD = bornage(resultatPearson);
		// On y remet les valeurs de l'ensemble d'entrainement
		reRemplissage(entrainementSVD, donneeValides);
		// Et on lance l'algo
		SVD.initialiser(entrainementSVD);

		SVD.decomposer(12);

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

		double[][] entrainementSGD = remplissageFiable(resultatMoyenne, entrainement, fiabilite, 20000);


		// 300 -> 250 si au dessus décommenté
		SGD.facteurs(0.0002, 0.1, 20, 20, 250);
		
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
				matriceFinale[i][j] = (resultatMoyenne[i][j] + SVD.get(i, j) + resultatSGD[i][j])/3;
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
		PredictFile.zip("Melange SVD(pers,12)+Moy+SGD(Moy,20000).zip");

		System.out.println("Travail accomplit.");
	}
}
