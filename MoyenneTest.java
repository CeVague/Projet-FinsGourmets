import static org.junit.Assert.*;

import org.junit.Test;

public class MoyenneTest {

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
