package optimod.tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Thibault on 28/11/2015.
 */
public class TSP3 extends TSP2 {


    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, Collection<Integer> nonVus, Graphe g) {
        return new IteratorMinFirst(nonVus, sommetCrt, g);
    }


}

