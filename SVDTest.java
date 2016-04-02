import static org.junit.Assert.*;

import org.junit.Test;


public class SVDTest {

	@Test
	public void test() {
		double[][] plein = new double[10][10];
		
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				plein[i][j] = Math.random() * 4 + 1;
			}
		}
		
		SVD.initialiser(plein);
		
		SVD.decomposer(10);
		
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				assertEquals(plein[i][j], SVD.get(i, j), 0.001);
			}
		}
		
		
		for(int i=0;i<9;i++){
			assertTrue(SVD.S.get(i,i) >= SVD.S.get(i,i));
		}
		
	}
}
