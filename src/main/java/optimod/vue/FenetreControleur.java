package optimod.vue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.graph.Graphe;
import optimod.vue.graph.IntersectionCercle;

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
        this.graphe = new Graphe();
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
        planCanvasAnchorPane.getChildren().clear();
        controleur.chargerPlan();

        // FORTESTING : Dessiner le graphe
        //dessinerPlan();
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
        Ordonnanceur ordonnanceur = (Ordonnanceur)o;

        for(Intersection intersection : ordonnanceur.getPlan().getIntersections()) {
            planCanvasAnchorPane.getChildren().add(new IntersectionCercle(intersection.getAdresse(), intersection));
        }
    }
}
