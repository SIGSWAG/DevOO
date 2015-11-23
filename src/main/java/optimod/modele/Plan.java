package optimod.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import optimod.es.xml.DeserialiseurXML;
import optimod.es.xml.ExceptionXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Plan extends Observable {

    private List<Intersection> intersections;
    private ObservableList<Intersection> intersectionsObservables;

    /**
     * Default constructor
     */
    public Plan() {
        this.intersections = new ArrayList<Intersection>();
        this.intersectionsObservables = FXCollections.observableList(intersections);
    }

    public boolean chargerPlan() throws ParserConfigurationException, ExceptionXML, SAXException, IOException {
        boolean retour = DeserialiseurXML.INSTANCE.chargerPlan(this);
        setChanged();
        notifyObservers();
        return retour;
    }

    /**
     * @param adresse l'identifiant où trouver l'intersection
     * @return l'intersection correspondant à l'adresse
     */
    public Intersection trouverIntersection(int adresse) {
        Intersection intersectionTrouvee = null;
        for(Intersection inter: intersections) {
            if (inter.getAdresse() == adresse) {
                intersectionTrouvee = inter;
                break;
            }
        }
        return intersectionTrouvee;
    }

    /**
     * réalloue tous les attributs à des attributs vides
     */
    public void reset(){
        intersections = new ArrayList<Intersection>();
        setChanged();
        notifyObservers();
    }

    /**
     * contrat : trouve la 1ere intersection dans le cercle, même si il y en a plusieurs (attention au radius trop grand)
     * @param x la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y la coordonnée y du cercle dans lequel trouver l'intersection
     * @param rayon le rayon du cercle dans lequel trouver l'intersection
     * @return la 1ere intersection dans le cercle, même si il y en a plusieurs
     */
    public Intersection trouverIntersection(int x, int y, int rayon) {
        Intersection intersectionTrouvee = null;
        for(Intersection inter: intersections) {
            if ( inter.estLocalisee(x, y, rayon) ) {
                intersectionTrouvee = inter;
                break;
            }
        }
        return intersectionTrouvee;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public ObservableList<Intersection> getIntersectionsObservables() { return this.intersectionsObservables; }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
        this.intersectionsObservables = FXCollections.observableList(intersections);
    }
}