package optimod.vue.graph;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import optimod.modele.Intersection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionCercle extends Pane {
    String cellId;

    List<IntersectionCercle> children = new ArrayList<IntersectionCercle>();
    List<IntersectionCercle> parents = new ArrayList<IntersectionCercle>();

    Node view;

    protected Intersection intersection;

    public IntersectionCercle(String cellId, Intersection intersection) {
        this.cellId = cellId;
        this.intersection = intersection;

        Circle noeudIntersection = new Circle(intersection.getX(), intersection.getY(), 10, Color.BLACK);
        setView(noeudIntersection);
    }

    public void addCellChild(IntersectionCercle cell) {
        children.add(cell);
    }

    public List<IntersectionCercle> getCellChildren() {
        return children;
    }

    public void addCellParent(IntersectionCercle cell) {
        parents.add(cell);
    }

    public List<IntersectionCercle> getCellParents() {
        return parents;
    }

    public void removeCellChild(IntersectionCercle cell) {
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
