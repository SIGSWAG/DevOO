package optimod.vue.graph;

import java.util.List;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Layout {

    Graph graph;

    public Layout(Graph graph) {
        this.graph = graph;
    }

    public void execute() {
        List<IntersectionCercle> cells = graph.getModel().getAllCells();

        for (IntersectionCercle cell : cells) {
            cell.relocate(cell.getX(), cell.getY());
        }
    }

}
