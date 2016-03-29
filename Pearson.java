

public class Pearson {
	private static double[] coefClient, coefRestau;
	private static double[][] train;
	
	private static double similarite_restaurant(double[] tab, int client) {
        double s1 = 0.0;
        double s2 = 0.0;
        double s3 = 0.0;

        for (int i = 0; i < tab.length; i++) {

        	double moy = Moyenne.moy_total;
        	
            if (tab[i] != 0) {
                double val = Moyenne.moy_restau[i] - moy;
                s1 += val * (tab[i] - Moyenne.moy_client[client]);
                s2 += Math.pow(val, 2);
                s3 += Math.pow(tab[i] - Moyenne.moy_client[client], 2);
            }
        }

        if (s1 == 0 || s2 == 0 || s3 == 0) {
            return 0;
        }
        
        return s1 / Math.sqrt(s2 * s3);
    }
	
	private static double vote_pondere_restaurant(int client, int restau) {
        double vp = 0.0;
        double s1 = 0.0;
        double s2 = 0.0;

        double x = coefClient[client];
 
        if (x != 0) {
            s1 += x * (train[client][restau] - Moyenne.moy_client[client]);
            s2 += Math.abs(x);
        }

        vp = train[client][restau];
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
    

	private static double similarite_client(double[] tab, int restaurant) {
        double s1 = 0.0;
        double s2 = 0.0;
        double s3 = 0.0;

        for (int i = 0; i < tab.length; i++) {

        	double moy = Moyenne.moy_total;
        	
            if (tab[i] != 0) {
                double val = Moyenne.moy_client[i] - moy;
                s1 += val * (tab[i] - Moyenne.moy_restau[restaurant]);
                s2 += Math.pow(val, 2);
                s3 += Math.pow(tab[i] - Moyenne.moy_restau[restaurant], 2);
            }
        }

        if (s1 == 0 || s2 == 0 || s3 == 0) {
            return 0;
        }
        
        return s1 / Math.sqrt(s2 * s3);
    }
	
    private static double vote_pondere_client(int client, int restau) {
        double vp = 0.0;
        double s1 = 0.0;
        double s2 = 0.0;

        double x = coefRestau[restau];
 
        if (x != 0) {
            s1 += x * (train[client][restau] - Moyenne.moy_restau[restau]);
            s2 += Math.abs(x);
        }
        
        vp = train[client][restau];
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
		MoyenneRegularise.initialiser(base, 14, 8);
		train = MoyenneRegularise.matrix();
    	
    	
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
            
            if(nb>140)
            	coefClient[i] = similarite_restaurant(tab, i);
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
    		
            if(nb>250)
            	coefRestau[i] = similarite_client(tab, i);
    	}
    }
    
    public static double get(int i, int j){
    	return (vote_pondere_client(i, j) + vote_pondere_restaurant(i, j))/2;
    }
    
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
