package optimod.modele;

import optimod.modele.Intersection;

import java.util.*;

/**
 * 
 */
public class Plan {

    /**
     * Default constructor
     */
    public Plan() {
    }

    /**
     * 
     */
    private List<Intersection> intersections;

    /**
     * 
     */
    public void chargerPlan() {
        /**
         * TODO
         */
    }

    /**
     * @param adresse
     */
    public Intersection trouverIntersection(int adresse) {
        Intersection intersectionTrouvee = null;
        for(Intersection inter: intersections) {
            if (inter.adresse == adresse) {
                intersectionTrouvee = inter;
                break;
            }
        }
        return intersectionTrouvee;
    }

    /**
     * contrat : trouve la 1ere intersection dans le cercle, même si il y en a plusieurs (attention au radius trop grand)
     * @param x 
     * @param y 
     * @param radius
     */
    public Intersection trouverIntersection(int x, int y, int radius) {
        Intersection intersectionTrouvee = null;
        for(Intersection inter: intersections) {
            if ( Math.pow(inter.x - x, 2) + Math.pow(inter.y - y, 2) <= radius*radius ) {
                intersectionTrouvee = inter;
                break;
            }
        }
        return intersectionTrouvee;
    }

    /**
     * 
     */
    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }
}