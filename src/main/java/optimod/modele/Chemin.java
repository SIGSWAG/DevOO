package optimod.modele;

import java.util.*;

/**
 * 
 */
public class Chemin {

    /**
     * Default constructor
     */

    public Chemin() {
    }

    /**
     * 
     */
    private int duree;

    /**
     * 
     */
    private List<Intersection> intersections = new ArrayList<Intersection>();

    /**
     * 
     */
    private Livraison depart;

    /**
     * 
     */
    private Livraison arrivee;



    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public Livraison getDepart() {
        return depart;
    }

    public void setDepart(Livraison depart) {
        this.depart = depart;
    }

    public Livraison getArrivee() {
        return arrivee;
    }

    public void setArrivee(Livraison arrivee) {
        this.arrivee = arrivee;
    }
}