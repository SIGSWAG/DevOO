package optimod.modele;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntersectionTest {

    @Test
    public void testEstLocalisee() throws Exception {

        Intersection intersection = new Intersection(0, 0, 15);

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

    @Test
    public void testGetTronconVers() throws Exception{
        Intersection i1 = new Intersection(0,0,1);
        Intersection i2 = new Intersection(5,5,2);
        Intersection i3 = new Intersection(5,5,2);
        Troncon i1i2 = new Troncon(i2, 5, 5, "troncon i1 vers i2");
        Troncon i1i3 = new Troncon(i3, 5, 5, "troncon i1 vers i3");
        List<Troncon> tronconsSortantsI1 = new ArrayList<>();
        tronconsSortantsI1.add(i1i2);
        tronconsSortantsI1.add(i1i3);
        i1.setSortants(tronconsSortantsI1);

        assertEquals(TronconTest.comparerTronconsBool(i1i2, i1.getTronconVers(i2)), true);
        assertEquals(TronconTest.comparerTronconsBool(i1i3, i1.getTronconVers(i3)), true);
    }

    @Test
    public void testGetTronconFail() {
        Intersection i1 = new Intersection(0,0,1);
        Intersection i2 = new Intersection(5,5,2);
        Intersection i3 = new Intersection(5,5,2);
        Troncon i1i2 = new Troncon(i2, 5, 5, "troncon i1 vers i2");
        Troncon i1i3 = new Troncon(i3, 5, 5, "troncon i1 vers i3");
        List<Troncon> tronconsSortantsI1 = new ArrayList<>();
        tronconsSortantsI1.add(i1i2);
        tronconsSortantsI1.add(i1i3);
        i1.setSortants(tronconsSortantsI1);

        Intersection i4 = new Intersection(10,10,3);
        Troncon troncon4 = new Troncon(i4, 2, 2, "troncon");
        List<Troncon> tronconsSortants4 = new ArrayList<>();
        tronconsSortants4.add(troncon4);
        i4.setSortants(tronconsSortants4);

        assertEquals(TronconTest.comparerTronconsBool(i1i2, i4.getTronconVers(i2)), false);
        assertEquals(TronconTest.comparerTronconsBool(troncon4, i1.getTronconVers(i4)), false);
    }


    public static void comparerIntersectionsPrimitives(Intersection intersectionATester, Intersection intersectionType) {
    	assertNotNull(intersectionATester);
    	assertNotNull(intersectionType);
        List<Troncon> tronconsATester = intersectionATester.getSortants();
        List<Troncon> tronconsType = intersectionType.getSortants();
        assertEquals(tronconsATester.size(), tronconsType.size());
    }

    public static boolean comparerIntersectionsBool(Intersection intersectionATester, Intersection intersectionType) {
        if (!comparerIntersectionsPrimitivesBool(intersectionATester, intersectionType)){
            return false;
        }


        List<Troncon> tronconsATester = intersectionATester.getSortants();
        List<Troncon> tronconsType = intersectionType.getSortants();


        if (!(tronconsATester!=null && tronconsType!=null && tronconsATester.size()==tronconsType.size())){
            return false;
        }

        for(int i = 0; i < tronconsATester.size(); i++) {
            Troncon tronconATester = tronconsATester.get(i);
            Troncon tronconType = tronconsType.get(i);
            if(!TronconTest.comparerTronconsBool(tronconATester, tronconType)){
                return false;
            }
        }
        return true;
    }


    public static boolean comparerIntersectionsPrimitivesBool(Intersection intersectionATester, Intersection intersectionType) {
        List<Troncon> tronconsATester = intersectionATester.getSortants();
        List<Troncon> tronconsType = intersectionType.getSortants();

        return(tronconsATester.size() == tronconsType.size() && intersectionType!=null && intersectionATester!=null);
    }

    @Test
    public void testEquals() throws Exception{
        Intersection i1 = new Intersection(0,0,1);
        Intersection i2 = new Intersection(2,5,1);
        Intersection i3 = new Intersection(0,0,5);
        assertTrue(i1.equals(i1));
        assertTrue(i1.equals(i2));
        assertFalse(i1.equals(i3));
        assertFalse(i2.equals(i3));
    }

}