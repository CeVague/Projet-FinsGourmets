
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour pouvoir creer facilement ses fichiers de sortie
 */
public class Predict {
	private final String chemin;
	private static List<Integer> liste;
	
	public Predict (String chemin){
		this.chemin = chemin;
		liste = new ArrayList<Integer>();
	}
	
	public static void add(int note){
		liste.add(note);
	}
	
	public static void add(String note){
		liste.add(Integer.parseInt(note));
	}
	
	/**
	 * Ecrit tout ce qui a été ajouté à la classe dans le fichier
	 */
	public void ecrit(){
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
}
