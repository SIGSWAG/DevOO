package optimod.vue.plan;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import optimod.modele.Troncon;

/**
 * Représente un tronçon à l'écran.
 * Created by aurelien on 23/11/15.
 */
public class TronconPane extends Group {

    // TODO dessiner la flèche

    private IntersectionPane source;
    private Troncon troncon;

    public TronconPane(IntersectionPane source, Troncon troncon) {

        this.source = source;
        this.troncon = troncon;

        super.getChildren().add(new Line(source.getX(), source.getY(), troncon.getArrivee().getX(), troncon.getArrivee().getY()));
        //dessinerFleche();

    }

    public Troncon getTroncon() {
        return troncon;
    }

    /*private void dessinerFleche() {
        IntersectionPane source = source;
        IntersectionPane cible = troncon.getArrivee();

        Point2D p1 = getCircleLineIntersectionPoint(source, cible).get(0);
        Line arc2 = new Line(source.getCentreX(), source.getCentreY(), p1.getX(), p1.getY());

        double size = Math.max(arc2.getBoundsInLocal().getWidth(),
                arc2.getBoundsInLocal().getHeight());
        double scale = size / 4d;

        Point2D ori = eval(arc2, 1);
        Point2D tan = evalDt(arc2, 1).normalize().multiply(scale);
        Path arrowEnd = new Path();
        arrowEnd.setFill(Color.AQUAMARINE);
        arrowEnd.getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
                ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrowEnd.getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
                ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));

        group.getChildren().add(arrowEnd);
    }

    private Point2D eval(Line c, float t) {
        Point2D p = new Point2D(Math.pow(1 - t, 3) * c.getStartX() +

                Math.pow(t, 3) * c.getEndX(),
                Math.pow(1 - t, 3) * c.getStartY() +

                        Math.pow(t, 3) * c.getEndY());
        return p;
    }

    private Point2D evalDt(Line c, float t) {
        Point2D p = new Point2D(-3 * Math.pow(1 - t, 2) * c.getStartX() +

                3 * Math.pow(t, 2) * c.getEndX(),
                -3 * Math.pow(1 - t, 2) * c.getStartY() +

                        3 * Math.pow(t, 2) * c.getEndY());
        return p;
    }


    public static List<Point2D> getCircleLineIntersectionPoint(IntersectionCercle pointA,
                                                               IntersectionCercle pointB) {
        double baX = pointB.getX() - pointA.getX();
        double baY = pointB.getY() - pointA.getY();
        double caX = pointB.getCentreX() - pointA.getX();
        double caY = pointB.getCentreY() - pointA.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - IntersectionCercle.TAILLE * IntersectionCercle.TAILLE;

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
        return Arrays.asList(p1, p2);*/
}
