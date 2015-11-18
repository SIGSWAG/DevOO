package optimod.es.xml;

import optimod.modele.Intersection;
import optimod.modele.Plan;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Loïc Touzard on 18/11/2015.
 */
public class DeserialiseurXML { // Singleton
    private static DeserialiseurXML instance = new DeserialiseurXML();

    public static DeserialiseurXML getInstance() {
        return instance;
    }

    private DeserialiseurXML() {
    }

    /**
     * Ouvre un fichier xml et cree plan a partir du contenu du fichier
     * @param plan Plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static void chargerPlan(Plan plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();

        // TODO vérifications dtd xsl
        if (racine.getNodeName().equals("Reseau")) {
            construirePlanAPartirDeDOMXML(racine, plan);
        }
        else
            throw new ExceptionXML("Document non conforme");
    }

    private static void construirePlanAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML, NumberFormatException{
        // Parcours de tous les Noeud (Intersections)
        NodeList listeNoeuds = noeudDOMRacine.getElementsByTagName("Noeud");
        Map<Integer, Intersection> intersections = new HashMap();
        for (int i = 0; i < listeNoeuds.getLength(); i++) {
            // Validation des attributs
            int adresse = Integer.parseInt(listeNoeuds.item(i).getAttributes().getNamedItem("id").getNodeValue());
            if (adresse <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être positif");
            int x = Integer.parseInt(listeNoeuds.item(i).getAttributes().getNamedItem("x").getNodeValue());
            if (x <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée x doit être positive");
            int y = Integer.parseInt(listeNoeuds.item(i).getAttributes().getNamedItem("y").getNodeValue());
            if (y <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée y doit être positive");

            intersections.put(adresse,new Intersection(x,y,adresse));
        }

        NodeList listeNoeud = noeudDOMRacine.getElementsByTagName("Noeud");
        Map<Integer, Intersection> intersections = new HashMap();





        int largeur = Integer.parseInt(noeudDOMRacine.getAttribute("largeur"));
        if (largeur <= 0)
            throw new ExceptionXML("Erreur lors de la lecture du fichier : La largeur du plan doit etre positive");
        plan.reset(largeur,hauteur);
        NodeList listeCercles = noeudDOMRacine.getElementsByTagName("cercle");
        for (int i = 0; i < listeCercles.getLength(); i++) {
            plan.ajoute(creeCercle((Element) listeCercles.item(i)));
        }
        NodeList listeRectangles = noeudDOMRacine.getElementsByTagName("rectangle");
        for (int i = 0; i < listeRectangles.getLength(); i++) {
            plan.ajoute(creeRectangle((Element) listeRectangles.item(i)));
        }
    }
}
