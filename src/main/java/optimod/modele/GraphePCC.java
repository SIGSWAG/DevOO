package optimod.modele;

import java.util.*;

/**
 * 
 */
public class GraphePCC implements Graphe {


    /**
     *
     */
    private List<Chemin> chemins;

    /**
     * Default constructor
     */
    public GraphePCC() {
    }

    /**
     * @param chemins
     */
    public void GraphePCC(Collection<Chemin> chemins) {
        // TODO implement here
    }

    /**
     * 
     */
    public void calculerItineraire() {
        // TODO implement here
    }

    /**
     * 
     */
    private void convertirLivraisonsEnSommets() {
        // TODO implement here
    }

    /**
     * @param  n
     */
    private void retrouverItinéraire(Collection<Integer>  n) {
        // TODO implement here
    }

    public int getCout(int i, int j) {
        return 0;
    }

    public boolean estArc(int i, int j) {
        return false;
    }

    public int getNbSommets() {
        return 0;
    }

    public List<Chemin> getChemins() {
        return chemins;
    }

    public void setChemins(List<Chemin> chemins) {
        this.chemins = chemins;
    }
}