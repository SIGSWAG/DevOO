package optimod.modele;

import java.util.*;

/**
 * 
 */
public class Ordonnanceur {

    /**
     * Default constructor
     */
    public Ordonnanceur() {
    }

    private DemandeLivraison demandeLivraison;

    private Plan plan;

    /**
     *
     */
    public void chargerPlan() {
        // TODO implement here
    }

    /**
     * @param adresse
     */
    public Intersection trouverIntersection(int adresse) {
        // TODO implement here
        return null;
    }

    /**
     * @param x 
     * @param y 
     * @param radius
     */
    public Intersection trouverIntersection(int x, int y, int radius) {
        // TODO implement here
        return null;
    }

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
        demandeLivraison.calculerItineraire();
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

    public DemandeLivraison getDemandeLivraison() {
        return demandeLivraison;
    }

    public void setDemandeLivraison(DemandeLivraison demandeLivraison) {
        this.demandeLivraison = demandeLivraison;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}