package optimod.modele;

import optimod.es.txt.GenerateurDeFeuilleDeRoute;
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

/**
 * Ensemble de livraisons et d’un entrepôt.
 */
public class DemandeLivraisons extends Observable {

    private static final int TEMPS_ARRET = 10 * 60;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Chemin> itineraire;

    private Plan plan;

    private List<FenetreLivraison> fenetres;

    private Livraison entrepot;

    private int heureDebutItineraire;


    /**
     * Default constructor
     */
    public DemandeLivraisons(Plan pl) {
        this.plan = pl;
        this.itineraire = new ArrayList<>();
        this.fenetres = new ArrayList<>();

    }


    public boolean chargerDemandeLivraison() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        boolean demandeLivraisonChargee = DeserialiseurXML.INSTANCE.chargerDemandeLivraison(this);
        if (demandeLivraisonChargee) {
            setChanged();
            notifyObservers(Evenement.DEMANDE_LIVRAISONS_CHARGEE);
        }
        return demandeLivraisonChargee;
    }

    /**
     * récupère tous les chemins entres les intersections de chaque fenetre horaire et d'une fenetre horaire vers la suivante
     * c'est à dire créé le graphe dans lequel on veut trouver le plus court chemin.
     * Puis appelle la méthode de GraphePCC permettant de trouver le + court parcours dans ce graphe.
     */
    public void calculerItineraire() {

        logger.debug("Calcul lancé");
        List<Chemin> graphe = new ArrayList<>();

        List<Livraison> listeEntrepot = new ArrayList<>();
        listeEntrepot.add(entrepot);
        FenetreLivraison fenEntrepot = new FenetreLivraison(listeEntrepot, 0, 1);

        graphe.addAll(fenEntrepot.calculPCCSuivant(fenetres.get(0)));//on calcule les plus courts chemins de l'entrepot a la premiere fenetre

        // toutes les fenetres sauf la derniere
        for (int i = 0; i < fenetres.size() - 1; i++) {
            FenetreLivraison fenetre = fenetres.get(i);
            graphe.addAll(fenetre.calculPCCInterne());
            graphe.addAll(fenetre.calculPCCSuivant(fenetres.get(i + 1)));
        }
        graphe.addAll(fenetres.get(fenetres.size() - 1).calculPCCInterne());
        graphe.addAll(fenetres.get(fenetres.size() - 1).calculPCCSuivant(fenEntrepot)); //calcul PCC vers entrepot.
        GraphePCC leGrapheAResoudre = new GraphePCC(entrepot, graphe);
        this.itineraire = leGrapheAResoudre.calculerItineraire(); //calcul de l'itinéraire

        //il faut maintenant mettre à jour les livraisons (suivantes et précédentes)
        Chemin premierChemin = itineraire.get(0);
        Livraison premiereLivraison = premierChemin.getArrivee();
        premiereLivraison.setHeureLivraison(premiereLivraison.getHeureDebutFenetre()); //la première livraison arrive
        //à l'heure de sa fenetre
        heureDebutItineraire = premiereLivraison.getHeureDebutFenetre() - premierChemin.getDuree(); //on part de l'entrepot

        for (Chemin chemin : itineraire) {

            mettreAJourTronconsEmpruntes(chemin);

            Livraison arrivee = chemin.getArrivee();
            Livraison depart = chemin.getDepart();
            depart.setCheminVersSuivante(chemin); //on maj le chemin vers la livraison suivante
            arrivee.setPrecedente(depart);//on maj le pointeur vers la livraison precedente

            if (depart.getIntersection().getAdresse() != entrepot.getIntersection().getAdresse()) {//temps déja maj pour l'entrepot
                if (arrivee.getHeureDebutFenetre() < depart.getHeureLivraison() + chemin.getDuree() + TEMPS_ARRET) { //pas d'attente
                    arrivee.setHeureLivraison(depart.getHeureLivraison() + chemin.getDuree() + TEMPS_ARRET);
                } else {
                    arrivee.setHeureLivraison(arrivee.getHeureDebutFenetre());
                }
            }
            logger.debug("Depart {} arrivee {}", chemin.getDepart().getIntersection().getAdresse(), chemin.getArrivee().getIntersection().getAdresse());
        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }


    private void mettreAJourTronconsEmpruntes(Chemin chemin) {
        chemin.getTroncons().forEach(Troncon::incrementeCompteurPassage);
    }

    /**
     * met à jour toutes les horaires de livraison prévues à partir d'une livraison donnée et jusqu'à l'entrepot
     *
     * @param livr la livraison à partir de laquelle on recalcule les heures
     */
    private void mettreAJourLesHeuresAPartirDe(Livraison livr) {

        if (livr.getPrecedente() == entrepot) {
            livr.setHeureLivraison(livr.getHeureDebutFenetre());
            livr = livr.getSuivante();
            heureDebutItineraire = entrepot.getCheminVersSuivante().getDuree();
        }
        while (!livr.equals(entrepot)) {
            int heurePrevue = livr.getPrecedente().getHeureLivraison() + livr.getPrecedente().getCheminVersSuivante().getDuree() + TEMPS_ARRET;

            int heureFinale = heurePrevue > livr.getHeureDebutFenetre() ? heurePrevue : livr.getHeureDebutFenetre();

            livr.setHeureLivraison(heureFinale);
            livr = livr.getSuivante();
        }
        entrepot.setHeureLivraison(entrepot.getPrecedente().getHeureLivraison() + entrepot.getPrecedente().getCheminVersSuivante().getDuree() + TEMPS_ARRET);

    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     *
     * @param nouvelleLivraison la livraison à ajouter
     * @param livraisonApres    la Livraison avant laquelle on ajoute la nouvelle Livraison
     * @param fenetreLivraison  la fenêtre de livraison dans laquelle ajouter la livraison
     */
    public void ajouterLivraison(final Livraison nouvelleLivraison, Livraison livraisonApres, FenetreLivraison fenetreLivraison) {

        nouvelleLivraison.getIntersection().setLivraison(nouvelleLivraison);
        nouvelleLivraison.setPrecedente(livraisonApres.getPrecedente());

        Chemin nouveauPCC1 = nouvelleLivraison.getPrecedente().calculPCC(nouvelleLivraison);
        logger.debug("Chemin de {} a {}", nouvelleLivraison.getPrecedente().getIntersection().getAdresse(), nouvelleLivraison.getIntersection().getAdresse());
        if (nouveauPCC1 != null) {

            Chemin ch = livraisonApres.getPrecedente().getCheminVersSuivante();
            int indexASupprimer = 0;
            for (int i = 0; i < itineraire.size(); i++) {
                if (ch == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }

            ch.getTroncons().forEach(Troncon::decrementeCompteurPassage);

            Chemin nouveauPCC2 = nouvelleLivraison.calculPCC(livraisonApres);
            logger.debug("Chemin de {} a {}", nouvelleLivraison.getIntersection().getAdresse(), livraisonApres.getIntersection().getAdresse());
            if (nouveauPCC2 != null) {
                livraisonApres.setPrecedente(nouvelleLivraison);
                nouvelleLivraison.getPrecedente().setCheminVersSuivante(nouveauPCC1);
                nouvelleLivraison.setCheminVersSuivante(nouveauPCC2);

                itineraire.add(indexASupprimer, nouveauPCC2); //mise a jour itineraire
                itineraire.add(indexASupprimer, nouveauPCC1); //mise a jour itineraire
                itineraire.remove(ch);

                nouveauPCC1.getTroncons().forEach(Troncon::incrementeCompteurPassage);
                nouveauPCC2.getTroncons().forEach(Troncon::incrementeCompteurPassage);

                fenetreLivraison.getLivraisons().add(nouvelleLivraison);
                mettreAJourLesHeuresAPartirDe(nouvelleLivraison);
            } else {
                logger.debug("chemin 2 null");
            }
        } else {
            logger.debug("Chemin 1 null");
        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);

    }

    /**
     * Supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     *
     * @param livraison la livraison à supprimer
     */
    public boolean supprimerLivraison(Livraison livraison) {
        if (livraison == entrepot) {
            logger.warn("Tentative de suppresion de l'entrepôt, action impossible sur l'entrepot");
            return false;
        } else if (livraison.getPrecedente() == livraison.getSuivante()) { //il ne reste qu'une livraison

            livraison.getIntersection().setLivraison(null);
            Chemin aller = entrepot.getCheminVersSuivante();
            Chemin retour = livraison.getCheminVersSuivante();

            aller.getTroncons().forEach(Troncon::decrementeCompteurPassage);
            retour.getTroncons().forEach(Troncon::decrementeCompteurPassage);
            Chemin cheminVide = new Chemin();
            cheminVide.setArrivee(entrepot);
            cheminVide.setDepart(entrepot);
            cheminVide.setDuree(0);
            cheminVide.setTroncons(new ArrayList<>());
            entrepot.setPrecedente(entrepot);
            entrepot.setCheminVersSuivante(cheminVide);
            itineraire.clear();

            for (FenetreLivraison f : fenetres) {
                if (f.getLivraisons().contains(livraison)) {
                    f.getLivraisons().remove(livraison);
                }
            }

            setChanged();
            notifyObservers(Evenement.ITINERAIRE_CALCULE);

            return true;

        }

        Chemin nouveauPCC = livraison.getPrecedente().calculPCC(livraison.getSuivante());

        if (nouveauPCC != null) {

            logger.debug("suppression de {}  pcc de {} vers {} longueur chemin {}", livraison.getIntersection().getAdresse(), nouveauPCC.getDepart().getIntersection().getAdresse(), nouveauPCC.getArrivee().getIntersection().getAdresse(), nouveauPCC.getTroncons().size());
            Chemin cheminASupprimer = livraison.getPrecedente().getCheminVersSuivante();
            Chemin cheminASupprimer2 = livraison.getCheminVersSuivante();

            int indexASupprimer = 0; //index de suppression
            for (int i = 0; i < itineraire.size(); i++) {
                if (cheminASupprimer == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }

            itineraire.add(indexASupprimer, nouveauPCC); //mise a jour itineraire
            itineraire.remove(cheminASupprimer);
            itineraire.remove(cheminASupprimer2);

            livraison.getPrecedente().getCheminVersSuivante().getTroncons().forEach(Troncon::decrementeCompteurPassage);

            livraison.getCheminVersSuivante().getTroncons().forEach(Troncon::decrementeCompteurPassage);

            livraison.getPrecedente().setCheminVersSuivante(nouveauPCC);
            livraison.getSuivante().setPrecedente(livraison.getPrecedente());

            nouveauPCC.getTroncons().forEach(Troncon::incrementeCompteurPassage);

            mettreAJourLesHeuresAPartirDe(livraison.getPrecedente());
            livraison.getIntersection().setLivraison(null);

            //on supprime la livraison de la fenetre
            for (FenetreLivraison f : fenetres) {
                if (f.getLivraisons().contains(livraison)) {
                    f.getLivraisons().remove(livraison);
                }
            }


        } else {

            logger.debug("PCC est null !!!!");
        }

        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);

        return true;
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires, les fenetre de livraison sont aussi echangées
     *
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        if (livr1 == entrepot || livr2 == entrepot) {
            logger.error("Action impossible sur l'entrepot");
            return;
        }

        if (livr1.getSuivante() == livr2) {
            Livraison temp = livr1;
            livr1 = livr2;
            livr2 = temp;

        }

        //prec et suiv l1
        Livraison livr1PrecTemp = livr1.getPrecedente();
        Livraison livr1SuivTemp = livr1.getSuivante();

        //prec et suiv l2
        Livraison livr2PrecTemp = livr2.getPrecedente();
        Livraison livr2SuivTemp = livr2.getSuivante();

        if (livr1PrecTemp == livr2) { //cas particulier, échange de livraisons consécutives

            //parcourus
            Chemin cheminl2precl1 = livr2PrecTemp.calculPCC(livr1);
            Chemin cheminl1l2 = livr1.calculPCC(livr2);
            Chemin cheminl2l1suiv = livr2.calculPCC(livr1SuivTemp);

            //a supprimer
            Chemin cheminl2precl2 = livr2PrecTemp.getCheminVersSuivante();
            Chemin cheminl2l1 = livr2.getCheminVersSuivante();
            Chemin cheminl1l1suiv = livr1.getCheminVersSuivante();

            List<Chemin> cheminsNonParcourus = new ArrayList<>();
            cheminsNonParcourus.add(cheminl2precl2);
            cheminsNonParcourus.add(cheminl2l1);
            cheminsNonParcourus.add(cheminl1l1suiv);

            List<Chemin> cheminsParcourus = new ArrayList<>();
            cheminsParcourus.add(cheminl1l2);
            cheminsParcourus.add(cheminl2precl1);
            cheminsParcourus.add(cheminl2l1suiv);

            //mise a jour des troncons non empruntes
            for (Chemin chemin : cheminsNonParcourus) {
                chemin.getTroncons().forEach(Troncon::decrementeCompteurPassage);
            }
            //mise a jour des troncons empruntes
            for (Chemin chemin : cheminsParcourus) {
                chemin.getTroncons().forEach(Troncon::incrementeCompteurPassage);
            }

            //changement itineraire
            int indexASupprimer = 0;
            for (int i = 0; i < itineraire.size(); i++) {
                if (cheminl2precl2 == itineraire.get(i)) {
                    indexASupprimer = i;
                }
            }

            itineraire.add(indexASupprimer, cheminl2l1suiv);
            itineraire.add(indexASupprimer, cheminl2l1);
            itineraire.add(indexASupprimer, cheminl2precl1);
            itineraire.remove(cheminl2precl2);
            itineraire.remove(cheminl2l1);
            itineraire.remove(cheminl1l1suiv);

            //Mise a jour des livraisons
            livr2PrecTemp.setCheminVersSuivante(cheminl2precl1);
            livr1.setPrecedente(livr2PrecTemp);
            livr1.setCheminVersSuivante(cheminl1l2);
            livr2.setPrecedente(livr1);
            livr2.setCheminVersSuivante(cheminl2l1suiv);
            livr1SuivTemp.setPrecedente(livr2);
        } else {

            //chemins de l1
            Chemin cheminl1prec = livr1PrecTemp.getCheminVersSuivante();
            Chemin cheminl1suiv = livr1.getCheminVersSuivante();

            //chemins de l2
            Chemin cheminl2prec = livr2PrecTemp.getCheminVersSuivante();
            Chemin cheminl2suiv = livr2.getCheminVersSuivante();

            //pcc 1
            Chemin cheminl1l2suiv = livr1.calculPCC(livr2.getSuivante());
            Chemin cheminl2precl1 = livr2PrecTemp.calculPCC(livr1);

            //pcc 2
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

                //mise a jour des troncons non empruntes
                for (Chemin chemin : cheminsNonParcourus) {
                    chemin.getTroncons().forEach(Troncon::decrementeCompteurPassage);
                }
                //mise a jour des troncons empruntes
                for (Chemin chemin : cheminsParcourus) {
                    chemin.getTroncons().forEach(Troncon::incrementeCompteurPassage);
                }

                //l1 chemins
                livr1.setPrecedente(livr2PrecTemp);
                livr1.setCheminVersSuivante(cheminl1l2suiv);
                livr2SuivTemp.setPrecedente(livr1);
                livr2PrecTemp.setCheminVersSuivante(cheminl2precl1);

                //l2 chemins
                livr2.setPrecedente(livr1PrecTemp);
                livr2.setCheminVersSuivante(cheminl2l1suiv);
                livr1SuivTemp.setPrecedente(livr2);
                livr1PrecTemp.setCheminVersSuivante(cheminl1precl2);


            }
        }

        //Mise a jour fenetres de livraison
        FenetreLivraison fenetre1 = trouverFenetreDeLivraison(livr1);
        FenetreLivraison fenetre2 = trouverFenetreDeLivraison(livr2);

        if (fenetre1 != fenetre2) {
            // Echange des fenetres
            fenetre1.getLivraisons().remove(livr1);
            fenetre1.getLivraisons().add(livr2);
            livr1.setHeureDebutFenetre(fenetre2.getHeureDebut());
            livr1.setHeureFinFenetre(fenetre2.getHeureFin());
            fenetre2.getLivraisons().remove(livr2);
            fenetre2.getLivraisons().add(livr1);
            livr2.setHeureDebutFenetre(fenetre1.getHeureDebut());
            livr2.setHeureFinFenetre(fenetre1.getHeureFin());
        }

        if (livr1.getHeureLivraison() < livr2.getHeureLivraison()) {
            mettreAJourLesHeuresAPartirDe(livr2);
        } else {
            mettreAJourLesHeuresAPartirDe(livr1);
        }
        //mettreAJourLesHeuresAPartirDe(entrepot.getSuivante());
        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }


    /**
     * réalloue tous les attributs à des attributs vides
     * Supprime tous les liens qu'ont les intersections vers les livraisons existantes
     */
    public void reset() {

        for (Chemin chemin : itineraire) {

            for (Troncon tr : chemin.getTroncons()) {
                tr.resetCompteur();
            }
        }

        itineraire = new ArrayList<>();

        // suppression de tous les liens Intersection -> Livraison
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

    /**
     * Permet de retrouver la FenetreDeLivraison dans laquelle se situe une livraison
     *
     * @param livraison La Livraison à rechercher
     * @return la FenetreDeLivraison correspondante si la Livraison est présente dans une FenetreDeLivraison, null sinon
     */
    public FenetreLivraison trouverFenetreDeLivraison(Livraison livraison) {
        List<FenetreLivraison> fenetres = this.fenetres;
        for (FenetreLivraison f : fenetres) {
            for (Livraison livraison1 : f.getLivraisons()) {
                if (livraison1.getIntersection().getAdresse() == livraison.getIntersection().getAdresse()) {
                    return f;
                }
            }
        }
        if (livraison == entrepot) {
            return fenetres.get(fenetres.size() - 1); // on renvoie la derniere fenetre de livraison pour pouvoir ajouter une livraison en dernière position
        } else {
            return null;
        }
    }

    public List<Chemin> getItineraire() {
        return itineraire;
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

    public boolean genererFeuilleDeRoute() throws IOException {
        return GenerateurDeFeuilleDeRoute.INSTANCE.genererFeuilleDeRoute(entrepot, heureDebutItineraire, itineraire, TEMPS_ARRET);
    }
}