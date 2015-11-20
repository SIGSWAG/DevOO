package modele;

import optimod.modele.Chemin;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Troncon;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by aurelien on 18/11/15.
 */
public class LivraisonTest {

    @Test
    public void testCalculPCC1() throws Exception {

        /**
         * Test Case 1
         */
        Intersection intersection1 = new Intersection(0, 0, 1, null);
        Intersection intersection2 = new Intersection(0, 0, 2, null);
        Intersection intersection3 = new Intersection(0, 0, 3, null);
        Intersection intersection4 = new Intersection(0, 0, 4, null);

        /**Intersection 1**/
        Troncon troncon1 = new Troncon(intersection2, 1, 1, "2.1");
        Troncon troncon2 = new Troncon(intersection3, 1, 2, "3.1");

        List<Troncon> tr1 = new ArrayList<Troncon>();
        tr1.add(troncon1);
        tr1.add(troncon2);

        intersection1.setSortants(tr1);

        /**Intersection 2**/
        Troncon troncon3 = new Troncon(intersection4, 1, 2, "4.1");


        List<Troncon> tr2 = new ArrayList<Troncon>();
        tr1.add(troncon3);


        intersection2.setSortants(tr2);

        /**Intersection 3**/
        Troncon troncon4 = new Troncon(intersection2, 1, 4, "2.2");
        Troncon troncon5 = new Troncon(intersection4, 1, 3, "4.2");

        List<Troncon> tr3 = new ArrayList<Troncon>();
        tr1.add(troncon4);
        tr1.add(troncon5);

        intersection3.setSortants(tr3);


        Livraison livraison = new Livraison(intersection1);
        intersection1.setLivraison(livraison);

        Livraison livraison4 = new Livraison(intersection4);
        intersection4.setLivraison(livraison4);

        Chemin chemin = livraison.calculPCC(livraison4);

        assertEquals(chemin.getArrivee(), livraison4);
        assertEquals(chemin.getDepart(), livraison);
        assertEquals(chemin.getIntersections().size(), 2);
        assertEquals(chemin.getIntersections().get(0), intersection2);

    }


}