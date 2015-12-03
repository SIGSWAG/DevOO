package optimod.controleur;

import optimod.controleur.etat.*;
import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Controleur général de l'application permettant de faire le lien entre la vue et le modèle
 */
public class Controleur {

    // Instances associees a chaque etat possible du controleur
    protected static final EtatInit etatInit = new EtatInit();
    protected static final EtatAttenteDemandeLivraison etatAttenteDemandeLivr = new EtatAttenteDemandeLivraison();
    protected static final EtatVisualisationDemandeLivraisons etatVisualisationDemandesLivr = new EtatVisualisationDemandeLivraisons();
    protected static final EtatPrincipal etatPrincipal = new EtatPrincipal();
    protected static final EtatUneLivraisonSelectionnee etatUneLivrSelectionnee = new EtatUneLivraisonSelectionnee();
    protected static final EtatDeuxLivraisonsSelectionnees etatDeuxLivrSelectionnees = new EtatDeuxLivraisonsSelectionnees();
    protected static final EtatPlusDeDeuxLivraisonsSelectionnees etatPlusDeDeuxLivrSelectionnees = new EtatPlusDeDeuxLivraisonsSelectionnees();
    protected static final EtatAjoutInit etatAjoutInit = new EtatAjoutInit();
    protected static final EtatAjoutLivraisonValidable etatAjoutLivraisonValidable = new EtatAjoutLivraisonValidable();
    private static Etat etatCourant;

    private Ordonnanceur ordonnanceur;
    private FenetreControleur fenetreControleur;
    private ListeDeCommandes listeDeCdes;
    private List<Intersection> intersectionsSelectionnees = new ArrayList<Intersection>();


    /**
     * Constructeur de Controleur
     *
     * @param ordonnanceur l'Ordonnanceur qui pilote le Controleur
     */
    public Controleur(Ordonnanceur ordonnanceur) {
        this.ordonnanceur = ordonnanceur;
        etatCourant = etatInit;
        listeDeCdes = new ListeDeCommandes();
    }

    /**
     * Change l'etat courant du controleur
     *
     * @param etat le nouvel etat courant
     */
    public static void setEtatCourant(Etat etat) {
        etatCourant = etat;
    }

    /**
     * Demande à la vue de se mettre à jour
     */
    public void updateVue() {
        fenetreControleur.autoriseBoutons(false);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de charger un plan de ville.
     */
    public void chargerPlan() {
        etatCourant.chargerPlan(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de charger une demande de livraison.
     */
    public void chargerDemandeLivraisons() {
        etatCourant.chargerDemandeLivraisons(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de calculer un itinéraire
     */
    public void calculerItineraire() {
        etatCourant.calculerItineraire(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet d'annuler la dernière commande
     */
    public void undo() {
        etatCourant.annulerDerniereAction(fenetreControleur, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de rejouer la dernière commande
     */
    public void redo() {
        etatCourant.rejouerDerniereAction(fenetreControleur, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet d'ajouter une livraison à l'itinéraire
     */
    public void ajouterLivraison() {
        etatCourant.ajouterLivraison(fenetreControleur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de valider l'ajout d'une livraison
     */
    public void validerAjoutLivraison() {
        etatCourant.validerAjout(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet d'annuler l'ajout d'une livraison
     */
    public void annulerAjoutLivraison() {
        etatCourant.annulerAjout(fenetreControleur, intersectionsSelectionnees);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet d'échanger deux livraisons
     */
    public void echangerLivraisons() {
        etatCourant.echangerLivraisonsSelectionnees(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de supprimer un ensemble de livraisons
     */
    public void supprimerLivraison() {
        etatCourant.supprimerLivraisonsSelectionnees(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de générer une feuille de route
     */
    public void genererFeuilleDeRoute() {
        etatCourant.genererFeuilleDeRoute(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de sélectionner une intersection
     *
     * @param intersection
     * @return
     */
    public boolean selectionnerIntersection(Intersection intersection) {
        boolean res = etatCourant.selectionnerIntersection(fenetreControleur, ordonnanceur, intersection, intersectionsSelectionnees);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
        return res;
    }

    /**
     * Permet de désélectionner une intersection
     *
     * @param intersection l'Intersection à désélectionner
     * @return
     */
    public boolean deselectionnerIntersection(Intersection intersection) {
        boolean res = etatCourant.deselectionnerIntersection(fenetreControleur, ordonnanceur, intersection, intersectionsSelectionnees);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
        return res;
    }

    /**
     * Permet de désélectionner toutes les livraisons
     */
    public void deselectionnerToutesIntersections() {
        etatCourant.deselectionnerToutesIntersections(fenetreControleur, ordonnanceur, intersectionsSelectionnees);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void setFenetreControleur(FenetreControleur fenetreControleur) {
        this.fenetreControleur = fenetreControleur;
    }

    public static EtatInit getEtatInit() {
        return etatInit;
    }

    public static EtatAttenteDemandeLivraison getEtatAttenteDemandeLivr() {
        return etatAttenteDemandeLivr;
    }

    public static EtatVisualisationDemandeLivraisons getEtatVisualisationDemandesLivr() {
        return etatVisualisationDemandesLivr;
    }

    public static EtatPrincipal getEtatPrincipal() {
        return etatPrincipal;
    }

    public static EtatUneLivraisonSelectionnee getEtatUneLivrSelectionnee() {
        return etatUneLivrSelectionnee;
    }

    public static EtatDeuxLivraisonsSelectionnees getEtatDeuxLivrSelectionnees() {
        return etatDeuxLivrSelectionnees;
    }

    public static EtatPlusDeDeuxLivraisonsSelectionnees getEtatPlusDeDeuxLivrSelectionnees() {
        return etatPlusDeDeuxLivrSelectionnees;
    }

    public static EtatAjoutInit getEtatAjoutInit() {
        return etatAjoutInit;
    }

    public static EtatAjoutLivraisonValidable getEtatAjoutLivraisonValidable() {
        return etatAjoutLivraisonValidable;
    }

    public static Etat getEtatCourant() {
        return etatCourant;
    }
}
