package optimod.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Une des deux extrémités d’un tronçon.
 */
public class Intersection {

    private int adresse;

    private int x;

    private int y;

    private List<Troncon> sortants = new ArrayList<>();

    private Livraison livraison;

    /**
     * Constructeur d'Intersection
     *
     * @param x        coordonnee X de l'intersection en m
     * @param y        coordonnee Y de l'intersection en m
     * @param adresse  adresse de l'intersection
     * @param sortants liste des Troncons partant de l'intersection
     */
    public Intersection(int x, int y, int adresse, List<Troncon> sortants) {
        this.x = x;
        this.y = y;
        this.adresse = adresse;
        this.sortants = sortants;

    }

    /**
     * Constructeur d'Intersection
     *
     * @param x       coordonnee X de l'intersection en m
     * @param y       coordonnee Y de l'intersection en m
     * @param adresse adresse de l'intersection
     */
    public Intersection(int x, int y, int adresse) {
        this.x = x;
        this.y = y;
        this.adresse = adresse;
    }


    /**
     * @param x     la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y     la coordonnée y du cercle dans lequel trouver l'intersection
     * @param rayon le rayon du cercle dans lequel trouver l'intersection
     * @return vrai si l'intersection se trouve dans le cercle, faux sinon
     */
    public boolean estLocalisee(int x, int y, int rayon) {
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= rayon * rayon;
    }

    public int getAdresse() {
        return adresse;
    }

    public void setAdresse(int adresse) {
        this.adresse = adresse;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Troncon> getSortants() {
        return sortants;
    }

    public void setSortants(List<Troncon> sortants) {
        this.sortants = sortants;
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    /**
     * Permet de trouver un Troncon partant de l'intersecion courante
     * et arrivant à l'Intsersection spécifiée en paramètre.
     *
     * @param intersection l'Intersection d'arrivée.
     * @return le Troncon concerné ou null, s'il n'existe pas de tel Troncon.
     */
    public Troncon getTronconVers(Intersection intersection) {

        if (sortants == null || sortants.size() == 0) {
            return null;
        }
        for (Troncon troncon : sortants) {
            if (troncon.getArrivee() == intersection) {
                return troncon;
            }

        }
        return null;

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Intersection && this.adresse == ((Intersection) obj).adresse;
    }
}