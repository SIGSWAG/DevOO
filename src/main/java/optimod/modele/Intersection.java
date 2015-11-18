package optimod.modele;

import java.util.*;

/**
 * 
 */
public class Intersection {

    /**
     * Default constructor
     */
    public Intersection(int x, int y, int adresse) {
    }

    /**
     * 
     */
    private int adresse;

    /**
     * 
     */
    private int x;

    /**
     *
     */
    private int y;


    /**
     * 
     */
    private List<Troncon> sortants;

    /**
     * 
     */
    private Livraison livraison;

    /**
     * @param x 
     * @param y 
     * @param radius
     */
    public boolean estLocalisee(int x, int y, int radius) {
        // TODO implement here
        return false;
    }

    public int getAdresse() {
        return adresse;
    }

    public void setAdresse(int adresse) {
        this.adresse = adresse;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }



    public List<Troncon> getSortants() {
        return sortants;
    }

    public void setSortants(List<Troncon> sortants) {
        this.sortants = sortants;
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }
}