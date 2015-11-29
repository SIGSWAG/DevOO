package optimod.modele;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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


    	List<Troncon> tronconsATester = intersectionATester.getSortants();
    	List<Troncon> tronconsType = intersectionType.getSortants();


        assertNotNull(tronconsATester);
        assertNotNull(tronconsType);


    	assertEquals(tronconsATester.size(), tronconsType.size());

    	for(int i = 0; i < tronconsATester.size(); i++) {
    		Troncon tronconATester = tronconsATester.get(i);
    		Troncon tronconType = tronconsType.get(i);
    		TronconTest.comparerTroncons(tronconATester, tronconType);
    	}
    }


    public static void comparerIntersectionsPrimitives(Intersection intersectionATester, Intersection intersectionType) {
    	assertNotNull(intersectionATester);
    	assertNotNull(intersectionType);
        List<Troncon> tronconsATester = intersectionATester.getSortants();
        List<Troncon> tronconsType = intersectionType.getSortants();
        assertEquals(tronconsATester.size(), tronconsType.size());

        // TODO vérifier ce code ! Pourquoi faire des traitements après le dernier assert ?
        for (int i = 0; i < tronconsATester.size(); i++) {
            Troncon tronconATester = tronconsATester.get(i);
            Troncon tronconType = tronconsType.get(i);
            TronconTest.comparerTroncons(tronconATester, tronconType);
        }
    }


}