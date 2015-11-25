package optimod.vue.plan;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
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

    private FenetreControleur fenetreControleur;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Group group;

    public AfficheurPlan(Group group, FenetreControleur fenetreControleur) {
        this.group = group;
        this.fenetreControleur = fenetreControleur;
    }

    /**
     * Ajoute des intersections au plan, ainsi que leurs tronçons.
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
     * @param demandeLivraisons
     */
    public void chargerDemandeLivraisons(DemandeLivraisons demandeLivraisons) {

        reinitialiserLivraisons();
        Livraison entrepot = demandeLivraisons.getEntrepot();
        IntersectionPane intersectionPane = trouverIntersectionPane(entrepot.getIntersection());
        intersectionPane.setEstEntrepot(true);

    }

    /**
     * Vide le plan de tous ses éléments.
     */
    private void vider() {
        group.getChildren().clear();
    }

    /**
     * Réinitialise toutes les livraisons dessinées.
     */
    private void reinitialiserLivraisons() {
        for (IntersectionPane intersectionPane : getIntersectionsPane()) {
            intersectionPane.setEstEntrepot(false);
        }
    }

    private IntersectionPane trouverIntersectionPane(Intersection intersection) {
        for (IntersectionPane intersectionPane : getIntersectionsPane()) {
            if (intersectionPane.getIntersection().equals(intersection))
                return intersectionPane;
        }
        return null;
    }

    public void selectionnerIntersection(Livraison livraison) {
        Intersection intersection = livraison.getIntersection();
        IntersectionPane intersectionPane = trouverIntersectionPane(intersection);
        if(intersectionPane != null) {
            logger.debug("Surbrillance");
            //intersectionPane.setStyle("-fx-background-color:#10cc00;");
            if (Platform.isSupported(ConditionalFeature.EFFECT)) {
                DropShadow dropShadow = new DropShadow(10, Color.BLUE);
                dropShadow.setBlurType(BlurType.GAUSSIAN);
                intersectionPane.setEffect(dropShadow);
            }
        }
    }

    public void deselectionnerToutesIntersections(){
        for(IntersectionPane intersectionPane: getIntersectionsPane()){
            intersectionPane.deselectionner();
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

    private Collection<TronconPane> getTronconsLigne() {
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
