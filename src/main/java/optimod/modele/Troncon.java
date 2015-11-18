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
    }

    /**
     * 
     */
    private int duree;

    /**
     * 
     */
    public Intersection arrivee;

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public void setArrivee(Intersection arrivee) {
        this.arrivee = arrivee;
    }
}