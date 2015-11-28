package optimod.vue.livraison;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.plan.AfficheurPlan;

import java.util.*;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison extends TreeView<Object> {

    private AfficheurPlan afficheurPlan;

    private Map<FenetreLivraison, Color> couleurFenetresLivraison;

    private DemandeLivraisons demandeLivraisons;

    public AfficheurFenetresLivraison() {
        this.couleurFenetresLivraison = new HashMap<>();
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> clicElementListe(newValue.getValue()));
    }

    public void reinitialiser() {
        if (getRoot() != null)
            getRoot().getChildren().clear();
    }

    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        this.demandeLivraisons = demandeLivraisons;

        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        setRoot(fenetreLivaisonRoot);
        setShowRoot(false);

        mettreAJour();
    }

    public void mettreAJour() {

        reinitialiser();

        for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
            Color couleur = afficheurPlan.colorierLivraisons(fenetreLivraison);
            couleurFenetresLivraison.put(fenetreLivraison, couleur);
            TreeItem<Object> fenetreLivraisonTreeItem = new TreeItem<>(fenetreLivraison);

            // On trie les livraisons par heure de passage prévues afin qu'elles s'affichent dans l'ordre.
            List<Livraison> livraisonsTriees = new ArrayList<>(fenetreLivraison.getLivraisons());
            Collections.sort(livraisonsTriees, (liv1, liv2) -> Integer.compare(liv1.getHeureLivraison(), liv2.getHeureLivraison()));

            for (Livraison livraison : livraisonsTriees) {
                TreeItem<Object> livraisonTreeItem = new TreeItem<>(livraison);
                fenetreLivraisonTreeItem.getChildren().add(livraisonTreeItem);
            }

            getRoot().getChildren().add(fenetreLivraisonTreeItem);
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
