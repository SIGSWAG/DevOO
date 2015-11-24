package optimod.controleur;

import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAttenteDemandeLivr extends EtatDefaut {
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
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr); // TODO à changer
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur){
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
    }
}
