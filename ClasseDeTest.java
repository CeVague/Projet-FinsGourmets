
import java.util.List;
import weka.core.matrix.*;

/*
    Juste une classe pour faire mes test
    @Autor Cédric
*/
public class ClasseDeTest {
    
    //private final static int nb_restau = 1751;
    //private final static int nb_clients = 3965;
    
    
    public static void main(String[] args){
        
        int[][] tabTemp = CsvFile.chargeTrain("train.csv");
        
        System.out.println("Fichier chargé...");
        
        // Initialisation de la liste des moyennes obtenues pour chaque restaurants
        double[] moy_rest = new double[tabTemp[0].length];

        // Remplissage de la liste :
        // Pour chaque restaurant
        for (int i = 0; i < tabTemp[0].length; i++) {
            double total = 0;
            double compt = 0;

            // On additionne les notes de tous les clients qui en ont donnée
            for (int j = 0; j < tabTemp.length; j++) {
                if (tabTemp[j][i] != 0) {
                    compt++;
                    total += tabTemp[j][i];
                }
            }

            // On fait la moyenne (arrondi scientifique)
            if (compt > 0) {
                total = total / compt;
            }

            // Et on place la moyenne dans notre liste des moyennes
            moy_rest[i] = total;
        }
        
        // On remplit les troues de la matrice avec les moyennes 
        double[][] tab = new double[tabTemp.length][tabTemp[0].length];
        
        for(int i=0;i<tabTemp.length;i++){
            for(int j=0;j<tabTemp[0].length;j++){
                tab[i][j] = tabTemp[i][j];
                if(tabTemp[i][j]==0){
                    tab[i][j] = moy_rest[j];
                }
            }
        }
        
        System.out.println("Tableau converti...");
        
        Matrix mat = new Matrix(tab);
         
        System.out.println("Matrice créée...");
        
        SingularValueDecomposition SVD = new SingularValueDecomposition(mat);
        
        System.out.println("SVD calculé...");
        
        Matrix U = SVD.getU();
        Matrix S = SVD.getS();
        Matrix V = SVD.getV().transpose();
        
        
        System.out.println("Sous matrices récupérés...");
        
        for(int k=29;k>=1;k--){
            // On ne garde que les valeurs de S assez grandes
            for(int i=k;i<Math.min(S.getRowDimension(), S.getColumnDimension());i++){
                S.set(i, i, 0);
            }

            System.out.println("Début des multiplications...");
            
            // U*S*V
            Matrix A = U.times(S);
            Matrix Fin = A.times(V);

            System.out.println("Fin de multiplication...");

            // Creation des prédictions pour le fichier dev
            List<int[]> fileLignes = CsvFile.chargeTest("dev.csv");

            PredictFile ff = new PredictFile("dev.predict");
            for (int[] binome : fileLignes) {
                ff.add(Fin.get(binome[0],binome[1]));
            }
            ff.close();

            // Creation des prédictions pour le fichier test
            fileLignes = CsvFile.chargeTest("test.csv");

            ff = new PredictFile("test.predict");
            for (int[] binome : fileLignes) {
                ff.add(Fin.get(binome[0],binome[1]));
            }
            ff.close();

            System.out.println("Fini pour k = " + k + " :)");

            PredictFile.zip("SVD k=" + Integer.toString(k) + ".zip");

            System.out.println("Et zippé");
        }
        
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
            
            
            // Test sur la méthode des moyennes
            {
                // Chargement des prédictions à faire
                List<int[]> fileLignes = test.trainEvaluation();

                for (int[] binome : fileLignes) {
                    test.add(moy_rest[binome[1]]);
                }
            }
            
            // Test sur la méthode des k voisins
            {
                // On précalcule la moyenne de chaque clients
                CollaborativeFiltering.moyenneClient = new double[3965];
                for (int i = 0; i < 3965; i++) {
                    CollaborativeFiltering.moyenneClient[i] = CollaborativeFiltering.vote_moyen(train_data[i]);
                }

                // Chargement des prédictions à faire
                List<int[]> fileLignes = test.trainEvaluation();

                for (int[] binome : fileLignes) {
                    double note = CollaborativeFiltering.vote_pondere(train_data, binome[0], binome[1]);
                    test.add((int) Math.round(note));
                }
            }
            
            
            
            moyenne += test.trainScore();
            
            System.out.println("On à fini le test n°" + tt);
        
            test.next();
            
        }
        
        System.out.println(moyenne/k);
        */
        
        
        
        
    }
}
