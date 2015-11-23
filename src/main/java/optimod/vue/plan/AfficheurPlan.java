package optimod.vue.plan;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import optimod.modele.Intersection;
import optimod.modele.Plan;
import optimod.modele.Troncon;

import java.util.*;

/**
 * Created by aurelien on 23/11/15.
 */
public final class AfficheurPlan {

    private static final double FACTEUR_FLECHE = 0.2;
    private static final int TAILLE_ARC = 1;

    private Group group;

    public AfficheurPlan(Group group) {
        this.group = group;
    }

    public static List<Point2D> getCircleLineIntersectionPoint(IntersectionPane pointA,
                                                               IntersectionPane pointB) {
        double baX = pointB.getX() - pointA.getX();
        double baY = pointB.getY() - pointA.getY();
        double caX = pointB.getX() - pointA.getX();
        double caY = pointB.getY() - pointA.getY();

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

        Point2D p1 = new Point2D(pointA.getX() - baX * abScalingFactor1, pointA.getY()
                - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Point2D p2 = new Point2D(pointA.getX() - baX * abScalingFactor2, pointA.getY()
                - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    /**
     * Ajoute des intersections au plan, ainsi que leurs tronçons.
     *
     * @param plan Le plan à charger.
     */
    public void chargerPlan(Plan plan) {
        for (Intersection intersection : plan.getIntersections()) {
            group.getChildren().add(new IntersectionPane(intersection));
            for (Troncon troncon : intersection.getSortants()) {
                group.getChildren().add(new TronconPane(intersection, troncon));
            }
        }
    }

    /**
     * Vide le plan de tous ses éléments.
     */
    public void vider() {
        group.getChildren().clear();
    }

    private Collection<IntersectionPane> getIntersectionsCercle() {
        ArrayList<IntersectionPane> intersectionsCercle = new ArrayList<IntersectionPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersectionsCercle.add((IntersectionPane) noeud);
            }
        }
        return intersectionsCercle;
    }

    private Collection<TronconPane> getTronconsLigne() {
        ArrayList<TronconPane> tronconsLigne = new ArrayList<TronconPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                tronconsLigne.add((TronconPane) noeud);
            }
        }
        return tronconsLigne;
    }

    private Collection<Intersection> getIntersections() {
        ArrayList<Intersection> intersections = new ArrayList<Intersection>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersections.add(((IntersectionPane) noeud).getIntersection());
            }
        }
        return intersections;
    }

    private Collection<Troncon> getTroncons() {
        ArrayList<Troncon> troncons = new ArrayList<Troncon>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                troncons.add(((TronconPane) noeud).getTroncon());
            }
        }
        return troncons;
    }

}
