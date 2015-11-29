package optimod.modele;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        tr2.add(troncon3);


        intersection2.setSortants(tr2);

        /**Intersection 3**/
        Troncon troncon4 = new Troncon(intersection2, 1, 4, "2.2");
        Troncon troncon5 = new Troncon(intersection4, 1, 3, "4.2");

        List<Troncon> tr3 = new ArrayList<Troncon>();
        tr3.add(troncon4);
        tr3.add(troncon5);

        intersection3.setSortants(tr3);


        Livraison livraison = new Livraison(intersection1);
        intersection1.setLivraison(livraison);

        Livraison livraison4 = new Livraison(intersection4);
        intersection4.setLivraison(livraison4);

        Chemin chemin = livraison.calculPCC(livraison4);

        comparerLivraisons(chemin.getArrivee(), livraison4);
        comparerLivraisons(chemin.getDepart(), livraison);
        assertEquals(chemin.getTroncons().size(), 2);
        IntersectionTest.comparerIntersections(chemin.getTroncons().get(0).getArrivee(), intersection2);

    }

    public static void comparerLivraisons(Livraison livraisonATester, Livraison livraisonType) {
        comparerLivraisonsPrimitives(livraisonATester, livraisonType);

        IntersectionTest.comparerIntersections(livraisonATester.getIntersection(), livraisonType.getIntersection());

        comparerLivraisonsPrimitives(livraisonATester.getPrecedente(), livraisonType.getPrecedente());        
    }

    public static void comparerLivraisonsPrimitives(Livraison livraisonATester, Livraison livraisonType) {
        assertNotNull(livraisonATester);
        assertNotNull(livraisonType);

        assertEquals(livraisonATester.getHeureLivraison(), livraisonType.getHeureLivraison());
        assertEquals(livraisonATester.getHeureDebutFenetre(), livraisonType.getHeureDebutFenetre());
        assertEquals(livraisonATester.getHeureFinFenetre(), livraisonType.getHeureFinFenetre());
        assertEquals(livraisonATester.getIdClient(), livraisonType.getIdClient());
    }


}