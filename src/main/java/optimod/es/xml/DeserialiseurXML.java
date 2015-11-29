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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Désérialise les fichiers XML au format d'Optimod.
 * Created by Loïc Touzard on 18/11/2015.
 */
public enum DeserialiseurXML { // Singleton
    INSTANCE;
    private final List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();

    DeserialiseurXML(){
        extensions.add(new FileChooser.ExtensionFilter("eXtensible Markup Language (*.xml)", "*.xml"));
        extensions.add(new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));
    }

    private Stage fenetre;

    /**
     * Ouvre un fichier XML et crée le plan à partir du contenu du fichier.
     *
     * @param plan Plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerPlan(final Plan plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        final File xml = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions)
                .setMode(OuvreurDeFichier.MODE_LECTURE)
                .setTitre("Sélectionner le plan à charger")
                .ouvre(fenetre);
        return chargerPlan(plan, xml);
    }

    /**
     * Lis un fichier XML et crée le plan à partir du contenu du fichier.
     *
     * @param plan Plan
     * @param xml  Fichier XML du plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerPlan(final Plan plan, final File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        if (xml == null) {
            return false;
        }
        final DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = docBuilder.parse(xml);
        final Element racine = document.getDocumentElement();

        if (racine.getNodeName().equals("Reseau")) {
            construirePlanAPartirDeDOMXML(racine, plan);
        } else {
            throw new ExceptionXML("Document non conforme");
        }
        return true;
    }

    private void construirePlanAPartirDeDOMXML(final Element noeudDOMRacine, final Plan plan) throws ExceptionXML, NumberFormatException {
        // Parcours de tous les Noeud (Intersections)
        final NodeList listeNoeuds = noeudDOMRacine.getElementsByTagName("Noeud");
        final Map<Integer, Intersection> intersections = new HashMap<>();
        final Map<Integer, Element> noeudsListe = new HashMap<>();

        // Récupération de toutes les intersections
        for (int i = 0; i < listeNoeuds.getLength(); i++) {
            final Element noeud = (Element) listeNoeuds.item(i);

            // Validation des attributs
            final int adresse = Integer.parseInt(noeud.getAttribute("id"));
            if (adresse < 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être positif");
            if (intersections.containsKey(adresse))
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'ID d'un Noeud doit être unique");
            final int x = Integer.parseInt(noeud.getAttribute("x"));
            if (x <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : la coordonnée x d'un Noeud doit être positive");
            final int y = Integer.parseInt(noeud.getAttribute("y"));
            if (y <= 0)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : la coordonnée y d'un Noeud doit être positive");

            // Création de l'intersection
            intersections.put(adresse, new Intersection(x, y, adresse));
            noeudsListe.put(adresse, noeud);
        }

        // Pour chaque intersection on crée ses tronconsSortants
        for (final Map.Entry<Integer, Intersection> paire : intersections.entrySet()) {
            final Intersection intersectionDepart = paire.getValue();

            final List<Troncon> tronconSortants = new ArrayList<>();
            final NodeList listeTroncons = noeudsListe.get(paire.getKey()).getElementsByTagName("LeTronconSortant");
            for (int j = 0; j < listeTroncons.getLength(); j++) {
                final Element leTronconSortant = (Element) listeTroncons.item(j);

                // Validation des attributs
                final String nomRue = leTronconSortant.getAttribute("nomRue");
                if (nomRue.isEmpty())
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : le nom d'un leTronconSortant doit être renseigné");
                final double vitesse = Double.parseDouble(leTronconSortant.getAttribute("vitesse").replaceAll(",", "."));
                if (vitesse <= 0)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : la vitesse d'un leTronconSortant doit être positive");
                final double longueur = Double.parseDouble(leTronconSortant.getAttribute("longueur").replaceAll(",", "."));
                if (longueur <= 0)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : la longueur d'un leTronconSortant doit être positive");
                final int idNoeudDestination = Integer.parseInt(leTronconSortant.getAttribute("idNoeudDestination"));
                final Intersection intersectionDestination = intersections.get(idNoeudDestination);
                if (intersectionDestination == null)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Un leTronconSortant possède un NoeudDestination inconnu");
                if (intersectionDestination == intersectionDepart)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : Un leTronconSortant ne peut pas avoir le même Noeud entrant et sortant");

                // Création du Troncon
                final Troncon troncon = new Troncon(intersectionDestination, vitesse, longueur, nomRue);
                tronconSortants.add(troncon);
            }

            // Ajout des TronconSortants à  l'Intersection
            intersectionDepart.setSortants(tronconSortants);
        }

        // S'il n'y a eu aucune erreur, on peut insérer ces Intersections dans le plan
        plan.reinitialiser();
        plan.setIntersections(new ArrayList<>(intersections.values()));
    }

    /**
     * Ouvre un fichier XML et crée une DemandeLivraisons à partir du contenu du fichier
     *
     * @param demandeLivraisons DemandeLivraisons
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerDemandeLivraison(final DemandeLivraisons demandeLivraisons) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        final File xml = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions)
                .setMode(OuvreurDeFichier.MODE_LECTURE)
                .setTitre("Sélectionner la demande de livraison à charger")
                .ouvre(fenetre);
        return chargerDemandeLivraison(demandeLivraisons, xml);
    }

    /**
     * Lis un fichier XML et crée une DemandeLivraisons à partir du contenu du fichier
     *
     * @param demandeLivraisons DemandeLivraisons
     * @param xml               Fichier XML de la demande de livraison
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public boolean chargerDemandeLivraison(final DemandeLivraisons demandeLivraisons, final File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        if (xml == null) {
            return false;
        }
        final DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = docBuilder.parse(xml);
        final Element racine = document.getDocumentElement();

        // TODO vérifications dtd xsl
        if (racine.getNodeName().equals("JourneeType")) {
            construireDemandeLivraisonAPartirDeDOMXML(racine, demandeLivraisons);
        } else
            throw new ExceptionXML("Document non conforme");
        return true;
    }

    private void construireDemandeLivraisonAPartirDeDOMXML(final Element noeudDOMRacine, final DemandeLivraisons demandeLivraisons) throws ExceptionXML, NumberFormatException {
        // intersectionsUtilisees permet de vérifier que l'on ne va pas ajouter une Livraison dans une intersection utilisée par ce fichier
        final List<Intersection> intersectionsUtilisees = new ArrayList<>();
        // fenetres représentes les fenêtre de livraison de la nouvelle demande de livraison
        final List<FenetreLivraison> fenetres = new ArrayList<>();
        // entrepot est la livraison représentant le point de départ de la demande de livraison
        final Livraison entrepot;

        // Récupération de l'entrepot
        final NodeList listeEntrepots = noeudDOMRacine.getElementsByTagName("Entrepot");
        if (listeEntrepots.getLength() != 1) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : une JourneeType doit n'avoir qu'un (seul) Entrepot");
        }
        final Element elementEntrepot = (Element) listeEntrepots.item(0);
        final int adresseEntrepot = Integer.parseInt(elementEntrepot.getAttribute("adresse"));
        final Intersection intersectionEntrepot = demandeLivraisons.getPlan().trouverIntersection(adresseEntrepot);
        if (intersectionEntrepot == null)
            throw new ExceptionXML("Erreur lors de la lecture du fichier : un Entrepot doit être un Noeud existant");

        intersectionsUtilisees.add(intersectionEntrepot);
        entrepot = new Livraison(intersectionEntrepot);

        // Parcours de toutes les PlagesHoraires (FenetreLivraison)
        final NodeList listePlagesHoraires = noeudDOMRacine.getElementsByTagName("PlagesHoraires");
        if (listePlagesHoraires.getLength() != 1) {
            throw new ExceptionXML("Erreur lors de la lecture du fichier : Une JourneeType doit avoir une (seule) PlagesHoraires");
        }
        final Element plagesHoraires = (Element) listePlagesHoraires.item(0);

        final NodeList listePlages = plagesHoraires.getElementsByTagName("Plage");
        for (int i = 0; i < listePlages.getLength(); i++) {
            final Element elementPlage = (Element) listePlages.item(i);

            // Validation des attributs
            final String heureDebut = elementPlage.getAttribute("heureDebut");
            final String[] horaireDebut = heureDebut.split(":");
            if (horaireDebut.length != 3)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'heureDebut d'une Plage doit s'écrire H:M:S");
            final String heureFin = elementPlage.getAttribute("heureFin");
            final String[] horaireFin = heureFin.split(":");
            if (horaireFin.length != 3)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'heureFin d'une Plage doit s'écrire H:M:S");
            final int heureDeb = Integer.parseInt(horaireDebut[0]);
            final int heureFi = Integer.parseInt(horaireFin[0]);
            if (heureDeb < 0 || heureDeb >= 24 || heureFi < 0 || heureFi >= 24)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'heure doit être comprise entre 0 et 23");
            final int minuteDeb = Integer.parseInt(horaireDebut[1]);
            final int minuteFi = Integer.parseInt(horaireFin[1]);
            if (minuteDeb < 0 || minuteDeb >= 60 || minuteDeb < 0 || minuteDeb >= 60)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : la minute doit être comprise entre 0 et 59");
            final int secondeDeb = Integer.parseInt(horaireDebut[2]);
            final int secondeFi = Integer.parseInt(horaireFin[2]);
            if (secondeDeb < 0 || secondeDeb >= 60 || secondeFi < 0 || secondeFi >= 60)
                throw new ExceptionXML("Erreur lors de la lecture du fichier : la seconde doit être comprise entre 0 et 59");
            // Vérification d'antériorité et d'ordre
            final int tempsDeb = heureDeb * 3600 + minuteDeb * 60 + secondeDeb;
            final int tempsFi = heureFi * 3600 + minuteFi * 60 + secondeFi;
            if (tempsDeb > tempsFi) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : l'heureDebut d'une Plage doit être inférieure à son HeureFin");
            }
            /*
            if(fenetres.size() > 0 && fenetres.get(fenetres.size()-1).getHeureFin() < tempsDeb){
                throw new ExceptionXML("Erreur lors de la lecture du fichier : Une Plage en suivant une autre doit avoir son heureDebut supérieure ou égale à l'heureFin de la Plage précédente");
            }
            */
            for (final FenetreLivraison fenetreLivraison : fenetres) {
                if ((tempsDeb < fenetreLivraison.getHeureFin() && tempsDeb > fenetreLivraison.getHeureDebut()) ||
                        tempsFi < fenetreLivraison.getHeureFin() && tempsFi > fenetreLivraison.getHeureDebut()) {
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : deux Plages doivent être distinctes");
                }
            }

            // Parcours de toutes les Livraisons (Livraison)
            final NodeList listeLivraisonss = elementPlage.getElementsByTagName("Livraisons");
            if (listeLivraisonss.getLength() != 1) {
                throw new ExceptionXML("Erreur lors de la lecture du fichier : une Plage doit avoir une (seule) Livraison");
            }
            final Element livraisonss = (Element) listeLivraisonss.item(0);

            // liste de livraisons de la fenetre courante
            final List<Livraison> livraisons = new ArrayList<>();

            final NodeList listeLivraisons = livraisonss.getElementsByTagName("Livraison");
            for (int j = 0; j < listeLivraisons.getLength(); j++) {
                final Element elementlivraison = (Element) listeLivraisons.item(j);

                // Validation des attributs
                final int adresse = Integer.parseInt(elementlivraison.getAttribute("adresse"));
                final Intersection intersectionDeLivraison = demandeLivraisons.getPlan().trouverIntersection(adresse);
                if (intersectionDeLivraison == null)
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : l'adresse d'une Livraison doit être un Noeud existant");
                if (intersectionsUtilisees.contains(intersectionDeLivraison))
                    throw new ExceptionXML("Erreur lors de la lecture du fichier : un Noeud ne peut avoir plus d'une Livraison");
                final int idClient = Integer.parseInt(elementlivraison.getAttribute("client"));

                // Création de la Livraison et liaisons
                final Livraison livraison = new Livraison(intersectionDeLivraison, tempsDeb, tempsFi, idClient);
                livraisons.add(livraison);
                intersectionsUtilisees.add(intersectionDeLivraison);
            }

            // Création de la FenetreLivraison
            fenetres.add(new FenetreLivraison(livraisons, tempsDeb, tempsFi));
        }

        // On supprime toute trace des ancienne Livraisons
        demandeLivraisons.reset();

        // S'il n'y a eu aucune erreur, on peut affecter la nouvelle DemandeLivraisons
        // Affecter l'entrepot
        demandeLivraisons.setEntrepot(entrepot);
        // Retour de visibilité Intersection / Livraison
        entrepot.getIntersection().setLivraison(entrepot);
        // Affectations des fenêtres de livraison

        fenetres.sort((o1, o2) -> o1.getHeureDebut() - o2.getHeureDebut());
        demandeLivraisons.setFenetres(fenetres);

        // Retour de visibilité Intersection / Livraison
        for (final FenetreLivraison fenetreLivraison : fenetres) {
            for (final Livraison livraison : fenetreLivraison.getLivraisons()) {
                livraison.getIntersection().setLivraison(livraison);
            }
        }
    }

    public void setFenetre(final Stage fenetre) {
        this.fenetre = fenetre;
    }
}
