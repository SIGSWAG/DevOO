package optimod.controleur.etat;

import optimod.controleur.Controleur;
import optimod.controleur.ListeDeCommandes;
import optimod.controleur.commande.CommandeEchange;
import optimod.controleur.commande.CommandeSuppression;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Etat où deux livraisons sont sélectionnées
 */
public class EtatDeuxLivraisonsSelectionnees extends EtatDefaut {

    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if (livraisonSelectionnee != null && livraisonSelectionnee != ordonnanceur.getDemandeLivraisons().getEntrepot()) {
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.getEtatPlusDeDeuxLivrSelectionnees());
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
            Controleur.setEtatCourant(Controleur.getEtatUneLivrSelectionnee());
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
    public void echangerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
        Livraison l1 = intersectionsSelectionnees.get(0).getLivraison();
        Livraison l2 = intersectionsSelectionnees.get(1).getLivraison();
        listeDeCdes.ajoute(new CommandeEchange(ordonnanceur, l1, l2));
        intersectionsSelectionnees.clear();
        Controleur.setEtatCourant(Controleur.getEtatPrincipal());
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.activerSelections(true);
        fenetreControleur.activerDeselections(true);
        fenetreControleur.activerEchanger(true);
        fenetreControleur.activerToutDeselectionner(true);
        fenetreControleur.activerSupprimer(true);
    }
}
