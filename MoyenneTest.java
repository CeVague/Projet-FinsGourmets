import static org.junit.Assert.*;

import org.junit.Test;

public class MoyenneTest {

	@Test
	// Vérifie que la matrice renvoyé est bien entièrement et correctement
	// remplis
	public void PasDeVides() {

		int[][] mat1ligne = new int[15][10];
		for (int i = 0; i < 10; i++) {
			mat1ligne[i][0] = 1;
		}

		Moyenne.initialiser(mat1ligne);

		for (int i = 0; i < mat1ligne.length; i++) {
			for (int j = 0; j < mat1ligne[0].length; j++) {
				assertEquals(1, (int) Math.round(Moyenne.get(i, j)));
			}
		}

	}

	@Test
	// Vérifie que les moyennes clients soient correctes
	public void MoyenneClient() {

		int[][] mat = new int[][] { { 1, 2 }, { 2, 0 }, { 3, 4 } };

		Moyenne.initialiser(mat);

		assertEquals(1.5, Moyenne.moy_client[0], 0.01);
		assertEquals(2.0, Moyenne.moy_client[1], 0.01);
		assertEquals(3.5, Moyenne.moy_client[2], 0.01);

	}


	@Test
	// Vérifie que les moyennes restaurants soient correctes
	public void MoyenneRestaurant() {

		int[][] mat = new int[][] { { 1, 2 }, { 2, 0 }, { 3, 4 } };

		Moyenne.initialiser(mat);

		assertEquals(2.0, Moyenne.moy_restau[0], 0.01);
		assertEquals(3.0, Moyenne.moy_restau[1], 0.01);

	}


	@Test
	// Vérifie que les notes prédites soient correctes
	public void MoyenneComplette() {

		int[][] mat1 = new int[15][10];

		for (int i = 0; i < mat1.length; i++) {
			for (int j = 0; j < mat1[0].length; j++) {
				mat1[i][j] = (int) Math.round(Math.random() * 4 + 1);
			}
		}

		Moyenne.initialiser(mat1);

		for (int i = 0; i < mat1.length; i++) {
			for (int j = 0; j < mat1[0].length; j++) {
				assertEquals((Moyenne.moy_client[i] + Moyenne.moy_restau[j]) / 2, Moyenne.get(i, j), 0.0);
			}
		}

		int[][] mat2 = new int[][] { { 1, 2, 3 }, { 2, 0, 3 }, { 3, 4, 3 } };
		Moyenne.initialiser(mat2);
		assertEquals(2.75, Moyenne.get(1, 1), 0.01);
	}

}
