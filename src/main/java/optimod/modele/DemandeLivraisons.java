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

public class DemandeLivraisons extends Observable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int TEMPS_ARRET = 10 * 60;

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

        if(livr.getPrecedente() == entrepot){
            livr.setHeureLivraison(livr.getHeureDebutFenetre());
            livr = livr.getSuivante();
            heureDebutItineraire = entrepot.getCheminVersSuivante().getDuree();
        }
        while(!livr.equals(entrepot)){
            int heurePrevue = livr.getPrecedente().getHeureLivraison() + livr.getPrecedente().getCheminVersSuivante().getDuree() + TEMPS_ARRET;

            int heureFinale =  heurePrevue > livr.getHeureDebutFenetre() ? heurePrevue : livr.getHeureDebutFenetre();

            livr.setHeureLivraison(heureFinale);
            livr = livr.getSuivante();
        }
        entrepot.setHeureLivraison(entrepot.getPrecedente().getHeureLivraison()+entrepot.getPrecedente().getCheminVersSuivante().getDuree()+TEMPS_ARRET);

    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     * @param nouvelleLivraison la livraison à ajouter
     * @param livr la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Livraison nouvelleLivraison, Livraison livr, FenetreLivraison fenetreLivraison) {

        nouvelleLivraison.getIntersection().setLivraison(nouvelleLivraison);
        nouvelleLivraison.setPrecedente(livr.getPrecedente());

        Chemin nouveauPCC1 = nouvelleLivraison.getPrecedente().calculPCC(nouvelleLivraison);
        System.out.println("Chemin de "+nouvelleLivraison.getPrecedente().getIntersection().getAdresse()+" a "+nouvelleLivraison.getIntersection().getAdresse());
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
            System.out.println("Chemin de "+nouvelleLivraison.getIntersection().getAdresse()+" a "+livr.getIntersection().getAdresse());
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

                fenetreLivraison.getLivraisons().add(nouvelleLivraison);
                mettreAJourLesHeuresAPartirDe(nouvelleLivraison);
            }else{
                System.out.println("chemin 2 null");
            }
        }else{
            System.out.println("Chemin 1 null");
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
        }else if(livr.getPrecedente() == livr.getSuivante()){ //il ne reste qu'une livraison

            livr.getIntersection().setLivraison(null);
            Chemin aller = entrepot.getCheminVersSuivante();
            Chemin retour = livr.getCheminVersSuivante();

            for(Troncon tr : aller.getTroncons()){
                tr.decrementeCompteurPassage();
            }
            for(Troncon tr : retour.getTroncons()){
                tr.decrementeCompteurPassage();
            }
            Chemin cheminVide = new Chemin();
            cheminVide.setArrivee(entrepot);
            cheminVide.setDepart(entrepot);
            cheminVide.setDuree(0);
            cheminVide.setTroncons(new ArrayList<>());
            entrepot.setPrecedente(entrepot);
            entrepot.setCheminVersSuivante(cheminVide);
            itineraire.clear();

            for (FenetreLivraison f : this.fenetres) {
                if (f.getLivraisons().contains(livr)) {
                    f.getLivraisons().remove(livr);
                }
            }

            setChanged();
            notifyObservers(Evenement.ITINERAIRE_CALCULE);

            return;

        }

        Chemin nouveauPCC = livr.getPrecedente().calculPCC(livr.getSuivante());


        if(nouveauPCC != null){

            System.out.println("suppression de "+livr.getIntersection().getAdresse()+" pcc de "+
                    nouveauPCC.getDepart().getIntersection().getAdresse()+" vers "+nouveauPCC.getArrivee().getIntersection().getAdresse()+" longueur chemin "+nouveauPCC.getTroncons().size());
            Chemin cheminASupprimer = livr.getPrecedente().getCheminVersSuivante();
            Chemin cheminASupprimer2 = livr.getCheminVersSuivante();

            int indexASupprimer = 0; //index de suppression
            for(int i=0; i<itineraire.size(); i++){
                if(cheminASupprimer == itineraire.get(i)){
                    indexASupprimer = i;
                }
            }

            itineraire.add(indexASupprimer, nouveauPCC); //mise a jour itineraire
            itineraire.remove(cheminASupprimer);
            itineraire.remove(cheminASupprimer2);

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

            //on supprime la livraison de la fenetre
            for (FenetreLivraison f : this.fenetres) {
                if (f.getLivraisons().contains(livr)) {
                    f.getLivraisons().remove(livr);
                }
            }


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

        if(livr1.getSuivante() == livr2){
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



        if(livr1PrecTemp == livr2){ //cas particulier, échange de livraisons consécutives

            //parcourus
            Chemin cheminl2precl1 = livr2PrecTemp.calculPCC(livr1);
            Chemin cheminl1l2 = livr1.calculPCC(livr2);
            Chemin cheminl2l1suiv = livr2.calculPCC(livr1SuivTemp);

            //a supprimer
            Chemin cheminl2precl2 = livr2PrecTemp.getCheminVersSuivante();
            Chemin cheminl2l1 = livr2.getCheminVersSuivante();
            Chemin cheminl1l1suiv = livr1.getCheminVersSuivante();

            List<Chemin> cheminsNonParcourus = new ArrayList<>();
            cheminsNonParcourus.add(cheminl2precl2 );
            cheminsNonParcourus.add(cheminl2l1);
            cheminsNonParcourus.add(cheminl1l1suiv);

            List<Chemin> cheminsParcourus = new ArrayList<>();
            cheminsParcourus.add(cheminl1l2);
            cheminsParcourus.add(cheminl2precl1);
            cheminsParcourus.add(cheminl2l1suiv);

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
        }else {


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
                    for (Troncon troncon : chemin.getTroncons()) {
                        troncon.decrementeCompteurPassage();
                    }
                }
                //mise a jour des troncons empruntes
                for (Chemin chemin : cheminsParcourus) {
                    for (Troncon troncon : chemin.getTroncons()) {
                        troncon.incrementeCompteurPassage();
                    }
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


    private void remplacerDansItineraire(Chemin remplacer, List<Chemin> nouveaux){

        int indexASupprimer = 0;
        for(int i=0; i<itineraire.size(); i++){
            if(remplacer == itineraire.get(i)){
                indexASupprimer = i;
            }
        }
        for(int i = nouveaux.size() - 1; i>=0; i--){
            itineraire.add(indexASupprimer, nouveaux.get(i));
        }
        itineraire.remove(remplacer);

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

    /**
     * Permet de retrouver la FenetreDeLivraison dans laquelle se situe une livraison
     * @param livraison
     * @return la FenetreDeLivraison correspondante si la
     */
    public FenetreLivraison trouverFenetreDeLivraison(Livraison livraison){
        List<FenetreLivraison> fenetres = this.fenetres;
        for (FenetreLivraison f : fenetres) {
            for(Livraison livraison1 : f.getLivraisons()){
                if(livraison1.getIntersection().getAdresse() == livraison.getIntersection().getAdresse() ){
                    return f;
                }
            }
        }
        if(livraison == entrepot)
            return fenetres.get(fenetres.size()-1); // on renvoie la derniere fenetre de livraison pour pouvoir ajouter une livraison en dernière position
        else
            return null;
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

    public boolean genererFeuilleDeRoute() throws IOException {
        return GenerateurDeFeuilleDeRoute.INSTANCE.genererFeuilleDeRoute(entrepot, heureDebutItineraire, itineraire, TEMPS_ARRET);
    }
}