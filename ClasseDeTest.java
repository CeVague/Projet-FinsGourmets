
import java.util.List;
import weka.core.matrix.*;

/*
    Juste une classe pour faire mes test
    @Autor Cédric
*/
public class ClasseDeTest {
    
    private final static int nb_restau = 1751;
    private final static int nb_clients = 3962;
    
    
    public static void main(String[] args){
        
        int[][] tabTemp = CsvFile.chargeTrain("train.csv");
        
        System.out.println("Fichier chargé...");
        
        double[][] tab = new double[tabTemp.length][tabTemp[0].length];
        
        for(int i=0;i<tabTemp.length;i++){
            for(int j=0;j<tabTemp[0].length;j++){
                tab[i][j] = tabTemp[i][j];
            }
        }
        
        System.out.println("Tableau converti...");
        
        Matrix mat = new Matrix(tab);
        // Matrix mat = new Matrix(new double[][]{{1,0,0,0,2},{0,0,3,0,0},{0,0,0,0,0},{0,4,0,0,0}});
        
        System.out.println("Matrix créée...");
        
        SingularValueDecomposition SVD = new SingularValueDecomposition(mat);
        
        System.out.println("SVD calculé...");
        
        Matrix U = SVD.getU();
        Matrix V = SVD.getS();
        Matrix S = SVD.getV().transpose();
        
        System.out.println("Sous matrices récupérés...");
        
        Matrix A = V.times(S);
        Matrix Fin = U.times(A);
        
        System.out.println("Fin de multiplication...");
        
        System.out.println(Fin.get(0, 0));
        System.out.println(Fin.get(1, 1));
        System.out.println(Fin.get(1989,1235));
        System.out.println(Fin.get(1558,59));
        
       
        
        /*
        int k = 10;
        
        ValidationCroisee test = new ValidationCroisee(k, "train.csv");
        
        double moyenne = 0;
        
        for(int tt=0;tt<k;tt++){
            // Chargement du fichier d'entrainement
            int[][] train_data = test.trainData();
            
            
            
            // Initialisation de la liste des moyennes obtenues pour chaque restaurants
            int[] moy_rest = new int[nb_restau];

            // Remplissage de la liste :
            // Pour chaque restaurant
            for (int i = 0; i < nb_restau; i++) {
                int total = 0;
                int compt = 0;

                // On additionne les notes de tous les clients qui en ont donnée
                for (int j = 0; j < nb_clients; j++) {
                    if (train_data[j][i] != 0) {
                        compt++;
                        total += train_data[j][i];
                    }
                }

                // On fait la moyenne (arrondi scientifique)
                if (compt > 0) {
                    total = (total + (compt / 2)) / compt;
                }

                // Et on place la moyenne dans notre liste des moyennes
                moy_rest[i] = total;
            }
            
            // Chargement des prédictions à faire
            List<Integer[]> fileLignes = test.trainEvaluation();
            
            for (Integer[] binome : fileLignes) {
                test.add(moy_rest[binome[1]]);
            }
            
            
            
            
            // On précalcule la moyenne de chaque clients
            CollaborativeFiltering.moyenneClient = new double[3965];
            for (int i = 0; i < 3965; i++) {
                CollaborativeFiltering.moyenneClient[i] = CollaborativeFiltering.vote_moyen(train_data[i]);
            }
            
            // Chargement des prédictions à faire
            List<Integer[]> fileLignes = test.trainEvaluation();
            
            for (Integer[] binome : fileLignes) {
                double note = CollaborativeFiltering.vote_pondere(train_data, binome[0], binome[1]);
                test.add((int) Math.round(note));
            }
            
            
            
            
            moyenne += test.trainScore();
            
            System.out.println("On à fini le test n°" + tt);
        
            test.next();
        }
        
        System.out.println(moyenne/k);
        */
        
        
        
        
    }
}
