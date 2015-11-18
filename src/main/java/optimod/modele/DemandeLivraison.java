package optimod.modele;

import java.util.*;

/**
 * 
 */
public class DemandeLivraison {

    /**
     * Default constructor
     */
    public DemandeLivraison(Plan pl) {
    }

    /**
     * 
     */
    private List<Chemin> itineraire;

    /**
     *
     *
     */
    private Plan plan;

    /**
     * 
     */
    private List<FenetreLivraison> fenetres;

    /**
     * 
     */
    public void chargerDemandeLivraison() {
        // TODO implement here
    }

    /**
     * 
     */
    public void calculerItineraire() {
        // TODO implement here
    }

    /**
     * @param intersection
     * @param livr
     */
    public void ajouterLivraison(Intersection intersection, Livraison livr) {
        // TODO implement here
    }

    /**
     * @param livr
     */
    public void supprimerLivraison(Livraison livr) {
        // TODO implement here
    }

    /**
     * @param livr1 
     * @param livr2
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        // TODO implement here
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