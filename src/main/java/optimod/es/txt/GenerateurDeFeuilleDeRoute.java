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
     * @param tempsArret le temps d'attente à une livraison
     * @return true si le fichier à bien été mis à jour, false sinon.
     */
    public boolean genererFeuilleDeRoute(Livraison entrepot, int heureDebutItineraire, List<Chemin> itineraire, int tempsArret) throws IOException {
        File fichier = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions)
                .setMode(OuvreurDeFichier.MODE_ECRITURE)
                .setTitre("Sélectionner le fichier où sauvegarder la feuille de route")
                .ouvre(fenetre);
        if(fichier == null){
            return false;
        }
        PrintStream fluxFichier = new PrintStream(fichier);
        
        int etape = 1;
        int distanceTotale = 0;
        int dureeChemin = 0;
        int heureDerniereLivraison = heureDebutItineraire;
        fluxFichier.println("------- FEUILLE DE ROUTE ------");
        fluxFichier.println(horodateur(etape++, heureDebutItineraire)+" Départ de l'Entrepôt : ("+entrepot.getIntersection().getAdresse()+")");
        for(Chemin chemin : itineraire) {
            dureeChemin = 0;
            Troncon rueCourante=chemin.getTroncons().get(0);

            double distanceRue=0;
            int dureeRue = 0;
            for(Troncon troncon : chemin.getTroncons()){
                if(troncon.getNom().equals(rueCourante.getNom() )){
                    distanceRue += troncon.getLongueur();
                    dureeRue += troncon.getDuree();
                }else{
                    fluxFichier.println(horodateur(etape++, heureDerniereLivraison+dureeChemin)+" Prendre la rue "+rueCourante.getNom()+" et continuer sur "+  (int)distanceRue+"m");
                    distanceTotale += distanceRue;
                    dureeChemin += dureeRue;
                    dureeRue = troncon.getDuree();
                    distanceRue = troncon.getLongueur();
                    rueCourante=troncon;
                }
            }

            fluxFichier.println(horodateur(etape++, heureDerniereLivraison+dureeChemin)+" Prendre la rue "+rueCourante.getNom()+" et continuer sur "+  (int)distanceRue+"m");
            distanceTotale += distanceRue;
            dureeChemin += dureeRue;
            if(entrepot == chemin.getArrivee()){
                fluxFichier.println(horodateur(etape++, chemin.getArrivee().getHeureLivraison())+" -- Retour à l'Entrepôt en "+chemin.getArrivee().getIntersection().getAdresse()+" --");
                heureDerniereLivraison = chemin.getArrivee().getHeureLivraison()+tempsArret;
            }
            else{
                int attente = chemin.getArrivee().getHeureLivraison() - (heureDerniereLivraison+dureeChemin);
                if(attente > 0){
                    fluxFichier.println(horodateur(etape++, heureDerniereLivraison+dureeChemin)+" -- Patienter en "+chemin.getArrivee().getIntersection().getAdresse()+" pendant "+
                            tempsEnHeures(attente) + "h" +
                            (tempsEnMinutes(attente)<10?"0":"")+tempsEnMinutes(attente)+ "m --");
                }
                fluxFichier.println(horodateur(etape++, chemin.getArrivee().getHeureLivraison())+" -- Effectuer la livraison en "+chemin.getArrivee().getIntersection().getAdresse()+" --");
                if(chemin.getArrivee().estEnRetard()){
                    int retard = chemin.getArrivee().getHeureLivraison() - chemin.getArrivee().getHeureFinFenetre();
                    fluxFichier.println("!! Cette livraison a un retard de "+
                            tempsEnHeures(retard) + "h" +
                            (tempsEnMinutes(retard)<10?"0":"")+tempsEnMinutes(retard)+ "m !!");
                }
                fluxFichier.println();
                heureDerniereLivraison = chemin.getArrivee().getHeureLivraison()+tempsArret;
            }
        }

        int dureeTotale = heureDerniereLivraison-heureDebutItineraire;
        fluxFichier.println();
        fluxFichier.println("La demande de livraison a duré "+
            tempsEnHeures(dureeTotale) + "h" +
            (tempsEnMinutes(dureeTotale)<10?"0":"")+tempsEnMinutes(dureeTotale)+ "m"+
            " pour une distance totale de "+ distanceTotale/1000 +"."+distanceTotale%1000+"km");

        fluxFichier.close();
        return true;
    }

    public void setFenetre(Stage fenetre) {
        this.fenetre = fenetre;
    }

    private int tempsEnHeures(int temps) {
        return temps / 3600;
    }

    private int tempsEnMinutes(int temps){
        return (temps % 3600) / 60;
    }

    private int tempsEnSecondes(int temps){
        return (temps % 3600) % 60;
    }
    private String horodateur(int etape, int temps){
        return (etape<10?" ":"")+etape+". ["+tempsEnHeures(temps) + "h" + (tempsEnMinutes(temps)<10?"0":"")+tempsEnMinutes(temps)+ "m]";
    }
}
