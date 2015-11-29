package optimod.vue.plan;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import optimod.modele.*;
import optimod.vue.FenetreControleur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by aurelien on 23/11/15.
 */
public final class AfficheurPlan {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FenetreControleur fenetreControleur;

    private Group group;

    private List<IntersectionPane> intersectionsSelectionnees;

    public AfficheurPlan(Group group, FenetreControleur fenetreControleur) {
        this.group = group;
        this.fenetreControleur = fenetreControleur;
        this.intersectionsSelectionnees = new ArrayList<>();
    }

    /**
     * Ajoute des intersections au plan, ainsi que leurs tronçons.
     *
     * @param plan Le plan à charger.
     */
    public void chargerPlan(Plan plan) {
        vider();

        for (Intersection intersection : plan.getIntersections()) {
            IntersectionPane intersectionPane = new IntersectionPane(intersection, fenetreControleur);
            group.getChildren().add(intersectionPane);
            for (Troncon troncon : intersection.getSortants()) {
                group.getChildren().add(new TronconPane(intersectionPane, troncon));
            }
            intersectionPane.toFront(); // Les intersections s'affichent au dessus des tronçons
        }
    }

    /**
     * Affiche une demande de livraison sur le plan.
     *
     * @param demandeLivraisons
     */
    public void chargerDemandeLivraisons(DemandeLivraisons demandeLivraisons) throws NullPointerException {
        reinitialiserLivraisons();

        Livraison entrepot = demandeLivraisons.getEntrepot();
        IntersectionPane intersectionPane = trouverIntersectionPane(entrepot.getIntersection());
        intersectionPane.setEstEntrepot(true);

        demandeLivraisons.getFenetres().forEach(fenetreLivraison -> {
            Color couleur = fenetreControleur.associerCouleur(fenetreLivraison);
            fenetreLivraison.getLivraisons().forEach(livraison -> {
                trouverIntersectionPane(livraison.getIntersection()).setCouleur(couleur);
            });
        });

        mettreAJour();
    }

    /**
     * Affiche un itinéraire sur le plan.
     */
    public void chargerItineraire() {
        mettreAJour();
    }

    /**
     * Vide le plan de tous ses éléments.
     */
    private void vider() {
        group.getChildren().clear();
    }

    /**
     * Mets à jour l'affichage du plan.
     */
    private void mettreAJour() {
        getTronconsPane().forEach(TronconPane::mettreAJour);
        getIntersectionsPane().forEach(IntersectionPane::mettreAJour);
    }

    /**
     * Réinitialise toutes les livraisons dessinées.
     */
    private void reinitialiserLivraisons() {
        for (IntersectionPane intersectionPane : getIntersectionsPane()) {
            intersectionPane.reinitialiser();
        }
    }

    private IntersectionPane trouverIntersectionPane(Intersection intersection) {
        for (IntersectionPane intersectionPane : getIntersectionsPane()) {
            if (intersectionPane.getIntersection().equals(intersection))
                return intersectionPane;
        }
        return null;
    }

    public void selectionner(Intersection intersection) {
        IntersectionPane intersectionPane = trouverIntersectionPane(intersection);
        if (intersectionPane != null) {
            intersectionPane.selectionner();
            intersectionsSelectionnees.add(intersectionPane);
        }
    }

    public void deselectionnerToutesIntersections() {
        getIntersectionsPane().forEach(IntersectionPane::deselectionner);
    }

    public void deselectionner(Intersection i){
        IntersectionPane ip = trouverIntersectionPane(i);
        if(ip != null) {
            ip.deselectionner();
            intersectionsSelectionnees.remove(ip);
        }
    }

    public void selectionnerLivraisons(FenetreLivraison fenetreLivraison) {
        deselectionnerIntersections();
        for(Livraison livraison : fenetreLivraison.getLivraisons()) {
            selectionner(livraison.getIntersection());
        }
    }

    private void deselectionnerIntersections() {
        for (IntersectionPane intersectionPane : intersectionsSelectionnees) {
            intersectionPane.setEffect(null);
        }
    }

    private Collection<IntersectionPane> getIntersectionsPane() {
        List<IntersectionPane> intersectionsCercle = new ArrayList<IntersectionPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersectionsCercle.add((IntersectionPane) noeud);
            }
        }
        return intersectionsCercle;
    }

    private Collection<TronconPane> getTronconsPane() {
        List<TronconPane> tronconsLigne = new ArrayList<TronconPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                tronconsLigne.add((TronconPane) noeud);
            }
        }
        return tronconsLigne;
    }

    private Collection<Intersection> getIntersections() {
        ArrayList<Intersection> intersections = new ArrayList<Intersection>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersections.add(((IntersectionPane) noeud).getIntersection());
            }
        }
        return intersections;
    }

    private Collection<Troncon> getTroncons() {
        List<Troncon> troncons = new ArrayList<Troncon>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                troncons.add(((TronconPane) noeud).getTroncon());
            }
        }
        return troncons;
    }

}
