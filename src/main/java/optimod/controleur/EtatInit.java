package optimod.controleur;

import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatInit extends EtatDefaut {
    // Etat initial

    @Override
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        if(ordonnanceur.chargerPlan())
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur){
        fenetreControleur.activerChargerPlan(true);
    }

}
