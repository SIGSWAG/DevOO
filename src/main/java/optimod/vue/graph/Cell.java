package optimod.vue.graph;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import optimod.modele.Intersection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Cell extends Pane {
    String cellId;

    List<Cell> children = new ArrayList<Cell>();
    List<Cell> parents = new ArrayList<Cell>();

    Node view;

    protected Intersection intersection;

    public Cell(String cellId, Intersection intersection) {
        this.cellId = cellId;
        this.intersection = intersection;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);
    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public int getX() {
        return intersection.getX();
    }

    public int getY() {
        return intersection.getY();
    }
}