package optimod.vue.graph;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class RectangleCell extends Cell {

    public RectangleCell( String id) {
        super( id);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        setView( view);

    }

}
