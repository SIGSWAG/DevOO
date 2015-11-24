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
public class EtatAjoutLivrValidable extends EtatDefaut {
    @Override
    public void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees){
        Intersection i1 = intersectionsSelectionnees.get(0);
        Intersection i2 = intersectionsSelectionnees.get(1);
        if(i1.getLivraison() != null)
            ordonnanceur.ajouterLivraison(i2, i1.getLivraison());
        else
            ordonnanceur.ajouterLivraison(i1, i2.getLivraison());
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
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
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerAnnulerAjout(true);
        fenetreControleur.activerValiderAjout(true);
    }
}
