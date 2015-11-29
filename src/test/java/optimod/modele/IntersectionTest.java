package optimod.modele;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by aurelien on 18/11/15.
 */
public class IntersectionTest {

    @Test
    public void testEstLocalisee() throws Exception {

        Intersection intersection = new Intersection(0, 0, 15);

        assertTrue(intersection.estLocalisee(0, 0, 1));
        assertFalse(intersection.estLocalisee(2, 2, 1));
        assertTrue(intersection.estLocalisee(5, 5, 10));
        assertTrue(intersection.estLocalisee(0, 1, 1));

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

        assertEquals(i1.getTronconVers(i2), i1i2);
        assertEquals(i1.getTronconVers(i3), i1i3);
    }
}