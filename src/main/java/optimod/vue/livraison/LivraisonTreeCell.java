package optimod.vue.livraison;

import javafx.scene.control.TreeCell;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.plan.IntersectionPane;

/**
 * Created by Jonathan on 24/11/2015.
 */
class LivraisonTreeCell extends TreeCell<Object> {

    private static final String FORMAT_HEURE = "%02d:%02d:%02d";

    private IntersectionPane intersectionPane;

    public LivraisonTreeCell() {
        super();
    }

    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            if (item instanceof FenetreLivraison) {
                FenetreLivraison fenetreLivraison = (FenetreLivraison) item;
                String heureDebut = String.format(FORMAT_HEURE, fenetreLivraison.getHeureDebutHeure(), fenetreLivraison.getHeureDebutMinute(), fenetreLivraison.getHeureDebutSeconde());
                String heureFin = String.format(FORMAT_HEURE, fenetreLivraison.getHeureFinHeure(), fenetreLivraison.getHeureFinMinute(), fenetreLivraison.getHeureFinSeconde());
                setText(heureDebut + " - " + heureFin);
            } else if (item instanceof Livraison) {
                Livraison livraison = (Livraison) item;
                setText("Client " + livraison.getIdClient() + " au " + livraison.getIntersection().getAdresse());
            }
        }
        setGraphic(null);
    }

    public IntersectionPane getIntersectionPane() {
        return intersectionPane;
    }

    public void setIntersectionPane(IntersectionPane intersectionPane) {
        this.intersectionPane = intersectionPane;
    }
}
