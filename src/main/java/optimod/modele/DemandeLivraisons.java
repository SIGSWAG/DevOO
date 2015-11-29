package optimod.modele;

import optimod.es.xml.DeserialiseurXML;
import optimod.es.xml.ExceptionXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class DemandeLivraisons extends Observable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static int TEMPS_ARRET = 10 * 60;

    private List<Chemin> itineraire;

    private Plan plan;

    private List<FenetreLivraison> fenetres;

    private Livraison entrepot;

    private int heureDebutItineraire;

    /**
     * Constructeur par défaut.
     */
    public DemandeLivraisons(Plan pl) {
        this.plan = pl;
        this.itineraire = new ArrayList<>();
        this.fenetres = new ArrayList<>();
    }

    /**
     * TODO Rédiger
     *
     * @return
     * @throws ParserConfigurationException
     * @throws ExceptionXML
     * @throws SAXException
     * @throws IOException
     */
    public boolean chargerDemandeLivraison() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        boolean demandeLivraisonChargee = DeserialiseurXML.INSTANCE.chargerDemandeLivraison(this);
        if (demandeLivraisonChargee) {
            setChanged();
            notifyObservers(Evenement.DEMANDE_LIVRAISONS_CHARGEES);
        }
        return demandeLivraisonChargee;
    }

    /**
     * Récupère tous les chemins entre les intersections de chaque fenêtre horaire et d'une fenêtre horaire vers la suivante.
     * C'est-à-dire crée le graphe dans lequel on veut trouver le plus court chemin.
     * Puis appelle la méthode de GraphePCC permettant de trouver le plus court parcours dans ce graphe.
     */
    public void calculerItineraire() {

        List<Chemin> graphe = new ArrayList<>();

        List<Livraison> listeEntrepot = new ArrayList<>();
        listeEntrepot.add(entrepot);
        FenetreLivraison fenEntrepot = new FenetreLivraison(listeEntrepot, 0, 1);

        graphe.addAll(fenEntrepot.calculPCCSuivant(fenetres.get(0))); // On calcule les plus courts chemins de l'entrepôt à la premiere fenêtre

        // Toutes les fenêtres sauf la dernière
        for (int i = 0; i < fenetres.size() - 1; i++) {
            FenetreLivraison fenetre = fenetres.get(i);
            graphe.addAll(fenetre.calculPCCInterne());
            graphe.addAll(fenetre.calculPCCSuivant(fenetres.get(i + 1)));
        }
        graphe.addAll(fenetres.get(fenetres.size() - 1).calculPCCInterne());
        graphe.addAll(fenetres.get(fenetres.size() - 1).calculPCCSuivant(fenEntrepot)); // Calcul PCC vers entrepôt
        GraphePCC leGrapheAResoudre = new GraphePCC(entrepot, graphe);
        this.itineraire = leGrapheAResoudre.calculerItineraire(); // Calcul de l'itinéraire

        // Il faut maintenant mettre à jour les livraisons (suivantes et précédentes)
        // TODO enlever les variables non utilisées ?
        int heureDepartItineraire = 0;
        int duree = 0;
        int fenetreCouranteDebut = 0; //heure de début de fenetre de la livraison courante
        Chemin premierChemin = itineraire.get(0);
        Livraison premiereLivraison = premierChemin.getArrivee();
        premiereLivraison.setHeureLivraison(premiereLivraison.getHeureDebutFenetre()); // La première livraison arrive
        fenetreCouranteDebut = premiereLivraison.getHeureDebutFenetre();
        // À l'heure de sa fenêtre
        heureDebutItineraire = premiereLivraison.getHeureDebutFenetre() - premierChemin.getDuree(); // On part de l'entrepôt

        for (Chemin chemin : itineraire) {

            mettreAJourTronconsEmpruntes(chemin);

            Livraison arrivee = chemin.getArrivee();
            Livraison depart = chemin.getDepart();
            depart.setCheminVersSuivante(chemin); // Mise à jour du chemin vers la livraison suivante
            arrivee.setPrecedente(depart);// Mise à jour du pointeur vers la livraison précédente

            if (depart.getIntersection().getAdresse() != entrepot.getIntersection().getAdresse()) { // Temps déja mis à jour pour l'entrepôt
                if (arrivee.getHeureDebutFenetre() < depart.getHeureLivraison() + chemin.getDuree() + TEMPS_ARRET) { // Pas d'attente
                    arrivee.setHeureLivraison(depart.getHeureLivraison() + chemin.getDuree() + TEMPS_ARRET);
                } else {
                    arrivee.setHeureLivraison(arrivee.getHeureDebutFenetre());
                }
            }

        }

        for (Chemin chemin : itineraire) {
            System.out.println("Depart " + chemin.getDepart().getIntersection().getAdresse() + " arrivee " + chemin.getArrivee().getIntersection().getAdresse());

        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }


    private void mettreAJourTronconsEmpruntes(Chemin chemin) {
        chemin.getTroncons().forEach(Troncon::incrementeCompteurPassage);
    }

    /**
     * Met à jour toutes les horaires de livraison prévues à partir d'une livraison donnée et jusqu'à l'entrepôt.
     *
     * @param livraison la livraison à partir de laquelle on recalcule les heures
     */
    private void mettreAJourLesHeuresAPartirDe(Livraison livraison) {
        while (!livraison.equals(entrepot)) {
            livraison.setHeureLivraison(livraison.getPrecedente().getHeureLivraison() + livraison.getPrecedente().getCheminVersSuivante().getDuree() + TEMPS_ARRET);
            livraison = livraison.getSuivante();
        }
        entrepot.setHeureLivraison(entrepot.getPrecedente().getHeureLivraison() + entrepot.getPrecedente().getCheminVersSuivante().getDuree() + TEMPS_ARRET);

    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     *
     * @param intersection l'intersection sur laquelle on ajoute la livraison
     * @param livraison    la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Intersection intersection, Livraison livraison) {
        Livraison nouvelleLivraison = new Livraison(intersection);
        nouvelleLivraison.setPrecedente(livraison.getPrecedente());

        Chemin nouveauPCC1 = nouvelleLivraison.getPrecedente().calculPCC(nouvelleLivraison);
        if (nouveauPCC1 != null) {

            Chemin ch = livraison.getPrecedente().getCheminVersSuivante();
            int indexASupprimer = 0;
            for (int i = 0; i < itineraire.size(); i++) {
                if (ch == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }

            ch.getTroncons().forEach(Troncon::decrementeCompteurPassage);

            Chemin nouveauPCC2 = nouvelleLivraison.calculPCC(livraison);

            if (nouveauPCC2 != null) {
                livraison.setPrecedente(nouvelleLivraison);
                nouvelleLivraison.getPrecedente().setCheminVersSuivante(nouveauPCC1);
                nouvelleLivraison.setCheminVersSuivante(nouveauPCC2);

                itineraire.add(indexASupprimer, nouveauPCC2); //mise a jour itineraire
                itineraire.add(indexASupprimer, nouveauPCC1); //mise a jour itineraire
                itineraire.remove(ch);


                for (Troncon troncon : nouveauPCC1.getTroncons()) {
                    troncon.incrementeCompteurPassage();
                }
                for (Troncon troncon : nouveauPCC2.getTroncons()) {
                    troncon.incrementeCompteurPassage();
                }

                mettreAJourLesHeuresAPartirDe(nouvelleLivraison);

            }

        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);

    }

    /**
     * Supprime la livraison en paramètre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     *
     * @param livraison la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livraison) {
        if (livraison == entrepot) {
            logger.error("Tentative de suppresion de l'entrepôt, action impossible sur l'entrepot");
            return;
        }

        Chemin nouveauPCC = livraison.getPrecedente().calculPCC(livraison.getSuivante());

        if (nouveauPCC != null) {

            Chemin cheminASupprimer = livraison.getPrecedente().getCheminVersSuivante();

            int indexASupprimer = 0; // Index de suppression
            for (int i = 0; i < itineraire.size(); i++) {
                if (cheminASupprimer == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }

            itineraire.add(indexASupprimer, nouveauPCC); //mise a jour itineraire
            itineraire.remove(cheminASupprimer);

            for (Troncon tr : livraison.getPrecedente().getCheminVersSuivante().getTroncons()) {
                tr.decrementeCompteurPassage();
            }

            for (Troncon tr : livraison.getCheminVersSuivante().getTroncons()) {
                tr.decrementeCompteurPassage();
            }

            livraison.getPrecedente().setCheminVersSuivante(nouveauPCC);
            livraison.getSuivante().setPrecedente(livraison.getPrecedente());

            for (Troncon tr : nouveauPCC.getTroncons()) {
                tr.incrementeCompteurPassage();
            }

            mettreAJourLesHeuresAPartirDe(livraison.getPrecedente());
            livraison.getIntersection().setLivraison(null);

        } else {
            System.out.println("PCC est null !!!!");
        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }

    /**
     * Échange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires, les fenetre de livraison sont aussi echangées
     *
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        // TODO Faire attention aux fenetres !
        if (livr1 == entrepot || livr2 == entrepot) {
            logger.error("Action impossible sur l'entrepot");
            return;
        }

        // prec et suiv l1
        Livraison livr1PrecTemp = livr1.getPrecedente();
        Livraison livr1SuivTemp = livr1.getSuivante();

        // prec et suiv l2
        Livraison livr2PrecTemp = livr2.getPrecedente();
        Livraison livr2SuivTemp = livr2.getSuivante();

        // chemins de l1
        Chemin cheminl1prec = livr1PrecTemp.getCheminVersSuivante();
        Chemin cheminl1suiv = livr1.getCheminVersSuivante();

        // chemins de l2
        Chemin cheminl2prec = livr2PrecTemp.getCheminVersSuivante();
        Chemin cheminl2suiv = livr2.getCheminVersSuivante();


        // pcc 1
        Chemin cheminl1l2suiv = livr1.calculPCC(livr2.getSuivante());
        Chemin cheminl2precl1 = livr2PrecTemp.calculPCC(livr1);

        // pcc 2
        Chemin cheminl2l1suiv = livr2.calculPCC(livr1.getSuivante());
        Chemin cheminl1precl2 = livr1PrecTemp.calculPCC(livr2);

        List<Chemin> cheminsNonParcourus = new ArrayList<>();
        cheminsNonParcourus.add(cheminl1prec);
        cheminsNonParcourus.add(cheminl1suiv);
        cheminsNonParcourus.add(cheminl2prec);
        cheminsNonParcourus.add(cheminl2suiv);

        List<Chemin> cheminsParcourus = new ArrayList<>();
        cheminsParcourus.add(cheminl1l2suiv);
        cheminsParcourus.add(cheminl2precl1);
        cheminsParcourus.add(cheminl2l1suiv);
        cheminsParcourus.add(cheminl1precl2);

        if (cheminl1l2suiv != null && cheminl1precl2 != null && cheminl2l1suiv != null && cheminl2precl1 != null) {

            int indexASupprimer = 0;
            for (int i = 0; i < itineraire.size(); i++) {
                if (cheminl1prec == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }
            itineraire.add(indexASupprimer, cheminl2l1suiv);
            itineraire.add(indexASupprimer, cheminl1precl2);
            itineraire.remove(cheminl1prec);
            itineraire.remove(cheminl1suiv);

            for (int i = 0; i < itineraire.size(); i++) {
                if (cheminl2prec == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }
            itineraire.add(indexASupprimer, cheminl1l2suiv);
            itineraire.add(indexASupprimer, cheminl2precl1);
            itineraire.remove(cheminl2prec);
            itineraire.remove(cheminl2suiv);

            // Mise à jour des troncons non empruntés
            for (Chemin chemin : cheminsNonParcourus) {
                chemin.getTroncons().forEach(Troncon::decrementeCompteurPassage);
            }

            // Mise à jour des troncons empruntés
            for (Chemin chemin : cheminsParcourus) {
                for (Troncon troncon : chemin.getTroncons()) {
                    troncon.incrementeCompteurPassage();
                }
            }

            // l1 chemins
            livr1.setPrecedente(livr2PrecTemp);
            livr1.setCheminVersSuivante(cheminl1l2suiv);
            livr2SuivTemp.setPrecedente(livr1);
            livr2PrecTemp.setCheminVersSuivante(cheminl2precl1);

            // l2 chemins
            livr2.setPrecedente(livr2PrecTemp);
            livr2.setCheminVersSuivante(cheminl2l1suiv);
            livr1SuivTemp.setPrecedente(livr2);
            livr1PrecTemp.setCheminVersSuivante(cheminl1precl2);

            FenetreLivraison fenetre1 = null, fenetre2 = null;
            for (FenetreLivraison f : this.fenetres) {
                if (fenetre1 == null && f.getLivraisons().contains(livr1)) {
                    fenetre1 = f;
                }
                if (fenetre2 == null && f.getLivraisons().contains(livr2)) {
                    fenetre2 = f;
                }
                if (fenetre1 != null && fenetre2 != null) {
                    break;
                }
            }
            if (fenetre1 != fenetre2) {
                // Échange des fenêtres
                // TODO vérifier l'avertissement
                fenetre1.getLivraisons().remove(livr1);
                fenetre1.getLivraisons().add(livr2);
                fenetre2.getLivraisons().remove(livr2);
                fenetre2.getLivraisons().add(livr1);
            }

            if (livr1.getHeureLivraison() < livr2.getHeureLivraison()) {
                mettreAJourLesHeuresAPartirDe(livr1);
            } else {
                mettreAJourLesHeuresAPartirDe(livr2);
            }
        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }

    /**
     * Réinitialise tous les attributs.
     * Supprime tous les liens qu'ont les intersections vers les livraisons existantes.
     */
    public void reinitialiser() {

        for (Chemin chemin : itineraire) {

            chemin.getTroncons().forEach(Troncon::resetCompteur);
        }

        itineraire = new ArrayList<>();

        // Suppression de tous les liens Intersection -> Livraison
        for (FenetreLivraison f : fenetres) {
            for (Livraison l : f.getLivraisons()) {
                l.getIntersection().setLivraison(null);
            }
        }

        fenetres = new ArrayList<>();

        if (entrepot != null) {
            entrepot.getIntersection().setLivraison(null);
            entrepot = null;
        }
    }

    public List<Chemin> getItineraire() {
        return itineraire;
    }

    public void setItineraire(List<Chemin> itineraire) {
        this.itineraire = itineraire;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public List<FenetreLivraison> getFenetres() {
        return fenetres;
    }

    public void setFenetres(List<FenetreLivraison> fenetres) {
        this.fenetres = fenetres;
    }

    public Livraison getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Livraison entrepot) {
        this.entrepot = entrepot;
    }

    public void genererFeuilleDeRoute() {

        System.out.println("-------Feuile de route------");
        System.out.println("Depart entrepot : (" + entrepot.getIntersection().getAdresse() + ")" + heureDebutItineraire);
        for (Chemin chemin : itineraire) {

            System.out.println("Chemin de " + chemin.getDepart().getIntersection().getAdresse() + " à " + chemin.getArrivee().getIntersection()
                    .getAdresse());

            Troncon rueCourante = chemin.getTroncons().get(0);
            double distanceRue = 0;
            for (Troncon troncon : chemin.getTroncons()) {

                if (troncon.getNom().equals(rueCourante.getNom())) {
                    distanceRue += troncon.getLongueur();
                } else {
                    System.out.println("Prendre rue " + rueCourante.getNom() + " sur " + distanceRue + "m");
                    distanceRue = troncon.getLongueur();
                    rueCourante = troncon;
                }

            }

            System.out.println("Arrivee a la livraison " + chemin.getArrivee().getIntersection().getAdresse() + " a " + chemin.getArrivee().getHeure() + " : " + chemin.getArrivee().getMinute());

        }

    }

}