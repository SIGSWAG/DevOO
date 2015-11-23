package optimod.controleur;

import optimod.es.xml.DeserialiseurXML;
import optimod.es.xml.ExceptionXML;
import optimod.modele.Plan;
import optimod.vue.FenetreControleur;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.awt.*;
import java.io.IOException;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAttenteDemandeLivr extends EtatDefaut {
    @Override
    public void chargerPlan(FenetreControleur fenetreControleur) {
        fenetreControleur.autoriseBoutons(false);
        Controleur.setEtatCourant(Controleur.etatInit);
    }

    @Override
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur) {
        super.chargerDemandeLivraisons(fenetreControleur);
    }

    @Override
    public void calculerItineraire(FenetreControleur fenetreControleur) {
        super.calculerItineraire(fenetreControleur);
    }

    @Override
    public void undo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes) {
        super.undo(fenetreControleur, listeDeCdes);
    }

    @Override
    public void redo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes) {
        super.redo(fenetreControleur, listeDeCdes);
    }

    @Override
    public void ajouterLivraison(FenetreControleur fenetreControleur) {
        super.ajouterLivraison(fenetreControleur);
    }

    @Override
    public void echangerLivraisons(FenetreControleur fenetreControleur) {
        super.echangerLivraisons(fenetreControleur);
    }

    @Override
    public void supprimerLivraison(FenetreControleur fenetreControleur) {
        super.supprimerLivraison(fenetreControleur);
    }

    @Override
    public void carSaisi(ListeDeCdes listeDeCdes, int codeCar) {
        super.carSaisi(listeDeCdes, codeCar);
    }

    @Override
    public void clicGauche(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes, Point p) {
        super.clicGauche(fenetreControleur, listeDeCdes, p);
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur){
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
    }



    @Override
    public void ajouterRectangle(FenetreControleur fenetreControleur) {
        fenetreControleur.autoriseBoutons(false);
        fenetreControleur.afficheMessage("Ajout d'un rectangle : [Clic gauche] sur un angle du rectangle ; " +
                "[Clic droit] pour annuler");
        Controleur.setEtatCourant(Controleur.etatRectangle1);
    }

    @Override
    public void supprimer(FenetreControleur fenetreControleur){
        fenetreControleur.autoriseBoutons(false);
        fenetreControleur.afficheMessage("Suppression : [Clic gauche] sur la forme a supprimer ; " +
                "[Clic droit] pour sortir du mode suppression");
        Controleur.setEtatCourant(Controleur.etatSupprimer);
    }

    @Override
    public void deplacer(FenetreControleur fenetreControleur){
        fenetreControleur.autoriseBoutons(false);
        fenetreControleur.afficheMessage("Deplacer une forme : [Clic gauche] pour selectionner la forme a deplacer ; " +
                "[Clic droit] pour annuler");
        Controleur.setEtatCourant(Controleur.etatDeplacer1);
    }

    @Override
    public void diminuerEchelle(FenetreControleur fenetreControleur) {
        int echelle = fenetreControleur.getEchelle();
        if (echelle > 1){
            fenetreControleur.setEchelle(fenetreControleur.getEchelle()-1);		}
        else fenetreControleur.afficheMessage("L'echelle ne peut pas etre diminuee.");
    }

    @Override
    public void augmenterEchelle(FenetreControleur fenetreControleur) {
        fenetreControleur.setEchelle(fenetreControleur.getEchelle()+1);
    }

    @Override
    public void undo(ListeDeCdes listeDeCdes){
        listeDeCdes.undo();
    }

    @Override
    public void redo(ListeDeCdes listeDeCdes){
        listeDeCdes.redo();
    }

    @Override
    public void sauver(Plan plan, FenetreControleur fenetreControleur){
        try {
            SerialiseurXML.getInstance().sauver(plan);
        } catch (ParserConfigurationException
                | TransformerFactoryConfigurationError
                | TransformerException | ExceptionXML e) {
            fenetreControleur.afficheMessage(e.getMessage());
        }
    }


    @Override
    public void ouvrir(Plan plan, ListeDeCdes listeDeCdes, FenetreControleur fenetreControleur){
        int largeur = plan.getLargeur();
        int hauteur = plan.getHauteur();
        try {
            DeserialiseurXML.charger(plan);
        } catch (ParserConfigurationException
                | SAXException | IOException
                | ExceptionXML | NumberFormatException e) {
            fenetreControleur.afficheMessage(e.getMessage());
            plan.reset(largeur, hauteur);
        }
        listeDeCdes.reset();
    }

}
