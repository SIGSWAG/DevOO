package optimod.vue.plan;

import javafx.scene.Group;
import javafx.scene.Node;
import optimod.modele.DemandeLivraisons;
import optimod.modele.Intersection;
import optimod.modele.Plan;
import optimod.modele.Troncon;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aurelien on 23/11/15.
 */
public final class AfficheurPlan {

    private Group group;

    public AfficheurPlan(Group group) {
        this.group = group;
    }

    /**
     * Ajoute des intersections au plan, ainsi que leurs tronçons.
     * @param plan Le plan à charger.
     */
    public void chargerPlan(Plan plan) {
        for (Intersection intersection : plan.getIntersections()) {
            IntersectionPane intersectionPane = new IntersectionPane(intersection);
            group.getChildren().add(intersectionPane);
            for (Troncon troncon : intersection.getSortants()) {
                group.getChildren().add(new TronconPane(intersectionPane, troncon));
            }
        }
    }

    /**
     * Affiche une demande de livraison sur le plan.
     *
     * @param demandeLivraisons
     */
    public void chargerDemandeLivraisons(DemandeLivraisons demandeLivraisons) {

    }

    /**
     * Vide le plan de tous ses éléments.
     */
    public void vider() {
        group.getChildren().clear();
    }

    private Collection<IntersectionPane> getIntersectionsCercle() {
        ArrayList<IntersectionPane> intersectionsCercle = new ArrayList<IntersectionPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersectionsCercle.add((IntersectionPane) noeud);
            }
        }
        return intersectionsCercle;
    }

    private Collection<TronconPane> getTronconsLigne() {
        ArrayList<TronconPane> tronconsLigne = new ArrayList<TronconPane>();
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
        ArrayList<Troncon> troncons = new ArrayList<Troncon>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                troncons.add(((TronconPane) noeud).getTroncon());
            }
        }
        return troncons;
    }

}
