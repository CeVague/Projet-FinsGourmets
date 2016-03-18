
import  org.apache.hadoop.conf.Configuration;
 import  org.apache.hadoop.io.IntWritable;
 import  org.apache.hadoop.mapreduce.Job;
 import  org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import  org.apache.hadoop.mapreduce.lib.map.MultithreadedMapper;
 import  org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
 import  org.apache.hadoop.util.ToolRunner;
 import org.apache.mahout.cf.taste.hadoop.RecommendedItemsWritable;
 import org.apache.mahout.common.AbstractJob;


import java.util.List;
 import java.util.Map;
public class Als {
public static void main(String[] args){
        
        int[][] tabTemp = CsvFile.chargeTrain("train.csv");
        
        System.out.println("Fichier chargé...");
        
    

        
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
}
