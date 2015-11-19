package optimod.modele;

public class Troncon {

    public Troncon(Intersection intersection, double vitesse, double longueur) {
        this.duree = (int) (longueur / vitesse);
        this.arrivee = intersection;
    }


    private int duree;

    private Intersection arrivee;

    private String nom;

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