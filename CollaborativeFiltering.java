
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollaborativeFiltering {
	public static double[] moyenneClient;
	
	public static double vote_moyen(int[] x) {
		double m = 0.0;
		for (int i : x) {
			m += i;
		}
		m = m / x.length;
		return m;
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
				s2 += Math.pow(val,2);
				s3 += Math.pow(tab[client][i] - moyenneClient[client],2);
				
				// System.out.println("s3 calculé");

			}
			// else {s2=1; s3=1;}
		}
		
		if (s2 == 0) {
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
				if(x!=0){
					s1 += x * (train_data[i][restau] - moyenneClient[i]);
					s2 += Math.abs(x);
				}

			}
		}
		// System.out.println("s2 : "+s2);
		vp = s1 / s2;
		return (moyenneClient[client_actuel] + vp);

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
			if (i != 0)
				return true;
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

	public static void main(String[] args) throws IOException {
		// Chargement du fichier d'entrainement
		int[][] train_data = CsvFile.chargeTrain("train.csv");
		// Creation des prédictions pour le fichier dev
		List<int[]> fileLignes = CsvFile.chargeTest("dev.csv");

		// On précalcule la moyenne de chaque clients
		moyenneClient = new double[3965];
		for(int i=0;i<3965;i++){
			moyenneClient[i] = vote_moyen(train_data[i]);
		}
		
		
		try {
			System.out.println("Ecriture dans dev3.predict.....");

			File ff = new File("dev3.predict");
			ff.createNewFile();
			FileWriter ffw = new FileWriter(ff);
			int i = 0;
			// Pour chaque ligne du fichier dev.csv
			for (int[] binome : fileLignes) {
				i++;
				
				// On récupère le numéro client
				int num_client = binome[0];
				// on récupère le numéro restaurant
				int num_restau = binome[1];
				double note = vote_pondere(train_data, num_client, num_restau);
				ffw.write(Integer.toString((int) Math.round(note)) + "\n");
				// System.out.println("c'est bon pour la ligne "+i+" du fichier dev.csv
				// La note prédite est
				// "+train_data[ligne_plus_proche][Integer.parseInt(temp_bis[1])]);
				//System.out.println("num clien  :  " + num_client);
				//System.out.println("c'est bon pour la ligne  " + i + " du fichier dev.csv  La note prédite est  " + note);

			}

			ffw.close();
		} catch (Exception e) {
			System.out.println("Problème");

		}
		System.out.println("dev3.predict est prêt");
		// Creation des prédictions pour le fichier test.csv
		fileLignes = CsvFile.chargeTest("test.csv");

		try {
			System.out.println("Ecriture dans test3.predict.....");

			File ff = new File("test3.predict");
			ff.createNewFile();
			FileWriter ffw = new FileWriter(ff);
			int i = 0;
			// Pour chaque ligne du fichier dev.csv
			for (int[] binome : fileLignes) {
				i++;
				// On récupère le numéro client
				int num_client = binome[0];
				// on récupère le numéro restaurant
				int num_restau = binome[1];
				double note = vote_pondere(train_data, num_client, num_restau);
				ffw.write(Integer.toString((int) Math.round(note)) + "\n");
				// System.out.println("c'est bon pour la ligne "+i+" du fichier
				// dev.csv La note prédite est
				// "+train_data[ligne_plus_proche][Integer.parseInt(temp_bis[1])]);
				//System.out.println("num clien  :  " + num_client);
				//System.out.println("c'est bon pour la ligne  " + i + " du fichier dev.csv  La note prédite est  " + note);

			}

			ffw.close();
		} catch (Exception e) {
			System.out.println("Problème");

		}

		System.out.println("Fini :)");

	}

}
