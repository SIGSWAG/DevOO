package optimod.vue.graph;

import optimod.modele.Intersection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Model {

    IntersectionCercle graphParent;

    List<IntersectionCercle> allCells;
    List<IntersectionCercle> addedCells;
    List<IntersectionCercle> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String,IntersectionCercle> cellMap; // <id,cell>

    public Model() {

        graphParent = new IntersectionCercle("_ROOT_", new Intersection(10, 10, 25, null));

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells = new ArrayList<IntersectionCercle>();
        addedCells = new ArrayList<IntersectionCercle>();
        removedCells = new ArrayList<IntersectionCercle>();

        allEdges = new ArrayList<Edge>();
        addedEdges = new ArrayList<Edge>();
        removedEdges = new ArrayList<Edge>();

        cellMap = new HashMap<String, IntersectionCercle>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<IntersectionCercle> getAddedCells() {
        return addedCells;
    }

    public List<IntersectionCercle> getRemovedCells() {
        return removedCells;
    }

    public List<IntersectionCercle> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addCell(IntersectionCercle cell) {

        addedCells.add(cell);

        cellMap.put( cell.getCellId(), cell);

    }

    public void addEdge( String sourceId, String targetId) {

        IntersectionCercle sourceCell = cellMap.get(sourceId);
        IntersectionCercle targetCell = cellMap.get(targetId);

        Edge edge = new Edge(sourceCell, targetCell);

        addedEdges.add(edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent
     * @param cellList
     */
    public void attachOrphansToGraphParent(List<IntersectionCercle> cellList) {

        for(IntersectionCercle cell: cellList) {
            if(cell.getCellParents().size() == 0) {
                graphParent.addCellChild(cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent(List<IntersectionCercle> cellList) {

        for(IntersectionCercle cell: cellList) {
            graphParent.removeCellChild(cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll(addedCells);
        allCells.removeAll(removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll(addedEdges);
        allEdges.removeAll(removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}
