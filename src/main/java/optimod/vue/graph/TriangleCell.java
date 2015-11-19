package optimod.vue.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class TriangleCell extends Cell {

    public TriangleCell( String id) {
        super( id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        setView(view);

    }

}
