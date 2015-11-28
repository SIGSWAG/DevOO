package optimod.modele;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by aurelien on 18/11/15.
 */
public class IntersectionTest {

    @Test
    public void testEstLocalisee() throws Exception {

        Intersection intersection = new Intersection(0, 0, 15, null);

        assertTrue(intersection.estLocalisee(0, 0, 1));
        assertFalse(intersection.estLocalisee(2, 2, 1));
        assertTrue(intersection.estLocalisee(5, 5, 10));
        assertTrue(intersection.estLocalisee(0, 1, 1));

    }

    public static void comparerIntersections(Intersection intersectionATester, Intersection intersectionType) {
    	comparerIntersectionsPrimitives(intersectionATester, intersectionType);

    	List<Troncon> tronconsATester = intersectionATester.getTroncons();
    	List<Troncon> tronconsType = intersectionType.getTroncons();

    	assertNotNull(tronconsATester);
    	assertNotNull(tronconsType);

    	assertEquals(tronconATester.size(), tronconType.size());

    	for(int i = 0; i < tronconATester.size(); i++) {
    		Troncon tronconATester = tronconsATester.get(i);
    		Troncon tronconType = tronconsType.get(i);
    		TronconTest.comparerTroncons(tronconATester, tronconsType);
    	}
    }

    private static void comparerIntersectionsPrimitives(Intersection intersectionATester, Intersection intersectionType) {
    	assertNotNull(intersectionATester)
    	assertNotNull(intersectionType)

    	assertEquals(intersectionATester.getX(), intersectionType.getX());
    	assertEquals(intersectionATester.getY(), intersectionType.getY());
    	assertEquals(intersectionATester.getAdresse(), intersectionType.getAdresse());
    }
}