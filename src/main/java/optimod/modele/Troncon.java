package optimod.modele;

public class Troncon {

    private double longueur;

    private double vitesse;

    private int duree;

    private Intersection arrivee;

    private String nom;

    private boolean estEmprunte;

    public Troncon(Intersection intersection, double vitesse, double longueur, String nom) {
        this.duree = (int) (longueur / vitesse);
        this.arrivee = intersection;
        this.nom = nom;
        this.longueur = longueur;
        this.vitesse = vitesse;
        this.estEmprunte = false;
    }

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

    public boolean estEmprunte() {
        return estEmprunte;
    }

    public void setEstEmprunte(boolean estEmprunte) {
        this.estEmprunte = estEmprunte;
    }
}