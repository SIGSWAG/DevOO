package optimod.vue.plan;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import optimod.modele.Intersection;
import optimod.modele.Troncon;

/**
 * Représente un tronçon à l'écran.
 * Created by aurelien on 23/11/15.
 */
public class TronconPane extends Group {

    // TODO dessiner la flèche

    private Intersection depart;
    private Troncon troncon;

    public TronconPane(Intersection depart, Troncon troncon) {
        this.depart = depart;
        this.troncon = troncon;
        super.getChildren().add(new Line(depart.getX(), depart.getY(), troncon.getArrivee().getX(), troncon.getArrivee().getY()));
    }

    public Troncon getTroncon() {
        return troncon;
    }

/*
    private void relierIntersections(TronconPane) {

        TronconPane troncon = new TronconPane(source.getX(), source.getY(), cible.getX(), cible.getY());
        group.getChildren().add(troncon);

        // TODO Voir si on avec le path.setNodeOrientation on pourrait mettre des flèches naturellement bien orientées

//        Point2D p1 = getCircleLineIntersectionPoint(source, cible).get(0);
//        Line arc2 = new Line(source.getCentreX(), source.getCentreY(), p1.getX(), p1.getY());
//
//        double size = Math.max(arc2.getBoundsInLocal().getWidth(),
//                arc2.getBoundsInLocal().getHeight());
//        double scale = size / 4d;
//
//        Point2D ori = eval(arc2, 1);
//        Point2D tan = evalDt(arc2, 1).normalize().multiply(scale);
//        Path arrowEnd = new Path();
//        arrowEnd.setFill(Color.AQUAMARINE);
//        arrowEnd.getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
//                ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
//        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
//        arrowEnd.getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
//                ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));
//
//        group.getChildren().add(arrowEnd);

    }*/

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

}
