package optimod.vue.livraison;

import javafx.scene.control.TreeCell;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;

/**
 * Created by Jonathan on 24/11/2015.
 */
class LivraisonTreeCell extends TreeCell<Object> {

    public LivraisonTreeCell()
    {
        super();
    }

    @Override
    public void updateItem( Object item, boolean empty )
    {
        super.updateItem( item, empty );

        if ( empty )
        {
            setText( null );
        }
        else
        {
            if(item instanceof FenetreLivraison) {
                FenetreLivraison fenetreLivraison = (FenetreLivraison) item;
                setText(fenetreLivraison.getHeureDebut() + " - " + fenetreLivraison.getHeureFin());
            }
            else if(item instanceof Livraison) {
                Livraison livraison = (Livraison) item;
                setText("Client 300" /** +  livraison.getClient **/ + " au " + livraison.getIntersection().getAdresse());
            }
        }
        setGraphic( null );
    }
}
