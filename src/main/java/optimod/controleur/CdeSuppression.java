package optimod.controleur;

import optimod.modele.FenetreLivraison;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class CdeSuppression implements Commande{
    private Ordonnanceur ordonnanceur;
    private List<Livraison> livraisons;
    private HashMap<Integer,FenetreLivraison> fenetreLivraisons;

    /**
     * Cree la commande qui supprime une liste de Livraison l de la DemandeLivraison
     * @param o
     * @param l
     */
    public CdeSuppression(Ordonnanceur o, List<Livraison> l){
        this.ordonnanceur = o;
        this.livraisons = l;
        this.fenetreLivraisons = new HashMap<>();
    }

    public void doCde() {
        for (int i = 0; i < livraisons.size(); i++) {

            FenetreLivraison fen = ordonnanceur.trouverFenetreDeLivraison(livraisons.get(i));
            fenetreLivraisons.put(livraisons.get(i).getIntersection().getAdresse(), fen);
            Intersection inters = ordonnanceur.trouverIntersection(livraisons.get(i).getIntersection().getAdresse());
            ordonnanceur.supprimerLivraison(inters.getLivraison());
        }
    }

    public void undoCde() {
        for (int i = livraisons.size()-1; i >= 0; i--) {
            Intersection inters = ordonnanceur.trouverIntersection(livraisons.get(i).getSuivante().getIntersection().getAdresse());

            ordonnanceur.ajouterLivraison(livraisons.get(i).getIntersection(), inters.getLivraison(), fenetreLivraisons.get(livraisons.get(i).getIntersection().getAdresse()));
        }
    }
}
