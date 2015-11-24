package optimod.vue.livraison;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison {

    private TreeView<Object> fenetreLivraisonTreeView;

    public AfficheurFenetresLivraison(TreeView<Object> fenetreLivraisonTreeView) {
        this.fenetreLivraisonTreeView = fenetreLivraisonTreeView;
    }

    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        fenetreLivraisonTreeView.setRoot(fenetreLivaisonRoot);
        fenetreLivraisonTreeView.setShowRoot(false);

        for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
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
