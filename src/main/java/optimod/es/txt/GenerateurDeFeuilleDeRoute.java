package optimod.es.txt;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import optimod.es.xml.ExceptionXML;
import optimod.modele.*;
import optimod.vue.es.OuvreurDeFichier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * Created by Loïc Touzard on 18/11/2015.
 */
public enum GenerateurDeFeuilleDeRoute { // Singleton
    INSTANCE;
    private List<FileChooser.ExtensionFilter> extensions = new ArrayList<FileChooser.ExtensionFilter>();

    GenerateurDeFeuilleDeRoute(){
        extensions.add(new FileChooser.ExtensionFilter("Texte (*.txt)", "*.txt"));
        extensions.add(new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));
    }

    private Stage fenetre;

    /**
     * Génère la feuille de route dasn un fichier texte
     * @param entrepot L'entrepot de départ et d'arrivée de la demande de livraison
     * @param heureDebutItineraire l'heure de début de l'itinéraire, au départ de l'entrepot
     * @param itineraire la liste des chemins à suivre pour effectuer la demande de livraison
     * @return true si le fichier à bien été mis à jour, false sinon.
     */
    public boolean genererFeuilleDeRoute(Livraison entrepot, int heureDebutItineraire, List<Chemin> itineraire) throws IOException {
        File fichier = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions)
                .setMode(OuvreurDeFichier.MODE_ECRITURE)
                .setTitre("Sélectionner le fichier où sauvegarder la feuille de route")
                .ouvre(fenetre);
        if(fichier == null){
            return false;
        }

        PrintStream fichierFlux = new PrintStream(fichier);

        fichierFlux.println("-------Feuile de route------");
        fichierFlux.println("Depart entrepot : ("+entrepot.getIntersection().getAdresse()+")"+heureDebutItineraire );

        for(Chemin chemin : itineraire) {
            fichierFlux.println("Chemin de "+chemin.getDepart().getIntersection().getAdresse()+" à "+chemin.getArrivee().getIntersection()
                    .getAdresse());
            Troncon rueCourante=chemin.getTroncons().get(0);
            double distanceRue=0;

            for(Troncon troncon : chemin.getTroncons()){
                if(troncon.getNom().equals(rueCourante.getNom())){
                    distanceRue+=troncon.getLongueur();
                }else{
                   fichierFlux.println("Prendre rue "+rueCourante.getNom()+" sur "+distanceRue+"m");
                    distanceRue = troncon.getLongueur();
                    rueCourante=troncon;

                }
            }
            fichierFlux.println("Arrivee a la livraison "+chemin.getArrivee().getIntersection().getAdresse()+" a "+chemin.getArrivee().getHeure()+" : "+chemin.getArrivee().getMinute());
        }
        fichierFlux.close();
        return true;
    }

    public void setFenetre(Stage fenetre) {
        this.fenetre = fenetre;
    }
}
