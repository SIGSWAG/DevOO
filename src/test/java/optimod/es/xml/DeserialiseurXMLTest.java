package optimod.es.xml;

import optimod.modele.Intersection;
import optimod.modele.Plan;
import optimod.modele.Troncon;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by (PRO) Loïc Touzard on 25/11/2015.
 */
public class DeserialiseurXMLTest {

	/**
	* Test de chargement d'un plan avec deux noeuds et deux intersections
	**/
	@Test
	public void testChargerPlanOk() {
		Plan planAComparer = new Plan();
		List<Intersection> intersections = new ArrayList<>();
		Intersection intersection1 = new Intersection(63, 100, 0);
		Intersection intersection2 = new Intersection(88, 171, 1);

		List<Troncon> troncons = new ArrayList<>();
		Troncon troncon1 = new Troncon(intersection2, 3.9, 602.1, "v0");
		Troncon troncon2 = new Troncon(intersection1, 4.1, 602.1, "v0");

		troncons.add(troncon1);
		intersection1.setSortants(troncons);

		troncons.clear();
		troncons.add(troncon2);
		intersection2.setSortants(troncons);

		intersections.add(intersection1);
		intersections.add(intersection2);

		planAComparer.setIntersections(intersections);

		File xml = new File("src/test/resources/exemples/plan2x1.xml");
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, true);

        // Vérification des intersections

        List<Intersection> intersectionsPlan = plan.getIntersections();
        Intersection intersectionPlan1 = intersectionsPlan.get(0);
        Intersection intersectionPlan2 = intersectionsPlan.get(1);

    	assertEquals(intersectionPlan1.getX(), intersection1.getX());
    	assertEquals(intersectionPlan1.getY(), intersection1.getY());
    	assertEquals(intersectionPlan1.getAdresse(), intersection1.getAdresse());

    	assertEquals(intersectionPlan2.getX(), intersection2.getX());
    	assertEquals(intersectionPlan2.getY(), intersection2.getY());
    	assertEquals(intersectionPlan2.getAdresse(), intersection2.getAdresse());

    	// Vérification des troncons

    	List<Troncon> tronconsPlan = intersectionPlan1.getSortants();
    	Troncon tronconPlan1 = tronconsPlan.get(0);
    	tronconsPlan = intersectionPlan2.getSortants();
    	Troncon tronconPlan2 = tronconsPlan.get(0);

		assertEquals(tronconPlan1.getVitesse(), troncon1.getVitesse());
		assertEquals(tronconPlan1.getLongueur(), troncon1.getLongueur());
		assertEquals(tronconPlan1.getNom(), troncon1.getNom());

		assertEquals(tronconPlan2.getVitesse(), troncon2.getVitesse());
		assertEquals(tronconPlan2.getLongueur(), troncon2.getLongueur());
		assertEquals(tronconPlan2.getNom(), troncon2.getNom());

		// Vérification de l'intersection d'arrivée

		Intersection tronconPlan1Arrivee = tronconPlan1.getArrivee();
		Intersection tronconPlan2Arrivee = tronconPlan2.getArrivee();

		assertEquals(tronconPlan1Arrivee.getX(), intersection2.getX());
		assertEquals(tronconPlan1Arrivee.getY(), intersection2.getY());
		assertEquals(tronconPlan1Arrivee.getAdresse(), intersection2.getAdresse());

		assertEquals(tronconPlan2Arrivee.getX(), intersection1.getX());
		assertEquals(tronconPlan2Arrivee.getY(), intersection1.getY());
		assertEquals(tronconPlan2Arrivee.getAdresse(), intersection1.getAdresse());


	}

    @Test
    public void testChargerPlanPlanVide() throws Exception {
        File xml = new File("src/test/resources/fail/plan-vide.xml");
        Plan plan = new Plan();
        boolean charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);

        // TODO A priori le getElementsByTagName sur Noeud ne pas va fonctionner, mais on dirait pas que c'est géré

        assertEquals(charge, false);
    }

    @Test
    public void testChargerDemandeLivraison() throws Exception {

    }

    @Test
    public void testChargerPlanPlanNull() {
    	File xml = new File("src/test/resources/fail/plan-vide.xml");
        Plan plan = null;
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanXmlNull() {
    	File xml = null;
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanPlanEtXmlNull() {
    	File xml = null;
        Plan plan = null;
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanXmlNonConforme() {
    	File xml = new File("src/test/resources/fail/plan-xml-invalide.xml");
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanXmlVide() {
    	File xml = new File("src/test/resources/fail/plan-xml-vide.xml");
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, false);
    }

    @Test
    public void testChargerPlanReseauManquant() {
    	File xml = new File("src/test/resources/fail/plan-reseau-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } 
        catch(Exception e) {
        	assertEquals(e.getMessage(), "Document non conforme");
        }
    }

    @Test
    public void testChargerPlanSansTroncon() {
    	File xml = new File("src/test/resources/fail/plan-troncon-manquant.xml");
        Plan plan = new Plan();
        boolean charge = false;
        try {
            charge = DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML exceptionXML) {
            exceptionXML.printStackTrace();
        }

        assertEquals(charge, false);
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

    public void testChargerPlanIdManquant() {
    	File xml = new File("src/test/resources/fail/plan-id-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }

    public void testChargerPlanXManquant() {
    	File xml = new File("src/test/resources/fail/plan-x-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }

    public void testChargerPlanYManquant() {
    	File xml = new File("src/test/resources/fail/plan-y-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }

    public void testChargerPlanNomRueManquant() {
    	File xml = new File("src/test/resources/fail/plan-nomRue-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }

    public void testChargerPlanVitesseManquante() {
    	File xml = new File("src/test/resources/fail/plan-vitesse-manquante.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }

    
    public void testChargerPlanLongueurManquante() {
    	File xml = new File("src/test/resources/fail/plan-longueur-manquante.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }

    public void testChargerPlanIdNoeudDestinationManquant() {
    	File xml = new File("src/test/resources/fail/plan-idNoeudDestination-manquant.xml");
        Plan plan = new Plan();
        try {
        	DeserialiseurXML.INSTANCE.chargerPlan(plan, xml);
        }
        catch(Exception e) {
        	assertEquals(e.getMessage(), "");
        }
    }


}