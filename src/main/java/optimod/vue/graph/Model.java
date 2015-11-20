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

    List<Arc> allArcs;
    List<Arc> addedArcs;
    List<Arc> removedArcs;

    Map<Integer,IntersectionCercle> cellMap; // <id,cell>

    public Model() {

        graphParent = new IntersectionCercle(0, new Intersection(10, 10, 25, null));

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells = new ArrayList<IntersectionCercle>();
        addedCells = new ArrayList<IntersectionCercle>();
        removedCells = new ArrayList<IntersectionCercle>();

        allArcs = new ArrayList<Arc>();
        addedArcs = new ArrayList<Arc>();
        removedArcs = new ArrayList<Arc>();

        cellMap = new HashMap<Integer, IntersectionCercle>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedArcs.clear();
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

    public List<Arc> getAddedArcs() {
        return addedArcs;
    }

    public List<Arc> getRemovedArcs() {
        return removedArcs;
    }

    public List<Arc> getAllArcs() {
        return allArcs;
    }

    public void addCell(IntersectionCercle cell) {

        addedCells.add(cell);

        cellMap.put( cell.getCellId(), cell);

    }

    public void addEdge( String sourceId, String targetId) {

        IntersectionCercle sourceCell = cellMap.get(sourceId);
        IntersectionCercle targetCell = cellMap.get(targetId);

        Arc arc = new Arc(sourceCell, targetCell);

        addedArcs.add(arc);

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
        allArcs.addAll(addedArcs);
        allArcs.removeAll(removedArcs);

        addedArcs.clear();
        removedArcs.clear();

    }
}
