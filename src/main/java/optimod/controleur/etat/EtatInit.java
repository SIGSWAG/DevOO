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
 * Etat initial
 */
public class EtatInit extends EtatDefaut {

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
    public void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes) {
        fenetreControleur.activerChargerPlan(true);
    }

}
