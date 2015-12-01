package optimod.modele;

import java.util.*;

/**
 * 
 */
public class Chemin {

    private int duree;

    private List<Troncon> troncons = new ArrayList<>();

    private Livraison depart;

    private Livraison arrivee;

    /**
     * Constructeur par défaut
     */
    public Chemin() {
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public List<Troncon> getTroncons() {
        return troncons;
    }

    public void setTroncons(List<Troncon> troncons) {
        this.troncons = troncons;
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