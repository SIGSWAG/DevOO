package optimod.vue.plan;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import optimod.modele.Intersection;

/**
 * Représente une intersection à l'écran.
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionPane extends Circle {

    public static final int TAILLE = 6;

    public static final Color COULEUR_ENTREPOT = Color.GREEN;
    public static final Color COULEUR_DEFAUT = Color.BLACK;

    private Intersection intersection;
    private boolean estEntrepot;

    public IntersectionPane(Intersection intersection) {
        super(intersection.getX(), intersection.getY(), TAILLE);
        this.intersection = intersection;
        setEstEntrepot(false);
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

    public void setEstEntrepot(boolean estEntrepot) {
        this.estEntrepot = estEntrepot;
        if (estEntrepot)
            setFill(COULEUR_ENTREPOT);
        else
            setFill(COULEUR_DEFAUT);
    }

}
