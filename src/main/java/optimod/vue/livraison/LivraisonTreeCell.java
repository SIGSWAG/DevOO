package optimod.vue.livraison;

import javafx.scene.control.TreeCell;
import javafx.scene.paint.Color;
import optimod.modele.FenetreLivraison;
import optimod.modele.Livraison;
import optimod.vue.plan.IntersectionPane;

/**
 * Created by Jonathan on 24/11/2015.
 */
class LivraisonTreeCell extends TreeCell<Object> {

    private static final String FORMAT_HEURE = "%02d:%02d:%02d";

    private IntersectionPane intersectionPane;

    private AfficheurFenetresLivraison afficheurFenetresLivraison;

    public LivraisonTreeCell(AfficheurFenetresLivraison afficheurFenetresLivraison) {
        super();
        this.afficheurFenetresLivraison = afficheurFenetresLivraison;
    }

    @Override
    public void updateItem(Object element, boolean vide)
    {
        super.updateItem(element, vide);

        if(vide) {
            setText(null);
        }
        else {
            if(element instanceof FenetreLivraison) {
                FenetreLivraison fenetreLivraison = (FenetreLivraison) element;
                String heureDebut = String.format(FORMAT_HEURE,
                        fenetreLivraison.getHeureDebutHeure(), fenetreLivraison.getHeureDebutMinute(), fenetreLivraison.getHeureDebutSeconde());
                String heureFin = String.format(FORMAT_HEURE,
                        fenetreLivraison.getHeureFinHeure(), fenetreLivraison.getHeureFinMinute(), fenetreLivraison.getHeureFinSeconde());
                setText(heureDebut + " - " + heureFin);
                setTextFill(afficheurFenetresLivraison.getCouleur(fenetreLivraison));
            }
            else if(element instanceof Livraison) {


                Livraison livraison = (Livraison) element;
                String retard="";
                String heure = "";
                if(livraison.estEnRetard()){
                   retard="\nLivraison en retard";
                }
                if(livraison.initeraireCalcule()){
                    String minute="";
                    if(livraison.getMinute()<10){
                        minute="0";
                    }
                    minute+=livraison.getMinute();

                    heure+=livraison.getHeure()+":"+minute;
                }
                setText("Client " +  livraison.getIdClient() + " au " + livraison.getIntersection().getAdresse()+" "+heure+retard);
                setTextFill(Color.BLACK);

            }
        }
        setGraphic(null);
    }

}
