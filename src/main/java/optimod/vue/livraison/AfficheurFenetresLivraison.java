package optimod.vue.livraison;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.FenetreControleur;
import optimod.vue.plan.AfficheurPlan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonathan on 24/11/2015.
 */
public final class AfficheurFenetresLivraison extends TreeView<Object> {

    private Map<FenetreLivraison, Color> couleurFenetresLivraison;
    private FenetreControleur fenetreControleur;

    public AfficheurFenetresLivraison() {
        this.couleurFenetresLivraison = new HashMap<>();
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> clicElementListe());
    }

    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        setRoot(fenetreLivaisonRoot);
        setShowRoot(false);

        for (FenetreLivraison fenetreLivraison : demandeLivraisons.getFenetres()) {
            Color couleur = fenetreControleur.colorierLivraisons(fenetreLivraison);
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

    private void clicElementListe() {
//        if (element instanceof FenetreLivraison) {
//            FenetreLivraison fenetreLivraison = (FenetreLivraison) element;
//            fenetreControleur.selectionnerLivraisons(fenetreLivraison);
//        } else {
        fenetreControleur.deselectionnerTout();
        for (int i = 0; i < getSelectionModel().getSelectedItems().size(); i++) {
            try {
                Object o = getSelectionModel().getSelectedItems().get(i).getValue();
                if (o instanceof Livraison) {
                    fenetreControleur.selectionner(((Livraison) o).getIntersection());
                }
            }catch (NullPointerException e){
                return;
            }
        }
//        }
    }

    public void selectionner(Livraison l){
        int item_index = 0;
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            Object o = getRoot().getChildren().get(i).getValue();
            if(o instanceof FenetreLivraison){
                for(int j = 0 ; j < getRoot().getChildren().get(i).getChildren().size() ; j++) {
                    Object o1 = getRoot().getChildren().get(i).getChildren().get(j).getValue();
                    if (o1 instanceof Livraison && l == o1) {
                        if(!getSelectionModel().isSelected(item_index + j + 1))
                            System.out.println("todo");
//                            getSelectionModel().select(item_index + j + 1);
                        return;
                    }
                }
            }
            item_index += getRoot().getChildren().get(i).getChildren().size() + i;
        }
    }

    public void deselectionner(Livraison l){

    }

    public void setFenetreControleur(FenetreControleur fenetreControleur) {
        this.fenetreControleur = fenetreControleur;
    }
}
