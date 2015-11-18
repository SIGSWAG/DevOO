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
import java.util.List;

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

        NodeList listeNoeud = noeudDOMRacine.getElementsByTagName("Noeud");
        List<Intersection> intersections = new ArrayList<Intersection>();
        for (int i = 0; i < listeNoeud.getLength(); i++) {
            intersections.add(new Intersection());
            plan.ajoute(creeCercle((Element) listeCercles.item(i)));
        }




        int hauteur = Integer.parseInt(noeudDOMRacine.getAttribute("hauteur"));
        if (hauteur <= 0)
            throw new ExceptionXML("Erreur lors de la lecture du fichier : La hauteur du plan doit etre positive");
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
