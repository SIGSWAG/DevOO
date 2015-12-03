package optimod.controleur.etat;

import javafx.scene.control.Alert;
import optimod.controleur.Controleur;
import optimod.controleur.ListeDeCommandes;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;

/**
 * Etat ajout initial
 */
public class EtatAjoutInit extends EtatDefaut {

    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if (intersectionsSelectionnees.size() == 0 && livraisonSelectionnee != null) {
            intersectionsSelectionnees.add(intersectionSelectionnee);
        } else if (livraisonSelectionnee == null && intersectionsSelectionnees.size() == 1 && intersectionsSelectionnees.get(0).getLivraison() != null) {
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.getEtatAjoutLivraisonValidable());
        } else {
            fenetreControleur.afficherMessage("Vous ne pouvez pas sélectionner cette intersection. Vous devez d'abord selectionner une livraison existante avant laquelle ajouter la livraison.\n\n" + "Puis sélectionner une intersection vide où insérer la nouvelle livraison.", "Mauvaise saisie", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        if (intersectionSelectionnee != null) {
            intersectionsSelectionnees.remove(intersectionSelectionnee);
            return true;
        }
        return false;
    }

    @Override
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.getEtatAjoutInit());
    }

    @Override
    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.getEtatPrincipal());
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerSelectionsEntrepot(true);
        fenetreControleur.activerDeselectionsEntrepot(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerToutDeselectionner(true);
        fenetreControleur.activerAnnulerAjout(true);
    }
}
