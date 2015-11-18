package java;

import optimod.modele.DemandeLivraison;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Plan;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aurelien on 18/11/15.
 */
public class DemandeLivraisonTest {

    private static DemandeLivraison demandeLivraison;

    @BeforeClass
    public static void oneTimeSetUp() {
        Plan plan = new Plan();
        demandeLivraison = new DemandeLivraison(plan);
    }

    @Test
    public void testChargerDemandeLivraison() throws Exception {
        // TODO à implémenter
    }

    @Test
    public void testCalculerItineraire() throws Exception {
        // TODO à implémenter
    }

    @Test
    public void testAjouterLivraison() throws Exception {

        final Intersection nouvelleIntersection = new Intersection(0, 0, 0, null);
        final Livraison livraisonAvant = new Livraison(new Intersection(1, 1, 1, null));
        final Livraison livraisonApres = new Livraison(new Intersection(2, 2, 2, null));

        demandeLivraison.ajouterLivraison(nouvelleIntersection, livraisonAvant);

        final Livraison nouvelleLivraison = nouvelleIntersection.getLivraison();

        assertNotNull(nouvelleLivraison);
        assertEquals(nouvelleLivraison.getPrecedente(), livraisonAvant);
        assertEquals(livraisonApres.getPrecedente(), nouvelleLivraison);

    }

    @Test
    public void testSupprimerLivraison() throws Exception {

        final Intersection intersection = new Intersection(0, 0, 0, null);
        final Livraison livraisonASupprimer = new Livraison(intersection);

        demandeLivraison.supprimerLivraison(livraisonASupprimer);

        assertNull(intersection.getLivraison());

    }

    @Test
    public void testEchangerLivraison() throws Exception {
        // TODO à voir avec l'hexanome ce que signifie vraiment "échanger"
    }
}