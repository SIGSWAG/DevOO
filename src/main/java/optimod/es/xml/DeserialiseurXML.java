package optimod.es.xml;

import optimod.modele.Intersection;
import optimod.modele.Plan;
import optimod.modele.Troncon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
        Map<Integer, Element> noeudsListe = new HashMap();

        // Récupération de toutes les intersections
        for (int i = 0; i < listeNoeuds.getLength(); i++) {
            Element noeud = (Element) listeNoeuds.item(i);

            // Validation des attributs
            int adresse = Integer.parseInt(noeud.getAttribute("id"));
            if (adresse <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être positif");
            int x = Integer.parseInt(noeud.getAttribute("x"));
            if (x <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée x d'un Noeud doit être positive");
            int y = Integer.parseInt(noeud.getAttribute("y"));
            if (y <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée y d'un Noeud doit être positive");

            // Création de l'intersection
            intersections.put(adresse, new Intersection(x, y, adresse));
            noeudsListe.put(adresse, noeud);
        }

        // Pour chaque intersection on créée ses tronçonsSortants
        for (Map.Entry<Integer, Intersection> paire : intersections.entrySet())
        {
            Intersection intersection = paire.getValue();

            NodeList listeTroncons = noeudsListe.get(paire.getKey()).getElementsByTagName("LeTronconSortant");
            for (int j = 0; j < listeTroncons.getLength(); j++){
                Element leTronconSortant = (Element) listeNoeuds.item(j);

                // Validation des attributs
                String nomRue = leTronconSortant.getAttribute("nomRue");
                if (nomRue.isEmpty())
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Le nom d'un leTronconSortant doit être renseigné");
                double vitesse = Double.parseDouble(leTronconSortant.getAttribute("vitesse"));
                if (vitesse < 0)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : La vitesse d'un leTronconSortant doit être positive");
                double longueur = Double.parseDouble(leTronconSortant.getAttribute("longueur"));
                if (longueur < 0)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : La longueur d'un leTronconSortant doit être positive");
                int idNoeudDestination = Integer.parseInt(leTronconSortant.getAttribute("idNoeudDestination"));
                Intersection intersectionDestination = intersections.get(idNoeudDestination);
                if(intersectionDestination == null)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Un leTronconSortant possède un NoeudDestination inconnu");

                // Création du troncon
            }
        }
    }
}
