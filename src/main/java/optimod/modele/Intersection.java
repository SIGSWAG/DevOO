package optimod.modele;

import java.util.*;

/**
 * 
 */
public class Intersection {

    /**
     * Default constructor
     */
    public Intersection() {
    }

    /**
     * 
     */
    private int adresse;

    /**
     * 
     */
    private int x;

    private int y;

    /**
     * 
     */
    private Plan plan;

    /**
     * 
     */
    public List<Troncon> sortants;

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

}