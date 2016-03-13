
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvFile {

    public final static String SEPARATOR = ",";
    public final static int nb_restau = 1751;
    public final static int nb_clients = 3965;

    /**
     * Système de lecture ligne par ligne de fichiers pour en retourner la
     * liste.
     *
     * @param file le fichier concerné
     * @return une List<String> contenant chaque ligne du fichier (sauf la
     * première) dans l'ordre.
     */
    private static List<String> readFileLines(File file) {
        List<String> result = new ArrayList<String>();

        try {

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            br.readLine(); // Pour sauter juste la 1ère ligne ("user,restaurants,stars")

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                result.add(line);
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du fichier.");
        }

        return result;
    }

    /**
     * Fonction de chargement des fichiers d'entrainements (client, restaurant,
     * note) en un tableau de notes.
     *
     * Pas de note = 0.
     *
     * @param chemin le chemin vers le fichier (ou son nom si il est à la racine
     * du projet java)
     * @return un int[][] où le premier paramètre est le num client et le
     * deuxième est le num du restaurant. Seul la dernière note du fichier est
     * prise en compte (en cas de doublons).
     */
    public static int[][] chargeTrain(String chemin) {
        // tab représente notre matrice, les clients en lignes et les notes par restaurants en colonnes
        // Elle est initialisée par défaut avec des zeros partout
        int[][] tab = new int[nb_clients][nb_restau];

        // Dans ce tableau on va stocker le num client , restau et la note mais en String
        String[] colonnes_split = new String[3];
        // Dans ce tableau on va stocker le num client , restau et la note mais en Entier
        int[] colonnes = new int[3];
        // Dans cette liste on va stocker toutes les lignes que nous allons lire à partir de notre Csv
        File myfile = new File(chemin);
        List<String> lignes = readFileLines(myfile);

        // On parcourt les lignes qu'on récupère une par une
        for (String ligne : lignes) {
            // On fait un split pour récuperer chaque valeur à part (user, restau, note)
            colonnes_split = ligne.split(SEPARATOR);
            // On convertit les valeurs qu'on eu grâce en entiers
            for (int k = 0; k <= 2; k++) {
                colonnes[k] = Integer.parseInt(colonnes_split[k]);
            }
            // System.out.println(colonnes[1]);
            // Remplissage de la matrice
            // Si la ligne qu'on récupérée est "3,0,5"
            // Grâce au split elle stocké dans colonnes_split {"3","0","5"}
            // Grâce à la conversion , elle est stocké dans colonnes {3,0,5}
            // donc on fait cette affecttaion tab[3][0]=5;
            tab[colonnes[0]][colonnes[1]] = colonnes[2];
        }

        return tab;
    }
    
     /**
     * Fonction de chargement des fichiers d'entrainements (client, restaurant,
     * note) en une liste de notes.
     *
     * @param chemin le chemin vers le fichier (ou son nom si il est à la racine
     * du projet java)
     * @return un List<int[]> où le premier paramètre est le num client et le
     * deuxième est le num du restaurant et le troisieme la note. Seul la
     * dernière note du fichier est prise en compte (en cas de doublons).
     */
    public static List<int[]> chargeTrainList(String chemin) {
        // Chargement des données (en enlevant les doublons)
        int[][] tab = chargeTrain(chemin);
            
        // Futur liste des données
        List<int[]> liste = new ArrayList<int[]>();

        // On parcourt les lignes qu'on récupère une par une
        for (int i=0;i<tab.length;i++) {
            for(int j=0;j<tab[0].length;j++){
                if(tab[i][j]!=0){
                    liste.add(new int[]{i,j,tab[i][j]});
                }
            }
        }

        return liste;
    }

    /**
     * Fonction de chargement des fichier de test (dev et test) (client,
     * restaurant, ?)
     *
     * @param chemin le chemin vers le fichier (ou son nom si il est à la racine
     * du projet java)
     * @return une List<int[]> donc chaque element est un couple (client,
     * restaurant).
     */
    public static List<int[]> chargeTest(String chemin) {
        // Dans cette liste on va stocker toutes les lignes que nous allons lire à partir de notre Csv
        List<String> lignes = readFileLines(new File(chemin));

        // Initialisation de la liste de retour
        List<int[]> liste = new ArrayList<int[]>();

        // Dans ce tableau on va stocker le couple (num client, restau) en int
        int[] couple = new int[2];

        // On récupère les lignes une par une
        for (String ligne : lignes) {
            // On fait un split pour récuperer chaque valeur à part (user, restau, ?)
            String[] temp = ligne.split(SEPARATOR);
            // On convertit les valeurs qu'on eu grâce en entiers
            for (int k = 0; k < 2; k++) {
                couple[k] = Integer.parseInt(temp[k]);
            }
            // Et on ajoute ce couple à la liste
            liste.add(couple.clone());
        }

        return liste;
    }
}
