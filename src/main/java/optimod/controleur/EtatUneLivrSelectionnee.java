package optimod.controleur;

import javafx.scene.control.Alert;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatUneLivrSelectionnee extends EtatDefaut {
    @Override
    public void selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Livraison> livraisonsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(livraisonSelectionnee != null){
            livraisonsSelectionnees.add(livraisonSelectionnee);
            Controleur.setEtatCourant(Controleur.etatDeuxLivrSelectionnee);
        }
    }

    @Override
    public void deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Livraison> livraisonsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        Intersection intersectionSelectionnee = ordonnanceur.trouverIntersection(p.x, p.y, rayon);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(livraisonSelectionnee != null){
            livraisonsSelectionnees.remove(livraisonSelectionnee);
            Controleur.setEtatCourant(Controleur.etatInit);
        }
    }

    @Override
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Livraison> livraisonsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        livraisonsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatInit);
    }

    @Override
    public void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Livraison> livraisonsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
        for(Livraison livr : livraisonsSelectionnees){
            ordonnanceur.supprimerLivraison(livr);
        }
        livraisonsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatInit);
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerToutesLesDeselections(true);
        fenetreControleur.activerSupprimer(true);
    }
}
