package optimod.controleur.etat;

import javafx.scene.control.Alert;
import optimod.controleur.Controleur;
import optimod.controleur.ListeDeCommandes;
import optimod.es.xml.ExceptionXML;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Etat associé à la visualisation de la demande de livraisons
 */
public class EtatVisualisationDemandeLivraisons extends EtatDefaut {

    @Override
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        try {
            if (ordonnanceur.chargerPlan()) {
                Controleur.setEtatCourant(Controleur.getEtatAttenteDemandeLivr());
            }
        } catch (SAXException | ParserConfigurationException | IOException | ExceptionXML e) {
            fenetreControleur.afficherException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        try {
            if (ordonnanceur.chargerDemandeLivraison()) {
                Controleur.setEtatCourant(Controleur.getEtatVisualisationDemandesLivr());
            }
        } catch (SAXException | ParserConfigurationException | IOException | ExceptionXML e) {
            fenetreControleur.afficherException("Erreur lors du chargement XML.", "Erreur XML", Alert.AlertType.ERROR, e);
        }
    }

    @Override
    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur) {
        fenetreControleur.autoriseBoutons(false);
        ordonnanceur.calculerItineraire();
        Controleur.setEtatCourant(Controleur.getEtatPrincipal());
    }

    @Override
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.activerChargerLivraisons(true);
        fenetreControleur.activerChargerPlan(true);
        fenetreControleur.activerCalculerItineraire(true);
        listeDeCdes.reset();
    }
}
