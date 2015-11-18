package optimod.modele;

import optimod.modele.Intersection;

import java.util.*;

/**
 * 
 */
public class Livraison {

    /**
     * Default constructor
     */
    public Livraison(Intersection intersection) {
        this.intersection = intersection;
    }

    /**
     * 
     */
    private int heureLivraison;

    /**
     * 
     */
    private Intersection intersection;

    /**
     * 
     */
    private Chemin cheminVersSuivante;

    /**
     * 
     */
    private Livraison precedente;

    /**
     * @param l1 la livraison vers laquelle on souhaite se diriger
     * @return le plus court chemin entre this et la livraison l1
     */
    public Chemin calculPCC(Livraison l1) {
        // TODO implement here
        // ne pas oublier la durée, le départ et l'arrivée !
        return null;
    }

    public int getHeureLivraison() {
        return heureLivraison;
    }

    public void setHeureLivraison(int heureLivraison) {
        this.heureLivraison = heureLivraison;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

    public Chemin getCheminVersSuivante() {
        return cheminVersSuivante;
    }

    public void setCheminVersSuivante(Chemin cheminVersSuivante) {
        this.cheminVersSuivante = cheminVersSuivante;
    }

    public Livraison getPrecedente() {
        return precedente;
    }

    public void setPrecedente(Livraison precedente) {
        this.precedente = precedente;
    }

    public Livraison getSuivante(){
        return this.cheminVersSuivante.getArrivee();
    }
}