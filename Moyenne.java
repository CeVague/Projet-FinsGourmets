

public class Moyenne {
	
	//public final static int nb_restau = 1751;
    //public final static int nb_clients = 3965;
	
	/**
	Fonction qui calcule la note moyenne qu'a obtenu un restaurant
	@param restau l'indice indentifiant le restaurant dont on souhaite
	calculer la moyenne, mat une matrice de données fournis par chargeTrain
	de la classe CsvFile.
	@return Retourne la moyenne, et 0 si que des 0.
	**/
	public static int moyenne_restau(int restau, int[][] mat) {
		int count = 0;
		int sum = 0;
		
		for (int i=0; i < mat.length; i++) {
			for (int j=0; j < mat[i].length; j++) {
				if (j == restau && mat[i][j] != 0) {
					count++;
					sum += mat[i][j];
				}
			}
		}		
		if (count !=0 ) {
			return sum/count;
		} else { return 0; }
	}
	
	/**
	Fonction qui calcule la moyenne des notes qu'a donné un certain 
	client
	@param client l'indice identifiant le client, mat une matrice de 
	données fournis par chargeTrain de la classe CsvFile.
	@return La moyenne.
	**/
	public static int moyenne_client(int client, int[][] mat) {
		int count = 0;
		int sum = 0;
		
		for (int i=0; i < mat.length; i++) {
			for (int j=0; j < mat[i].length; j++) {
				if (i == client && mat[i][j] != 0) {
					count++;
					sum += mat[i][j];
				}
			}
		}		
		if (count !=0 ) {
			return sum/count;
		} else { return 0; }
	}
	
	/**
	 * Fonction qui complete une matrice creuse en remplacant les 0 par 
	 * la moyenne de la moyenne des notes recu par le restaurant et donnée
	 * par le client
	 * @param mat une matrice fournis par chargement des données en utilisant
	 * la classe CsvFile
	 * @return Retourne une matrice remplie
	 * **/
	public static int[][] completeMatrix(int[][] mat) {
		for (int i=0; i < mat.length; i++) {
			for (int j=0; j < mat[i].length; j++) {
				if (mat[i][j]==0) {
					//System.out.println("["+i+"]"+"["+j+"] remplis");
						
					System.out.println( (i*100)/1751+" % complete");
					int moyClient = moyenne_client(i, mat);
					int moyRestau = moyenne_restau(j, mat);
					mat[i][j] = ( moyClient + moyRestau ) /2;
				}
			}
		}
		
		return mat;
	}
	
	public static void main(String[] args) {
		
		int[][] mat = CsvFile.chargeTrain("train.csv");
		int[][] finalMat = completeMatrix(mat);
		
		//System.out.print(mat[125][15]);
		
		
	}
	
	
}