package optimod.modele;

import java.util.*;

/**
 * 
 */
public interface Graphe {

    /**
     * 
     */
    void getCout(int i, int j);
    boolean estArc(int i, int j);
    int getNbSommets();

}