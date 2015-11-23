package optimod.vue;

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

    public static final int TAILLE = 10;

    protected int cellId;

    protected List<Intersection> intersectionsSortantes = new ArrayList<Intersection>();

    protected Node vue;

    protected Intersection intersection;

    public IntersectionCercle(int cellId, Intersection intersection) {
        this.cellId = cellId;
        this.intersection = intersection;

        this.intersectionsSortantes = new ArrayList<Intersection>();

        for(Troncon troncon : intersection.getSortants()) {
            intersectionsSortantes.add(troncon.getArrivee());
        }

        Circle noeudIntersection = new Circle(intersection.getX(), intersection.getY(), TAILLE, Color.BLACK);
        setVue(noeudIntersection);
    }

    public int getCentreX() {
        return intersection.getX() + (TAILLE / 2);
    }

    public int getCentreY() {
        return intersection.getY() + (TAILLE / 2);
    }

    public Node getVue() {
        return this.vue;
    }

    public void setVue(Node vue) {
        this.vue = vue;
        getChildren().add(vue);
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
