package optimod.controleur;

import javafx.stage.Stage;
import optimod.modele.Ordonnanceur;
import optimod.vue.Fenetre;

import java.io.File;

/**
 *
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    private Ordonnanceur ordonnanceur;

    public Controleur() {
        ordonnanceur = new Ordonnanceur();
    }

    public void chargerPlan(Stage fenetre) {
        // TODO Rôle de l'ordonnanceur
        ordonnanceur.chargerPlan(fenetre);

    }

    public void chargerDemandeLivraisons(Stage fenetre) {
        // TODO Rôle de l'ordonnanceur
        ordonnanceur.chargerDemandeLivraison(fenetre);
    }

    public void calculerItineraire() {

    }

    public void annulerDerniereAction() {

    }

    public void rejouerDerniereAction() {

    }

    public void ajouterLivraison() {

    }

    public void echangerLivraisons() {

    }

    public void supprimerLivraison() {

    }

    public void genererFeuilleDeRoute() {

    }
}
