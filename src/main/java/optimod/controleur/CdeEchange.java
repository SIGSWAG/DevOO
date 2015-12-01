package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class CdeEchange implements Commande{
    private Ordonnanceur ordonnanceur;
    private Livraison livraison1;
    private Livraison livraison2;

    /**
     * Cree la commande qui ajoute une Intersection i avant la Livraison l à la DemandeLivraison
     * @param o
     * @param l1
     * @param l2
     */
    public CdeEchange(Ordonnanceur o, Livraison l1, Livraison l2){
        this.ordonnanceur = o;
        this.livraison1 = l1;
        this.livraison2 = l2;
    }

    /**
     * Execute la commande
     */
    public void doCde() {
        ordonnanceur.echangerLivraison(livraison1, livraison2);
    }

    /**
     * Annule la commande
     */
    public void undoCde() {
        ordonnanceur.echangerLivraison(livraison2, livraison1);
    }
}
