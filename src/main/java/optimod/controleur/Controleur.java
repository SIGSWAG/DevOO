package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    // Instances associees a chaque etat possible du controleur
    protected static final EtatInit etatInit = new EtatInit();
    protected static final EtatAttenteDemandeLivraison etatAttenteDemandeLivr = new EtatAttenteDemandeLivraison();
    protected static final EtatVisualisationDemandesLivraison etatVisualisationDemandesLivr = new EtatVisualisationDemandesLivraison();
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
    protected static void setEtatCourant(Etat etat) {
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
        etatCourant.undo(fenetreControleur, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    /**
     * Permet de rejouer la dernière commande
     */
    public void redo() {
        etatCourant.redo(fenetreControleur, listeDeCdes);
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
        etatCourant.echangeesLivraisonsSelectionnees(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
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
}
