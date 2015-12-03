package optimod.controleur.commande;

import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class CommandeSuppression implements Commande {
    private Ordonnanceur ordonnanceur;
    private List<Livraison> livraisonsASupprimer;
    private HashMap<Integer, FenetreLivraison> fenetresLivraisons;

    /**
     * Cree la commande qui supprime une liste de Livraisons l de la DemandeLivraison
     *
     * @param o
     * @param l
     */
    public CommandeSuppression(Ordonnanceur o, List<Livraison> l) {
        this.ordonnanceur = o;
        this.livraisonsASupprimer = l;
        this.fenetresLivraisons = new HashMap<>();
        for (Livraison livraison : livraisonsASupprimer) {
            fenetresLivraisons.put(livraison.getIntersection().getAdresse(), ordonnanceur.trouverFenetreDeLivraison(livraison));
        }
    }

    /**
     * Execute la commande
     */
    public void doCde() {
        for (int i = 0; i < livraisonsASupprimer.size(); i++) {
            ordonnanceur.supprimerLivraison(livraisonsASupprimer.get(i));
        }
    }

    /**
     * Annule la commande
     */
    public void undoCde() {
        for (int i = livraisonsASupprimer.size() - 1; i >= 0; i--) {
            ordonnanceur.ajouterLivraison(livraisonsASupprimer.get(i), livraisonsASupprimer.get(i).getSuivante(), fenetresLivraisons.get(livraisonsASupprimer.get(i).getIntersection().getAdresse()));
        }
    }
}