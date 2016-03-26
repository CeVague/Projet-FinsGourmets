import java.util.ArrayList;
import java.util.List;

public class SGD {

	public static double[][] U, V;
	public static int h, l, factK = 11, nb = 10, iterations = 310;
	private static double alpha = 0.0002, beta = 0.1;

	public static double multSimple(int i, int j) {
		double somme = 0.0;

		for (int K = 0; K < factK; K++) {
			somme += U[i][K] * V[K][j];
		}

		return somme;
	}

	public static double[][] multTout() {
		double[][] matrice = new double[h][l];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < l; j++) {
				double temp = 0.0;
				for (int K = 0; K < factK; K++) {
					temp += U[i][K] * V[K][j];
				}
				matrice[i][j] = temp;
			}
		}

		return matrice;
	}
        
	public static void facteurs(double a, double b, int k, int n, int i){
		alpha = a;
		beta = b;
		factK = k;
		nb = n;
		iterations = i;
	}
	
	public static double[][] factorisation(List<int[]> M,int H, int L) {
		h = H;
		l = L;
		
		U = new double[h][factK];
		V = new double[factK][l];
		for (int K = 0; K < factK; K++) {
            for (int i = 0; i < h; i++) {
            	U[i][K] = Math.random();
            }
            for (int i = 0; i < l; i++) {
                V[K][i] = Math.random();
            }
		}


		for(int rien=0;rien<iterations;rien++) {
			for(int[] d : M){
				int i = d[0];
				int j = d[1];
				double eij = d[2] - multSimple(i, j);
				for (int K = 0; K < factK; K++) {
					U[i][K] += alpha * (2 * eij * V[K][j] - beta * U[i][K]);
					V[K][j] += alpha * (2 * eij * U[i][K] - beta * V[K][j]);
				}
			}
			
			//if(rien%50==0) System.out.print(".");
		}
		
		//System.out.println("U et V ont étés trouvés");
		
		//System.out.print(".");
		
		return multTout();
	}
	
	
	public static double[][] lance(int[][] table){

		// Initialise la table de train
		List<int[]> train = new ArrayList<int[]>();
		
		for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j]!=0) {
                    train.add(new int[]{i, j, table[i][j]});
                }
            }
        }
		
		// Matrice final
		double[][] combine = new double[table.length][table[0].length];
		
		// Moyenne de plusieurs générations
		for(int rien=0;rien<nb;rien++){
			double[][] temp = factorisation(train, table.length, table[0].length);
			
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < l; j++) {
                	combine[i][j] += temp[i][j];
                }
            }
		}
		
		// Rien ne dépassera (et moyenne)
		for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++){
            	combine[i][j] /= nb;
            	
                if(combine[i][j]>5) {
                    combine[i][j] = 5;
                }else if(combine[i][j]<1) {
                    combine[i][j] = 1;
                }
            }
        }
		
		return combine;
	}
	
	public static void main(String[] args) {
		
		/*
		 * factK = 11 et nb=10 marche bien et est rapide
		 * factK = 20 et nb=20 résultats maximum
		 */
		facteurs(alpha, beta, 20, 20, 300);

		double[][] combine = lance(CsvFile.chargeTrain("train.csv"));
		
		
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
				sortie.add(combine[couple[0]][couple[1]]);
			}

			// Fermeture du .predict
			sortie.close();
			
			System.out.println("- " + nom + ".predict fait");
		}

		// Zippage des deux fichiers
		PredictFile.zip("SGD.zip");

		System.out.println("Travail accomplit.");
		
	}

}