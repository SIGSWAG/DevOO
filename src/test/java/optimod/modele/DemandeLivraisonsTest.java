package optimod.modele;

import optimod.es.xml.DeserialiseurXML;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aurelien on 18/11/15.
 */
public class DemandeLivraisonsTest {

    private static DemandeLivraisons demandeLivraisons;

    @BeforeClass
    public static void oneTimeSetUp() {
        Plan plan = new Plan();
        demandeLivraisons = new DemandeLivraisons(plan);
    }

    @Test
    public void testCalculerItineraire() throws Exception {

        File xmlPlan1 = new File("src/test/resources/itineraire/plan1.xml");
        Plan plan1 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan1, xmlPlan1);

        File xmlLivraison1 = new File("src/test/resources/itineraire/livraison1.xml");
        DemandeLivraisons demandeLivraisons1 = new DemandeLivraisons(plan1);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons1, xmlLivraison1);

        demandeLivraisons1.calculerItineraire();
        List<Chemin> chemins1 = demandeLivraisons1.getItineraire();

        assertEquals(chemins1.size(), 2);
        assertEquals(chemins1.get(0).getDuree(), 1);
        assertEquals(chemins1.get(1).getDuree(), 1);
        assertEquals(chemins1.get(0).getArrivee().getIdClient(), 1);
        assertEquals(chemins1.get(1).getDepart().getIdClient(), 1);
        assertEquals(chemins1.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins1.get(1).getArrivee().getIdClient(), 0); // 0 = pas de client

        // ------------------------------------------------------------------------------

        File xmlPlan2 = new File("src/test/resources/itineraire/plan2.xml");
        Plan plan2 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan2, xmlPlan2);

        File xmlLivraison2 = new File("src/test/resources/itineraire/livraison2.xml");
        DemandeLivraisons demandeLivraisons2 = new DemandeLivraisons(plan2);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons2, xmlLivraison2);

        demandeLivraisons2.calculerItineraire();
        List<Chemin> chemins2 = demandeLivraisons2.getItineraire();

        assertEquals(chemins2.size(), 2);
        assertEquals(chemins2.get(0).getDuree(), 2);
        assertEquals(chemins2.get(1).getDuree(), 2);
        assertEquals(chemins2.get(0).getArrivee().getIdClient(), 1);
        assertEquals(chemins2.get(1).getDepart().getIdClient(), 1);
        assertEquals(chemins2.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins2.get(1).getArrivee().getIdClient(), 0); // 0 = pas de client

        // ------------------------------------------------------------------------------

        File xmlPlan3 = new File("src/test/resources/itineraire/plan3.xml");
        Plan plan3 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan3, xmlPlan3);

        File xmlLivraison3 = new File("src/test/resources/itineraire/livraison3.xml");
        DemandeLivraisons demandeLivraisons3 = new DemandeLivraisons(plan3);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons3, xmlLivraison3);

        demandeLivraisons3.calculerItineraire();
        List<Chemin> chemins3 = demandeLivraisons3.getItineraire();

        assertEquals(chemins3.size(), 3);
        assertEquals(chemins3.get(0).getDuree(), 1);
        assertEquals(chemins3.get(1).getDuree(), 1);
        assertEquals(chemins3.get(2).getDuree(), 1);
        assertEquals(chemins3.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins3.get(2).getArrivee().getIdClient(), 0); // 0 = pas de client
        // Ici, c'est chiant de tester tous les cas, car plusieurs solutions sont valides (on peut faire 123 ou 132)

        // ------------------------------------------------------------------------------

        File xmlPlan4 = new File("src/test/resources/itineraire/plan4.xml");
        Plan plan4 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan4, xmlPlan4);

        File xmlLivraison4 = new File("src/test/resources/itineraire/livraison4.xml");
        DemandeLivraisons demandeLivraisons4 = new DemandeLivraisons(plan4);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons4, xmlLivraison4);

        demandeLivraisons4.calculerItineraire();
        List<Chemin> chemins4 = demandeLivraisons4.getItineraire();

        assertEquals(chemins4.size(), 3);
        assertEquals(chemins4.get(0).getDuree(), 1);
        assertEquals(chemins4.get(1).getDuree(), 2);
        assertEquals(chemins4.get(2).getDuree(), 1);
        assertEquals(chemins4.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins4.get(2).getArrivee().getIdClient(), 0); // 0 = pas de client
        // Ici, c'est chiant de tester tous les cas, car plusieurs solutions sont valides (on peut faire 231 ou 213)

        // ------------------------------------------------------------------------------

        // TODO pleins d'autres cas à tester...

    }

    @Test
    public void testAjouterLivraison() throws Exception {

        //TODO tester les fenetres de livraisons
        final Intersection nouvelleIntersection = new Intersection(0, 0, 0, null);
        final Livraison livraisonAvant = new Livraison(new Intersection(1, 1, 1, null));
        final Livraison livraisonApres = new Livraison(new Intersection(2, 2, 2, null));

        Livraison nouvelleLivraison = new Livraison(nouvelleIntersection);
        demandeLivraisons.ajouterLivraison(nouvelleLivraison, livraisonAvant,null);

        assertNotNull(nouvelleLivraison);
        LivraisonTest.comparerLivraisons(nouvelleLivraison.getPrecedente(), livraisonAvant);

        LivraisonTest.comparerLivraisons(livraisonApres.getPrecedente(), nouvelleLivraison);
    }

    @Test
    public void testSupprimerLivraison() throws Exception {

        final Intersection intersection = new Intersection(0, 0, 0, null);
        final Livraison livraisonASupprimer = new Livraison(intersection);

        demandeLivraisons.supprimerLivraison(livraisonASupprimer);

        assertNull(intersection.getLivraison());

    }

    @Test
    public void testEchangerLivraison() throws Exception {

    }

}