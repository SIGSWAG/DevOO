package optimod.controleur;

import javafx.scene.control.Alert;
import optimod.es.xml.ExceptionXML;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by hdelval on 11/23/15.
 */
public class EtatAttenteDemandeLivr extends EtatDefaut {
    @Override
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        try {
            if(ordonnanceur.chargerPlan())
                Controleur.setEtatCourant(Controleur.etatAttenteDemandeLivr);
        } catch (SAXException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        } catch (ParserConfigurationException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        } catch (ExceptionXML exceptionXML) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, exceptionXML);
        } catch (IOException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        try {
            if(ordonnanceur.chargerDemandeLivraison())
                Controleur.setEtatCourant(Controleur.etatVisualisationDemandesLivr);
        } catch (SAXException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        } catch (ParserConfigurationException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        } catch (ExceptionXML exceptionXML) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, exceptionXML);
        } catch (IOException e) {
            fenetreControleur.afficheException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void updateVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes){
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
    }
}
