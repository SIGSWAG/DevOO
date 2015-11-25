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
public class EtatUneLivrSelectionnee extends EtatDefaut {
    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(livraisonSelectionnee != null){
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatDeuxLivrSelectionnees);
            return true;
        }else{
            fenetreControleur.afficheMessage("Vous ne pouvez pas sélectionner cette intersection car elle ne possède aucune livraison.", "Mauvaise saisie", Alert.AlertType.ERROR);
            return false;
        }
    }

    @Override
    public void deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(livraisonSelectionnee != null){
            intersectionsSelectionnees.remove(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatPrincipal);
        }
    }

    @Override
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes){
        fenetreControleur.autoriseBoutons(false);
        for(Intersection inter: intersectionsSelectionnees){
            Livraison l =inter.getLivraison();
            if(l != null){
                ordonnanceur.supprimerLivraison(l);
            }
        }
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerToutesLesDeselections(true);
        fenetreControleur.activerSupprimer(true);
    }
}