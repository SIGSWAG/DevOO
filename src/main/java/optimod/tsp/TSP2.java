package optimod.tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Thibault on 28/11/2015.
 */
public class TSP2 extends TSP1 {

    @Override
    protected int bound(Integer sommetCourant, Collection<Integer> nonVus) {

        int sumBound = getMinCoutSortant(sommetCourant, nonVus);

        for (Integer i : nonVus) {


            sumBound += getMinCoutSortant(i, nonVus);
        }

        return sumBound;
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, Collection<Integer> nonVus, Graphe g) {
        return new IteratorSeq(nonVus, sommetCrt, g);
    }


    private int getMinCoutSortant(int sommetCourant, Collection<Integer> nonVus) {
        int minArcCourant = 100000;
        if (g.estArc(sommetCourant, 0)) {
            minArcCourant = g.getCout(sommetCourant, 0);
        }
        for (Integer i : nonVus) {
            if (g.estArc(sommetCourant, i) && g.getCout(sommetCourant, i) < minArcCourant) {
                minArcCourant = g.getCout(sommetCourant, i);
            }
        }
        return minArcCourant;
    }

}

