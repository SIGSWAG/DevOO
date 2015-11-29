package optimod.vue.plan;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurve;
import optimod.modele.Intersection;
import optimod.modele.Troncon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Représente un tronçon à l'écran.
 * Created by aurelien on 23/11/15.
 */
public class TronconPane extends Group {

    public static final float TAILLE_FLECHE = 5;

    private static final Color COULEUR_DEFAUT = Color.LIGHTGRAY;
    private final List<Color> couleurs;

    private final IntersectionPane source;
    private final Troncon troncon;

    public TronconPane(final IntersectionPane source, final Troncon troncon) {
        this.source = source;
        this.troncon = troncon;
        couleurs = new ArrayList<>();

        mettreAJour();
    }

    public void mettreAJour() {
        getChildren().clear();
        dessinerFleches();
    }

    public Troncon getTroncon() {
        return troncon;
    }

    private void dessinerFleches() {
        final Intersection cible = troncon.getArrivee();

        // Calcul d'où "part" et "arrive" la flèche pour éviter qu'elle ne passe sur les intersections
        final Point2D pointSource = intersectionCercleLigne(cible.getX(), cible.getY(), source.getX(), source.getY()).get(0);
        final Point2D pointCible = intersectionCercleLigne(source.getX(), source.getY(), cible.getX(), cible.getY()).get(0);

        //final Line ligne = new Line(pointSource.getX(), pointSource.getY(), pointCible.getX(), pointCible.getY());

        final Point2D tan = new Point2D(pointCible.getX() - pointSource.getX(), pointCible.getY() - pointSource.getY()).normalize();

        if (troncon.estEmprunte()) {
            for (int i = 0; i < couleurs.size(); i++) { // Une flèche pour chaque passage
                final Color couleur = couleurs.get(i);
                ajouterFleche(pointSource, pointCible, tan, couleur, i);
            }
        } else
            ajouterFleche(pointSource, pointCible, tan, COULEUR_DEFAUT, 0); // On trace la flèche par défaut
        toBack(); // Si on dessine la flèche par défaut, il faut la mettre en dessous pour éviter les pollutions visuelles

    }

    private void ajouterFleche(final Point2D pointSource, final Point2D pointCible, final Point2D tan, final Color couleur, final int ratioCourbe) {

        final Path fleche = new Path();

        final QuadCurve quad = new QuadCurve();

        quad.setStartX(pointSource.getX());
        quad.setStartY(pointSource.getY());
        quad.setEndX(pointCible.getX());
        quad.setEndY(pointCible.getY());
        quad.setStrokeWidth(1);
        quad.setFill(null);

        double courbe = 10 * (ratioCourbe - 1);
        if (courbe < 0)
            courbe = 0;
        final Point2D pass = calculePointPassage(pointCible, pointSource, courbe);

        quad.setControlX(pass.getX());
        quad.setControlY(pass.getY());

        fleche.getElements().add(new MoveTo(pointCible.getX() - TAILLE_FLECHE * tan.getX() - TAILLE_FLECHE * tan.getY(), pointCible.getY() - TAILLE_FLECHE * tan.getY() + TAILLE_FLECHE * tan.getX()));
        fleche.getElements().add(new LineTo(pointCible.getX(), pointCible.getY()));
        fleche.getElements().add(new LineTo(pointCible.getX() - TAILLE_FLECHE * tan.getX() + TAILLE_FLECHE * tan.getY(), pointCible.getY() - TAILLE_FLECHE * tan.getY() - TAILLE_FLECHE * tan.getX()));

        quad.setStroke(couleur);
        fleche.setStroke(couleur);
        fleche.setFill(couleur);

        getChildren().add(quad);
        getChildren().add(fleche);

    }

    public void ajouterPassage(final Color couleur) {
        couleurs.add(couleur);
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

    /**
     * Résolution de système :
     * Calcule le point correspondant situé à une distance distance de la droite
     * passant par p1 et p2, dont la projection sur (p1p2) est le milieu du segment [p1p2]
     *
     * @param p1
     * @param p2
     * @param distance
     * @return
     */
    public Point2D calculePointPassage(Point2D p1, Point2D p2, double distance) {

        double a = p2.getY() - p1.getY();
        double b = p1.getX() - p2.getX();
        double c = -(a * p1.getX() + b * p1.getY());

        double j = b * (p1.getX() + p2.getX()) / 2 - a * (p1.getY() + p2.getY()) / 2;

        double d = distance * Math.sqrt(a * a + b * b) - c;

        double X = 0;
        double Y = 0;
        double det = a * a + b * b;

        X = 1 / det * (j * b + d * a);
        Y = 1 / det * (b * d - j * a);

        return new Point2D(X, Y);

    }
}
