package optimod.modele;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jonathan on 18/11/15.
 */
public class TronconTest {


	@Test
	public static void comparerTroncons(Troncon tronconATester, Troncon tronconType) {
    	comparerTronconsPrimitives(tronconATester, tronconType);

		Intersection intersectionArriveeATester = tronconATester.getArrivee();
		Intersection intersectionArriveeType = tronconType.getArrivee();

		assertNotNull(intersectionArriveeATester);
		assertNotNull(intersectionArriveeType);

		IntersectionTest.comparerIntersectionsPrimitives(intersectionArriveeATester, intersectionArriveeType);
    }

	@Test
    public static void comparerTronconsPrimitives(Troncon tronconATester, Troncon tronconType) {
    	assertNotNull(tronconATester);
    	assertNotNull(tronconType);

    	assertEquals(tronconATester.getVitesse(), tronconType.getVitesse());
		assertEquals(tronconATester.getLongueur(), tronconType.getLongueur());
		assertEquals(tronconATester.getNom(), tronconType.getNom());

	}

}