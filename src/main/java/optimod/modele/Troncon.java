package optimod.modele;

import java.util.*;

public class Troncon {

    public Troncon(Intersection intersection, double vitesse, double longueur) {
        this.duree = longueur / vitesse;
        this.arrivee = intersection;
    }


    private double duree;

    private Intersection arrivee;

    private String nom;

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public void setArrivee(Intersection arrivee) {
        this.arrivee = arrivee;
    }
}