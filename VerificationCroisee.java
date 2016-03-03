
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Petite classe pour faire des test sur nos algos
 * en utilisant une partie de l'ensemble d'entrainement.
 * Ca évite de toujours tout envoyer en ligne, surtout pour les ajustements
 * d'algos
 * @author Cédric
 */
public class VerificationCroisee {
    private final int[][] train_data;
    private final List<Integer[]> listeTrainTotal;
    private final int tailleDeK;
    
    private int k;
    private List<Integer[]> listeEvalTemp;
    private List<Integer> listeNotes;
    
    
    public VerificationCroisee(int k, String chemin){
        // On charge la matrice de base
        this.train_data = CsvFile.chargeTrain(chemin);
        
        // Initialisation de la liste de valeur
        listeTrainTotal = new ArrayList<Integer[]>();
        for(int i=0;i<train_data.length;i++){
            for(int j=0;j<train_data[0].length;j++){
                if(train_data[i][j] != 0){
                    listeTrainTotal.add(new Integer[]{i, j, train_data[i][j]});
                }
            }
        }
        
        // Mélange les valeurs
        Collections.shuffle(listeTrainTotal);
        
        // On initialise le nombre de découpe et l'emplacement actuel
        this.tailleDeK = listeTrainTotal.size() / k;
        this.k = 0;
        
        // Initialise la liste de notes prédites
        listeNotes = new ArrayList<Integer>();
    }
    
    public void next(){
        this.k++;
    }
    
    public int[][] trainData(){
        int[][] retour = new int[train_data.length][train_data[0].length];
        
        for(int i=0;i<train_data.length;i++){
            for(int j=0;j<train_data[0].length;j++){
                if(train_data[i][j] != 0){
                    retour[i][j] =  train_data[i][j];
                }
            }
        }
        
        for(int i=(k*tailleDeK);i<((k+1)*tailleDeK);i++){
            Integer[] temp = listeTrainTotal.get(i);
            retour[temp[0]][temp[1]] = 0;
        }
        
        return retour;
    }
    
    public List<Integer[]> trainEvaluation(){
        listeEvalTemp = new ArrayList<Integer[]>();
        
        for(int i=(k*tailleDeK);i<((k+1)*tailleDeK);i++){
            Integer[] temp = listeTrainTotal.get(i);
            listeEvalTemp.add(new Integer[]{temp[0], temp[1]});
        }
        
        return listeEvalTemp;
    }
    
    public void add(int note){
        listeNotes.add(note);
    }
    
    public double trainScore(){
        double resultat = 0;
        
        for(int i=0;i<listeNotes.size();i++){
            Integer[] temp = listeEvalTemp.get(i);
            resultat+=Math.pow(listeNotes.get(i) - train_data[temp[0]][temp[1]] ,2);
        }
        
        resultat = resultat / listeNotes.size();
        
        listeNotes = new ArrayList<Integer>();
        
        return Math.sqrt(resultat);
    }
    
}
