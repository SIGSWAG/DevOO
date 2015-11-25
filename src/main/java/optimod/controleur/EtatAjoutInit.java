package optimod.controleur;

import javafx.scene.control.Alert;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;
import java.awt.*;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAjoutInit extends EtatDefaut {
    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(intersectionsSelectionnees.size() == 0){
            intersectionsSelectionnees.add(intersectionSelectionnee);
        }else if(livraisonSelectionnee == null && intersectionsSelectionnees.get(0).getLivraison() != null ||
                livraisonSelectionnee != null && intersectionsSelectionnees.get(0).getLivraison() == null){
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatAjoutLivrValidable);
        }else{
            fenetreControleur.afficheMessage("Vous ne pouvez pas sélectionner cette intersection. Vous devez selectionner une intersection vide où insérer la livraison et une livraison existate avant laquelle ajouter la livraison.", "Mauvaise saisie", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        if(intersectionSelectionnee != null)
            intersectionsSelectionnees.remove(intersectionSelectionnee);
    }

    @Override
    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerAnnulerAjout(true);
    }
}
