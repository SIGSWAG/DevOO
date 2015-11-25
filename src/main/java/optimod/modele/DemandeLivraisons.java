package optimod.modele;

import optimod.es.xml.DeserialiseurXML;
import optimod.es.xml.ExceptionXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class DemandeLivraisons extends Observable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static int TEMPS_ARRET = 1;

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
        this.itineraire = new ArrayList<Chemin>();
        this.fenetres = new ArrayList<FenetreLivraison>();

    }


    public boolean chargerDemandeLivraison() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        boolean demandeLivraisonChargee = DeserialiseurXML.INSTANCE.chargerDemandeLivraison(this);
        if(demandeLivraisonChargee){
            setChanged();
            notifyObservers(Evenement.DEMANDE_LIVRAISONS_CHARGEES);
        }
        return demandeLivraisonChargee;
    }

    /**
     * récupère tous les chemins entres les intersections de chaque fenetre horaire et d'une fenetre horaire vers la suivante
     * c'est à dire créé le graphe dans lequel on veut trouver le plus court chemin.
     * Puis appelle la méthode de GraphePCC permettant de trouver le + court parcours dans ce graphe.
     */
    public void calculerItineraire() {

        System.out.println("Calcul lancé");
        List<Chemin> graphe = new ArrayList<>();

        List<Livraison> listeEntrepot = new ArrayList<>();
        listeEntrepot.add(entrepot);
        FenetreLivraison fenEntrepot = new FenetreLivraison(listeEntrepot,0,1);


        graphe.addAll(fenEntrepot.calculPCCSuivant(fenetres.get(0)));//on calcule les plus courts chemins de l'entrepot a la premiere fenetre


        // toutes les fenetres sauf la derniere
        for(int i = 0 ; i < fenetres.size() - 1 ; i++){
            FenetreLivraison fenetre = fenetres.get(i);
            graphe.addAll(fenetre.calculPCCInterne());
            graphe.addAll(fenetre.calculPCCSuivant(fenetres.get(i+1)));
        }
        graphe.addAll(fenetres.get(fenetres.size()-1).calculPCCInterne());
        graphe.addAll(fenetres.get(fenetres.size()-1).calculPCCSuivant(fenEntrepot)); //calcul PCC vers entrepot.
        GraphePCC leGrapheAResoudre = new GraphePCC(entrepot,graphe);
        this.itineraire = leGrapheAResoudre.calculerItineraire(); //calcul de l'itinéraire

        //il faut maintenant mettre à jour les livraisons (suivantes et précédentes)
        int heureDepartItineraire = 0;
        int duree = 0;
        int fenetreCouranteDebut = 0; //heure de début de fenetre de la livraison courante
        Chemin premierChemin = itineraire.get(0);
        Livraison premiereLivraison = premierChemin.getArrivee();
        premiereLivraison.setHeureLivraison(premiereLivraison.getHeureDebutFenetre()); //la première livraison arrive
        fenetreCouranteDebut = premiereLivraison.getHeureDebutFenetre();
        //à l'heure de sa fenetre
        heureDebutItineraire = premiereLivraison.getHeureDebutFenetre()-premierChemin.getDuree(); //on part de l'entrepot

        for(Chemin chemin : itineraire){

            mettreAJourTronconsEmpruntes(chemin);

            Livraison arrivee = chemin.getArrivee();
            Livraison depart = chemin.getDepart();
            depart.setCheminVersSuivante(chemin); //on maj le chemin vers la livraison suivante
            arrivee.setPrecedente(depart);//on maj le pointeur vers la livraison precedente

            if(depart.getIntersection().getAdresse() != entrepot.getIntersection().getAdresse()) {//temps déja maj pour l'entrepot
                if (arrivee.getHeureDebutFenetre() < depart.getHeureLivraison() + chemin.getDuree() + TEMPS_ARRET) { //pas d'attente
                    arrivee.setHeureLivraison(depart.getHeureLivraison() + chemin.getDuree() + TEMPS_ARRET);
                } else {
                    arrivee.setHeureLivraison(arrivee.getHeureDebutFenetre());
                }
            }

        }

        for(Chemin chemin : itineraire){
            System.out.println("Depart "+chemin.getDepart().getIntersection().getAdresse()+" arrivee "+chemin.getArrivee().getIntersection().getAdresse());

        }
        setChanged();

        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }


    private void mettreAJourTronconsEmpruntes(Chemin chemin){

       for(Troncon troncon : chemin.getTroncons()){

           troncon.incrementeCompteurPassage();
       }

    }

    /**
     * met à jour toutes les horaires de livraison prévues à partir d'une livraison donnée et jusqu'à l'entrepot
     * @param livr la livraison à partir de laquelle on recalcule les heures
     */
    private void mettreAJourLesHeuresAPartirDe(Livraison livr) {
        while(!livr.equals(entrepot)){
            livr.setHeureLivraison(livr.getPrecedente().getHeureLivraison() + livr.getPrecedente().getCheminVersSuivante().getDuree() + TEMPS_ARRET);
            livr = livr.getSuivante();
        }
        entrepot.setHeureLivraison(entrepot.getPrecedente().getHeureLivraison()+entrepot.getPrecedente().getCheminVersSuivante().getDuree()+TEMPS_ARRET);

    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     * @param intersection l'intersection sur laquelle on ajoute la livraison
     * @param livr la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Intersection intersection, Livraison livr) {
        Livraison nouvelleLivraison = new Livraison(intersection);
        nouvelleLivraison.setPrecedente(livr.getPrecedente());

        Chemin nouveauPCC1 = nouvelleLivraison.getPrecedente().calculPCC(nouvelleLivraison);
        if(nouveauPCC1 != null){

            Chemin ch = livr.getPrecedente().getCheminVersSuivante();
            int indexASupprimer = 0;
            for(int i=0; i<itineraire.size(); i++){
                if(ch == itineraire.get(i)){
                    indexASupprimer = i;
                }
            }

            for(Troncon troncon : ch.getTroncons()){
                troncon.decrementeCompteurPassage();
            }

            Chemin nouveauPCC2 = nouvelleLivraison.calculPCC(livr);

            if(nouveauPCC2 != null){
                livr.setPrecedente(nouvelleLivraison);
                nouvelleLivraison.getPrecedente().setCheminVersSuivante(nouveauPCC1);
                nouvelleLivraison.setCheminVersSuivante(nouveauPCC2);

                itineraire.add(indexASupprimer, nouveauPCC2); //mise a jour itineraire
                itineraire.add(indexASupprimer, nouveauPCC1); //mise a jour itineraire
                itineraire.remove(ch);


                for(Troncon troncon : nouveauPCC1.getTroncons()){
                    troncon.incrementeCompteurPassage();
                }
                for(Troncon troncon : nouveauPCC2.getTroncons()){
                    troncon.incrementeCompteurPassage();
                }

                mettreAJourLesHeuresAPartirDe(nouvelleLivraison);

            }



        }


        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);

    }

    /**
     * supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     * @param livr la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        if(livr == entrepot){
            logger.error("Tentative de suppresion de l'entrepôt, action impossible sur l'entrepot");
            return;
        }

        Chemin nouveauPCC = livr.getPrecedente().calculPCC(livr.getSuivante());


        if(nouveauPCC != null){

            System.out.println("suppression de "+livr.getIntersection().getAdresse()+" pcc de "+
            nouveauPCC.getDepart().getIntersection().getAdresse()+" vers "+nouveauPCC.getArrivee().getIntersection().getAdresse()+" longueur chemin "+nouveauPCC.getTroncons().size());
            Chemin cheminASupprimer = livr.getPrecedente().getCheminVersSuivante();

            int indexASupprimer = 0; //index de suppression
            for(int i=0; i<itineraire.size(); i++){
                if(cheminASupprimer == itineraire.get(i)){
                    indexASupprimer = i;
                }
            }

            itineraire.add(indexASupprimer, nouveauPCC); //mise a jour itineraire
            itineraire.remove(cheminASupprimer);

            for(Troncon tr : livr.getPrecedente().getCheminVersSuivante().getTroncons()){
                tr.decrementeCompteurPassage();
            }

            for(Troncon tr : livr.getCheminVersSuivante().getTroncons()){
                tr.decrementeCompteurPassage();
            }

            livr.getPrecedente().setCheminVersSuivante(nouveauPCC);
            livr.getSuivante().setPrecedente(livr.getPrecedente());

            for(Troncon tr : nouveauPCC.getTroncons()){
                tr.incrementeCompteurPassage();
            }
            mettreAJourLesHeuresAPartirDe(livr.getPrecedente());
            livr.getIntersection().setLivraison(null);
        }else{

            System.out.println("PCC est null !!!!");
        }


        setChanged();
        notifyObservers(Evenement.ITINERAIRE_CALCULE);
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires, les fenetre de livraison sont aussi echangées
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        // TODO Faire attention aux fenetres !
        if(livr1 == entrepot || livr2 == entrepot) {
            logger.error("Action impossible sur l'entrepot");
            return;
        }
        //prec et suiv l1
        Livraison livr1PrecTemp = livr1.getPrecedente();
        Livraison livr1SuivTemp = livr1.getSuivante();

        //prec et suiv l2
        Livraison livr2PrecTemp = livr2.getPrecedente();
        Livraison livr2SuivTemp = livr2.getSuivante();

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

        if(cheminl1l2suiv != null && cheminl1precl2 != null && cheminl2l1suiv != null && cheminl2precl1 != null ) {

            int indexASupprimer = 0;
            for(int i=0; i<itineraire.size(); i++){
                if(cheminl1prec == itineraire.get(i)){
                    indexASupprimer = i;
                }
            }
            itineraire.add(indexASupprimer,cheminl2l1suiv);
            itineraire.add(indexASupprimer,cheminl1precl2);
            itineraire.remove(cheminl1prec);
            itineraire.remove(cheminl1suiv);

            for(int i=0; i<itineraire.size(); i++){
                if(cheminl2prec == itineraire.get(i)){
                    indexASupprimer = i;
                }
            }
            itineraire.add(indexASupprimer,cheminl1l2suiv);
            itineraire.add(indexASupprimer,cheminl2precl1);
            itineraire.remove(cheminl2prec);
            itineraire.remove(cheminl2suiv);


            //mise a jour des troncons non empruntes
            for(Chemin chemin : cheminsNonParcourus){
                for(Troncon troncon : chemin.getTroncons()){
                    troncon.decrementeCompteurPassage();
                }
            }
            //mise a jour des troncons empruntes
            for(Chemin chemin : cheminsParcourus){
                for(Troncon troncon : chemin.getTroncons()){
                    troncon.incrementeCompteurPassage();
                }
            }

            //l1 chemins
            livr1.setPrecedente(livr2PrecTemp);
            livr1.setCheminVersSuivante(cheminl1l2suiv);
            livr2SuivTemp.setPrecedente(livr1);
            livr2PrecTemp.setCheminVersSuivante(cheminl2precl1);

            //l2 chemins
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
                // Echange des fenetres
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
     * réalloue tous les attributs à des attributs vides
     * Supprime tous les liens qu'ont les intersections vers les livraisons existantes
     */
    public void reset(){



        for(Chemin chemin : itineraire) {

            for (Troncon tr : chemin.getTroncons()) {
                tr.resetCompteur();
            }
        }




        itineraire = new ArrayList<Chemin>();




        // suppression de tous les liens Intersection -> Livraison
        for (FenetreLivraison f : fenetres){
            for (Livraison l : f.getLivraisons()){
                l.getIntersection().setLivraison(null);
            }
        }

        fenetres = new ArrayList<FenetreLivraison>();

        if(entrepot != null) {
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

        System.out.println("Depart entrepot : ("+entrepot.getIntersection().getAdresse()+")"+heureDebutItineraire );
        for(Chemin chemin : itineraire) {



        }


    }
}