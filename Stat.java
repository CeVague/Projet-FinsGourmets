import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Stat {
	public static int valeur_manquante(int[][] tab){
		int nb=0;
		for (int[] i:tab){
			for (int j:i){
				if (j==0)
                                    nb++;
			}
		}
		return nb;
	}
	public static int valeur_présente(int[][] tab){
		int nb=0;
		for (int[] i:tab){
			for (int j:i){
				if (j!=0)
                                    nb++;
			}
		}
		return nb;
	}
	public static boolean client_tout_note(int[] v){
		for(int i:v){
			if (i==0)
                            return false;
		}
		return true;
	}
	public static boolean client_rien_note(int[] v){
		for(int i:v){
			if (i!=0)
                            return false;
		}
		return true;
	}
	
	public static boolean restau_pas_note(int[][] train_data, int restau){
		for(int[] i:train_data){
			if (i[restau]!=0)
                            return false;
		}
		return true;
	}
	
	public static int client_note_restau(int[][] train_data,int client_actuel, int restau){
		int nb=0;
		for(int i=0;i<CsvFile.nb_clients;i++){
			if (i!=client_actuel){
				if (train_data[i][restau]!=0)
                                    nb++;
			}
		}
		return nb;
	}
	
	public static int nb_vote_restau(int[][] train_data, int restau){
		int nb=0;
		for(int[] i:train_data){
			if (i[restau]!=0)
                            nb++;
		}
		return nb;
	}
	
	public static int nb_vote_client(int[][] train_data, int client){
		int nb=0;
		for(int i:train_data[client]){
			if (i!=0)
                            nb++;
		}
		return nb;
	}
        
	public static void main(String[] args) throws IOException {
		System.out.println("Lecture des données");
	    	int[][] tab=CsvFile.chargeTrain("train.csv");
                System.out.println("Recherche..");

	    	for(int i=0; i<CsvFile.nb_clients;i++){
	    		if (client_tout_note(tab[i])){
                            System.out.println("client num  :"+i+"  a tout noté");
	    		}
	    	}
                
	    	for(int i=0; i<CsvFile.nb_clients;i++){
	    		if (client_rien_note(tab[i])){
                            System.out.println("client num  :"+i+"  n'a rien noté");
	    		}
	    	}
                System.out.println("Fin de la recherche.\n");
                
                System.out.println("Recherche des restau non noté ");
                for(int i=0; i<CsvFile.nb_restau;i++){
                        if (restau_pas_note(tab,i)){
                            System.out.println("restau num  :"+i+"  n'a pas été noté");

                        }
                }
                System.out.println("Fin de la recherche.\n");
                
                System.out.println("Il manque : "+valeur_manquante(tab)+("  valeurs dans la matrice sur "+(CsvFile.nb_clients*CsvFile.nb_restau)+" valeur en tout"));
                System.out.println("Il manque : "+valeur_manquante(tab)*100/(CsvFile.nb_clients*CsvFile.nb_restau)+"% ");
                System.out.println("On a "+valeur_présente(tab)+" valeur");
                System.out.println("En moyenne chaque client a noté "+valeur_présente(tab)/CsvFile.nb_clients+" restaurants parmi "+CsvFile.nb_restau);

                //Creation des stats restau
                int max=0;
                int min=4000;
                try {
                        System.out.println("Ecriture dans statrestau.txt.....");

                        File ff = new File("statrestau.txt");
                        ff.createNewFile();
                        FileWriter ffw = new FileWriter(ff);
                        // Pour chaque ligne du fichier dev.csv
                        for (int i = 0 ; i<1751 ; i++) {
                                int vote=nb_vote_restau(tab,i);
                                ffw.write(("Le restaurant numéro ")+i+" a été noté  par "+vote+" personnes."+ "\n");

                                if (vote>max) {
                                    max=vote;
                                }else if (vote<min) {
                                    min=vote;
                                }
                        }
                        
                        ffw.write("On a un minimum de vote par restaurant égal à "+min+ "\n");
                        ffw.write("On a un maximum de vote par restaurant égal à "+max+ "\n");

                        ffw.close();
                } catch (Exception e ) {
                    System.out.println("erreur");
                }
                
                
                //Creation des stats client
                max=0;
                min=4000;
                try {
                        System.out.println("Ecriture dans statclient.txt.....");

                        File ff = new File("statclient.txt");
                        ff.createNewFile();
                        FileWriter ffw = new FileWriter(ff);
                        // Pour chaque ligne du fichier dev.csv
                        for (int i = 0 ; i<3965 ; i++) {
                                int vote=nb_vote_client(tab,i);
                                ffw.write(("Le client numéro ")+i+" a noté "+vote+" fois."+ "\n");

                                if (vote>max) {
                                    max=vote;
                                }else if (vote<min) {
                                    min=vote;
                                }
                        }
                        
                        ffw.write("On a un minimum de vote par restaurant égal à "+min+ "\n");
                        ffw.write("On a un maximum de vote par restaurant égal à "+max+ "\n");

                        ffw.close();
                } catch (Exception e ) {
                    System.out.println("erreur");
                }
	    }
}
