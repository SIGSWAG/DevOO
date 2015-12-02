package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAjoutLivraisonValidable extends EtatDefaut {
    @Override
    public void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
        Intersection i1 = intersectionsSelectionnees.get(0);
        Intersection i2 = intersectionsSelectionnees.get(1);
        if (i1.getLivraison() != null) {
            listeDeCdes.ajoute(new CommandeAjout(ordonnanceur, i1.getLivraison(), i2));
        } else {
            listeDeCdes.ajoute(new CommandeAjout(ordonnanceur, i2.getLivraison(), i1));
        }
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        if (intersectionSelectionnee != null) {
            intersectionsSelectionnees.remove(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatAjoutInit);
            return true;
        }
        return false;
    }

    @Override
    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatPrincipal);
    }

    @Override
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.etatAjoutInit);
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerToutDeselectionner(true);
        fenetreControleur.activerDeselectionsEntrepot(true);
        fenetreControleur.activerAnnulerAjout(true);
        fenetreControleur.activerValiderAjout(true);
    }
}
