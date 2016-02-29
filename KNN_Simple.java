
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class KNN_Simple {
    // public static final int nb_restau=CsvFile.nb_restau;
    // public static final int nb_clients=CsvFile.nb_clients;

    // Fonction qui calcule la distance euclidienne entre deux vecteurs
    public static double Distance(int[] x, int[] y) {
        double d = 0.0;
        for (int i = 0; i < CsvFile.nb_restau; i++) {
            d += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(d);
    }

    // Calculer toutes les distances et les stocker dans un tableau
    public static double[] Tout_Distances(int[][] train_data, int x) {
        double[] tab = new double[CsvFile.nb_clients];
        for (int i = 0; i < CsvFile.nb_clients; i++) {
            tab[i] = Distance(train_data[i], train_data[x]);
        }
        return tab;
    }

    // Fonction qui renvoie l'indice du minimum d'un tableau
    public static int indice_min(int[][] tab, double[] t, int x, int y) {
        int indice = 0;
        for (int i = 1; i < t.length - 1; i++) {
            if (t[i] < t[indice] && i != x && tab[i][y] != 0) {
                indice = i;
            }
        }
        return indice;
    }

    // Fonction qui renvoie la distance minimale dans un tableau
    public static double distance_min(double[] t, int x) {
        double d = t[0];
        for (int i = 1; i < t.length - 1; i++) {
            if (t[i] < d && i != x) {
                d = t[i];
            }
        }
        return d;
    }

    public static void main(String[] args) throws IOException {
        // Chargement du fichier d'entrainement
        int[][] train_data = CsvFile.chargeTrain("train.csv");
        // Creation des prédictions pour le fichier dev
        List<int[]> fileLignes = CsvFile.chargeTest("dev.csv");

        try {
            System.out.println("Ecriture dans dev2.predict.....");

            File ff = new File("dev2.predict");
            ff.createNewFile();
            FileWriter ffw = new FileWriter(ff);
            int i = 0;
            // Pour chaque ligne du fichier dev.csv
            for (int[] binome : fileLignes) {
                i++;
                // On récupère le numéro client
                int num_client = binome[0];
                System.out.println("num clien  :  " + num_client);
                // on récupère le numéro restaurant
                int num_restau = binome[1];
                // On calcule la distance entre notre client et
                // tout les autres clients qui ont voté pour ce restaurant
                double[] distances = Tout_Distances(train_data, num_client);
                // On cherche le voisin/ligne plus proche/similaire
                int ligne_plus_proche = indice_min(train_data, distances, num_client, num_restau);
                // On récupère la note donnée à ce restaurant dans cette ligne
                // et on l'écrit dans le fichier
                ffw.write(Integer.toString(train_data[ligne_plus_proche][num_restau]) + "\n");
                System.out.println("c'est bon pour la ligne  " + i + " du fichier dev.csv  La note prédite est  "
                        + train_data[ligne_plus_proche][num_restau]);

            }

            ffw.close();
        } catch (Exception e) {

        }
        System.out.println("dev2.predict est prêt");

        // Creation des prédictions pour le fichier test
        fileLignes = CsvFile.chargeTest("test.csv");
        System.out.println("Ecriture dans test2.predict...");

        try {
            File ff = new File("test2.predict");
            ff.createNewFile();
            FileWriter ffw = new FileWriter(ff);
            int i = 0;
            // Pour chaque ligne du fichier dev.csv
            for (int[] binome : fileLignes) {
                i++;
                // On récupère le numéro client
                int num_client = binome[0];
                System.out.println("num clien  :  " + num_client);
                // on récupère le numéro restaurant
                int num_restau = binome[1];
                // On calcule la distance entre notre client et tout les autres clients qui ont voté pour ce restaurant
                double[] distances = Tout_Distances(train_data, num_client);
                // On cherche le voisin/ligne plus proche/similaire
                int ligne_plus_proche = indice_min(train_data, distances, num_client, num_restau);
                // On récupère la note donnée à ce restaurant dans cette ligne et on l'écrit dans le fichier
                ffw.write(Integer.toString(train_data[ligne_plus_proche][num_restau]) + "\n");
                System.out.println("c'est bon pour la ligne  " + i + " du ficher test.csv  La note prédite est  "
                        + train_data[ligne_plus_proche][num_restau]);

            }

            ffw.close();
        } catch (Exception e) {
            System.out.println("hhhhhhhh");

        }
        System.out.println("test2.predict est prêt");
        System.out.println("Fini :)");

    }

}
