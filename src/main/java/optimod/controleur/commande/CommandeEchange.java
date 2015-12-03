package optimod.controleur.commande;

import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

/**
 *
 */
public class CommandeEchange implements Commande {
    private Ordonnanceur ordonnanceur;
    private Livraison livraison1;
    private Livraison livraison2;

    /**
     * Cree la commande qui ajoute une Intersection i avant la Livraison l à la DemandeLivraison
     *
     * @param o
     * @param l1
     * @param l2
     */
    public CommandeEchange(Ordonnanceur o, Livraison l1, Livraison l2) {
        this.ordonnanceur = o;
        this.livraison1 = l1;
        this.livraison2 = l2;
    }

    /**
     * Execute la commande
     */
    public void executerCommande() {
        ordonnanceur.echangerLivraison(livraison1, livraison2);
    }

    /**
     * Annule la commande
     */
    public void annulerCommande() {
        ordonnanceur.echangerLivraison(livraison2, livraison1);
    }
}
