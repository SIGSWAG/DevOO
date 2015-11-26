package optimod.vue.livraison;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.plan.AfficheurPlan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison extends TreeView<Object> {

    private AfficheurPlan afficheurPlan;

    private Map<FenetreLivraison, Color> couleurFenetresLivraison;

    public AfficheurFenetresLivraison() {
        this.couleurFenetresLivraison = new HashMap<>();
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> clicElementListe(newValue.getValue()));
    }

    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        setRoot(fenetreLivaisonRoot);
        setShowRoot(false);

        for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
            Color couleur = afficheurPlan.colorierLivraisons(fenetreLivraison);
            couleurFenetresLivraison.put(fenetreLivraison, couleur);
            TreeItem<Object> fenetreLivraisonTreeItem = new TreeItem<>(fenetreLivraison);
            for (Livraison livraison : fenetreLivraison.getLivraisons()) {
                TreeItem<Object> livraisonTreeItem = new TreeItem<>(livraison);
                fenetreLivraisonTreeItem.getChildren().add(livraisonTreeItem);
            }
            fenetreLivaisonRoot.getChildren().add(fenetreLivraisonTreeItem);
            setCellFactory(callback -> new LivraisonTreeCell(this));
        }

    }

    public Color getCouleur(FenetreLivraison fenetreLivraison) {
        return couleurFenetresLivraison.get(fenetreLivraison);
    }

    private void clicElementListe(Object element) {
        if (element instanceof FenetreLivraison) {
            FenetreLivraison fenetreLivraison = (FenetreLivraison) element;
            afficheurPlan.selectionnerLivraisons(fenetreLivraison);
        } else if (element instanceof Livraison) {
            Livraison livraison = (Livraison) element;
            afficheurPlan.selectionnerLivraison(livraison, true);
        }
    }

    public void setAfficheurPlan(AfficheurPlan afficheurPlan) {
        this.afficheurPlan = afficheurPlan;
    }

}
