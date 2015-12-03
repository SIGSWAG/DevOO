package optimod.modele;

/**
 * Partie de rue reliant deux intersections successives dans un sens donné.
 */
public class Troncon {

    private final double longueur;

    private final double vitesse;

    private final int duree;
    private final String nom;
    private int compteurPassage;
    private Intersection arrivee;

    /**
     * Constructeur de Troncon
     *
     * @param intersection l'Intersection d'arrivée du Troncon
     * @param vitesse      la vitesse moyenne en m/s sur le Troncon
     * @param longueur     la longueur en m du Troncon
     * @param nom          le nom de la rue correspondant au Troncon
     */
    public Troncon(Intersection intersection, double vitesse, double longueur, String nom) {
        this.duree = (int) (longueur / vitesse);
        this.arrivee = intersection;
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