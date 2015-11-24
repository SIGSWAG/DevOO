package optimod.modele;

import optimod.es.xml.ExceptionXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Ordonnanceur {

    private DemandeLivraison demandeLivraison;

    private Plan plan;

    /**
     * Default constructor
     */
    public Ordonnanceur() {
        plan = new Plan();
        demandeLivraison = new DemandeLivraison(plan);
    }

    /**
     *
     */
    public boolean chargerPlan() throws SAXException, ParserConfigurationException, ExceptionXML, IOException {
        return plan.chargerPlan();
    }

    /**
     * @param adresse l'identifiant de l'adresse à trouver
     * @return l'intersection correspondant à l'adresse
     */
    public Intersection trouverIntersection(int adresse) {
        return plan.trouverIntersection(adresse);
    }

    /**
     * contrat : trouve la 1ere intersection dans le cercle, même si il y en a plusieurs (attention au radius trop grand)
     * @param x la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y la coordonnée y du cercle dans lequel trouver l'intersection
     * @param rayon le rayon du cercle dans lequel trouver l'intersection
     * @return la 1ere intersection dans le cercle, même si il y en a plusieurs
     */
    public Intersection trouverIntersection(int x, int y, int rayon) {
        return plan.trouverIntersection(x,y,rayon);
    }

    /**
     *
     */
    public boolean chargerDemandeLivraison() throws SAXException, ParserConfigurationException, ExceptionXML, IOException {
        return demandeLivraison.chargerDemandeLivraison();
    }

    /**
     * 
     */
    public void calculerItineraire() {
        demandeLivraison.calculerItineraire();
    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     * @param intersection l'intersection sur laquelle on ajoute la livraison
     * @param livr la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Intersection intersection, Livraison livr) {
        demandeLivraison.ajouterLivraison(intersection, livr);
    }

    /**
     * supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     * @param livr la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        demandeLivraison.supprimerLivraison(livr);
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        demandeLivraison.echangerLivraison(livr1, livr2);
    }

    public DemandeLivraison getDemandeLivraison() {
        return demandeLivraison;
    }

    public void setDemandeLivraison(DemandeLivraison demandeLivraison) {
        this.demandeLivraison = demandeLivraison;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void genererFeuilleDeRoute(){
        demandeLivraison.genererFeuilleDeRoute();
    }
}