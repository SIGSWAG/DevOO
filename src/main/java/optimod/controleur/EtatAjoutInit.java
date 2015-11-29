package optimod.controleur;

import javafx.scene.control.Alert;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAjoutInit extends EtatDefaut {
    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(intersectionsSelectionnees.size() == 0 && livraisonSelectionnee != null){
            intersectionsSelectionnees.add(intersectionSelectionnee);
        }else if(livraisonSelectionnee == null && intersectionsSelectionnees.size() == 1 && intersectionsSelectionnees.get(0).getLivraison() != null){
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatAjoutLivrValidable);
        }else{
            fenetreControleur.afficheMessage("Vous ne pouvez pas sélectionner cette intersection. Vous devez d'abbord selectionner une livraison existante avant laquelle ajouter la livraison.\n\n" +
                    "Puis sélectionner une intersection vide où insérer la nouvelle livraison.", "Mauvaise saisie", Alert.AlertType.ERROR);
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
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatAjoutInit);
    }

    @Override
    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerSelectionsEntrepot(true);
        fenetreControleur.activerDeselectionsEntrepot(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerToutDeselectionner(true);
        fenetreControleur.activerAnnulerAjout(true);
    }
}
