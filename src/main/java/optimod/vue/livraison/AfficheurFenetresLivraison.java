package optimod.vue.livraison;

import javafx.collections.ListChangeListener;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import optimod.modele.DemandeLivraisons;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.FenetreControleur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TreeView customisée permettant d'afficher les livraisons et fenêtres de livraison sous la forme d'une TreeView
 */
public final class AfficheurFenetresLivraison extends TreeView<Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FenetreControleur fenetreControleur;

    private DemandeLivraisons demandeLivraisons;

    private boolean ecouteurActive;

    public AfficheurFenetresLivraison() {
        ecouteurActive = true;
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem>) change -> clicElementListe());
    }

    /**
     * Réintialise la TreeView en enlevant tous les élements
     */
    public void reinitialiser() {
        if (getRoot() != null) {
            getRoot().getChildren().clear();
        }
    }

    /**
     * Charge la demande de livraisons
     *
     * @param demandeLivraisons
     */
    public void chargerFenetresLivraison(DemandeLivraisons demandeLivraisons) {
        this.demandeLivraisons = demandeLivraisons;

        TreeItem<Object> fenetreLivaisonRoot = new TreeItem<>(new FenetreLivraison(null, 0, 0));
        fenetreLivaisonRoot.setExpanded(true);
        setRoot(fenetreLivaisonRoot);
        setShowRoot(false);

        mettreAJour();
    }

    /**
     * Met à jour la TreeView avec les fenêtres de livraison et livraisons associées de l'attribut demandeLivraisons
     */
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

    /**
     * Permet de notifier au controlleur interne de la fenêtre de sélectionner la livraison sélectionnée dans la TreeView
     */
    private void clicElementListe() {
        if (ecouteurActive) {
            List<Livraison> livraisonsSelectionnees = new ArrayList<Livraison>();
            for (int i = 0; i < getSelectionModel().getSelectedItems().size(); i++) {
                try {
                    Object o = getSelectionModel().getSelectedItems().get(i).getValue();
                    if (o instanceof Livraison) {
                        livraisonsSelectionnees.add((Livraison) o);
                    }
                } catch (NullPointerException e) {
                    logger.error("Problème au clic : ", e);
                }
            }
            fenetreControleur.deselectionnerToutesIntersections();
            livraisonsSelectionnees.forEach(livraison -> fenetreControleur.selectionner(livraison.getIntersection()));
        }
    }

    /**
     * Permet d'étendre les fenêtres de livraison dans la TreeView afin d'afficher les livraisons associées aux fenêtres
     */
    private void etendre() {
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            getRoot().getChildren().get(i).setExpanded(true);
        }
    }

    /**
     * Sélectionne la livraison visuellement (surbrillance bleue) dans la TreeView
     *
     * @param livraison La livraison à sélectionner
     */
    public void selectionner(Livraison livraison) {
        etendre();
        int itemIndex = 0;
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            TreeItem<Object> objectTreeItem = getRoot().getChildren().get(i);
            Object o = objectTreeItem.getValue();
            if (o instanceof FenetreLivraison) {
                for (int j = 0; j < objectTreeItem.getChildren().size(); j++) {
                    Object o1 = objectTreeItem.getChildren().get(j).getValue();
                    if (o1 instanceof Livraison && livraison == o1) {
                        if (!getSelectionModel().isSelected(itemIndex + j + 1)) {
                            getSelectionModel().select(itemIndex + j + 1);
                        }
                        return;
                    }
                }
            }
            itemIndex += objectTreeItem.getChildren().size() + 1;
        }
    }

    /**
     * Déselectionne la livraison dans la TreeView
     *
     * @param livraison La livraison à déselectionner
     */
    public void deselectionner(Livraison livraison) {
        etendre();
        ArrayList<Integer> livrSelectionnees = new ArrayList<Integer>();
        // on récupère toutes les livraisons selectionnées sauf celle que l'on va enlever
        int itemIndex = 0;
        for (int i = 0; i < getRoot().getChildren().size(); i++) {
            TreeItem<Object> objectTreeItem = getRoot().getChildren().get(i);
            Object o = objectTreeItem.getValue();
            if (o instanceof FenetreLivraison) {
                for (int j = 0; j < objectTreeItem.getChildren().size(); j++) {
                    Object o1 = objectTreeItem.getChildren().get(j).getValue();
                    if (o1 instanceof Livraison && livraison != o1) {
                        if (getSelectionModel().isSelected(itemIndex + j + 1)) {
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

    /**
     * Déselectionne toute sélection courante dans la TreeView
     */
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
