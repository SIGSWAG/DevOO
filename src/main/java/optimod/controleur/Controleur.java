package optimod.controleur;

import javafx.stage.Stage;
import optimod.vue.Fenetre;
import optimod.xml.OuvreurDeFichierXML;

import java.io.File;

/**
 *
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    public Controleur() {

    }

    public void chargerPlan(Stage fenetre) {
        // TODO Rôle de l'ordonnanceur
        File fichier = OuvreurDeFichierXML.INSTANCE.ouvre(fenetre);
    }

    public void chargerDemandeLivraisons(Stage fenetre) {
        // TODO Rôle de l'ordonnanceur
        File fichier = OuvreurDeFichierXML.INSTANCE.ouvre(fenetre);
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
