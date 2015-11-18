package optimod.modele;

import java.util.*;

/**
 * 
 */
public class Troncon {

    /**
     * Default constructor
     */
    public Troncon(Intersection intersection, int duree) {
        this.duree = duree;
        this.arrivee = intersection
    }

    /**
     * 
     */
    private int duree;

    /**
     * 
     */
    private Intersection arrivee;

}