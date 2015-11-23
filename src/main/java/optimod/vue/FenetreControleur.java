package optimod.vue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.modele.Chemin;
import optimod.modele.DemandeLivraison;
import optimod.modele.Intersection;
import optimod.modele.Plan;

import java.util.*;

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
    private Group planGroup;

    @FXML
    private Button chargerPlan;
    @FXML
    private Button chargerLivraisons;
    @FXML
    private Button calculerItineraire;
    @FXML
    private Button toutDeselectionner;
    @FXML
    private Button genererFeuilleRoute;
    @FXML
    private Button annuler;
    @FXML
    private Button rejouer;
    @FXML
    private Button ajouter;
    @FXML
    private Button supprimer;
    @FXML
    private Button echanger;

    public FenetreControleur(Stage fenetre, Controleur controleur) {
        this.fenetre = fenetre;
        this.controleur = controleur;
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
        planGroup.getChildren().clear();
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
        controleur.undo();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton rejouer dans l'interface
     */
    @FXML
    protected void rejouerDerniereAction(ActionEvent evenement) {
        controleur.redo();
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
        System.out.println("update");
        // Si la mise à jour vient du plan, on redessine le plan
        if (o instanceof Plan) {
            Plan plan = (Plan) o;
            List<IntersectionCercle> intersectionCercles = new ArrayList<IntersectionCercle>();

            // Pour chaque intersection, on crée une IntersectionCercle que l'on ajoute dans la vue
            for (Intersection intersection : plan.getIntersections()) {
                IntersectionCercle intersectionCercle = new IntersectionCercle(intersection.getAdresse(), intersection);
                intersectionCercles.add(intersectionCercle);
                planGroup.getChildren().add(intersectionCercle);
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

        planGroup.getChildren().add(arc);

        // TODO Voir si on avec le path.setNodeOrientation on pourrait mettre des flèches naturellement bien orientées

        /*path.getElements().add(moveTo);
        path.getElements().add(arc);
        path.setStrokeWidth(3);
        path.setStroke(Color.BLACK);
        path.setMouseTransparent(true);

        planCanvasAnchorPane.getChildren().add(path);*/

        //ajouterFlecheOrientation(cubicCurve);

//        double phi = Math.toRadians( 40 );
//        double barb = 10;
//
//        double dx = cible.getX() - source.getX();
//        double dy = cible.getY() - source.getY();
//        double theta = Math.atan2( dy, dx );
//        double x, y, rho = theta + phi;
//
//        x = cible.getX() - barb * Math.cos( rho );
//        y = cible.getY() - barb * Math.sin( rho );
//
//        Point2D p1 = getCircleLineIntersectionPoint(source, cible).get(1);
//
//        Line head1 = new Line();
//        head1.setStartX( p1.getX() );
//        head1.setStartY( p1.getY() );
//        head1.setEndX( x );
//        head1.setEndY( y );
//        rho = theta - phi;
//        x = cible.getX() - barb * Math.cos( rho );
//        y = cible.getY() - barb * Math.sin( rho );
//        Line head2 = new Line();
//        head2.setStartX( p1.getX() );
//        head2.setStartY( p1.getY() );
//        head2.setEndX( x );
//        head2.setEndY( y );
//
//        planGroup.getChildren().addAll(head1, head2);

        Point2D p1 = getCircleLineIntersectionPoint(source, cible).get(0);
        Line arc2 = new Line(source.getCentreX(), source.getCentreY(), p1.getX(), p1.getY());

        double size=Math.max(arc2.getBoundsInLocal().getWidth(),
                arc2.getBoundsInLocal().getHeight());
        double scale=size/4d;

        Point2D ori=eval(arc2,1);
        Point2D tan=evalDt(arc2,1).normalize().multiply(scale);
        Path arrowEnd=new Path();
        arrowEnd.setFill(Color.AQUAMARINE);
        arrowEnd.getElements().add(new MoveTo(ori.getX()-0.2*tan.getX()-0.2*tan.getY(),
                ori.getY()-0.2*tan.getY()+0.2*tan.getX()));
        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrowEnd.getElements().add(new LineTo(ori.getX()-0.2*tan.getX()+0.2*tan.getY(),
                ori.getY()-0.2*tan.getY()-0.2*tan.getX()));

        planGroup.getChildren().add(arrowEnd);

    }

    private Point2D eval(Line c, float t){
        Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+

                Math.pow(t, 3)*c.getEndX(),
                Math.pow(1-t,3)*c.getStartY()+

                        Math.pow(t, 3)*c.getEndY());
        return p;
    }

    private Point2D evalDt(Line c, float t){
        Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+

                3*Math.pow(t, 2)*c.getEndX(),
                -3*Math.pow(1-t,2)*c.getStartY()+

                        3*Math.pow(t, 2)*c.getEndY());
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
        return Arrays.asList(p1, p2);
    }

    public void activerChargerPlan(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerChargerLivraisons(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerToutDeselectionner(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerGenererFeuilleRoute(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerAnnuler(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerRejouer(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerAjouter(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerSupprimer(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerEchanger(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void autoriseBoutons(boolean estActif){
        activerChargerPlan(estActif);
        activerChargerLivraisons(estActif);
        activerToutDeselectionner(estActif);
        activerAnnuler(estActif);
        activerGenererFeuilleRoute(estActif);
        activerAjouter(estActif);
        activerRejouer(estActif);
        activerSupprimer(estActif);
        activerEchanger(estActif);
    }

    public void afficheMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
