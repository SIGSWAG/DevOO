package optimod.vue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.modele.Intersection;
import optimod.vue.graph.Graph;
import optimod.vue.graph.IntersectionCercle;
import optimod.vue.graph.Layout;
import optimod.vue.graph.Model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Contrôleur interne utilisé par la vue (JavaFX) afin d'appeler le Contrôleur général avec les bons paramètres
 * Permet que le Contrôleur soit indépendant de l'implémentation choisie pour la vue, et de passer les paramètres nécessaires
 * Created by Jonathan on 19/11/2015.
 */
public class FenetreControleur {
    private Stage fenetre;
    private Controleur controleur;

    Graph graph = new Graph();

    @FXML
    private AnchorPane planAnchorPane;

    public FenetreControleur(Stage fenetre, Controleur controleur) {
        this.fenetre = fenetre;
        this.controleur = controleur;
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
        controleur.chargerPlan();

        // FORTESTING : Dessiner le graphe
        dessinerPlan();
    }

    private void dessinerPlan() {
        BorderPane root = new BorderPane();

        graph = new Graph();

        root.setCenter(graph.getAnchorPane());

        planAnchorPane.getChildren().add(root);

        addGraphComponents();

        Layout layout = new Layout(graph);
        layout.execute();
    }

    private void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell(new IntersectionCercle("Cell A", new Intersection(15, 15, 0, null)));
        model.addCell(new IntersectionCercle("Cell B", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
        model.addCell(new IntersectionCercle("Cell C", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
        model.addCell(new IntersectionCercle("Cell D", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
        model.addCell(new IntersectionCercle("Cell E", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
        model.addCell(new IntersectionCercle("Cell F", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));
        model.addCell(new IntersectionCercle("Cell G", new Intersection(ThreadLocalRandom.current().nextInt(0, 100 + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1), 0, null)));

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();
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
}