package optimod.modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by jonathan on 18/11/15.
 */
public class TronconTest {


	public static void comparerTroncons(Troncon tronconATester, Troncon tronconType) {
    	comparerTronconsPrimitives(tronconATester, tronconType);

		Intersection intersectionArriveeATester = tronconATester.getArrivee();
		Intersection intersectionArriveeType = tronconType.getArrivee();

		assertNotNull(intersectionArriveeATester);
		assertNotNull(intersectionArriveeType);

		//comparerIntersectionsPrimitives(intersectionArriveeATester, intersectionArriveeType);
	}

    public static void comparerTronconsPrimitives(Troncon tronconATester, Troncon tronconType) {
    	assertNotNull(tronconATester);
    	assertNotNull(tronconType);

    	assertEquals(tronconATester.getVitesse(), tronconType.getVitesse());
		assertEquals(tronconATester.getLongueur(), tronconType.getLongueur());
		assertEquals(tronconATester.getNom(), tronconType.getNom());

	}

}