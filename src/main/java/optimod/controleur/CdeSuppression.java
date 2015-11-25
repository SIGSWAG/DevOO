package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class CdeSuppression implements Commande{
    private Ordonnanceur ordonnanceur;
    private Livraison livraison;
    private Intersection intersection;

    /**
     * Cree la commande qui ajoute supprime une Livraison l de la DemandeLivraison
     * @param o
     * @param l
     */
    public CdeSuppression(Ordonnanceur o, Livraison l){
        this.ordonnanceur = o;
        this.livraison = l;
        this.intersection = l.getIntersection();
    }

    public void doCde() {
        ordonnanceur.supprimerLivraison(livraison);
    }

    public void undoCde() {
        ordonnanceur.ajouterLivraison(intersection, livraison.getSuivante());
    }
}
