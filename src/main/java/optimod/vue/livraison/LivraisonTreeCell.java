package optimod.vue.livraison;

import javafx.scene.control.TreeCell;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;

/**
 * Created by Jonathan on 24/11/2015.
 */
class LivraisonTreeCell extends TreeCell<Object> {

    private static final String FORMAT_HEURE = "%02d:%02d:%02d";

    public LivraisonTreeCell()
    {
        super();
    }

    @Override
    public void updateItem(Object element, boolean vide)
    {
        super.updateItem(element, vide);

        if(vide) {
            setText(null);
        }
        else {
            if(element instanceof FenetreLivraison) {
                FenetreLivraison fenetreLivraison = (FenetreLivraison) element;
                String heureDebut = String.format(FORMAT_HEURE,
                        fenetreLivraison.getHeureDebutHeure(), fenetreLivraison.getHeureDebutMinute(), fenetreLivraison.getHeureDebutSeconde());
                String heureFin = String.format(FORMAT_HEURE,
                        fenetreLivraison.getHeureFinHeure(), fenetreLivraison.getHeureFinMinute(), fenetreLivraison.getHeureFinSeconde());
                setText(heureDebut + " - " + heureFin);
            }
            else if(element instanceof Livraison) {
                Livraison livraison = (Livraison) element;
                setText("Client " +  livraison.getIdClient() + " au " + livraison.getIntersection().getAdresse());
            }
        }
        setGraphic(null);
    }
}
