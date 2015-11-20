package optimod.vue.graph;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Created by Jonathan on 19/11/2015.
 */
public class Graphe {

    private Model model;

    private Group canvas;

    private AnchorPane anchorPane;

    private Pane canvasPane;

    public Graphe() {
        model = new Model();
        canvas = new Group();
        canvasPane = new Pane();
        canvas.getChildren().add(canvasPane);
        anchorPane = new AnchorPane(canvas);
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Model getModel() { return model; }

    public void mettreAJour() {
        // add components to graph pane
        canvasPane.getChildren().addAll(model.getAddedArcs());
        canvasPane.getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        canvasPane.getChildren().removeAll(model.getRemovedCells());
        canvasPane.getChildren().removeAll(model.getRemovedArcs());

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        model.attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        model.disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        model.merge();
    }

}
