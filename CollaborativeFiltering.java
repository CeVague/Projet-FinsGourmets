
import java.util.List;

public class CollaborativeFiltering {

    public static double[] moyenneClient;

    public static double vote_moyen(int[] x) {
        double m = 0.0;
        double n = 0.0;
        
        for (int i : x) {
            if (i!=0){
                m += i;
                n += 1;
            }
        }
        
        return m/n;
    }

    public static double similarite(int[][] tab, int client_actuel, int client) {
        double s1 = 0.0;
        double s2 = 0.0;
        double s3 = 0.0;

        for (int i = 0; i < CsvFile.nb_restau; i++) {
            // System.out.println("tab[client][i]: "+tab[client][i]);
            // System.out.println("tab[client_actuel][i]: "+tab[client_actuel][i]);

            if (tab[client][i] != 0 && tab[client_actuel][i] != 0) {
                double val = tab[client_actuel][i] - moyenneClient[client_actuel];
                s1 += val * (tab[client][i] - moyenneClient[client]);
                s2 += Math.pow(val, 2);
                s3 += Math.pow(tab[client][i] - moyenneClient[client], 2);

                // System.out.println("s3 calculé");
            }
            // else {s2=1; s3=1;}
        }

        if (s1 == 0 || s2 == 0 || s3 == 0) {
            return 0;
        }
        
        return s1 / Math.sqrt(s2 * s3);
    }

    // Pour voir s'il existe au moins un restaurant pour lequel les deux clients ont donné une note
    public static boolean existe(int[] u, int[] v) {
        for (int i = 0; i < u.length; i++) {
            if (u[i] != 0 && v[i] != 0) {
                return true;
            }
        }
        return false;
    }

    public static double vote_pondere(int[][] train_data, int client_actuel, int restau) {
        double vp = 0.0;
        double s1 = 0.0;
        double s2 = 0.0;
        // double[] tab=KNN_Simple.Tout_Distances(train_data, client);
        for (int i = 0; i < 3965; i++) {
            // System.out.println("le client num : " +i);

            if (client_actuel != i && train_data[i][restau] != 0) {

                double x = similarite(train_data, client_actuel, i);

                // System.out.println("x : "+x);
                if (x != 0) {
                    s1 += x * (train_data[i][restau] - moyenneClient[i]);
                    s2 += Math.abs(x);
                }

            }
        }
        // System.out.println("s2 : "+s2);
        vp = moyenneClient[client_actuel];
        if(s2!=0){
            vp += s1 / s2;
        }
        
        if(vp>5){
            return 5;
        }else if(vp<1){
            return 1;
        }
        
        return vp;

        // Calculer la vote ponderé en utlisant le cos comme indice de similarité
        /*
		 * double vp=0.0; double s1=0.0; double s2=0.0; //double[]
		 * tab=KNN_Simple.Tout_Distances(train_data, client); for(int
		 * i=0;i<KNN_Simple.nb_clients;i++){
		 *
		 * double cos=cosinus(train_data[client],train_data[i]);
		 * System.out.println("cos moy  "+cos_moy(train_data,client));
		 * if(Math.abs(cos)<cos_moy(train_data,client) && client!=i &&
		 * non_nul(train_data[i]) && train_data[i][restau]!=0 && cos!=0){
		 * System.out.println("cos :  "+cos);
		 * s1+=cos*(train_data[i][restau]-vote_moyen(train_data[i]));
		 * s2+=Math.abs(cos); } }
		 *
		 * vp=s1/s2; return (vote_moyen(train_data[client])+vp);
         */
    }

    // pour tester si un vecteur est nul ou non
    public static boolean non_nul(int[] u) {
        for (int i : u) {
            if (i != 0) {
                return true;
            }
        }
        return false;
    }

    public static double cosinus(int[] u, int[] v) {
        double cos = 0.0;
        int s = 0;
        double s_u = 0;
        double s_v = 0;
        for (int i = 0; i < u.length; i++) {
            s += u[i] * v[i];
            s_u += Math.pow(u[i], 2);
            s_v += Math.pow(v[i], 2);
        }
        // System.out.println("su : "+s_u);
        cos = s / (Math.sqrt(s_u * s_v));
        // System.out.println("cos : "+cos);

        return cos;
    }

    public static void main(String[] args) {
        // Chargement du fichier d'entrainement
        int[][] train_data = CsvFile.chargeTrain("train.csv");

        // On précalcule la moyenne de chaque clients
        moyenneClient = new double[3965];
        for (int i = 0; i < 3965; i++) {
            moyenneClient[i] = vote_moyen(train_data[i]);
        }

        // Creation des prédictions pour le fichier dev.csv
        List<int[]> fileLignes = CsvFile.chargeTest("dev.csv");

        System.out.println("Ecriture dans dev.predict.....");
        PredictFile ff = new PredictFile("dev.predict");
        for (int[] binome : fileLignes) {
            double note = vote_pondere(train_data, binome[0], binome[1]);
            ff.add(note);
        }
        ff.close();

        // Creation des prédictions pour le fichier test.csv
        fileLignes = CsvFile.chargeTest("test.csv");

        System.out.println("Ecriture dans test.predict.....");
        ff = new PredictFile("test.predict");
        for (int[] binome : fileLignes) {
            double note = vote_pondere(train_data, binome[0], binome[1]);
            ff.add(note);
        }
        ff.close();

        // On zip le résultat
        PredictFile.zip("FiltreColaboratif.zip");

        System.out.println("Fini :)");

    }

}
