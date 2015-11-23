package optimod.controleur;


import javafx.scene.control.Alert.AlertType;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.awt.*;

public abstract class EtatDefaut implements Etat{
    // Definition des comportements par defaut des methodes
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void undo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void redo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes) {
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void ajouterLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void echangerLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void supprimerLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void carSaisi(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, int codeCar){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    };
    public void clicGauche(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, Point p){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void updateVue(FenetreControleur fenetreControleur){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
}
