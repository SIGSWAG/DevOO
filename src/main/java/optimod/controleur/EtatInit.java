package optimod.controleur;

import java.awt.*;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import optimod.modele.Ordonnanceur;
import org.xml.sax.SAXException;

import optimod.modele.Plan;
import optimod.vue.FenetreControleur;
import optimod.es.xml.DeserialiseurXML;
import optimod.es.xml.ExceptionXML;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatInit extends EtatDefaut {
    // Etat initial

    @Override
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        if(ordonnanceur.chargerPlan()){
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
            fenetreControleur.activerChargerLivraisons(true);
            fenetreControleur.activerChargerPlan(true);
        }else{
            fenetreControleur.activerChargerPlan(true);
        }
    }

    @Override
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        if(ordonnanceur.chargerDemandeLivraison()){
            Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr); // TODO à changer
            fenetreControleur.act;
        }else{
            fenetreControleur.activerChargerPlan(true);
        }
    }

    @Override
    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        super.calculerItineraire(fenetreControleur);
    }

    @Override
    public void undo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes) {
        super.undo(fenetreControleur, listeDeCdes);
    }

    @Override
    public void redo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes) {
        super.redo(fenetreControleur, listeDeCdes);
    }

    @Override
    public void ajouterLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        super.ajouterLivraison(fenetreControleur);
    }

    @Override
    public void echangerLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        super.echangerLivraisons(fenetreControleur);
    }

    @Override
    public void supprimerLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        super.supprimerLivraison(fenetreControleur);
    }

    @Override
    public void carSaisi(Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, int codeCar) {
        super.carSaisi(listeDeCdes, codeCar);
    }

    @Override
    public void clicGauche(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, Point p) {
        super.clicGauche(fenetreControleur, listeDeCdes, p);
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur){
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
