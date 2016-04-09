import java.util.ArrayList;
import java.util.List;

public class SGD {

	private static double[][] U, V;
	private static int h, l, factK = 11, nb = 10, iterations = 310;
	private static double alpha = 0.0002, beta = 0.1;

	private static double multSimple(int i, int j) {
		double somme = 0.0;

		for (int K = 0; K < factK; K++) {
			somme += U[i][K] * V[K][j];
		}

		return somme;
	}

	private static double[][] multTout() {
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
    
	/**
	 * Pour initialiser les différents facteurs de cet algorythme
	 * @param a le taux d'apprentissage
	 * @param b un autre taux évitant le sur apprentissage
	 * @param k la taille k des matrices U et V
	 * @param n le nombre de prédictions à moyenner
	 * @param i le nombre d'itération du calcul de gradient
	 */
	public static void facteurs(double a, double b, int k, int n, int i){
		alpha = a;
		beta = b;
		factK = k;
		nb = n;
		iterations = i;
	}
	
	private static double[][] factorisation(List<double[]> M, int H, int L) {
		h = H;
		l = L;
		
		// Initialisation random de U et V
		U = new double[h][factK];
		V = new double[factK][l];
		for (int K = 0; K < factK; K++) {
            for (int i = 0; i < h; i++) {
            	U[i][K] = (Math.random() * 0.7) + 0.1;
            }
            for (int i = 0; i < l; i++) {
                V[K][i] = (Math.random() * 0.7) + 0.1;
            }
		}

		// On applique la SGD autant de fois que demandé
		for(int rien=0;rien<iterations;rien++) {
			// On ne calcul les erreurs que aux endroits où on connait les notes
			for(double[] d : M){
				int i = (int) d[0];
				int j = (int) d[1];
				double eij = d[2] - multSimple(i, j);
				for (int K = 0; K < factK; K++) {
					U[i][K] += alpha * (2 * eij * V[K][j] - beta * U[i][K]);
					V[K][j] += alpha * (2 * eij * U[i][K] - beta * V[K][j]);
				}
			}
		}
		
		return multTout();
	}
	
	/**
	 * Lancement de la SGD
	 * @param table la matrice creuse
	 * @return une matrice pleine
	 */
	public static double[][] lance(int[][] table){
		double[][] d = new double[table.length][table[0].length];
		
		for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++){
            	d[i][j] = table[i][j];
            }
        }
		
		return lance(d);
	}
	
	/**
	 * Lancement de la SGD
	 * @param table la matrice creuse
	 * @return une matrice pleine
	 */
	public static double[][] lance(double[][] table){

		// Initialise la table de train
		List<double[]> train = new ArrayList<double[]>();
		
		for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j]!=0) {
                    train.add(new double[]{i, j, table[i][j]});
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
            	
                if(combine[i][j]>=5.5) {
                    combine[i][j] = 5.4;
                }else if(combine[i][j]<0.5) {
                    combine[i][j] = 0.5;
                }
            }
        }
		
		return combine;
	}

}
