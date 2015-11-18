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
    public Collection<Intersection> getIntersections() {
        // TODO implement here
        return null;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }
}