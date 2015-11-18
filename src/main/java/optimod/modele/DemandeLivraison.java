package optimod.modele;

import java.util.*;

public class DemandeLivraison {

    /**
     * Default constructor
     */
    public DemandeLivraison(Plan pl) {
        this.plan = pl;
    }


    private List<Chemin> itineraire = new ArrayList<Chemin>();

    private Plan plan;

    private List<FenetreLivraison> fenetres;


    public void chargerDemandeLivraison() {
        // TODO implement here
    }

    /**
     * récupère tous les chemins entres les intersections de chaque fenetre horaire et d'une fenetre horaire vers la suivante
     * c'est à dire créé le graphe dans lequel on veut trouver le plus court chemin.
     * Puis appelle la méthode de GraphePCC permettant de trouver le + court parcours dans ce graphe.
     */
    public void calculerItineraire() {
        List<Chemin> graphe = new ArrayList<Chemin>();
        // toutes les fenetres sauf la derniere
        for(int i = 0 ; i < fenetres.size() - 1 ; i++){
            FenetreLivraison fenetre = fenetres.get(i);
            graphe.addAll(fenetre.calculPCCInterne());
            graphe.addAll(fenetre.calculPCCSuivant(fenetres.get(i+1)));
        }
        graphe.addAll(fenetres.get(fenetres.size()-1).calculPCCInterne());
        GraphePCC leGrapheAResoudre = new GraphePCC(graphe);
        this.itineraire = leGrapheAResoudre.calculerItineraire();
    }

    /**
     * met à jour toutes les horaires de livraison prévues à partir d'une livraison donnée et jusqu'à l'entrepot
     * @param livr la livraison à partir de laquelle on recalcule les heures
     */
    private void mettreAJourLesHeuresAPartirDe(Livraison livr) {
        /**
         * TODO
         */
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
        livr.getSuivante().setPrecedente(livr.getPrecedente());
        Chemin nouveauPCC = livr.getPrecedente().calculPCC(livr.getSuivante());
        livr.getPrecedente().setCheminVersSuivante(nouveauPCC);
        mettreAJourLesHeuresAPartirDe(livr.getPrecedente());
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        Livraison livr1PrecTemp = livr1.getPrecedente();
        Livraison livr1SuivTemp = livr1.getSuivante();

        livr1.setPrecedente(livr2.getPrecedente());
        livr1.calculPCC(livr2.getSuivante());
        livr1.getPrecedente().calculPCC(livr1);

        livr2.setPrecedente(livr1PrecTemp);
        livr2.calculPCC(livr1SuivTemp);
        livr2.getPrecedente().calculPCC(livr2);

        if(livr1.getHeureLivraison() < livr2.getHeureLivraison())
            mettreAJourLesHeuresAPartirDe(livr1);
        else
            mettreAJourLesHeuresAPartirDe(livr2);
    }

    /**
     * réalloue tous les attributs à des attributs vides
     */
    public void reset(){
        itineraire = new ArrayList<Chemin>();
        plan = new Plan();
        fenetres = new ArrayList<FenetreLivraison>();
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
}