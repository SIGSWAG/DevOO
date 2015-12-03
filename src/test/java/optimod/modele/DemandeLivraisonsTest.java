package optimod.modele;

import optimod.es.xml.DeserialiseurXML;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Créé par aurelien le 18/11/15.
 */
public class DemandeLivraisonsTest {

    @Test
    public void testCalculerItineraire() throws Exception {

        final File xmlPlan1 = new File("src/test/resources/itineraire/plan1.xml");
        final Plan plan1 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan1, xmlPlan1);

        final File xmlLivraison1 = new File("src/test/resources/itineraire/livraison1.xml");
        final DemandeLivraisons demandeLivraisons1 = new DemandeLivraisons(plan1);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons1, xmlLivraison1);

        demandeLivraisons1.calculerItineraire();
        final List<Chemin> chemins1 = demandeLivraisons1.getItineraire();

        assertEquals(chemins1.size(), 2);
        assertEquals(chemins1.get(0).getDuree(), 1);
        assertEquals(chemins1.get(1).getDuree(), 1);
        assertEquals(chemins1.get(0).getArrivee().getIdClient(), 1);
        assertEquals(chemins1.get(1).getDepart().getIdClient(), 1);
        assertEquals(chemins1.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins1.get(1).getArrivee().getIdClient(), 0); // 0 = pas de client

        // ------------------------------------------------------------------------------

        final File xmlPlan2 = new File("src/test/resources/itineraire/plan2.xml");
        final Plan plan2 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan2, xmlPlan2);

        final File xmlLivraison2 = new File("src/test/resources/itineraire/livraison2.xml");
        final DemandeLivraisons demandeLivraisons2 = new DemandeLivraisons(plan2);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons2, xmlLivraison2);

        demandeLivraisons2.calculerItineraire();
        final List<Chemin> chemins2 = demandeLivraisons2.getItineraire();

        assertEquals(chemins2.size(), 2);
        assertEquals(chemins2.get(0).getDuree(), 2);
        assertEquals(chemins2.get(1).getDuree(), 2);
        assertEquals(chemins2.get(0).getArrivee().getIdClient(), 1);
        assertEquals(chemins2.get(1).getDepart().getIdClient(), 1);
        assertEquals(chemins2.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins2.get(1).getArrivee().getIdClient(), 0); // 0 = pas de client

        // ------------------------------------------------------------------------------

        final File xmlPlan3 = new File("src/test/resources/itineraire/plan3.xml");
        final Plan plan3 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan3, xmlPlan3);

        final File xmlLivraison3 = new File("src/test/resources/itineraire/livraison3.xml");
        final DemandeLivraisons demandeLivraisons3 = new DemandeLivraisons(plan3);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons3, xmlLivraison3);

        demandeLivraisons3.calculerItineraire();
        final List<Chemin> chemins3 = demandeLivraisons3.getItineraire();

        assertEquals(chemins3.size(), 3);
        assertEquals(chemins3.get(0).getDuree(), 1);
        assertEquals(chemins3.get(1).getDuree(), 1);
        assertEquals(chemins3.get(2).getDuree(), 1);
        assertEquals(chemins3.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins3.get(2).getArrivee().getIdClient(), 0); // 0 = pas de client
        // Ici, c'est chiant de tester tous les cas, car plusieurs solutions sont valides (on peut faire 123 ou 132)

        // ------------------------------------------------------------------------------

        final File xmlPlan4 = new File("src/test/resources/itineraire/plan4.xml");
        final Plan plan4 = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan4, xmlPlan4);

        final File xmlLivraison4 = new File("src/test/resources/itineraire/livraison4.xml");
        final DemandeLivraisons demandeLivraisons4 = new DemandeLivraisons(plan4);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons4, xmlLivraison4);

        demandeLivraisons4.calculerItineraire();
        final List<Chemin> chemins4 = demandeLivraisons4.getItineraire();

        assertEquals(chemins4.size(), 3);
        assertEquals(chemins4.get(0).getDuree(), 1);
        assertEquals(chemins4.get(1).getDuree(), 2);
        assertEquals(chemins4.get(2).getDuree(), 1);
        assertEquals(chemins4.get(0).getDepart().getIdClient(), 0); // 0 = pas de client
        assertEquals(chemins4.get(2).getArrivee().getIdClient(), 0); // 0 = pas de client
        // Ici, c'est chiant de tester tous les cas, car plusieurs solutions sont valides (on peut faire 231 ou 213)

        // ------------------------------------------------------------------------------

        // TODO encore d'autres cas à tester... en vrai c'est sans fin

    }

    @Test
    public void testAjouterLivraison() throws Exception {

        final File xmlPlan = new File("src/test/resources/modification/plan-test.xml");
        final Plan plan = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan, xmlPlan);

        final File xmlLivraison = new File("src/test/resources/modification/livraison-test-ajout.xml");
        final DemandeLivraisons demandeLivraisons = new DemandeLivraisons(plan);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons, xmlLivraison);

        final List<Intersection> intersections = plan.getIntersections();

        // ! Cette portion peut jeter des NoSuchElementExceptions ! Normalement il n'y a pas de problème car le plan est correct, mais il faut faire très attention en utilisant Optional.get().
        final Intersection intersectionNouvelleLivraison = intersections.stream().filter(i -> i.getAdresse() == 99).findFirst().get(); // Retrouve l'intersection sans livraison (la numéro 99)
        final Intersection intersectionApres = intersections.stream().filter(i -> i.getAdresse() == 2).findFirst().get();

        final Livraison nouvelleLivraison = new Livraison(intersectionNouvelleLivraison);
        final FenetreLivraison fenetreLivraison = demandeLivraisons.getFenetres().get(0); // La seule fenêtre...

        demandeLivraisons.calculerItineraire();
        demandeLivraisons.ajouterLivraison(nouvelleLivraison, intersectionApres.getLivraison(), fenetreLivraison);

        assertTrue(fenetreLivraison.getLivraisons().contains(nouvelleLivraison));
        assertTrue(nouvelleLivraison.initeraireCalcule());
        assertEquals(nouvelleLivraison.getIntersection(), intersectionNouvelleLivraison);
        assertEquals(intersectionApres.getLivraison().getPrecedente(), nouvelleLivraison);

    }

    @Test
    public void testSupprimerLivraisonEntrepot() {
        Plan plan = new Plan();
        final Intersection intersection1 = new Intersection(0, 0, 0, null);

        final Livraison livraisonASupprimer = new Livraison(intersection1);
        DemandeLivraisons demandeLivraisons = new DemandeLivraisons(plan);
        demandeLivraisons.setEntrepot(livraisonASupprimer);

        boolean supprime = demandeLivraisons.supprimerLivraison(livraisonASupprimer);

        assertEquals(supprime, false);
    }

    @Test
    public void testSupprimerNouvelleLivraison() throws Exception {

        final File xmlPlan = new File("src/test/resources/modification/plan-test.xml");
        final Plan plan = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan, xmlPlan);

        final File xmlLivraison = new File("src/test/resources/modification/livraison-test-suppression.xml");
        final DemandeLivraisons demandeLivraisons = new DemandeLivraisons(plan);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons, xmlLivraison);

        final List<Intersection> intersections = plan.getIntersections();

        // ! Cette portion peut jeter des NoSuchElementExceptions ! Normalement il n'y a pas de problème car le plan est correct, mais il faut faire très attention en utilisant Optional.get().
        final Intersection intersectionAvant = intersections.stream().filter(i -> i.getAdresse() == 1).findFirst().get();
        final Intersection intersectionSupprimerLivraison = intersections.stream().filter(i -> i.getAdresse() == 99).findFirst().get(); // Retrouve l'intersection sans livraison (la numéro 99)
        final Intersection intersectionApres = intersections.stream().filter(i -> i.getAdresse() == 2).findFirst().get();

        final Livraison livraisonASupprimer = intersectionSupprimerLivraison.getLivraison();
        final FenetreLivraison fenetreLivraison = demandeLivraisons.getFenetres().get(0); // La seule fenêtre...

        demandeLivraisons.calculerItineraire();
        demandeLivraisons.supprimerLivraison(livraisonASupprimer);

        assertFalse(fenetreLivraison.getLivraisons().contains(livraisonASupprimer));
        assertNull(intersectionSupprimerLivraison.getLivraison());
        assertTrue(intersectionApres.getLivraison().getPrecedente().equals(intersectionAvant.getLivraison()) || intersectionAvant.getLivraison().getPrecedente().equals(intersectionApres.getLivraison())); // En fonction du sens de l'itinéraire calculé

    }

    @Test
    public void testEchangerLivraison() throws Exception {

        final File xmlPlan = new File("src/test/resources/modification/plan-test.xml");
        final Plan plan = new Plan();
        DeserialiseurXML.INSTANCE.chargerPlan(plan, xmlPlan);

        final File xmlLivraison = new File("src/test/resources/modification/livraison-test-ajout.xml");
        final DemandeLivraisons demandeLivraisons = new DemandeLivraisons(plan);
        DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisons, xmlLivraison);

        final List<Intersection> intersections = plan.getIntersections();

        // ! Cette portion peut jeter des NoSuchElementExceptions ! Normalement il n'y a pas de problème car le plan est correct, mais il faut faire très attention en utilisant Optional.get().
        final Intersection intersection1 = intersections.stream().filter(i -> i.getAdresse() == 1).findFirst().get();
        final Intersection intersection2 = intersections.stream().filter(i -> i.getAdresse() == 2).findFirst().get();

        demandeLivraisons.calculerItineraire();
        final boolean _1Avant2 = intersection2.getLivraison().getPrecedente().equals(intersection1.getLivraison());
        demandeLivraisons.echangerLivraison(intersection1.getLivraison(), intersection2.getLivraison());

        if (_1Avant2) {
            assertEquals(intersection1.getLivraison().getPrecedente(), intersection2.getLivraison());
        } else {
            assertEquals(intersection2.getLivraison().getPrecedente(), intersection1.getLivraison());
        }

    }



}