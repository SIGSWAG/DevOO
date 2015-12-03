package optimod.modele;

import optimod.es.xml.ExceptionXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Ordonnanceur {

    private DemandeLivraisons demandeLivraisons;

    private Plan plan;

    /**
     * Constructeur d'Ordonnanceur par défaut.
     */
    public Ordonnanceur() {
        plan = new Plan();
        demandeLivraisons = new DemandeLivraisons(plan);
    }

    /**
     * Permet de charger un plan. La méthode prend en charge la demande à l'utilisateur d'un fichier.
     *
     * @return true si le plan se charge correctement, false sinon.
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ExceptionXML
     * @throws IOException
     */
    public boolean chargerPlan() throws SAXException, ParserConfigurationException, ExceptionXML, IOException {
        return plan.chargerPlan();
    }

    /**
     * Permet de trouver une Intersection dans le plan grâce à son adresse.
     *
     * @param adresse l'identifiant de l'adresse à trouver
     * @return l'intersection correspondant à l'adresse
     */
    public Intersection trouverIntersection(int adresse) {
        return plan.trouverIntersection(adresse);
    }

    /**
     * contrat : trouve la 1ere intersection dans le cercle, même si il y en a plusieurs (attention au radius trop grand)
     *
     * @param x     la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y     la coordonnée y du cercle dans lequel trouver l'intersection
     * @param rayon le rayon du cercle dans lequel trouver l'intersection
     * @return la 1ere intersection dans le cercle, même si il y en a plusieurs
     */
    public Intersection trouverIntersection(int x, int y, int rayon) {
        return plan.trouverIntersection(x, y, rayon);
    }

    /**
     * Permet de charger une demande de Livraison. La méthode prend en charge la demande à l'utilisateur d'un fichier.
     *
     * @return true si la demande de livraison se charge correctement, false sinon.
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ExceptionXML
     * @throws IOException
     */
    public boolean chargerDemandeLivraison() throws SAXException, ParserConfigurationException, ExceptionXML, IOException {
        return demandeLivraisons.chargerDemandeLivraison();
    }

    /**
     * Permet de demander le calcul d'un itinéraire.
     * La fin du calcul est notifiée par un notify() dans les Observables concernés.
     */
    public void calculerItineraire() {
        demandeLivraisons.calculerItineraire();
    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     *
     * @param nouvelleLivraison la livraison à ajouter
     * @param livr              la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Livraison nouvelleLivraison, Livraison livr, FenetreLivraison fenetreLivraison) {
        demandeLivraisons.ajouterLivraison(nouvelleLivraison, livr, fenetreLivraison);
    }

    /**
     * supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     *
     * @param livr la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        demandeLivraisons.supprimerLivraison(livr);
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires
     *
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        demandeLivraisons.echangerLivraison(livr1, livr2);
    }


    public DemandeLivraisons getDemandeLivraisons() {
        return demandeLivraisons;
    }

    public void setDemandeLivraisons(DemandeLivraisons demandeLivraisons) {
        this.demandeLivraisons = demandeLivraisons;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * Permet de générer une feuille de route au format HTML.
     *
     * @throws IOException
     */
    public void genererFeuilleDeRoute() throws IOException {
        demandeLivraisons.genererFeuilleDeRoute();
    }

    /**
     * Permet de trouver une Fenetre de Livraison correspondant à une Livraison
     *
     * @param livraison la Livraison pour laquelle on veut trouver une fenêtre, false si la Livraison n'a
     *                  pas de fenêtre
     * @return La FenetreLivraison concernée.
     */
    public FenetreLivraison trouverFenetreDeLivraison(Livraison livraison) {
        return demandeLivraisons.trouverFenetreDeLivraison(livraison);
    }
}