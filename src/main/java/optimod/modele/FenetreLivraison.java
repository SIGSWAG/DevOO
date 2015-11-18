package optimod.modele;

import java.util.*;

/**
 * 
 */
public class FenetreLivraison {

    /**
     * Default constructor
     */
    public FenetreLivraison(Collection<Livraison> livraisons, int heureDebut, int heureFin) {
    }

    /**
     * 
     */
    private int heureDebut;

    /**
     * 
     */
    private int heureFin;

    /**
     * 
     */
    private List<Livraison> livraisons;

    /**
     * 
     */
    public Collection<Chemin> calculPCCInterne() {
        // TODO implement here
        return null;
    }

    /**
     * @param fdl
     */
    public Collection<Chemin> calculerPCCSuivant(FenetreLivraison fdl) {
        // TODO implement here
        return null;
    }

    public int getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(int heureDebut) {
        this.heureDebut = heureDebut;
    }

    public int getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(int heureFin) {
        this.heureFin = heureFin;
    }

    public List<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(List<Livraison> livraisons) {
        this.livraisons = livraisons;
    }
}