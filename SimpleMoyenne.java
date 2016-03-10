
import java.util.List;

public class SimpleMoyenne {

    private final static int nb_restau = 1751;
    private final static int nb_clients = 3962;

    public static void main(String[] args) {

        // Chargement du fichier d'entrainement
        int[][] tab = CsvFile.chargeTrain("train.csv");

        // Initialisation de la liste des moyennes obtenues pour chaque restaurants
        int[] moy_rest = new int[nb_restau];

        // Remplissage de la liste :
        // Pour chaque restaurant
        for (int i = 0; i < nb_restau; i++) {
            int total = 0;
            int compt = 0;

            // On additionne les notes de tous les clients qui en ont donnée
            for (int j = 0; j < nb_clients; j++) {
                if (tab[j][i] != 0) {
                    compt++;
                    total += tab[j][i];
                }
            }

            // On fait la moyenne (arrondi scientifique)
            if (compt > 0) {
                total = (total + (compt / 2)) / compt;
            }

            // Et on place la moyenne dans notre liste des moyennes
            moy_rest[i] = total;
        }

        
        
        /*
        // Calcul de l'écart type entre note client et moyenne restau
        {
        PredictFile ff = new PredictFile("statsET.txt");
        for(int[] client : tab){
            double somme = 0;
            double nb = 0;
            
            for(int i=0;i<nb_restau;i++){
                if(client[i]!=0){
                    somme += Math.pow(client[i]-moy_rest[i], 2);
                    nb++;
                }
            }
            ff.add(Math.sqrt(somme)/nb);
        }
        ff.close();
        
        }
        */
        
        // Creation des prédictions pour le fichier dev
        List<int[]> fileLignes = CsvFile.chargeTest("dev.csv");

        PredictFile ff = new PredictFile("dev.predict");
        for (int[] binome : fileLignes) {
            ff.add(moy_rest[binome[1]]);
        }
        ff.close();

        // Creation des prédictions pour le fichier test
        fileLignes = CsvFile.chargeTest("test.csv");

        ff = new PredictFile("test.predict");
        for (int[] binome : fileLignes) {
            ff.add(Integer.toString(moy_rest[binome[1]]));
        }
        ff.close();

        System.out.println("Fini :)");

        PredictFile.zip("MoyenneSimple.zip");

        System.out.println("Et zippé");

    }
}
