package optimod.modele;

public class Troncon {

    private final double longueur;

    private final double vitesse;

    private final int duree;

    private int compteurPassage;

    private Intersection arrivee;

    private final String nom;

    public Troncon(Intersection intersection, double vitesse, double longueur, String nom) {
        this.duree = (int) (longueur / vitesse);
        this.arrivee = arrivee;
        this.nom = nom;
        this.longueur = longueur;
        this.vitesse = vitesse;
        this.compteurPassage = 0;
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
        return compteurPassage > 0;
    }

    public int getCompteurPassage() {
        return compteurPassage;
    }

    public void incrementeCompteurPassage() {
        compteurPassage++;
    }

    public void decrementeCompteurPassage() {
        compteurPassage = compteurPassage > 0 ? compteurPassage - 1 : 0;
    }

    public void resetCompteur() {
        compteurPassage = 0;
    }
}