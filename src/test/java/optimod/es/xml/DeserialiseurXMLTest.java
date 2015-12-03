package optimod.es.xml;

import optimod.modele.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by (PRO) Loïc Touzard on 25/11/2015.
 */
public class DeserialiseurXMLTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

	/**
	* Test de chargement d'un plan avec deux noeuds et deux intersections
	**/
	@Test
    public void testChargerPlanOk() {
        Plan planAComparer = new Plan();
		List<Intersection> intersections = new ArrayList<>();
		Intersection intersection1 = new Intersection(63, 100, 0);
		Intersection intersection2 = new Intersection(88, 171, 1);

		Troncon troncon1 = new Troncon(intersection2, 3.9, 602.1, "v0");
		Troncon troncon2 = new Troncon(intersection1, 4.1, 602.1, "v0");

		intersection1.setSortants(Arrays.asList(troncon1));

		intersection2.setSortants(Arrays.asList(troncon2));

		intersections.add(intersection1);
		intersections.add(intersection2);

		planAComparer.setIntersections(intersections);

		File xml = new File("src/test/resources/exemples/plan2x1.xml");
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            fail();
        }

        assertEquals(charge, true);

        // Vérification des intersections

        List<Intersection> intersectionsPlan = plan.getIntersections();
        Intersection intersectionPlan1 = intersectionsPlan.get(0);
        Intersection intersectionPlan2 = intersectionsPlan.get(1);

        IntersectionTest.comparerIntersections(intersectionPlan1, intersection1);
        IntersectionTest.comparerIntersections(intersectionPlan2, intersection2);

    	// Vérification des troncons


    	List<Troncon> tronconsPlan = intersectionPlan1.getSortants();
    	Troncon tronconPlan1 = tronconsPlan.get(0);
    	tronconsPlan = intersectionPlan2.getSortants();
    	Troncon tronconPlan2 = tronconsPlan.get(0);

        TronconTest.comparerTroncons(tronconPlan1, troncon1);
        TronconTest.comparerTroncons(tronconPlan2, troncon2);

		// Vérification de l'intersection d'arrivée

		Intersection tronconPlan1Arrivee = tronconPlan1.getArrivee();
		Intersection tronconPlan2Arrivee = tronconPlan2.getArrivee();

        IntersectionTest.comparerIntersections(tronconPlan1Arrivee, intersection2);
        IntersectionTest.comparerIntersections(tronconPlan2Arrivee, intersection1);
	}

    @Test
    public void testChargerPlanPlanVide() {
        File xml = new File("src/test/resources/fail/plan-vide.xml");
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (Exception e) {

        }

        assertEquals(charge, true);
        assertEquals(plan.getIntersections().size(), 0);
    }

    @Test
    public void testChargerPlanPlanNull() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        File xml = new File("src/test/resources/fail/plan-vide.xml");
        Plan plan = null;
        boolean charge = false;
        charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanXmlNull() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        File xml = null;
        Plan plan = new Plan();
        boolean charge = false;

        charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanPlanEtXmlNull()  {
        File xml = null;
        Plan plan = null;
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (Exception e) {
            fail();
        }

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanXmlNonConforme() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        File xml = new File("src/test/resources/fail/plan-xml-invalide.xml");
        Plan plan = new Plan();
        boolean charge = false;

        exception.expect(SAXException.class);
        charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanXmlVide() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        File xml = new File("src/test/resources/fail/plan-xml-vide.xml");
        Plan plan = new Plan();
        boolean charge = false;

        exception.expect(SAXException.class);
        charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);

    }


    @Test
    public void testChargerPlanReseauManquant() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
    	File xml = new File("src/test/resources/fail/plan-reseau-manquant.xml");
        Plan plan = new Plan();

        exception.expect(ExceptionXML.class);
        exception.expectMessage("Document non conforme la racine du document doit être un Reseau");
        DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
    }

    @Test
    public void testChargerPlanSansTroncon() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        File xml = new File("src/test/resources/fail/plan-troncon-manquant.xml");
        Plan plan = new Plan();
        boolean charge = false;
        exception.expect(ExceptionXML.class);
        exception.expectMessage("Erreur lors de la lecture du fichier : Un Noeud doit avoir au moins un élément leTronconSortant");
        charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
    }

    @Test
    public void testChargerPlanIdNoeudDestinationInvalide() {
    	File xml = new File("src/test/resources/fail/plan-idNoeudDestination-invalide.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : Un leTronconSortant possède un NoeudDestination inconnu");
        }
    }

    @Test
    public void testChargerPlanAttributManquant() {
    	// id
    	testChargerPlanIdManquant();

    	// x
		testChargerPlanXManquant();

    	// y
		testChargerPlanYManquant();

    	// nomRue
    	testChargerPlanNomRueManquant();

    	// vitesse
    	testChargerPlanVitesseManquante();

    	// longueur
    	testChargerPlanLongueurManquante();

    	// idNoeudDestination
    	testChargerPlanIdNoeudDestinationManquant();

    }

    public void testChargerPlanIdManquant()  {
    	File xml = new File("src/test/resources/fail/plan-id-manquant.xml");
        Plan plan = new Plan();

        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être défini", e.getMessage());
            }
        }
    }

    public void testChargerPlanXManquant() {
    	File xml = new File("src/test/resources/fail/plan-x-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : La coordonnée x d'un Noeud doit être définie");
            }
        }
    }

    public void testChargerPlanYManquant() {
    	File xml = new File("src/test/resources/fail/plan-y-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : La coordonnée y d'un Noeud doit être définie");
            }
        }
    }

    public void testChargerPlanNomRueManquant() {
    	File xml = new File("src/test/resources/fail/plan-nomRue-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : Le nom d'un leTronconSortant doit être renseigné");
            }
        }
    }

    public void testChargerPlanVitesseManquante() {
    	File xml = new File("src/test/resources/fail/plan-vitesse-manquante.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : La vitesse d'un leTronconSortant doit être définie");
            }
        }
    }


    public void testChargerPlanLongueurManquante() {
    	File xml = new File("src/test/resources/fail/plan-longueur-manquante.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : La longueur d'un leTronconSortant doit être définie");
            }
        }
    }

    public void testChargerPlanIdNoeudDestinationManquant() {
    	File xml = new File("src/test/resources/fail/plan-idNoeudDestination-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
            if(e instanceof ExceptionXML) {
                assertEquals(e.getMessage(), "Erreur lors de la lecture du fichier : L'idNoeudDestination d'un leTronconSortant doit être défini");
            }
        }
    }

    /**
     * Test de chargement d'une demande de livraison
     **/
    @Test
    public void testChargerDemandeLivraisonOk() {
        // chargement des plans
        Plan planAttendu = new Plan();
        Plan planObtenu = new Plan();
        DemandeLivraisons demandeLivraisonsAttendue = new DemandeLivraisons(planAttendu);
        DemandeLivraisons demandeLivraisonsObtenue = new DemandeLivraisons(planObtenu);
        boolean chargePlan = false;
        try {
            chargePlan = DeserialiseurXML.INSTANCE.chargerPlan(planAttendu, new File("src/test/resources/exemples/plan10x10.xml"));
            chargePlan &= DeserialiseurXML.INSTANCE.chargerPlan(planObtenu, new File("src/test/resources/exemples/plan10x10.xml"));
        }
        catch(Exception e) {
            fail();
        }
        assertTrue(chargePlan);

        Livraison entrepot = new Livraison(planAttendu.trouverIntersection(14));
        entrepot.getIntersection().setLivraison(entrepot);
        demandeLivraisonsAttendue.setEntrepot(entrepot);
        List<FenetreLivraison> fenetres = new ArrayList<>();

        // fenetre 1
        List<Livraison> listeLivr1 = new ArrayList<>();
        int tempsDebF1 = 8*3600;
        int tempsFinF1 = 9*3600+30*60;
        Livraison liv1 = new Livraison(planAttendu.trouverIntersection(97), tempsDebF1, tempsFinF1, 329);
        liv1.getIntersection().setLivraison(liv1);
        listeLivr1.add(liv1);
        fenetres.add(new FenetreLivraison(listeLivr1, tempsDebF1, tempsFinF1));

        // fenetre 2
        List<Livraison> listeLivr2 = new ArrayList<>();
        int tempsDebF2 = 9*3600+30*60;
        int tempsFinF2 = 11*3600;
        Livraison liv2 = new Livraison(planAttendu.trouverIntersection(88), tempsDebF2, tempsFinF2, 838);
        liv2.getIntersection().setLivraison(liv2);
        listeLivr2.add(liv2);
        Livraison liv3 = new Livraison(planAttendu.trouverIntersection(34), tempsDebF2, tempsFinF2, 479);
        liv3.getIntersection().setLivraison(liv3);
        listeLivr2.add(liv3);
        fenetres.add(new FenetreLivraison(listeLivr2, tempsDebF2, tempsFinF2));

        fenetres.sort((o1, o2) -> o1.getHeureDebut() - o2.getHeureDebut());
        demandeLivraisonsAttendue.setFenetres(fenetres);


        boolean chargeDemandeLivraison = false;
        try {
            chargeDemandeLivraison = DeserialiseurXML.INSTANCE.chargerDemandeLivraison(demandeLivraisonsObtenue, new File("src/test/resources/exemples/livraison10x10-test-1.xml"));
        }
        catch(Exception e) {
            fail();
        }

        assertTrue(chargeDemandeLivraison);

        // vérification des fenetres
        assertEquals(demandeLivraisonsAttendue.getFenetres().size(), demandeLivraisonsObtenue.getFenetres().size());
        for (int i = 0; i < demandeLivraisonsAttendue.getFenetres().size(); i++) {
            assertEquals(demandeLivraisonsAttendue.getFenetres().get(i).getHeureDebut(),demandeLivraisonsObtenue.getFenetres().get(i).getHeureDebut());
            assertEquals(demandeLivraisonsAttendue.getFenetres().get(i).getHeureFin(),demandeLivraisonsObtenue.getFenetres().get(i).getHeureFin());

            // verification des livraison
            for (int j = 0; j < demandeLivraisonsAttendue.getFenetres().get(i).getLivraisons().size(); j++) {
                LivraisonTest.comparerLivraisonsPrimitives(demandeLivraisonsAttendue.getFenetres().get(i).getLivraisons().get(j),demandeLivraisonsObtenue.getFenetres().get(i).getLivraisons().get(j));
                IntersectionTest.comparerIntersections(demandeLivraisonsAttendue.getFenetres().get(i).getLivraisons().get(j).getIntersection(),demandeLivraisonsObtenue.getFenetres().get(i).getLivraisons().get(j).getIntersection());
            }
        }

        // verification de l'entrepot
        LivraisonTest.comparerLivraisonsPrimitives(demandeLivraisonsAttendue.getEntrepot(), demandeLivraisonsObtenue.getEntrepot());

    }

}