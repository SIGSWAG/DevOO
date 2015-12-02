package optimod.es.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import optimod.modele.*;
import optimod.vue.es.OuvreurDeFichier;
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
public enum DeserialiseurXML { // Singleton
    INSTANCE;
    private List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();
    private Stage fenetre;

    DeserialiseurXML() {
        extensions.add(new FileChooser.ExtensionFilter("eXtensible Markup Language (*.xml)", "*.xml"));
        extensions.add(new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));
    }

    /**
     * Ouvre un fichier XML et cree le plan a partir du contenu du fichier
     *
     * @param plan Plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerPlan(Plan plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        File xml = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions).setMode(OuvreurDeFichier.MODE_LECTURE).setTitre("Sélectionner le plan à charger").ouvre(fenetre);
        return chargerPlan(plan, xml);
    }

    /**
     * Lis un fichier XML et cree le plan a partir du contenu du fichier
     *
     * @param plan Plan
     * @param xml  Fichier XML du plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerPlan(Plan plan, File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        if (xml == null) {
            return false;
        }
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();

        if (racine.getNodeName().equals("Reseau")) {
            construirePlanAPartirDeDOMXML(racine, plan);
        } else {
            throw new ExceptionXML("Document non conforme la racine du document doit être un Reseau");
        }
        return true;
    }

    private void construirePlanAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML {
        // Parcours de tous les Noeud (Intersections)
        NodeList listeNoeuds = noeudDOMRacine.getElementsByTagName("Noeud");
        Map<Integer, Intersection> intersections = new HashMap<Integer, Intersection>();
        Map<Integer, Element> noeudsListe = new HashMap<Integer, Element>();

        // Récupération de toutes les intersections
        for (int i = 0; i < listeNoeuds.getLength(); i++) {
            Element noeud = (Element) listeNoeuds.item(i);

            // Validation des attributs
            String attributAdresse = noeud.getAttribute("id");
            if (attributAdresse.isEmpty()) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être défini");
            }

            int adresse = parseInt(attributAdresse);
            if (adresse < 0) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être positif");
            }
            if (intersections.containsKey(adresse)) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'ID d'un Noeud doit être unique");
            }
            String attributX = noeud.getAttribute("x");
            if (attributX.isEmpty()) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée x d'un Noeud doit être définie");
            }
            int x = parseInt(attributX);
            if (x <= 0) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée x d'un Noeud doit être positive");
            }
            String attributY = noeud.getAttribute("y");
            if (attributY.isEmpty()) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée y d'un Noeud doit être définie");
            }
            int y = parseInt(attributY);
            if (y <= 0) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La coordonnée y d'un Noeud doit être positive");
            }

            // Création de l'intersection
            intersections.put(adresse, new Intersection(x, y, adresse));
            noeudsListe.put(adresse, noeud);
        }

        // Pour chaque intersection on créée ses tronçonsSortants
        for (Map.Entry<Integer, Intersection> paire : intersections.entrySet()) {
            Intersection intersectionDepart = paire.getValue();

            List<Troncon> tronconSortants = new ArrayList<Troncon>();
            NodeList listeTroncons = noeudsListe.get(paire.getKey()).getElementsByTagName("LeTronconSortant");
            if (listeTroncons.getLength() < 1) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : Un Noeud doit avoir au moins un élément leTronconSortant");
            }
            for (int j = 0; j < listeTroncons.getLength(); j++) {
                Element leTronconSortant = (Element) listeTroncons.item(j);

                // Validation des attributs
                String nomRue = leTronconSortant.getAttribute("nomRue");
                if (nomRue.isEmpty()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Le nom d'un leTronconSortant doit être renseigné");
                }
                String attributVitesse = leTronconSortant.getAttribute("vitesse");
                if (attributVitesse.isEmpty()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : La vitesse d'un leTronconSortant doit être définie");
                }
                double vitesse = parseDouble(attributVitesse.replaceAll(",", "."));
                if (vitesse <= 0) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : La vitesse d'un leTronconSortant doit être positive");
                }
                String attributLongueur = leTronconSortant.getAttribute("longueur");
                if (attributLongueur.isEmpty()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : La longueur d'un leTronconSortant doit être définie");
                }
                double longueur = parseDouble(attributLongueur.replaceAll(",", "."));
                if (longueur <= 0) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : La longueur d'un leTronconSortant doit être positive");
                }
                String attributIdNoeudDestination = leTronconSortant.getAttribute("idNoeudDestination");
                if (attributIdNoeudDestination.isEmpty()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : L'idNoeudDestination d'un leTronconSortant doit être défini");
                }
                int idNoeudDestination = parseInt(attributIdNoeudDestination);
                Intersection intersectionDestination = intersections.get(idNoeudDestination);
                if (intersectionDestination == null) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Un leTronconSortant possède un NoeudDestination inconnu");
                }
                if (intersectionDestination == intersectionDepart) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Un leTronconSortant ne peut pas avoir le même Noeud entrant et sortant");
                }

                // Création du troncon
                Troncon troncon = new Troncon(intersectionDestination, vitesse, longueur, nomRue);
                tronconSortants.add(troncon);
            }

            // Ajout des TronconSortants à  l'Intersection
            intersectionDepart.setSortants(tronconSortants);
        }

        // s'il n'y a eu aucunes erreur, on peut inserer ces Intersections dans le plan
        plan.reinitialiser();
        plan.setIntersections(new ArrayList<Intersection>(intersections.values()));
    }

    /**
     * Ouvre un fichier XML et cree une Demande de Livraison a partir du contenu du fichier
     *
     * @param demandeLivraisons DemandeLivraisons
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerDemandeLivraison(DemandeLivraisons demandeLivraisons) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        File xml = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions).setMode(OuvreurDeFichier.MODE_LECTURE).setTitre("Sélectionner la demande de livraison à charger").ouvre(fenetre);
        return chargerDemandeLivraison(demandeLivraisons, xml);
    }

    /**
     * Lis un fichier XML et cree une Demande de Livraison a partir du contenu du fichier
     *
     * @param demandeLivraisons DemandeLivraisons
     * @param xml               Fichier XML de la demande de livraison
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerDemandeLivraison(DemandeLivraisons demandeLivraisons, File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        if (xml == null) {
            return false;
        }
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();

        // TODO vérifications dtd xsl
        if (racine.getNodeName().equals("JourneeType")) {
            construireDemandeLivraisonAPartirDeDOMXML(racine, demandeLivraisons);
        } else {
            throw new ExceptionXML("Document non conforme la racine du document doit être une JourneeType");
        }
        return true;
    }

    private void construireDemandeLivraisonAPartirDeDOMXML(Element noeudDOMRacine, DemandeLivraisons demandeLivraisons) throws ExceptionXML {
        // intersectionsUtilisees permet de vérifier que l'on ne va pas ajouter une Livraison dans une intersection utilisée par ce fichier
        List<Intersection> intersectionsUtilisees = new ArrayList<Intersection>();
        // fenetres représentes les fenêtre de livraison de la nouvelle demande de livraison
        List<FenetreLivraison> fenetres = new ArrayList<FenetreLivraison>();
        // entrepot est la livraison représentant le point de départ de la demande de livraison
        Livraison entrepot;


        // Récupération de l'entrepot
        NodeList listeEntrepots = noeudDOMRacine.getElementsByTagName("Entrepot");
        if (listeEntrepots.getLength() != 1) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : Une JourneeType doit avoir un (seul) Entrepot");
        }
        Element elementEntrepot = (Element) listeEntrepots.item(0);
        String attributAdresseEntrepot = elementEntrepot.getAttribute("adresse");
        if (attributAdresseEntrepot.isEmpty()) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : Un Entrepot doit avoir une adresse");
        }
        int adresseEntrepot = parseInt(attributAdresseEntrepot);
        Intersection intersectionEntrepot = demandeLivraisons.getPlan().trouverIntersection(adresseEntrepot);
        if (intersectionEntrepot == null) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : Un Entrepot doit être un Noeud existant");
        }

        intersectionsUtilisees.add(intersectionEntrepot);
        entrepot = new Livraison(intersectionEntrepot);


        // Parcours de toutes les PlagesHoraires (FenetreLivraison)
        NodeList listePlagesHoraires = noeudDOMRacine.getElementsByTagName("PlagesHoraires");
        if (listePlagesHoraires.getLength() != 1) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : Une JourneeType doit avoir une (seule) PlagesHoraires");
        }
        Element plagesHoraires = (Element) listePlagesHoraires.item(0);

        NodeList listePlages = plagesHoraires.getElementsByTagName("Plage");
        if (listePlages.getLength() < 1) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : L'élément PlagesHoraires doit comporter au moins une Plage");
        }
        for (int i = 0; i < listePlages.getLength(); i++) {
            Element elementPlage = (Element) listePlages.item(i);

            // Validation des attributs
            String heureDebut = elementPlage.getAttribute("heureDebut");
            if (heureDebut.isEmpty()) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'heureDebut d'une Plage doit être définie");
            }
            String[] horaireDebut = heureDebut.split(":");
            if (horaireDebut.length != 3) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'heureDebut d'une Plage doit s'écrire H:M:S");
            }
            String heureFin = elementPlage.getAttribute("heureFin");
            if (heureFin.isEmpty()) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'heureFin d'une Plage doit être définie");
            }
            String[] horaireFin = heureFin.split(":");
            if (horaireFin.length != 3) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'heureFin d'une Plage doit s'écrire H:M:S");
            }
            int heureDeb = parseInt(horaireDebut[0]);
            int heureFi = parseInt(horaireFin[0]);
            if (heureDeb < 0 || heureDeb >= 24 || heureFi < 0 || heureFi >= 24) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'heure est comprise entre 0 et 23");
            }
            int minuteDeb = parseInt(horaireDebut[1]);
            int minuteFi = parseInt(horaireFin[1]);
            if (minuteDeb < 0 || minuteDeb >= 60 || minuteDeb < 0 || minuteDeb >= 60) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La minute est comprise entre 0 et 59");
            }
            int secondeDeb = parseInt(horaireDebut[2]);
            int secondeFi = parseInt(horaireFin[2]);
            if (secondeDeb < 0 || secondeDeb >= 60 || secondeFi < 0 || secondeFi >= 60) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : La seconde est comprise entre 0 et 59");
            }
            // Vérification d'antériorité et d'ordre
            int tempsDeb = heureDeb * 3600 + minuteDeb * 60 + secondeDeb, tempsFi = heureFi * 3600 + minuteFi * 60 + secondeFi;
            if (tempsDeb > tempsFi) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'heureDebut d'une Plage doit être inférieure à son HeureFin");
            }
            /*
            if(fenetres.size() > 0 && fenetres.get(fenetres.size()-1).getHeureFin() < tempsDeb){
                throw new ExceptionXML("Erreur lors de la lecture du fichier : Une Plage en suivant une autre doit avoir son heureDebut supérieure ou égale à l'heureFin de la Plage précédente");
            }
            */
            for (FenetreLivraison fl : fenetres) {
                if ((tempsDeb < fl.getHeureFin() && tempsDeb > fl.getHeureDebut()) || tempsFi < fl.getHeureFin() && tempsFi > fl.getHeureDebut()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Deux Plages doivent être distinctes");
                }
            }

            // Parcours de toutes les Livraisons (Livraison)
            NodeList listeLivraisonss = elementPlage.getElementsByTagName("Livraisons");
            if (listeLivraisonss.getLength() != 1) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : Une Plage doit avoir un (seul) élément Livraisons");
            }
            Element livraisonss = (Element) listeLivraisonss.item(0);

            // liste de livraisons de la fenetre courante
            List<Livraison> livraisons = new ArrayList<Livraison>();

            NodeList listeLivraisons = livraisonss.getElementsByTagName("Livraison");
            if (listeLivraisons.getLength() < 1) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : L'élément Livraisons doit être composé d'au moins une Livraison");
            }
            for (int j = 0; j < listeLivraisons.getLength(); j++) {
                Element elementlivraison = (Element) listeLivraisons.item(j);

                // Validation des attributs
                String attributAdresse = elementlivraison.getAttribute("adresse");
                if (attributAdresse.isEmpty()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : L'adresse d'une Livraison doit être définie");
                }
                int adresse = parseInt(attributAdresse);
                Intersection intersectionDeLivraison = demandeLivraisons.getPlan().trouverIntersection(adresse);
                if (intersectionDeLivraison == null) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : L'adresse d'une Livraison doit être un Noeud existant");
                }
                if (intersectionsUtilisees.contains(intersectionDeLivraison)) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Un Noeud ne peut avoir plus d'une Livraison");
                }
                String attributIdCLient = elementlivraison.getAttribute("client");
                if (attributIdCLient.isEmpty()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Une Livraison doit avoir un idClient");
                }
                int idClient = parseInt(elementlivraison.getAttribute("client"));

                // Création de la Livraison et liaisons
                Livraison livraison = new Livraison(intersectionDeLivraison, tempsDeb, tempsFi, idClient);
                livraisons.add(livraison);
                intersectionsUtilisees.add(intersectionDeLivraison);
            }

            // Création de la FenetreLivraison
            fenetres.add(new FenetreLivraison(livraisons, tempsDeb, tempsFi));
        }

        // On supprime toute trace des ancienne Livraisons
        demandeLivraisons.reset();

        // s'il n'y a eu aucunes erreur, on peut setter la nouvelle DemandeLivraisons
        // Setter l'entrepot
        demandeLivraisons.setEntrepot(entrepot);
        // retour de visibilité Intersection / Livraison
        entrepot.getIntersection().setLivraison(entrepot);
        // affectations des fenêtres de livraison

        fenetres.sort(new Comparator<FenetreLivraison>() {
            public int compare(FenetreLivraison o1, FenetreLivraison o2) {
                return o1.getHeureDebut() - o2.getHeureDebut();
            }
        });
        demandeLivraisons.setFenetres(fenetres);
        // retour de visibilité Intersection / Livraison
        for (FenetreLivraison f : fenetres) {
            for (Livraison l : f.getLivraisons()) {
                l.getIntersection().setLivraison(l);
            }
        }
    }

    public void setFenetre(Stage fenetre) {
        this.fenetre = fenetre;
    }


    private int parseInt(String s) throws ExceptionXML {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new ExceptionXML("Erreur de parsing en nombre entier : " + e.getMessage());
        }
    }

    private double parseDouble(String s) throws ExceptionXML {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new ExceptionXML("Erreur de parsing en nombre décimal : " + e.getMessage());
        }
    }
}
