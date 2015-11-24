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
public class EtatPrincipal extends EtatDefaut {
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
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
    }

    @Override
    public void ajouterLivraison(FenetreControleur fenetreControleur){
        fenetreControleur.autoriseBoutons(false);
        fenetreControleur.afficheMessage("Vous passez en mode ajout d'une livraison.\n\n" +
                "Pour ajouter une livraison vous devez sélectionner l'intersection concernée (il ne doit pas y avoir de livraison sur l'intersection sélectionnée.\n" +
                "Ensuite vous devez sélectionner une livraison avant laquelle votre livraison sera créée.\n" +
                "Enfin vous devrez valider l'ajout grâce au bouton associé dans la barre de menu.\n\n" +
                "NB: A tout moment vous pouvez sortir du mode d'ajout grâce au bouton associé dans la barre de menu.",
                "Ajout d'une livraison : Mode d'emploi",
                Alert.AlertType.INFORMATION
        );
        Controleur.setEtatCourant(Controleur.etatAjoutInit);
    }

    @Override
    public void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.autoriseBoutons(false);
        ordonnanceur.genererFeuilleDeRoute();
    }

    @Override
    public void undo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.autoriseBoutons(false);
        listeDeCdes.undo();
    }

    @Override
    public void redo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
        listeDeCdes.redo();
    }

    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(livraisonSelectionnee != null){
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatUneLivrSelectionnee);
            return true;
        }else{
            fenetreControleur.afficheMessage("Vous ne pouvez pas sélectionner cette intersection car elle ne possède aucune livraison.", "Mauvaise saisie", Alert.AlertType.ERROR);
            return false;
        }
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
        fenetreControleur.activerAjouter(true);
        if(listeDeCdes.onPeutAnnuler()){
            fenetreControleur.activerAnnuler(true);
        }
        if(listeDeCdes.onPeutRejouer()){
            fenetreControleur.activerRejouer(true);
        }
        fenetreControleur.activerGenererFeuilleRoute(true);
        fenetreControleur.activerSelections(true);
    }
}
