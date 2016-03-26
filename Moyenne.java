

public class Moyenne {
	
	//public final static int nb_restau = 1751;
    //public final static int nb_clients = 3965;
	
	/**
	Fonction qui calcule la note moyenne qu'a obtenu un restaurant
	@param restau l'indice indentifiant le restaurant dont on souhaite
	calculer la moyenne, mat une matrice de donn�es fournis par chargeTrain
	de la classe CsvFile.
	@return Retourne la moyenne, et 0 si que des 0.
	**/
	public static double moyenne_restau(int restau, int[][] mat) {
		double count = 0;
		double sum = 0;
		
		for (int i=0; i < mat.length; i++) {
			if (mat[i][restau] != 0) {
				count++;
				sum += mat[i][restau];
			}
		}
		
		if (count !=0 ) {
			return sum/count;
		}
		
		return 0;
	}
	
	/**
	Fonction qui calcule la moyenne des notes qu'a donn� un client
	@param client l'indice identifiant le client, mat une matrice de 
	donn�es fournis par chargeTrain de la classe CsvFile.
	@return La moyenne.
	**/
	public static double moyenne_client(int client, int[][] mat) {
		double count = 0;
		double sum = 0;
		
		for (int j=0; j < mat[0].length; j++) {
			if (mat[client][j] != 0) {
				count++;
				sum += mat[client][j];
			}
		}
	
		if (count !=0 ) {
			return sum/count;
		}
		
		return 0;
	}
	
	/**
	 * Fonction qui complete une matrice creuse en remplacant les 0 par 
	 * la moyenne de la moyenne des notes recu par le restaurant et donn�e
	 * par le client
	 * @param mat une matrice fournis par chargement des donn�es en utilisant
	 * la classe CsvFile
	 * @return Retourne une matrice remplie
	 * **/
	public static int[][] completeMatrix(int[][] mat) {
		for (int i=0; i < mat.length; i++) {
			double moyClient = moyenne_client(i, mat);
			
			for (int j=0; j < mat[i].length; j++) {
				if (mat[i][j]==0) {
					//System.out.println("["+i+"]"+"["+j+"] remplis");
						
					double moyRestau = moyenne_restau(j, mat);
					mat[i][j] =(int) Math.round(( moyClient + moyRestau ) /2);
				}
			}
			
			if(i%40==0)
				System.out.println( (i*100)/mat.length+" % complete");
		}
		
		return mat;
	}
	
	public static double get(int i, int j, int[][] mat){
		return ( moyenne_client(i, mat) + moyenne_restau(j, mat) ) /2;
	}
	
	public static void main(String[] args) {
		
		int[][] mat = CsvFile.chargeTrain("train.csv");
		int[][] finalMat = completeMatrix(mat);
		
		System.out.print(finalMat[125][15]);
		
		
	}
	
	
}