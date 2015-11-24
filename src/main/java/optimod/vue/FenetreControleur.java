package optimod.vue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.modele.*;
import optimod.modele.DemandeLivraison;
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
        // Si la mise à jour vient du plan, on redessine le plan
        if (o instanceof Plan) {

            Plan plan = (Plan) o;
            afficheurPlan.chargerPlan(plan);

        } else if (o instanceof DemandeLivraison) {
            DemandeLivraison demandeLivraison = (DemandeLivraison) o;

            TreeItem<String> root = new TreeItem<String>("Fenêtres de livraison", null);

            for (FenetreLivraison fenetreLivraison : demandeLivraison.getFenetres()) {
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
        } else {
            // TODO
            System.err.println("PROBLEM !");
        }
    }

    public void activerChargerPlan(boolean estActif){
        chargerPlan.setDisable(estActif);
    }

    public void activerChargerLivraisons(boolean estActif){
        chargerLivraisons.setDisable(estActif);
    }

    public void activerToutDeselectionner(boolean estActif){
        toutDeselectionner.setDisable(estActif);
    }

    public void activerGenererFeuilleRoute(boolean estActif){
        genererFeuilleRoute.setDisable(estActif);
    }

    public void activerAnnuler(boolean estActif){
        annuler.setDisable(estActif);
    }

    public void activerRejouer(boolean estActif){
        rejouer.setDisable(estActif);
    }

    public void activerAjouter(boolean estActif){
        ajouter.setDisable(estActif);
    }

    public void activerSupprimer(boolean estActif){
        supprimer.setDisable(estActif);
    }

    public void activerEchanger(boolean estActif){
        echanger.setDisable(estActif);
    }

    public void activerCalculerItineraire(boolean estActif){
        calculerItineraire.setDisable(estActif);
    }

    public void activerSelections(boolean estActif){
        /**
         * TODO @jonathan @aurélien
         */
        System.out.println("selections activees (ou pas)");
    }

    public void activerDeselections(boolean estActif){
        /**
         * TODO @jonathan @aurélien
         */
        System.out.println("deselections activees (ou pas)");
    }

    public void activerToutesLesDeselections(boolean estActif){
        /**
         * TODO @jonathan @aurélien
         */
        System.out.println("toutes les deselections activees (ou pas)");
    }

    public void activerAnnulerAjout(boolean estActif) {
        /**
         * TODO @jonathan @aurélien
         */
        System.out.println("on peut annuler l'ajout pour revenir à l'état principal");
    }

    public void activerValiderAjout(boolean estActif) {
        /**
         * TODO @jonathan @aurélien
         */
        System.out.println("on peut valider l'ajout pour revenir à l'état principal");
    }

    public void autoriseBoutons(boolean estActif){
        activerChargerPlan(estActif);
        activerChargerLivraisons(estActif);
        activerToutDeselectionner(estActif);
        activerGenererFeuilleRoute(estActif);
        activerAnnuler(estActif);
        activerRejouer(estActif);
        activerAjouter(estActif);
        activerSupprimer(estActif);
        activerEchanger(estActif);
        activerCalculerItineraire(estActif);
        activerSelections(estActif);
        activerDeselections(estActif);
        activerToutesLesDeselections(estActif);
        activerAnnulerAjout(estActif);
        activerValiderAjout(estActif);
    }

    public void afficheMessage(String message, String titre, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
