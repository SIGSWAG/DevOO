package optimod.vue.plan;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import optimod.modele.Intersection;
import optimod.modele.Troncon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Représente un tronçon à l'écran.
 * Created by aurelien on 23/11/15.
 */
public class TronconPane extends Group {

    public static final float TAILLE_FLECHE = 5;

    private IntersectionPane source;
    private Troncon troncon;

    public TronconPane(IntersectionPane source, Troncon troncon) {
        this.source = source;
        this.troncon = troncon;
        dessinerFleche();
    }

    private static List<Point2D> intersectionCercleLigne(int x1, int y1, int x2, int y2) {
        // http://stackoverflow.com/a/26705532
        double baX = x2 - x1;
        double baY = y2 - y1;
        double caX = x2 - x1;
        double caY = y2 - y1;

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - IntersectionPane.TAILLE * IntersectionPane.TAILLE;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Point2D p1 = new Point2D(x1 - baX * abScalingFactor1, y1 - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Point2D p2 = new Point2D(x1 - baX * abScalingFactor2, y1 - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    public Troncon getTroncon() {
        return troncon;
    }

    private void dessinerFleche() {
        final Intersection cible = troncon.getArrivee();
        final Point2D pointCible = intersectionCercleLigne(source.getX(), source.getY(), cible.getX(), cible.getY()).get(0);
        final Line ligne = new Line(source.getX(), source.getY(), pointCible.getX(), pointCible.getY());
        final Point2D tan = new Point2D(cible.getX() - source.getX(), cible.getY() - source.getY()).normalize();
        final Path arrowEnd = new Path();

        arrowEnd.getElements().add(new MoveTo(pointCible.getX() - TAILLE_FLECHE * tan.getX() - TAILLE_FLECHE * tan.getY(), pointCible.getY() - TAILLE_FLECHE * tan.getY() + TAILLE_FLECHE * tan.getX()));
        arrowEnd.getElements().add(new LineTo(pointCible.getX(), pointCible.getY()));
        arrowEnd.getElements().add(new LineTo(pointCible.getX() - TAILLE_FLECHE * tan.getX() + TAILLE_FLECHE * tan.getY(), pointCible.getY() - TAILLE_FLECHE * tan.getY() - TAILLE_FLECHE * tan.getX()));

        getChildren().add(ligne);
        getChildren().add(arrowEnd);
    }

}
