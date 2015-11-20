package optimod.controleur;

import optimod.modele.Ordonnanceur;

/**
 *
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    private Ordonnanceur ordonnanceur;

    public Controleur(Ordonnanceur ordonnanceur) {
        this.ordonnanceur = ordonnanceur;
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
