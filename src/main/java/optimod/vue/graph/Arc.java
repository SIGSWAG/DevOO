package optimod.vue.graph;

import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Arc extends Group {

    protected IntersectionCercle source;
    protected IntersectionCercle cible;

    protected Line ligne;

    public Arc(IntersectionCercle source, IntersectionCercle cible) {

        this.source = source;
        this.cible = cible;

        source.addCellChild(cible);
        cible.addCellParent(source);

        ligne = new Line();

        ligne.startXProperty().bind(source.layoutXProperty().add(source.getX()));
        ligne.startYProperty().bind(source.layoutYProperty().add(source.getY()));

        ligne.endXProperty().bind(cible.layoutXProperty().add( cible.getX()));
        ligne.endYProperty().bind(cible.layoutYProperty().add( cible.getY()));

        getChildren().add(ligne);
    }

    public IntersectionCercle getSource() {
        return source;
    }

    public IntersectionCercle getCible() {
        return cible;
    }

}