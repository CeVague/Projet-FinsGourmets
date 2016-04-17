
public class Pearson {
	private static double[] coefClient, coefRestau;
	private static double[][] train;
    
	/**
	 * Calcul la similaritée (coef de Pearson) d'un client ou d'un restaurant
	 * 
	 * @param tab le tableau d'entrainement
	 * @param id le numéro du client ou du restaurant concernée
	 * @param qui si c'est l'id d'une "client" ou d'un "restaurant"
	 * @return le coef
	 */
	private static double similarite(double[] tab, int id, String qui) {
        double s1 = 0.0;
        double s2 = 0.0;
        double s3 = 0.0;

        double moyenne_qui;
        double[] moyenne;
        
        if(qui == "client"){
        	moyenne = Moyenne.moy_client;
        	moyenne_qui = Moyenne.moy_restau[id];
        }else {
        	moyenne = Moyenne.moy_restau;
        	moyenne_qui = Moyenne.moy_client[id];
        }

    	double moy = Moyenne.moy_total;
        for (int i = 0; i < tab.length; i++) {
        	
            if (tab[i] != 0) {
                double val = moyenne[i] - moy;
                s1 += val * (tab[i] - moyenne_qui);
                s2 += Math.pow(val, 2);
                s3 += Math.pow(tab[i] - moyenne_qui, 2);
            }
        }

        if (s1 == 0 || s2 == 0 || s3 == 0) {
            return 0;
        }
        
        return s1 / Math.sqrt(s2 * s3);
    }
	
	/**
	 * Modifie la note moyenne selon le coef de Pearson
	 * 
	 * @param client le numéro du client
	 * @param restau le numéro du restaurant
	 * @param qui si on cherche la moyenne client ou restaurant
	 * @return la nouvelle moyenne
	 */
	private static double vote_pondere(int client, int restau, String qui) {
        double coef, moy;
        
        if(qui == "client"){
            coef = coefRestau[restau];
            moy = Moyenne.moy_restau[restau];
        }else {
            coef = coefClient[client];
            moy = Moyenne.moy_client[client];
        }
        
        double s1 = 0.0;
        double s2 = 0.0;
        
        if (coef != 0) {
            s1 += coef * (train[client][restau] - moy);
            s2 += Math.abs(coef);
        }

        double vp = train[client][restau];
        
        if(s2!=0){
            vp += s1 / s2;
        }
        
        if(vp>5){
            return 5;
        }else if(vp<1){
            return 1;
        }
        
        return vp;
    }
    
    
    public static void initialiser(int[][] base){
    	// Initialisation de la moyenne
		Moyenne.initialiser(base);
		MoyenneRegularisee.initialiser(base, 13, 8);
		train = MoyenneRegularisee.matrix();
    	
    	
		// Sauvegarde des dimentions de la matrice
		int h = base.length;
		int l = base[0].length;
		
		for(int i=0;i<h;i++){
			for(int j=0;j<l;j++){
				if(base[i][j]!=0){
					train[i][j] = base[i][j];
				}
			}
		}
		
    	coefClient = new double[h];
    	
    	for(int i=0;i<h;i++){
    		double[] tab = new double[l];
            int nb = 0;
            
            for (int j=0;j<l;j++){
            	if(base[i][j]!=0){
            		tab[j] = base[i][j];
            		nb++;
            	}
            }
            
            if(nb>120)
            	coefClient[i] = similarite(tab, i, "restaurant");
    	}
    	

    	coefRestau = new double[l];
    	
    	for(int i=0;i<l;i++){
        	double[] tab = new double[h];
            int nb = 0;
            
            for (int j=0;j<h;j++){
            	if(base[j][i]!=0){
            		tab[j] = base[j][i];
            		nb++;
            	}
            }
    		
            if(nb>225)
            	coefRestau[i] = similarite(tab, i, "client");
    	}
    }
    
    /**
     * Pour récupérer une prédiction (après initialisation)
     * 
     * @param i
     * @param j
     * @return la valeur prédite
     */
    public static double get(int i, int j){
    	return (vote_pondere(i, j, "client") + vote_pondere(i, j, "restaurant"))/2;
    }
    
    /**
     * Pour récupérer la matrice de prédiction
     * 
     * @return la matrice en question
     */
    public static double[][] matrix(){
    	double[][] plein = new double[train.length][train[0].length];
    	
    	for(int i=0;i<plein.length;i++){
        	for (int j=0;j<plein[0].length;j++){
        		plein[i][j] = get(i, j);
            }
    	}
    	
    	return plein;
    }
}
