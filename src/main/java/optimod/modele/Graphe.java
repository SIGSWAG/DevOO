package optimod.modele;

import java.util.*;

/**
 * 
 */
public interface Graphe {

    /**
     * 
     */
    int getCout(int i, int j);
    boolean estArc(int i, int j);
    int getNbSommets();

}