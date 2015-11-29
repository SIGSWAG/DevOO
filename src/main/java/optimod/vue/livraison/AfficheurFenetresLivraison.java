package optimod.vue.livraison;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison extends TreeView<Object> {

    private FenetreControleur fenetreControleur;

    private DemandeLivraisons demandeLivraisons;
    private boolean ecouteurActive = true;

    public AfficheurFenetresLivraison() {
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().getSelectedItems()
                .addListener(new ListChangeListener<TreeItem>() {
                    @Override
                    public void onChanged(Change<? extends TreeItem> change) {
                       clicElementListe();
                    }
                });
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

    private void clicElementListe() {
        if(ecouteurActive) {
            List<Livraison> livraisonsSelectionnees = new ArrayList<Livraison>();
            for (int i = 0; i < getSelectionModel().getSelectedItems().size(); i++) {
                try {
                    Object o = getSelectionModel().getSelectedItems().get(i).getValue();
                    if (o instanceof Livraison) {
                        livraisonsSelectionnees.add((Livraison) o);
                    }
                } catch (NullPointerException e) {
                    //
                }
            }
            fenetreControleur.deselectionnerTout();
            livraisonsSelectionnees.forEach(l -> fenetreControleur.selectionner(l.getIntersection()));
        }
    }

    private void expand() {
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            getRoot().getChildren().get(i).setExpanded(true);
        }
    }

    public void selectionner(Livraison l) {
        expand();
        int itemIndex = 0;
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            TreeItem<Object> objectTreeItem = getRoot().getChildren().get(i);
            Object o = objectTreeItem.getValue();
            if (o instanceof FenetreLivraison) {
                for (int j = 0; j < objectTreeItem.getChildren().size(); j++) {
                    Object o1 = objectTreeItem.getChildren().get(j).getValue();
                    if (o1 instanceof Livraison && l == o1) {
                        if(!getSelectionModel().isSelected(itemIndex + j + 1)) {
                            getSelectionModel().select(itemIndex + j + 1);
                        }
                        return;
                    }
                }
            }
            itemIndex += objectTreeItem.getChildren().size() + 1;
        }
    }

    public void deselectionner(Livraison l) {
        expand();
        ArrayList<Integer> livrSelectionnees = new ArrayList<Integer>();
        // on récupère toutes les livraisons selectionnées sauf celle que l'on va enlever
        int itemIndex = 0;
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            TreeItem<Object> objectTreeItem = getRoot().getChildren().get(i);
            Object o = objectTreeItem.getValue();
            if (o instanceof FenetreLivraison) {
                for (int j = 0; j < objectTreeItem.getChildren().size(); j++) {
                    Object o1 = objectTreeItem.getChildren().get(j).getValue();
                    if (o1 instanceof Livraison && l != o1) {
                        if(getSelectionModel().isSelected(itemIndex + j + 1)) {
                            livrSelectionnees.add(itemIndex + j + 1);
                        }
                    }
                }
            }
            itemIndex += objectTreeItem.getChildren().size() + 1;
        }

        getSelectionModel().clearSelection();
        for (int i : livrSelectionnees) {
            getSelectionModel().select(i);
        }
    }

    // deselection sans action !
    public void deselectionnerTout() {
        ecouteurActive = false;
        getSelectionModel().clearSelection();
        ecouteurActive = true;
    }

    public FenetreControleur getFenetreControleur() {
        return fenetreControleur;
    }

    public void setFenetreControleur(FenetreControleur fenetreControleur) {
        this.fenetreControleur = fenetreControleur;
    }
}
