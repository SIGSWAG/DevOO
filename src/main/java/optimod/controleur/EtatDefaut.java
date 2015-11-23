package optimod.controleur;


import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.awt.*;

public abstract class EtatDefaut implements Etat{
    // Definition des comportements par defaut des methodes
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){}
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){}
    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){}
    public void undo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes){}
    public void redo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes) {}
    public void ajouterLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {}
    public void echangerLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){}
    public void supprimerLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {}
    public void carSaisi(Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, int codeCar){};
    public void clicGauche(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, Point p){}
    public void updateVue(FenetreControleur fenetreControleur){}
}
