package optimod.vue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.modele.*;
import optimod.vue.livraison.AfficheurFenetresLivraison;
import optimod.vue.plan.AfficheurPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    private Button annulerAction;

    @FXML
    private Button rejouerAction;

    @FXML
    private Button ajouterLivraison;

    @FXML
    private Button supprimerLivraison;

    @FXML
    private Button echangerLivraisons;

    @FXML
    private Button validerAjoutLivraison;

    @FXML
    private Button annulerAjoutLivraison;

    @FXML
    private AfficheurFenetresLivraison afficheurFenetresLivraison;

    private AfficheurPlan afficheurPlan;

    private boolean selectionsActivees;

    private boolean deselectionsActivees;

    public FenetreControleur(Stage fenetre, Controleur controleur) {
        this.fenetre = fenetre;
        this.controleur = controleur;
        selectionsActivees = false;
        deselectionsActivees = false;
    }

    public void initialize(URL location, ResourceBundle resources) {
        afficheurPlan = new AfficheurPlan(planGroup, this);
        afficheurFenetresLivraison.setFenetreControleur(this);
        associerVisibiliteBoutons();
        validerAjoutLivraison.setVisible(false);
        annulerAjoutLivraison.setVisible(false);
    }

    /**
     * Associe pour chaque bouton de l'IHM déclaré dans FenetreControleur une propriété "managée" permettant d'écouter les changements de visiblité
     * et ainsi mettre à jour la vue en conséquence
     */
    private void associerVisibiliteBoutons() {
        Class fenetreControleurClass = getClass();
        for (Field champ : fenetreControleurClass.getDeclaredFields()) {
            Object champObj = null;
            try {
                champObj = champ.get(this);
            } catch (IllegalAccessException e) {
                logger.error("Impossible d'associer les boutons à leurs controleurs", e);
                afficheException("Impossible d'associer les boutons à leurs controleurs", "Initialisation application - Erreur", Alert.AlertType.ERROR, e);
            }
            if (champObj instanceof Button) {
                Button bouton = (Button) champObj;
                bouton.managedProperty().bind(bouton.visibleProperty());
            }
        }
    }

    /**
     * Appelée lorsque l'utilisateur clique sur le bouton "Charger plan" dans l'interface
     */
    @FXML
    protected void chargerPlan(ActionEvent evenement) {
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
    protected void deselectionnerToutesIntersections(ActionEvent evenement) {
        deselectionnerTout();
        afficheurFenetresLivraison.deselectionnerTout();
    }

    @FXML
    protected void validerAjoutLivraison(ActionEvent evenement) {
        controleur.validerAjoutLivraison();
    }

    @FXML
    protected void annulerAjoutLivraison(ActionEvent evenement) {
        controleur.annulerAjoutLivraison();
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
        Evenement evenement = (Evenement) arg;
        if (evenement != null) {

            // Si la mise à jour vient du plan, on redessine le plan
            if (evenement.equals(Evenement.PLAN_CHARGE)) {
                Plan plan = (Plan) o;
                afficheurFenetresLivraison.reinitialiser();
                afficheurPlan.chargerPlan(plan);
            } else if (evenement.equals(Evenement.DEMANDE_LIVRAISONS_CHARGEE)) {
                DemandeLivraisons demandeLivraisons = (DemandeLivraisons) o;
                afficheurFenetresLivraison.chargerFenetresLivraison(demandeLivraisons);
                afficheurPlan.chargerDemandeLivraisons(demandeLivraisons);
            } else if (evenement.equals(Evenement.ITINERAIRE_CALCULE)) {
                afficheurFenetresLivraison.mettreAJour();
                afficheurPlan.chargerItineraire();
            } else {
                // TODO
                logger.warn("PROBLEM !");
            }

        }
    }


    public void activerChargerPlan(boolean estActif) {
        chargerPlan.setDisable(!estActif);
    }

    public void activerChargerLivraisons(boolean estActif) {
        chargerLivraisons.setDisable(!estActif);
    }

    public void activerToutDeselectionner(boolean estActif) {
        toutDeselectionner.setDisable(!estActif);
    }

    public void activerGenererFeuilleRoute(boolean estActif) {
        genererFeuilleRoute.setDisable(!estActif);
    }

    public void activerAnnuler(boolean estActif) {
        annulerAction.setDisable(!estActif);
    }

    public void activerRejouer(boolean estActif) {
        rejouerAction.setDisable(!estActif);
    }

    public void activerAjouter(boolean estActif) {
        ajouterLivraison.setDisable(!estActif);
    }

    public void activerSupprimer(boolean estActif) {
        supprimerLivraison.setDisable(!estActif);
    }

    public void activerEchanger(boolean estActif) {
        echangerLivraisons.setDisable(!estActif);
    }

    public void activerCalculerItineraire(boolean estActif) {
        calculerItineraire.setDisable(!estActif);
    }

    public void activerSelections(boolean estActif) {
        this.selectionsActivees = estActif;
    }

    public void activerDeselections(boolean estActif) {
        this.deselectionsActivees = estActif;
    }

    public void activerAnnulerAjout(boolean estActif) {
        annulerAjoutLivraison.setVisible(estActif);
    }

    public void activerValiderAjout(boolean estActif) {
        validerAjoutLivraison.setVisible(estActif);
    }

    public void autoriseBoutons(boolean estActif) {
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
        activerAnnulerAjout(estActif);
        activerValiderAjout(estActif);
    }

    public void afficheMessage(String message, String titre, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(480, 320);

        alert.showAndWait();
    }

    public void afficheException(String message, String titre, Alert.AlertType alertType, Exception ex) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(ex.getMessage());


        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Voici l'exception levée par le système :");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.setResizable(true);

        alert.showAndWait();
    }

    private void updateVue() {
        this.controleur.updateVue();
    }

    public boolean selectionner(Intersection intersection) {
        if (selectionsActivees) {
            if(this.controleur.selectionnerIntersection(intersection)){
                this.afficheurPlan.selectionner(intersection);
//                this.afficheurFenetresLivraison.selectionner(intersection.getLivraison());
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public void deselectionner(Intersection intersection) {
        if(deselectionsActivees) {
            if(this.controleur.deselectionnerIntersection(intersection)){
                afficheurPlan.deselectionner(intersection);
                afficheurFenetresLivraison.deselectionner(intersection.getLivraison());
            }
        }
    }

    public Color colorierLivraisons(FenetreLivraison fenetreLivraison) {
        return afficheurPlan.colorierLivraisons(fenetreLivraison);
    }

    public void selectionnerLivraisons(FenetreLivraison fenetreLivraison) {
        afficheurPlan.selectionnerLivraisons(fenetreLivraison);
    }


    public void deselectionnerTout() {
        if(deselectionsActivees) {
            controleur.deselectionnerToutesIntersections();
            afficheurPlan.deselectionnerToutesIntersections();
        }
    }

}
