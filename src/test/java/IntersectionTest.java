

import optimod.modele.Intersection;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aurelien on 18/11/15.
 */
public class IntersectionTest {

    @Test
    public void testEstLocalisee() throws Exception {


        Intersection intersection = new Intersection(0,0,15,null);

        assertTrue(intersection.estLocalisee(0,0,1));
        assertFalse(intersection.estLocalisee(2,2,1));
        assertTrue(intersection.estLocalisee(5,5,10));
        assertTrue(intersection.estLocalisee(0,1,1));

    }
}