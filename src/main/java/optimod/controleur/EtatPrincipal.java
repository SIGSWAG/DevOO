package optimod.controleur;

import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatPrincipal extends EtatDefaut {
    @Override
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        if(ordonnanceur.chargerPlan())
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
    }

    @Override
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        if(ordonnanceur.chargerDemandeLivraison())
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
    }

    @Override
    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        ordonnanceur.calculerItineraire();
        Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr); // TODO à changer
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur){
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
        fenetreControleur.activerAjouter(true);
        fenetreControleur.activerAnnuler(true);
        fenetreControleur.activerRejouer(true);
        fenetreControleur.activerGenererFeuilleRoute(true);
    }
}
