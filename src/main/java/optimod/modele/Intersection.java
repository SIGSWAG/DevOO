package optimod.modele;

import java.util.*;

public class Intersection {

    private int adresse;

    private int x;

    private int y;

    private List<Troncon> sortants = new ArrayList<Troncon>();

    private Livraison livraison;

    /**
     * 
     * @param x
     * @param y
     * @param adresse
     * @param sortants
     */
    public Intersection(int x, int y, int adresse, List<Troncon> sortants) {
        this.x = x;
        this.y = y;
        this.adresse = adresse;
        this.sortants = sortants;

    }

    public Intersection(int x, int y, int adresse) {
        this.x = x;
        this.y = y;
        this.adresse = adresse;
    }


    /**
     * @param x la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y la coordonnée y du cercle dans lequel trouver l'intersection
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

    public Troncon getTronconVers(Intersection intersection){


        if(sortants == null || sortants.size() == 0){
            return null;
        }
        for(Troncon troncon : sortants){
            if(troncon.getArrivee() == intersection){
                return troncon;
            }

        }
        return null;

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Intersection)) {
            return false;
        }
        return this.adresse == ((Intersection) obj).adresse;
    }
}