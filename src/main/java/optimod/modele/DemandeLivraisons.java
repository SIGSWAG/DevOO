package optimod.modele;

import optimod.es.xml.DeserialiseurXML;
import optimod.es.xml.ExceptionXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class DemandeLivraisons extends Observable {


    private static int TEMPS_ARRET = 1;

    private List<Chemin> itineraire;

    private Plan plan;

    private List<FenetreLivraison> fenetres;

    private Livraison entrepot;

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
            notifyObservers();
        }
        return demandeLivraisonChargee;
    }

    /**
     * récupère tous les chemins entres les intersections de chaque fenetre horaire et d'une fenetre horaire vers la suivante
     * c'est à dire créé le graphe dans lequel on veut trouver le plus court chemin.
     * Puis appelle la méthode de GraphePCC permettant de trouver le + court parcours dans ce graphe.
     */
    public void calculerItineraire() {
        List<Chemin> graphe = new ArrayList<Chemin>();

        List<Livraison> listeEntrepot = new ArrayList<Livraison>();
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
        heureDepartItineraire = premiereLivraison.getHeureDebutFenetre()-premierChemin.getDuree(); //on part de l'entrepot

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







    }


    private void mettreAJourTronconsEmpruntes(Chemin chemin){

        Intersection depart = chemin.getDepart().getIntersection();
        Intersection arrivee = chemin.getArrivee().getIntersection();

        if(chemin.getIntersections() != null && chemin.getIntersections().size()>0){

            //depart vers premiereIntersection
            Troncon premierTroncon = depart.getTronconVers(chemin.getIntersections().get(0));
            if(premierTroncon != null ){
                premierTroncon.setEstEmprunte(true);
            }

            for(int i=1; i<chemin.getIntersections().size()-1; i++){ //intersection quelconque vers suivante
                Intersection dep = chemin.getIntersections().get(i-1);
                Intersection arr = chemin.getIntersections().get(i);

                Troncon troncon = dep.getTronconVers(arr);
                if(troncon != null ){
                    troncon.setEstEmprunte(true);
                }
            }

            // de la derniere intersection vers l'arrivee
            Intersection derniereIntersection = chemin.getIntersections().get(chemin.getIntersections().size()-1);
            Troncon dernierTroncon = derniereIntersection.getTronconVers(arrivee);

            if(dernierTroncon != null ){
                dernierTroncon.setEstEmprunte(true);
            }


        }else { //chemin direct, pas d'intersection
            Troncon troncon = depart.getTronconVers(arrivee);
            troncon.setEstEmprunte(true);
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
        livr.setPrecedente(nouvelleLivraison);
        Chemin nouveauPCC1 = nouvelleLivraison.getPrecedente().calculPCC(nouvelleLivraison);
        nouvelleLivraison.getPrecedente().setCheminVersSuivante(nouveauPCC1);
        Chemin nouveauPCC2 = nouvelleLivraison.calculPCC(livr);
        nouvelleLivraison.setCheminVersSuivante(nouveauPCC2);
        mettreAJourLesHeuresAPartirDe(nouvelleLivraison);
    }

    /**
     * supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     * @param livr la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        if(livr == entrepot){
            System.out.println("erreur, action impossible sur l'entrepot");
            return;
        }
        livr.getSuivante().setPrecedente(livr.getPrecedente());
        Chemin nouveauPCC = livr.getPrecedente().calculPCC(livr.getSuivante());
        livr.getPrecedente().setCheminVersSuivante(nouveauPCC);
        mettreAJourLesHeuresAPartirDe(livr.getPrecedente());
        livr.getIntersection().setLivraison(null);
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires, les fenetre de livraison sont aussi echangées
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        // TODO Faire attention aux fenetres !
        if(livr1 == entrepot || livr2 == entrepot){
            System.out.println("erreur, action impossible sur l'entrepot");
            return;
        }
        Livraison livr1PrecTemp = livr1.getPrecedente();
        Livraison livr1SuivTemp = livr1.getSuivante();

        livr1.setPrecedente(livr2.getPrecedente());
        livr1.calculPCC(livr2.getSuivante());
        livr1.getPrecedente().calculPCC(livr1);

        livr2.setPrecedente(livr1PrecTemp);
        livr2.calculPCC(livr1SuivTemp);
        livr2.getPrecedente().calculPCC(livr2);

        FenetreLivraison fenetre1 = null, fenetre2 = null;
        for (FenetreLivraison f : this.fenetres) {
            if(fenetre1 == null && f.getLivraisons().contains(livr1)){
                fenetre1 = f;
            }
            if(fenetre2 == null && f.getLivraisons().contains(livr2)){
                fenetre2 = f;
            }
            if(fenetre1 != null && fenetre2 != null){
                break;
            }
        }
        if(fenetre1 != fenetre2){
            // Echange des fenetres
            fenetre1.getLivraisons().remove(livr1);
            fenetre1.getLivraisons().add(livr2);
            fenetre2.getLivraisons().remove(livr2);
            fenetre2.getLivraisons().add(livr1);
        }


        if(livr1.getHeureLivraison() < livr2.getHeureLivraison()) {
            mettreAJourLesHeuresAPartirDe(livr1);
        }
        else {
            mettreAJourLesHeuresAPartirDe(livr2);
        }
    }

    /**
     * réalloue tous les attributs à des attributs vides
     * Supprime tous les liens qu'ont les intersections vers les livraisons existantes
     */
    public void reset(){
        itineraire = new ArrayList<Chemin>();

        //reset les troncons (est emprunte)
        List<Intersection> intersections = plan.getIntersections();
        for(Intersection intersection : intersections){
            if(intersection.getSortants() != null && intersection.getSortants().size()>0){
                List<Troncon> sortants = intersection.getSortants();
                for(Troncon troncon : sortants){
                    troncon.setEstEmprunte(false);
                }
            }
        }


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

    }
}