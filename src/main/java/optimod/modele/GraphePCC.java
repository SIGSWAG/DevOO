package optimod.modele;

import java.util.*;

/**
 * 
 */
public class GraphePCC implements Graphe {

    private List<Chemin> chemins = new ArrayList<Chemin>();

    /**
     * Default constructor
     */
    public GraphePCC() {
    }

    /**
     * @param chemins les chemins à définissants le graphe
     */
    public GraphePCC(List<Chemin> chemins) {
        this.chemins = chemins;
    }

    /**
     * TODO !! Vide de sens pour l'instant
     */
    public List<Chemin> calculerItineraire() {
        List<Chemin> plusCourtParcours = new ArrayList<Chemin>();
        convertirLivraisonsEnSommets();
        return plusCourtParcours;
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
    private void retrouverItinéraire(List<Integer>  n) {
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

    // à virer ???
    public void setChemins(List<Chemin> chemins) {
        this.chemins = chemins;
    }
}