
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Classe pour pouvoir creer facilement ses fichiers de sortie
 * et les compresser automatiquement
 */
public class PredictFile {
	private String chemin;
	private List<Integer> liste;
	
	
	public PredictFile (String chemin){
		this.chemin = chemin;
		liste = new ArrayList<Integer>();
	}
	
	public void add(int note){
		liste.add(note);
	}
	
	public void add(double note){
		liste.add((int) Math.round(note));
	}
	
	public void add(String note){
		liste.add(Integer.parseInt(note));
	}
	
	/**
	 * Ecrit tout ce qui a été ajouté à la classe dans le fichier
	 */
	public void close(){
		try {
			File ff = new File(this.chemin);
			ff.createNewFile();
			FileWriter ffw = new FileWriter(ff);

			// On écrit chaque note de la liste dans le fichier
			for (Integer note : liste) {
				ffw.write(note + "\n");
			}

			ffw.close();
		} catch (Exception e) {
			System.out.println("Erreur lors de la création du fichier " + chemin);
		}
	}
	
	/**
	 * Crée le zip contenant dev.predict et test.predict
	 * @param nomZip le nom du fichier Zip que vous voulez créer
	 */
	public static void zip(String nomZip){
    	byte[] buffer = new byte[1024];
    	
    	try{
    		
    		FileOutputStream fos = new FileOutputStream(nomZip);
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		
    		
    		for(String nomTemp : new String[]{"dev.predict", "test.predict"}){

        		ZipEntry ze= new ZipEntry(nomTemp);
        		zos.putNextEntry(ze);
        		FileInputStream in = new FileInputStream(nomTemp);
       	   
        		int len;
        		while ((len = in.read(buffer)) > 0) {
        			zos.write(buffer, 0, len);
        		}

        		in.close();
    		}
    		
    		zos.closeEntry();
    		zos.close();
    		
    	}catch(IOException ex){
    	   ex.printStackTrace();
    	}
	}
}
