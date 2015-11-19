package optimod.vue.graph;

import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Edge extends Group {

    protected IntersectionCercle source;
    protected IntersectionCercle target;

    Line line;

    public Edge(IntersectionCercle source, IntersectionCercle target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        line.startXProperty().bind(source.layoutXProperty().add(source.getX()));
        line.startYProperty().bind(source.layoutYProperty().add(source.getY()));

        line.endXProperty().bind(target.layoutXProperty().add( target.getX()));
        line.endYProperty().bind(target.layoutYProperty().add( target.getY()));

        getChildren().add(line);
    }

    public IntersectionCercle getSource() {
        return source;
    }

    public IntersectionCercle getTarget() {
        return target;
    }

}