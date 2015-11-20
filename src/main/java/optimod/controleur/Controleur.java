package optimod.controleur;

import javafx.stage.Stage;
import optimod.modele.Ordonnanceur;

/**
 *
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    private Ordonnanceur ordonnanceur;

    public Controleur() {
        ordonnanceur = new Ordonnanceur();
    }

    public void chargerPlan() {
        ordonnanceur.chargerPlan();
    }

    public void chargerDemandeLivraisons() {
        ordonnanceur.chargerDemandeLivraison();
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
