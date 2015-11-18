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

    private static Plan monInstance = new Plan();

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
     * @param adresse l'identifiant où trouver l'intersection
     * @return l'intersection correspondant à l'adresse
     */
    public Intersection trouverIntersection(int adresse) {
        Intersection intersectionTrouvee = null;
        for(Intersection inter: intersections) {
            if (inter.getAdresse() == adresse) {
                intersectionTrouvee = inter;
                break;
            }
        }
        return intersectionTrouvee;
    }


    public static Plan getInstance() {
        return monInstance;
    }

    /**
     * contrat : trouve la 1ere intersection dans le cercle, même si il y en a plusieurs (attention au radius trop grand)
     * @param x la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y la coordonnée y du cercle dans lequel trouver l'intersection
     * @param rayon le rayon du cercle dans lequel trouver l'intersection
     * @return la 1ere intersection dans le cercle, même si il y en a plusieurs
     */
    public Intersection trouverIntersection(int x, int y, int rayon) {
        Intersection intersectionTrouvee = null;
        for(Intersection inter: intersections) {
            if ( inter.estLocalisee(x, y, rayon) ) {
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