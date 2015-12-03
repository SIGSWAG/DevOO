package optimod.controleur.etat;


import javafx.scene.control.Alert.AlertType;
import optimod.controleur.ListeDeCommandes;
import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;

/**
 * Etat par défaut, définissait le comportement par défaut de toutes les méthodes de l'interface Etat
 */
public abstract class EtatDefaut implements Etat {

    // Definition des comportements par defaut des methodes
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void undo(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void redo(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void ajouterLivraison(FenetreControleur fenetreControleur) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void carSaisi(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCommandes listeDeCdes, int codeCar) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees) {
        return false;
    }

    public boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees) {
        return false;
    }

    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void echangeesLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> livraisonsSelectionnees) {
        fenetreControleur.afficherMessage("Désolé, vous ne pouvez pas faire cette action dans cet état.", "Erreur", AlertType.ERROR);
    }

    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
    }
}
