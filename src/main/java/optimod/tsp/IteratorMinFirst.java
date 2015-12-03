package optimod.tsp;

import java.util.*;

public class IteratorMinFirst implements Iterator<Integer> {

    private List<ArcCout> candidats;
    private int nbCandidats;

    /**
     * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus qui sont successeurs de sommetCrt dans le graphe g,
     * dans l'odre d'apparition dans <code>nonVus</code>
     *
     * @param nonVus
     * @param sommetCrt
     * @param g
     */
    public IteratorMinFirst(Collection<Integer> nonVus, int sommetCrt, Graphe g) {
        this.candidats = new ArrayList<>();
        Iterator<Integer> it = nonVus.iterator();
        while (it.hasNext()) {
            Integer s = it.next();
            if (g.estArc(sommetCrt, s)) {
                int cout = g.getCout(sommetCrt, s);
                candidats.add(new ArcCout(s, cout));
            }
        }
        Collections.sort(candidats);
    }


    public boolean hasNext() {
        return nbCandidats < candidats.size();
    }


    public Integer next() {
        return candidats.get(nbCandidats++).noeud;
    }


    public void remove() {
    }


    private class ArcCout implements Comparable<ArcCout> {

        int noeud;
        int cout;

        public ArcCout(int noeud, int cout) {
            this.noeud = noeud;
            this.cout = cout;
        }

        @Override
        public int compareTo(ArcCout arc) {
            if (arc.cout >= cout) {
                return -1;
            }
            return 1;
        }
    }
}
