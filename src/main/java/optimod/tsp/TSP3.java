package optimod.tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 * Classe surchargeant iterator(), en renvoyant un
 * itérateur qui se comporte comme une file de priorités min.
 */
public class TSP3 extends TSP2 {


    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, Collection<Integer> nonVus, Graphe g) {
        return new IteratorMinFirst(nonVus, sommetCrt, g);
    }


}

