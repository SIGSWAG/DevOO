package java;

import optimod.modele.Intersection;
import optimod.modele.Livraison;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aurelien on 18/11/15.
 */
public class LivraisonTest {

    @Test
    public void testCalculPCC() throws Exception {
        Intersection intersection = new Intersection(0,0,1);

        Livraison livraison  = new Livraison(intersection);
        intersection.setLivraison(livraison);



    }
}