package optimod.vue.graph;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import optimod.modele.Intersection;
import optimod.modele.Troncon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionCercle extends Pane {
    protected int cellId;

    protected List<Intersection> intersectionsSortantes = new ArrayList<Intersection>();
    protected List<IntersectionCercle> children = new ArrayList<IntersectionCercle>();
    protected List<IntersectionCercle> parents = new ArrayList<IntersectionCercle>();

    protected Node view;

    protected Intersection intersection;

    public IntersectionCercle(int cellId, Intersection intersection) {
        this.cellId = cellId;
        this.intersection = intersection;

        this.intersectionsSortantes = new ArrayList<Intersection>();

        for(Troncon troncon : intersection.getSortants()) {
            intersectionsSortantes.add(troncon.getArrivee());
        }

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

    public int getCellId() {
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

    public List<Intersection> getIntersectionsSortantes() {
        return intersectionsSortantes;
    }
}
