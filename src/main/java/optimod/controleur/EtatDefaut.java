package optimod.controleur;


import javafx.scene.control.Alert.AlertType;
import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;

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
    public void undo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void redo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes) {
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void ajouterLivraison(FenetreControleur fenetreControleur) {
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void carSaisi(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, int codeCar){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees) {
        return false;
    }

    public boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees) {
        return false;
    }
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void echangeesLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes) {
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }
    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> livraisonsSelectionnees){
        fenetreControleur.afficheMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
    }
}
