package optimod.vue.livraison;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.plan.AfficheurPlan;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison {

    private TreeView<Object> fenetreLivraisonTreeView;
    private AfficheurPlan afficheurPlan;

    public AfficheurFenetresLivraison(TreeView<Object> fenetreLivraisonTreeView, AfficheurPlan afficheurPlan) {
        this.fenetreLivraisonTreeView = fenetreLivraisonTreeView;
        this.afficheurPlan = afficheurPlan;

        fenetreLivraisonTreeView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> clicElementListe(newValue.getValue()));
    }

    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        fenetreLivraisonTreeView.setRoot(fenetreLivaisonRoot);
        fenetreLivraisonTreeView.setShowRoot(false);

        for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
            TreeItem<Object> fenetreLivraisonTreeItem = new TreeItem<>(fenetreLivraison);
            for (Livraison livraison : fenetreLivraison.getLivraisons()) {
                TreeItem<Object> livraisonTreeItem = new TreeItem<>(livraison);
                fenetreLivraisonTreeItem.getChildren().add(livraisonTreeItem);
            }
            fenetreLivaisonRoot.getChildren().add(fenetreLivraisonTreeItem);

            fenetreLivraisonTreeView.setCellFactory(callback -> new LivraisonTreeCell());
        }
    }

    private void clicElementListe(Object element) {
        if (element instanceof FenetreLivraison) {
            FenetreLivraison fenetreLivraison = (FenetreLivraison) element;
            afficheurPlan.selectionnerIntersections(fenetreLivraison);
        } else if (element instanceof Livraison) {
            Livraison livraison = (Livraison) element;
            afficheurPlan.selectionnerIntersection(livraison);
        }
    }

}
