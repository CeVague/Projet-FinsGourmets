
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class CsvFile {
	public final static String SEPARATOR = ",";
	public final static int nb_restau=1751;
	public final static int nb_clients=3965;    

    public static List<String> readFileline(File file) throws IOException {

        List<String> result = new ArrayList<String>();

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        //Pour lire juste la 1ère ligne ("user,restaurants,stars")
        br.readLine();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
        	//Ce test est juste pour ne pas prendre la 1ère ligne
        	// Sûrement y'a une méthode plus prope , A voir!!
        	
            result.add(line);
        	
        }

        br.close();
        fr.close();

        return result;
    }
    
    public static Integer[][] readFile(File file) throws IOException {
    	//tab représente notre matrice, clients en lignes et les notes par restaurants en colones
    	Integer[][] tab = new Integer[nb_clients][nb_restau];
    	// Dans ce tableau on va stocker le nu client , restau et la note mais en String
    	String[] colones_split=new String[3];
    	// Dans ce tableau on va stocker le nu client , restau et la note mais en Entier
    	int[] colones=new int[3];
    	//Sans ce tableau on va stocké toutes les lignes que nous allons lire à partir de notre Csv
    	List<String> tab_line = new ArrayList<String>();
    	//Lecture du fichier csv
    	tab_line=readFileline(file);
        // Iniialisation de la matrice avec des 0 partout
    	for (int i=0;i<nb_clients;i++){
    		for (int j=0;j<nb_restau;j++){
    		tab[i][j]=0;
    		}
    	}
    	
    	//On parcourt les lignes qu'on récupérer ligne par ligne
    	for (String line : tab_line) {
    		//System.out.println(line);
    		// On fait un split pour récuperer chaque valeur à part(user, restau, note)
    		colones_split = line.split(SEPARATOR);
    		//On convertit les valeur qu'on eu grâce au split en entier
    		for (int k=0;k<=2;k++){
        		//System.out.println(colones_split[k]);
    			colones[k]=Integer.parseInt(colones_split[k]);  
            }
    		//System.out.println(colones[1]);
    		// Remplissage de la matrice
    		// Si la ligne qu'on récupérée est "3,0,5"
    		// Grâce au split elle stocké dans colones_split {"3","0","5"}
    		// Grâce à la conversion , elle est stocké dans colones {3,0,5}
    		// donc on  fait cette affecttaion tab[3][0]=5;
    		tab[colones[0]][colones[1]]=colones[2];
    	}
        
    	return tab;
    }
    
    //Pour afficher une matrice
    public static void affiche_mat(Integer[][] tab){
    	for (int i=0;i<tab.length;i++){
    			System.out.println(tab[i][0]+","+tab[i][1]+","+tab[i][2]);
    		
    	}
    }
  
    public static void main(String[] args) throws IOException {
    	File myfile = new File("train.csv");
    	Integer[][] tab=readFile(myfile);
    	System.out.println("Hello!");
    	affiche_mat(tab);
    }
}
