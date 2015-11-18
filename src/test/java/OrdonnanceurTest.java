

import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aurelien on 18/11/15.
 */
public class OrdonnanceurTest {

    private static Ordonnanceur ordonnanceur;

    @BeforeClass
    public static void oneTimeSetUp() {
        ordonnanceur = new Ordonnanceur();
    }

    @Test
    public void testChargerPlan() throws Exception {
        // TODO à implémenter
    }

    @Test
    public void testTrouverIntersection() throws Exception {
        // TODO à implémenter
    }

    @Test
    public void testTrouverIntersection1() throws Exception {
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

        ordonnanceur.ajouterLivraison(nouvelleIntersection, livraisonAvant);

        final Livraison nouvelleLivraison = nouvelleIntersection.getLivraison();

        assertNotNull(nouvelleLivraison);
        assertEquals(nouvelleLivraison.getPrecedente(), livraisonAvant);
        assertEquals(livraisonApres.getPrecedente(), nouvelleLivraison);

    }

    @Test
    public void testSupprimerLivraison() throws Exception {

        final Intersection intersection = new Intersection(0, 0, 0, null);
        final Livraison livraisonASupprimer = new Livraison(intersection);

        ordonnanceur.supprimerLivraison(livraisonASupprimer);

        assertNull(intersection.getLivraison());

    }

    @Test
    public void testEchangerLivraison() throws Exception {
        // TODO à voir avec l'hexanome ce que signifie vraiment "échanger"
    }
}