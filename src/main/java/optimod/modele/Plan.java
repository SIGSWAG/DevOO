package optimod.modele;

import optimod.es.xml.DeserialiseurXML;

import java.util.ArrayList;
import java.util.List;

public class Plan {

    /**
     * Default constructor
     */
    public Plan() {
    }


    private List<Intersection> intersections = new ArrayList<Intersection>();


    public void chargerPlan() {
        try {
            DeserialiseurXML.INSTANCE.chargerPlan(this);
        }
        catch( Exception e){
            // TODO Gérer les exceptions
            System.out.println(e.getMessage());
        }
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

    /**
     * réalloue tous les attributs à des attributs vides
     */
    public void reset(){
        intersections = new ArrayList<Intersection>();
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

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }
}