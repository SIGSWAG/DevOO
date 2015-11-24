package optimod.vue.livraison;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import optimod.modele.DemandeLivraison;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;

import java.util.List;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison {

    private TreeView<Object> fenetreLivraisonTreeView;

    public AfficheurFenetresLivraison(TreeView<Object> fenetreLivraisonTreeView) {
        this.fenetreLivraisonTreeView = fenetreLivraisonTreeView;
    }

    public void chargerFenetresLivraison(DemandeLivraison demandeLivraison) {
        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        fenetreLivraisonTreeView.setRoot(fenetreLivaisonRoot);
        fenetreLivraisonTreeView.setShowRoot(false);

        for (FenetreLivraison fenetreLivraison : demandeLivraison.getFenetres()) {
            TreeItem<Object> fenetreLivraisonTreeItem = new TreeItem<>(fenetreLivraison);
            for(Livraison livraison : fenetreLivraison.getLivraisons()) {
                TreeItem<Object> livraisonTreeItem = new TreeItem<>(livraison);
                fenetreLivraisonTreeItem.getChildren().add(livraisonTreeItem);
            }
            fenetreLivaisonRoot.getChildren().add(fenetreLivraisonTreeItem);

            fenetreLivraisonTreeView.setCellFactory(callback -> new LivraisonTreeCell());
//            fenetreLivraisonTreeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
//                public TreeCell<Object> call(TreeView<Object> param) {
//                    return new LivraisonTreeCell();
//                }
//            });
        }
    }
}
