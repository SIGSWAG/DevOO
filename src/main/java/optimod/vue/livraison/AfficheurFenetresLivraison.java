package optimod.vue.livraison;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison extends TreeView<Object> {

    public AfficheurFenetresLivraison() {
        super();
    }

    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        setRoot(fenetreLivaisonRoot);
        setShowRoot(false);

        for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
            TreeItem<Object> fenetreLivraisonTreeItem = new TreeItem<>(fenetreLivraison);
            for(Livraison livraison : fenetreLivraison.getLivraisons()) {
                TreeItem<Object> livraisonTreeItem = new TreeItem<>(livraison);
                fenetreLivraisonTreeItem.getChildren().add(livraisonTreeItem);
            }
            fenetreLivaisonRoot.getChildren().add(fenetreLivraisonTreeItem);

            setCellFactory(callback -> new LivraisonTreeCell());
//            fenetreLivraisonTreeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
//                public TreeCell<Object> call(TreeView<Object> param) {
//                    return new LivraisonTreeCell();
//                }
//            });
        }
    }
}
