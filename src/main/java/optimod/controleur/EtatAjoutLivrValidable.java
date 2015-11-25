package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;
import java.awt.*;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAjoutLivrValidable extends EtatDefaut {
    @Override
    public void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes){
        Intersection i1 = intersectionsSelectionnees.get(0);
        Intersection i2 = intersectionsSelectionnees.get(1);
        if(i1.getLivraison() != null) {
            ordonnanceur.ajouterLivraison(i2, i1.getLivraison());
            listeDeCdes.ajoute(new CdeAjout(ordonnanceur, i1.getLivraison(), i2));
        }
        else {
            ordonnanceur.ajouterLivraison(i1, i2.getLivraison());
            listeDeCdes.ajoute(new CdeAjout(ordonnanceur, i2.getLivraison(), i1));
        }
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public void deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees){
        fenetreControleur.autoriseBoutons(false);
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
