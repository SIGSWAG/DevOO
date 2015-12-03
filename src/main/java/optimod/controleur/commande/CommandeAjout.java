package optimod.controleur.commande;

import optimod.modele.FenetreLivraison;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

/**
 *
 */
public class CommandeAjout implements Commande {
    private Ordonnanceur ordonnanceur;
    private Livraison livraison;
    private Livraison nouvelleLivraison;
    private Intersection intersection;
    private FenetreLivraison fenetreDeNouvelleLivraison;

    /**
     * Cree la commande qui ajoute une Livraison sur l'Intersection i avant la Livraison l à la DemandeLivraison
     *
     * @param o
     * @param l
     * @param i
     */
    public CommandeAjout(Ordonnanceur o, Livraison l, Intersection i) {
        this.ordonnanceur = o;
        this.livraison = l;
        this.intersection = i;
        this.fenetreDeNouvelleLivraison = o.trouverFenetreDeLivraison(l);
        this.nouvelleLivraison = new Livraison(intersection, this.fenetreDeNouvelleLivraison.getHeureDebut(), this.fenetreDeNouvelleLivraison.getHeureFin(), -1);
    }

    /**
     * Execute la commande
     */
    public void executerCommande() {
        ordonnanceur.ajouterLivraison(nouvelleLivraison, livraison, fenetreDeNouvelleLivraison);
    }

    /**
     * Annule la commande
     */
    public void annulerCommande() {
        ordonnanceur.supprimerLivraison(intersection.getLivraison());
    }
}
