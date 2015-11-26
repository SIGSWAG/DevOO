package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    private Ordonnanceur ordonnanceur;
    private FenetreControleur fenetreControleur;
    private ListeDeCdes listeDeCdes;
    private List<Intersection> intersectionsSelectionnees = new ArrayList<Intersection>();

    private static Etat etatCourant;
    // Instances associees a chaque etat possible du controleur
    protected static final EtatInit etatInit = new EtatInit();
    protected static final EtatAttenteDemandeLivr etatAttenteDemandeLivr = new EtatAttenteDemandeLivr();
    protected static final EtatVisualisationDemandesLivr etatVisualisationDemandesLivr = new EtatVisualisationDemandesLivr();
    protected static final EtatPrincipal etatPrincipal = new EtatPrincipal();
    protected static final EtatUneLivrSelectionnee etatUneLivrSelectionnee = new EtatUneLivrSelectionnee();
    protected static final EtatDeuxLivrSelectionnees etatDeuxLivrSelectionnees = new EtatDeuxLivrSelectionnees();
    protected static final EtatPlusDeDeuxLivrSelectionnees etatPlusDeDeuxLivrSelectionnees = new EtatPlusDeDeuxLivrSelectionnees();
    protected static final EtatAjoutInit etatAjoutInit = new EtatAjoutInit();
    protected static final EtatAjoutLivrValidable etatAjoutLivrValidable = new EtatAjoutLivrValidable();


    public Controleur(Ordonnanceur ordonnanceur) {
        this.ordonnanceur = ordonnanceur;
        etatCourant = etatInit;
        listeDeCdes = new ListeDeCdes();
    }

    public void updateVue() {
        fenetreControleur.autoriseBoutons(false);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void chargerPlan() {
        etatCourant.chargerPlan(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void chargerDemandeLivraisons() {
        etatCourant.chargerDemandeLivraisons(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void calculerItineraire() {
        etatCourant.calculerItineraire(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void undo() {
        etatCourant.undo(fenetreControleur, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void redo() {
        etatCourant.redo(fenetreControleur, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void ajouterLivraison() {
        etatCourant.ajouterLivraison(fenetreControleur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void validerAjoutLivraison(){
        etatCourant.validerAjout(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void annulerAjoutLivraison(){
        etatCourant.annulerAjout(fenetreControleur, intersectionsSelectionnees);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void echangerLivraisons() {
        etatCourant.echangeesLivraisonsSelectionnees(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void supprimerLivraison() {
        etatCourant.supprimerLivraisonsSelectionnees(fenetreControleur, ordonnanceur, intersectionsSelectionnees, listeDeCdes);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void genererFeuilleDeRoute() {
        etatCourant.genererFeuilleDeRoute(fenetreControleur, ordonnanceur);
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public boolean selectionnerIntersection(Intersection intersection){
        boolean res = false;
        if(intersection.getLivraison() != ordonnanceur.getDemandeLivraisons().getEntrepot())
            res = etatCourant.selectionnerIntersection(
                    fenetreControleur, ordonnanceur, intersection, intersectionsSelectionnees
            );
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
        return res;
    }

    public boolean deselectionnerIntersection(Intersection intersection){
        boolean res = false;
        if(intersection.getLivraison() != ordonnanceur.getDemandeLivraisons().getEntrepot())
            res = etatCourant.deselectionnerIntersection(
                    fenetreControleur, ordonnanceur, intersection, intersectionsSelectionnees
            );
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
        return res;
    }

    public void deselectionnerToutesIntersections(){
        etatCourant.deselectionnerToutesIntersections(
                fenetreControleur, ordonnanceur, intersectionsSelectionnees
        );
        etatCourant.mettreAJourVue(fenetreControleur, listeDeCdes);
    }

    public void setFenetreControleur(FenetreControleur fenetreControleur) {
        this.fenetreControleur = fenetreControleur;
    }

    /**
     * Change l'etat courant du controleur
     * @param etat le nouvel etat courant
     */
    protected static void setEtatCourant(Etat etat){
        etatCourant = etat;
    }
}
