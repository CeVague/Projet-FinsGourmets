import static org.junit.Assert.*;

import org.junit.Test;

public class MoyenneTest {

	@Test
	public void PasDeVides() {

		int[][] mat1ligne = new int[15][10];
		for(int i=0;i<10;i++){
			mat1ligne[i][0] = 1;
		}
		
		Moyenne.initialiser(mat1ligne);
		
		for (int i=0; i < mat1ligne.length; i++) {
			for (int j=0; j < mat1ligne[0].length; j++) {
				assertEquals(1, (int) Math.round(Moyenne.get(i, j)));
			}
		}
		
	}
	
	@Test
	public void MoyenneClient() {

		int[][] mat = new int[][]{{1,2},{2,0},{3,4}};
		
		Moyenne.initialiser(mat);
		
		assertEquals(1.5, Moyenne.moy_client[0], 0.01);
		assertEquals(2.0, Moyenne.moy_client[1], 0.01);
		assertEquals(3.5, Moyenne.moy_client[2], 0.01);
		
	}
	
	
	@Test
	public void MoyenneRestaurant() {

		int[][] mat = new int[][]{{1,2},{2,0},{3,4}};
		
		Moyenne.initialiser(mat);
		
		assertEquals(2.0, Moyenne.moy_restau[0], 0.01);
		assertEquals(3.0, Moyenne.moy_restau[1], 0.01);
		
	}
	
	
	@Test
	public void MoyenneComplette() {

		int[][] mat1 = new int[15][10];
		
		for (int i=0; i < mat1.length; i++) {
			for (int j=0; j < mat1[0].length; j++) {
				mat1[i][j] = (int) Math.round(Math.random() * 4 + 1);
			}
		}
		
		Moyenne.initialiser(mat1);
		
		for (int i=0; i < mat1.length; i++) {
			for (int j=0; j < mat1[0].length; j++) {
				assertEquals((Moyenne.moy_client[i] + Moyenne.moy_restau[j])/2, Moyenne.get(i, j), 0.0);
			}
		}
		
		int[][] mat2 = new int[][]{{1,2,3},{2,0,3},{3,4,3}};
		Moyenne.initialiser(mat2);
		assertEquals(2.75, Moyenne.get(1, 1), 0.01);
	}
	
	
	@Test
	public void test() {
		
		 
		int[][] matZero = new int[10][10]; //Matrice carr�e remplie de zero
		int[][] matZero2 = new int[10][123]; //Matrice non carr�e remple de zero
		
		assertEquals(0, (int) Math.round(Moyenne.moyenne_restau(5, matZero)));
		assertEquals(0, (int) Math.round(Moyenne.moyenne_restau(0, matZero2)));
		assertEquals(0, (int) Math.round(Moyenne.moyenne_client(0, matZero)));
		assertEquals(0, (int) Math.round(Moyenne.moyenne_client(0, matZero2)));
		
		int[][] mat1 = new int[10][15]; //Matrice non carr�e remplie de 5
		for (int i=0; i < mat1.length; i++) {
			for (int j=0; j < mat1[i].length; j++) {
				mat1[i][j] = 5;
			}
		}
		
		for (int i=0; i < mat1.length; i++) {
			assertEquals(5, (int) Math.round(Moyenne.moyenne_restau(i, mat1)));
			assertEquals(5, (int) Math.round(Moyenne.moyenne_client(i, mat1)));
		}
	}

}
