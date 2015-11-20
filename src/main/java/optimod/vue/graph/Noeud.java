package optimod.vue.graph;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import optimod.modele.Intersection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Noeud extends Pane {
    protected String idNoeud;

    protected List<Noeud> enfants;
    protected List<Noeud> parents;

    protected Node vue;

    protected Intersection intersection;

    public Noeud(String idNoeud, Intersection intersection) {
        this.idNoeud = idNoeud;
        this.intersection = intersection;
        this.enfants = new ArrayList<Noeud>();
        this.parents = new ArrayList<Noeud>();
    }

    public void addCellChild(Noeud noeud) {
        enfants.add(noeud);
    }

    public List<Noeud> getEnfants() {
        return enfants;
    }

    public void ajouterParent(Noeud noeud) {
        parents.add(noeud);
    }

    public List<Noeud> getParents() {
        return parents;
    }

    public void supprimerEnfant(Noeud noeud) {
        enfants.remove(noeud);
    }

    public void setVue(Node vue) {
        this.vue = vue;
        getChildren().add(this.vue);
    }

    public Node getVue() {
        return this.vue;
    }

    public String getIdNoeud() {
        return idNoeud;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public int getX() {
        return intersection.getX();
    }

    public int getY() {
        return intersection.getY();
    }
}
