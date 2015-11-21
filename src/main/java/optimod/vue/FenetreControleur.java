package optimod.vue;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import optimod.controleur.Controleur;
import optimod.modele.*;
import optimod.vue.graph.Graphe;
import optimod.vue.graph.IntersectionCercle;

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
    private Stage fenetre;
    private Controleur controleur;

    private Graphe graphe;

    @FXML
    private AnchorPane planAnchorPane;

    @FXML
    private AnchorPane planCanvasAnchorPane;

    @FXML
    private Group planCanvasGroup;

    public FenetreControleur(Stage fenetre, Controleur controleur) {
        this.fenetre = fenetre;
        this.controleur = controleur;
        //this.graphe = new Graphe();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
        planCanvasAnchorPane.getChildren().clear();
        controleur.chargerPlan();

//        // bending curve
//        Rectangle srcRect1 = new Rectangle(100,100,50,50);
//        Rectangle dstRect1 = new Rectangle(100,200,50,50);
//
//        CubicCurve curve1 = new CubicCurve( 125, 150, 125, 225, 325, 225, 125, 200);
//        curve1.setStroke(Color.BLACK);
//        curve1.setStrokeWidth(1);
//        curve1.setFill(null);
//
//        double size=Math.max(curve1.getBoundsInLocal().getWidth(),
//                curve1.getBoundsInLocal().getHeight());
//        double scale=size/4d;
//
//        Point2D ori=eval(curve1,0);
//        Point2D tan=evalDt(curve1,0).normalize().multiply(scale);
//        Path arrowIni=new Path();
//        arrowIni.getElements().add(new MoveTo(ori.getX()+0.2*tan.getX()-0.2*tan.getY(),
//                ori.getY()+0.2*tan.getY()+0.2*tan.getX()));
//        arrowIni.getElements().add(new LineTo(ori.getX(), ori.getY()));
//        arrowIni.getElements().add(new LineTo(ori.getX()+0.2*tan.getX()+0.2*tan.getY(),
//                ori.getY()+0.2*tan.getY()-0.2*tan.getX()));
//
//        ori=eval(curve1,1);
//        tan=evalDt(curve1,1).normalize().multiply(scale);
//        Path arrowEnd=new Path();
//        arrowEnd.getElements().add(new MoveTo(ori.getX()-0.2*tan.getX()-0.2*tan.getY(),
//                ori.getY()-0.2*tan.getY()+0.2*tan.getX()));
//        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
//        arrowEnd.getElements().add(new LineTo(ori.getX()-0.2*tan.getX()+0.2*tan.getY(),
//                ori.getY()-0.2*tan.getY()-0.2*tan.getX()));
//        planCanvasAnchorPane.getChildren().addAll(srcRect1, dstRect1, curve1, arrowIni);

    }

    private Point2D eval(QuadCurveTo debut, QuadCurveTo fin, float t){
        Point2D p=new Point2D(Math.pow(1-t,3)*debut.getX()+
                3*t*Math.pow(1-t,2)*debut.getControlX()+
                3*(1-t)*t*t*fin.getControlX()+
                Math.pow(t, 3)*fin.getX(),
                Math.pow(1-t,3)*debut.getY()+
                        3*t*Math.pow(1-t, 2)*debut.getControlY()+
                        3*(1-t)*t*t*fin.getControlY()+
                        Math.pow(t, 3)*fin.getControlY());
        return p;
    }

    private Point2D evalDt(QuadCurveTo debut, QuadCurveTo fin, float t){
        Point2D p=new Point2D(-3*Math.pow(1-t,2)*debut.getX()+
                3*(Math.pow(1-t, 2)-2*t*(1-t))*debut.getControlX()+
                3*((1-t)*2*t-t*t)*fin.getControlX()+
                3*Math.pow(t, 2)*fin.getX(),
                -3*Math.pow(1-t,2)*debut.getY()+
                        3*(Math.pow(1-t, 2)-2*t*(1-t))*debut.getControlY()+
                        3*((1-t)*2*t-t*t)*fin.getControlY()+
                        3*Math.pow(t, 2)*fin.getY());
        return p;
    }

    private void dessinerPlan() {
        BorderPane root = new BorderPane();

        graphe = new Graphe();

        root.setCenter(graphe.getAnchorPane());

        planAnchorPane.getChildren().add(root);

        addGraphComponents();

        afficherIntersections();
    }

    private void addGraphComponents() {
//        Model model = graphe.getModel();
//
//        model.addCell(new IntersectionCercle("Cell A", new Intersection(15, 15, 0, null)));
//        model.addCell(new IntersectionCercle("Cell B", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
//        model.addCell(new IntersectionCercle("Cell C", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
//        model.addCell(new IntersectionCercle("Cell D", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
//        model.addCell(new IntersectionCercle("Cell E", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
//        model.addCell(new IntersectionCercle("Cell F", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
//        model.addCell(new IntersectionCercle("Cell G", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
//
//        model.addEdge("Cell A", "Cell B");
//        model.addEdge("Cell A", "Cell C");
//        model.addEdge("Cell B", "Cell C");
//        model.addEdge("Cell C", "Cell D");
//        model.addEdge("Cell B", "Cell E");
//        model.addEdge("Cell D", "Cell F");
//        model.addEdge("Cell D", "Cell G");
//
//        graphe.mettreAJour();
    }

    private void afficherIntersections() {
        List<IntersectionCercle> intersections = graphe.getModel().getAllCells();

        for (IntersectionCercle intersection : intersections) {
            intersection.relocate(intersection.getX(), intersection.getY());
        }
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

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Plan) {
            Plan plan = (Plan) o;
            List<IntersectionCercle> intersectionCercles = new ArrayList<IntersectionCercle>();

            for (Intersection intersection : plan.getIntersections()) {
                IntersectionCercle intersectionCercle = new IntersectionCercle(intersection.getAdresse(), intersection);
                intersectionCercles.add(intersectionCercle);
                planCanvasAnchorPane.getChildren().add(intersectionCercle);
            }

            for(IntersectionCercle intersectionCercle : intersectionCercles) {
                for(Intersection intersection : intersectionCercle.getIntersectionsSortantes()) {
                    for(IntersectionCercle intersectionCercleSortante : intersectionCercles) {
                        if(intersectionCercleSortante.getIntersection().equals(intersection)) {
                            relierIntersections(intersectionCercle, intersectionCercleSortante);
                            break;
                        }
                    }
                }
            }
        }
        else if(o instanceof DemandeLivraison) {
            DemandeLivraison demandeLivraison = (DemandeLivraison) o;

            for(Chemin chemin : demandeLivraison.getItineraire()) {
                // TODO
            }
        }
        else {
            System.err.println("PROBLEM !");
        }
    }

    protected void relierIntersections(IntersectionCercle source, IntersectionCercle cible) {

        Path path = new Path();

        MoveTo moveTo = new MoveTo();

        moveTo.setX(source.getX()+ (source.getWidth()/2) );
        moveTo.setY(source.getY()+ (source.getHeight()/2) );

        QuadCurveTo quadCurveTo = new QuadCurveTo();

        quadCurveTo.setX(cible.getX()+(cible.getWidth()/2));
        quadCurveTo.setY(cible.getY()+(cible.getHeight()/2));

        // Déterminer le point de contrôle (point de courbure) de la courbe en fonction de la position des intersections
        double controlX = (source.getX() + cible.getX()) / 2;
        double controlY = 0;
        if(source.getX() < cible.getX()) {
            if(source.getY() < cible.getY()) {
                controlY = source.getY();
            }
            else {
                controlY = cible.getY();
            }
        }
        else {
            if(source.getY() < cible.getY()) {
                controlY = cible.getY();
            }
            else {
                controlY = source.getY();
            }
        }

        quadCurveTo.setControlX(controlX);
        quadCurveTo.setControlY(controlY);

        path.getElements().add(moveTo);
        path.getElements().add(quadCurveTo);
        path.setStrokeWidth(3);
        path.setStroke(Color.BLACK);
        path.setMouseTransparent(true);

        double size=Math.max(10,
                10);
        double scale=size/4d;

        Point2D ori=eval(quadCurveTo, quadCurveTo, 1);
        Point2D tan=evalDt(quadCurveTo, quadCurveTo, 1).normalize().multiply(scale);

        Path arrowEnd=new Path();
        arrowEnd.getElements().add(new MoveTo(source.getX()-0.2*tan.getX()-0.2*tan.getY(),
                source.getY()-0.2*tan.getY()+0.2*tan.getX()));
        arrowEnd.getElements().add(new LineTo(source.getX(), source.getY()));
        arrowEnd.getElements().add(new LineTo(source.getX()-0.2*tan.getX()+0.2*tan.getY(),
                source.getY()-0.2*tan.getY()-0.2*tan.getX()));

        planCanvasAnchorPane.getChildren().add(path);
        planCanvasAnchorPane.getChildren().add(arrowEnd);

        boolean animate = false;
        if(animate) {
            final Polygon arrow = new Polygon(); // Create arrow

            arrow.getPoints().addAll(new Double[]{50.0, 50.0, 70.0, 50.0, 70.0, 42.0, 82.0, 54.0, 70.0, 66.0, 70.0, 58.0, 50.0, 58.0});
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
    }
}
