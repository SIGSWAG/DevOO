package optimod.controleur.etat;

import optimod.controleur.Controleur;
import optimod.controleur.ListeDeCommandes;
import optimod.controleur.commande.CommandeSuppression;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Etat représentant plus de deux livraisons sélectionnées
 */
public class EtatPlusDeDeuxLivraisonsSelectionnees extends EtatDefaut {

    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if (livraisonSelectionnee != null && livraisonSelectionnee != ordonnanceur.getDemandeLivraisons().getEntrepot()) {
            intersectionsSelectionnees.add(intersectionSelectionnee);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if (livraisonSelectionnee != null && livraisonSelectionnee != ordonnanceur.getDemandeLivraisons().getEntrepot()) {
            intersectionsSelectionnees.remove(intersectionSelectionnee);
            if (intersectionsSelectionnees.size() > 2) {
                Controleur.setEtatCourant(Controleur.getEtatPlusDeDeuxLivrSelectionnees());
            } else {
                Controleur.setEtatCourant(Controleur.getEtatDeuxLivrSelectionnees());
            }
            return true;
        }
        return false;
    }

    @Override
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.getEtatPrincipal());
    }

    @Override
    public void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
        List<Livraison> lesLivraisonsASupp = new ArrayList<>();
        for (Intersection inter : intersectionsSelectionnees) {
            Livraison l = inter.getLivraison();
            if (l != null) {
                lesLivraisonsASupp.add(l);
            }
        }
        listeDeCdes.ajoute(new CommandeSuppression(ordonnanceur, lesLivraisonsASupp));
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.getEtatPrincipal());
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerToutDeselectionner(true);
        fenetreControleur.activerSupprimer(true);
    }
}
