package optimod.vue;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import optimod.controleur.Controleur;
import optimod.modele.Chemin;
import optimod.modele.DemandeLivraison;
import optimod.modele.Intersection;
import optimod.modele.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Contrôleur interne utilisé par la vue (JavaFX) afin d'appeler le Contrôleur général avec les bons paramètres
 * Permet que le Contrôleur soit indépendant de l'implémentation choisie pour la vue, et de passer les paramètres nécessaires
 * Created by Jonathan on 19/11/2015.
 */
public class FenetreControleur implements Observer {

    private static final double FACTEUR_FLECHE = 0.2;
    private static final int TAILLE_ARC = 1;

    private Stage fenetre;
    private Controleur controleur;

    @FXML
    private AnchorPane planAnchorPane;

    @FXML
    private AnchorPane planCanvasAnchorPane;

    @FXML
    private Group planCanvasGroup;

    public FenetreControleur(Stage fenetre, Controleur controleur) {
        this.fenetre = fenetre;
        this.controleur = controleur;
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
        planCanvasAnchorPane.getChildren().clear();
        controleur.chargerPlan();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger livraisons" dans l'interface
     */
    @FXML
    protected void chargerDemandeLivraisons(ActionEvent evenement) {
        controleur.chargerDemandeLivraisons();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Calculer l'itinéraire" dans l'interface
     */
    @FXML
    protected void calculerItineraire(ActionEvent evenement) {
        controleur.calculerItineraire();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton annuler dans l'interface
     */
    @FXML
    protected void annulerDerniereAction(ActionEvent evenement) {
        controleur.annulerDerniereAction();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton rejouer dans l'interface
     */
    @FXML
    protected void rejouerDerniereAction(ActionEvent evenement) {
        controleur.rejouerDerniereAction();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "+" pour ajouter une livraison dans l'interface
     */
    @FXML
    protected void ajouterLivraison(ActionEvent evenement) {
        controleur.ajouterLivraison();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton pour échanger des livraisons dans l'interface
     */
    @FXML
    protected void echangerLivraisons(ActionEvent evenement) {
        controleur.echangerLivraisons();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton pour supprimer une livraison dans l'interface
     */
    @FXML
    protected void supprimerLivraison(ActionEvent evenement) {
        controleur.supprimerLivraison();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Tout déselectionner" dans l'interface
     */
    @FXML
    protected void toutDeselectionner(ActionEvent evenement) {
        // Déselectionner sur le plan
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Générer feuille de route" dans l'interface
     */
    @FXML
    protected void genererFeuilleDeRoute(ActionEvent evenement) {
        controleur.genererFeuilleDeRoute();
    }

    public void update(Observable o, Object arg) {
        // Si la mise à jour vient du plan, on redessine le plan
        if (o instanceof Plan) {
            Plan plan = (Plan) o;
            List<IntersectionCercle> intersectionCercles = new ArrayList<IntersectionCercle>();

            // Pour chaque intersection, on crée une IntersectionCercle que l'on ajoute dans la vue
            for (Intersection intersection : plan.getIntersections()) {
                IntersectionCercle intersectionCercle = new IntersectionCercle(intersection.getAdresse(), intersection);
                intersectionCercles.add(intersectionCercle);
                planCanvasAnchorPane.getChildren().add(intersectionCercle);
            }

            // Pour chaque IntersectionCercle créée, on relie les intersections entre elles en fonction de leurs tronçons sortants
            for (IntersectionCercle intersectionCercle : intersectionCercles) {
                for (Intersection intersection : intersectionCercle.getIntersectionsSortantes()) {
                    for (IntersectionCercle intersectionCercleSortante : intersectionCercles) {
                        if (intersectionCercleSortante.getIntersection().equals(intersection)) {
                            relierIntersections(intersectionCercle, intersectionCercleSortante);
                            break;
                        }
                    }
                }
            }
        } else if (o instanceof DemandeLivraison) {
            DemandeLivraison demandeLivraison = (DemandeLivraison) o;

            for (Chemin chemin : demandeLivraison.getItineraire()) {
                // TODO
            }
        } else {
            // TODO
            System.err.println("PROBLEM !");
        }
    }

    /**
     * Relie deux intersections entre elle en traçant une courbe de Bézier paramétrique (cubique) entre ces deux intersections,
     * avec une flèche pour l'orientation au niveau de la cible
     *
     * @param source
     * @param cible
     */
    protected void relierIntersections(IntersectionCercle source, IntersectionCercle cible) {

        Line arc = new Line(source.getCentreX(), source.getCentreY(), cible.getCentreX(), cible.getCentreY());

        planCanvasAnchorPane.getChildren().add(arc);

        // TODO Voir si on avec le path.setNodeOrientation on pourrait mettre des flèches naturellement bien orientées

        /*path.getElements().add(moveTo);
        path.getElements().add(arc);
        path.setStrokeWidth(3);
        path.setStroke(Color.BLACK);
        path.setMouseTransparent(true);

        planCanvasAnchorPane.getChildren().add(path);*/

        //ajouterFlecheOrientation(cubicCurve);

        boolean animate = false;
        if (animate) {
            //animerArc(path);
        }
    }

    private void ajouterFlecheOrientation(CubicCurve cubicCurve) {
        double taille = Math.max(cubicCurve.getBoundsInLocal().getWidth(), cubicCurve.getBoundsInLocal().getHeight());
        double echelle = taille / 4d;

        Point2D orientation = calculerPointCourbure(cubicCurve, 1);
        Point2D tangente = caclulerPointTangenteCourbe(cubicCurve, 1).normalize().multiply(echelle);

        Path fleche = new Path();

        MoveTo moveTo = new MoveTo(orientation.getX() - FACTEUR_FLECHE * tangente.getX() - FACTEUR_FLECHE * tangente.getY(), orientation.getY() - FACTEUR_FLECHE * tangente.getY() + FACTEUR_FLECHE * tangente.getX());
        LineTo lineTo = new LineTo(orientation.getX(), orientation.getY());
        LineTo lineTo2 = new LineTo(orientation.getX() - FACTEUR_FLECHE * tangente.getX() + FACTEUR_FLECHE * tangente.getY(), orientation.getY() - FACTEUR_FLECHE * tangente.getY() - FACTEUR_FLECHE * tangente.getX());
        fleche.getElements().addAll(moveTo, lineTo, lineTo2);

        planCanvasAnchorPane.getChildren().add(fleche);
    }

    private void animerArc(Path path) {
        final Polygon arrow = new Polygon(); // Create arrow

        arrow.getPoints().addAll(50.0, 50.0, 70.0, 50.0, 70.0, 42.0, 82.0, 54.0, 70.0, 66.0, 70.0, 58.0, 50.0, 58.0);
        arrow.setFill(Color.GREEN);

        planCanvasAnchorPane.getChildren().add(arrow);

        PathTransition pathTransition = new PathTransition(); //

        pathTransition.setDuration(Duration.millis(750));
        pathTransition.setPath(path);
        pathTransition.setNode(arrow);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }

    /**
     * Calculer la courbe cubique à un paramètre t
     *
     * @param c La courbe cubique
     * @param t paramètre entre 0 et 1
     * @return Point2D
     */
    private Point2D calculerPointCourbure(CubicCurve c, float t) {
        Point2D p = new Point2D(Math.pow(1 - t, 3) * c.getStartX() + 3 * t * Math.pow(1 - t, 2) * c.getControlX1() +
                3 * (1 - t) * t * t * c.getControlX2() + Math.pow(t, 3) * c.getEndX(),
                Math.pow(1 - t, 3) * c.getStartY() + 3 * t * Math.pow(1 - t, 2) * c.getControlY1() +
                        3 * (1 - t) * t * t * c.getControlY2() + Math.pow(t, 3) * c.getEndY());
        return p;
    }

    /**
     * Calculer la tangente à la courbe cubique à un paramètre t
     *
     * @param c La courbe cubique
     * @param t paramètre entre 0 et 1
     * @return Point2D
     */
    private Point2D caclulerPointTangenteCourbe(CubicCurve c, float t) {
        Point2D p = new Point2D(-3 * Math.pow(1 - t, 2) * c.getStartX() + 3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlX1() +
                3 * ((1 - t) * 2 * t - t * t) * c.getControlX2() + 3 * Math.pow(t, 2) * c.getEndX(),
                -3 * Math.pow(1 - t, 2) * c.getStartY() + 3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlY1() +
                        3 * ((1 - t) * 2 * t - t * t) * c.getControlY2() + 3 * Math.pow(t, 2) * c.getEndY());
        return p;
    }
}
