package optimod.modele;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class TronconTest {

    private static final double EPSILON = 1e-15;

    @Test
    public void testTroncon() {
        assertEquals(new Troncon(null, 5, 5, "").getDuree(), 1, EPSILON);
        assertEquals(new Troncon(null, 1, 10, "").getDuree(), 10, EPSILON);
        assertEquals(new Troncon(null, 10, 1, "").getDuree(), 0, EPSILON);
        assertEquals(new Troncon(null, 5, 0, "").getDuree(), 0, EPSILON);

        // Je voulais tester une division par 0 mais le calcul se fait avec des doubles...
        assertEquals(new Troncon(null, 0, 5, "").getDuree(), (int) Double.MAX_VALUE, EPSILON);
    }

    public static void comparerTroncons(Troncon tronconATester, Troncon tronconType) {

    	comparerTronconsPrimitives(tronconATester, tronconType);

		Intersection intersectionArriveeATester = tronconATester.getArrivee();
		Intersection intersectionArriveeType = tronconType.getArrivee();

		assertNotNull(intersectionArriveeATester);
		assertNotNull(intersectionArriveeType);


		IntersectionTest.comparerIntersectionsPrimitives(intersectionArriveeATester, intersectionArriveeType);
    }

    public static void comparerTronconsPrimitives(Troncon tronconATester, Troncon tronconType) {
    	assertNotNull(tronconATester);
    	assertNotNull(tronconType);

        assertEquals(tronconATester.getVitesse(), tronconType.getVitesse(), EPSILON);
        assertEquals(tronconATester.getLongueur(), tronconType.getLongueur(), EPSILON);
        assertEquals(tronconATester.getNom(), tronconType.getNom());

	}


    public static boolean comparerTronconsBool(Troncon tronconATester, Troncon tronconType) {

        if (!comparerTronconsPrimitivesBool(tronconATester, tronconType)){
            return false;
        }

        Intersection intersectionArriveeATester = tronconATester.getArrivee();
        Intersection intersectionArriveeType = tronconType.getArrivee();


        return (intersectionArriveeATester!=null && intersectionArriveeType!=null && IntersectionTest.comparerIntersectionsPrimitivesBool(intersectionArriveeATester, intersectionArriveeType));
     }

    public static boolean comparerTronconsPrimitivesBool(Troncon tronconATester, Troncon tronconType) {

        return (tronconATester!=null && tronconType != null && Math.abs(tronconATester.getVitesse() - tronconType.getVitesse()) < EPSILON
                && Math.abs(tronconATester.getLongueur() - tronconType.getLongueur()) < EPSILON && tronconATester.getNom().equals(tronconType.getNom()));

    }

}
