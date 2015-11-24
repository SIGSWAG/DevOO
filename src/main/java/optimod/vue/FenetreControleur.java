package optimod.vue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.modele.Plan;
import optimod.vue.plan.AfficheurPlan;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Contrôleur interne utilisé par la vue (JavaFX) afin d'appeler le Contrôleur général avec les bons paramètres
 * Permet que le Contrôleur soit indépendant de l'implémentation choisie pour la vue, et de passer les paramètres nécessaires
 * Created by Jonathan on 19/11/2015.
 */
public class FenetreControleur implements Observer, Initializable {

    private Stage fenetre;
    private Controleur controleur;

    @FXML
    private Group planGroup;

    @FXML
    private TreeView<String> fenetresLivraisonTreeView;

    private AfficheurPlan afficheurPlan;

    public FenetreControleur(Stage fenetre, Controleur controleur) {
        this.fenetre = fenetre;
        this.controleur = controleur;
    }

    public void initialize(URL location, ResourceBundle resources) {
        afficheurPlan = new AfficheurPlan(planGroup);
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
        afficheurPlan.vider();
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
            afficheurPlan.chargerPlan(plan);

        } else if (o instanceof DemandeLivraisons) {
            DemandeLivraisons demandeLivraisons = (DemandeLivraisons) o;

            // Mise à jour de la liste de livraisons
            TreeItem<String> root = new TreeItem<String>("Fenêtres de livraison", null);

            for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
                TreeItem<String> treeItem = new TreeItem<String>(fenetreLivraison.getHeureDebut() + " - " + fenetreLivraison.getHeureFin());
                for(int i = 0; i < fenetreLivraison.getLivraisons().size(); i++) {
                    Livraison livraison = fenetreLivraison.getLivraisons().get(i);
                    TreeItem<String> treeItem1 = new TreeItem<String>(i + 1 + ". " + livraison.getIntersection().getAdresse());
                    treeItem.getChildren().add(treeItem1);
                }
                root.getChildren().add(treeItem);
            }

            fenetresLivraisonTreeView.setRoot(root);
            fenetresLivraisonTreeView.setShowRoot(true);

            // Mise à jour du plan
            afficheurPlan.chargerDemandeLivraisons(demandeLivraisons);

        } else {
            // TODO
            System.err.println("PROBLEM !");
        }
    }

}
