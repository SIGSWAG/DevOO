package optimod.vue.livraison;

import javafx.scene.control.TreeCell;
import javafx.scene.paint.Color;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;

/**
 * Cellule customisée de AfficheurFenetresLivraison qui permet d'afficher des informations sur une fenêtre de livraison
 * ou sur une livraison
 */
class LivraisonTreeCell extends TreeCell {

    private static final String FORMAT_HEURE = "%02d:%02d:%02d";

    private AfficheurFenetresLivraison afficheurFenetresLivraison;

    public LivraisonTreeCell(AfficheurFenetresLivraison afficheurFenetresLivraison) {
        this.afficheurFenetresLivraison = afficheurFenetresLivraison;
    }

    /**
     * Met à jour la cellule avec l'élément passé en paramètre
     *
     * @param element Un objet de type FenetreLivraison ou Livraison
     * @param vide
     */
    @Override
    public void updateItem(Object element, boolean vide) {
        super.updateItem(element, vide);

        setGraphic(null);
        setText(null);

        if (element != null && !vide) {

            if (element instanceof FenetreLivraison) {

                FenetreLivraison fenetreLivraison = (FenetreLivraison) element;
                String heureDebut = String.format(FORMAT_HEURE, fenetreLivraison.getHeureDebutHeure(), fenetreLivraison.getHeureDebutMinute(), fenetreLivraison.getHeureDebutSeconde());
                String heureFin = String.format(FORMAT_HEURE, fenetreLivraison.getHeureFinHeure(), fenetreLivraison.getHeureFinMinute(), fenetreLivraison.getHeureFinSeconde());
                setText(heureDebut + " - " + heureFin);
                setTextFill(afficheurFenetresLivraison.getFenetreControleur().associerCouleur(fenetreLivraison));

            } else if (element instanceof Livraison) {

                Livraison livraison = (Livraison) element;
                String retard = "";
                String heure = "";
                if (livraison.estEnRetard()) {
                    retard = "\nLivraison en retard";
                }
                if (livraison.initeraireCalcule()) {
                    String minute = "";
                    if (livraison.getMinute() < 10) {
                        minute = "0";
                    }
                    minute += livraison.getMinute();

                    heure += " à " + livraison.getHeure() + "h" + minute;
                }
                String client = "";
                if (livraison.getIdClient() == -1) {
                    client += "Nouveau client";
                } else {
                    client += livraison.getIdClient();
                }

                setText("Client " + client + "\nAdresse " + livraison.getIntersection().getAdresse() + heure + retard);
                setTextFill(Color.BLACK);
            }
        }

    }

}
