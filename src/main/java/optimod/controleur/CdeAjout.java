package optimod.controleur;

import optimod.modele.*;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class CdeAjout implements Commande{
    private Ordonnanceur ordonnanceur;
    private Livraison livraison;
    private Intersection intersection;

    /**
     * Cree la commande qui ajoute une Intersection i avant la Livraison l à la DemandeLivraison
     * @param o
     * @param l
     * @param i
     */
    public CdeAjout(Ordonnanceur o, Livraison l, Intersection i){
        this.ordonnanceur = o;
        this.livraison = l;
        this.intersection = i;
    }

    public void doCde() {
        ordonnanceur.ajouterLivraison(intersection, livraison);
    }

    public void undoCde() {
        ordonnanceur.supprimerLivraison(intersection.getLivraison());
    }
}
