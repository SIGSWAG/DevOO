package optimod.vue.plan;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import optimod.modele.Intersection;

/**
 * Représente une intersection à l'écran.
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionPane extends Circle {

    public static final int TAILLE = 5;
    public static final Color COULEUR = Color.BLACK;

    protected Intersection intersection;

    public IntersectionPane(Intersection intersection) {
        super(intersection.getX(), intersection.getY(), TAILLE, COULEUR);
        this.intersection = intersection;
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
