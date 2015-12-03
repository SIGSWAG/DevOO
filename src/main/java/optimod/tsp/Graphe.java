package optimod.tsp;

public interface Graphe {

    /**
     * @return le nombre de sommets de <code>this</code>
     */
    int getNbSommets();

    /**
     * @param i
     * @param j
     * @return le cout de l'arc (i,j) si (i,j) est un arc ; -1 sinon
     */
    int getCout(int i, int j);

    /**
     * @param i
     * @param j
     * @return true si <code>(i,j)</code> est un arc de <code>this</code>
     */
    boolean estArc(int i, int j);


}