package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

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
    }

    /**
     * Change l'etat courant du controleur
     * @param etat le nouvel etat courant
     */
    protected static void setEtatCourant(Etat etat){
        etatCourant = etat;
    }

    public void chargerPlan() {
        etatCourant.chargerPlan(fenetreControleur, ordonnanceur);
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void chargerDemandeLivraisons() {
        etatCourant.chargerDemandeLivraisons(fenetreControleur, ordonnanceur);
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void calculerItineraire() {
        etatCourant.calculerItineraire(fenetreControleur, ordonnanceur);
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void undo() {
        etatCourant.undo(fenetreControleur, listeDeCdes);
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void redo() {
        etatCourant.redo(fenetreControleur, listeDeCdes);
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void ajouterLivraison() {

    }

    public void echangerLivraisons() {

    }

    public void supprimerLivraison() {

    }

    public void genererFeuilleDeRoute() {
        etatCourant.genererFeuilleDeRoute(fenetreControleur, ordonnanceur);
    }

    public void selectionnerIntersection(Point p, int rayon){
        etatCourant.selectionnerIntersection(
                fenetreControleur, ordonnanceur, p, rayon, intersectionsSelectionnees
        );
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void deselectionnerIntersection(Point p, int rayon){
        etatCourant.deselectionnerIntersection(
                fenetreControleur, ordonnanceur, p, rayon, intersectionsSelectionnees
        );
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void deselectionnerToutesIntersections(){
        etatCourant.deselectionnerToutesIntersections(
                fenetreControleur, ordonnanceur, intersectionsSelectionnees
        );
        etatCourant.updateVue(fenetreControleur, listeDeCdes);
    }

    public void setFenetreControleur(FenetreControleur fenetreControleur) {
        this.fenetreControleur = fenetreControleur;
    }
}
