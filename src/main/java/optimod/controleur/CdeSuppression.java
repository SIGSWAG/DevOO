package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

import java.util.List;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class CdeSuppression implements Commande{
    private Ordonnanceur ordonnanceur;
    private List<Livraison> livraisons;

    /**
     * Cree la commande qui supprime une liste de Livraison l de la DemandeLivraison
     * @param o
     * @param l
     */
    public CdeSuppression(Ordonnanceur o, List<Livraison> l){
        this.ordonnanceur = o;
        this.livraisons = l;
    }

    public void doCde() {
        for (int i = 0; i < livraisons.size(); i++) {
            ordonnanceur.supprimerLivraison(livraisons.get(i));
        }
    }

    public void undoCde() {
        for (int i = livraisons.size()-1; i >= 0; i--) {
            ordonnanceur.ajouterLivraison(livraisons.get(i).getIntersection(), livraisons.get(i).getSuivante());
        }
    }
}
