package optimod.controleur;

import javafx.scene.control.Alert;
import optimod.es.xml.ExceptionXML;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatPrincipal extends EtatDefaut {
    @Override
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        try {
            if(ordonnanceur.chargerPlan())
                Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
        } catch (SAXException | ParserConfigurationException | ExceptionXML | IOException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        try {
            if(ordonnanceur.chargerDemandeLivraison())
                Controleur.setEtatCourant(Controleur.etatVisualisationDemandesLivr);
        } catch (SAXException | ParserConfigurationException | ExceptionXML | IOException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void ajouterLivraison(FenetreControleur fenetreControleur){
        fenetreControleur.autoriseBoutons(false);
        fenetreControleur.afficheMessage("Vous passez en mode ajout de livraison.\n\n" +
                "Pour ajouter une livraison, veuillez sélectionner une livraison avant laquelle votre livraison sera créée.\n" +
                "Veuillez ensuite sélectionner une intersection (il ne doit pas y avoir de livraison sur l'intersection sélectionnée).\n" +
                "Finalement, veuillez valider l'ajout grâce au bouton associé dans la barre de menu.\n\n" +
                "NB: Vous pouvez sortir du mode d'ajout à tout moment grâce au bouton associé dans la barre de menu.",
                "Ajout d'une livraison : Mode d'emploi",
                Alert.AlertType.INFORMATION
        );
        Controleur.setEtatCourant(Controleur.etatAjoutInit);
    }

    @Override
    public void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur){
        fenetreControleur.autoriseBoutons(false);
        try {
            ordonnanceur.genererFeuilleDeRoute();
        } catch (IOException e) {
            fenetreControleur.afficheException("Erreur lors de l'écriture de la feuille de route.", "Erreur E/S", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void undo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.autoriseBoutons(false);
        listeDeCdes.undo();
    }

    @Override
    public void redo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes) {
        fenetreControleur.autoriseBoutons(false);
        listeDeCdes.redo();
    }

    @Override
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersectionSelectionnee, List<Intersection> intersectionsSelectionnees) {
        fenetreControleur.autoriseBoutons(false);
        Livraison livraisonSelectionnee = intersectionSelectionnee.getLivraison();
        if(livraisonSelectionnee != null && livraisonSelectionnee != ordonnanceur.getDemandeLivraisons().getEntrepot()){
            intersectionsSelectionnees.add(intersectionSelectionnee);
            Controleur.setEtatCourant(Controleur.etatUneLivrSelectionnee);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
        fenetreControleur.activerAjouter(true);
        if(listeDeCdes.onPeutAnnuler()){
            fenetreControleur.activerAnnuler(true);
        }
        if(listeDeCdes.onPeutRejouer()){
            fenetreControleur.activerRejouer(true);
        }
        fenetreControleur.activerGenererFeuilleRoute(true);
        fenetreControleur.activerSelections(true);
    }
}
