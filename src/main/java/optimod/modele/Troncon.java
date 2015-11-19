package optimod.modele;

public class Troncon {

    public Troncon(Intersection intersection, double vitesse, double longueur, String nom) {
        this.duree = (int) (longueur / vitesse);
        this.arrivee = intersection;
        this.nom = nom;
        this.longueur = longueur;
        this.vitesse = vitesse;
    }


    private double longueur;

    private double vitesse;

    private int duree;

    private Intersection arrivee;

    private String nom;

    public int getDuree() {
        return duree;
    }

    public Intersection getArrivee() {
        return arrivee;
    }

    public void setArrivee(Intersection arrivee) {
        this.arrivee = arrivee;
    }

    public String getNom() {
        return nom;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getVitesse() {
        return vitesse;
    }
}