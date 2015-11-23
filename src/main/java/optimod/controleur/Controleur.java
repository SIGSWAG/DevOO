package optimod.controleur;

import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

/**
 *
 * Created by Jonathan on 18/11/2015.
 */
public class Controleur {

    private Ordonnanceur ordonnanceur;
    private FenetreControleur fenetreControleur;
    private ListeDeCdes listeDeCdes;

    private static Etat etatCourant;
    // Instances associees a chaque etat possible du controleur
    protected static final EtatInit etatInit = new EtatInit();
    protected static final EtatAttenteDemandeLivr etatAttenteDemandeLivr = new EtatAttenteDemandeLivr();
//    protected static final EtatVisualisationDemandesLivr etatVisualisationDemandesLivr = new EtatVisualisationDemandesLivr();
//    protected static final EtatPrincipal etatPrincipal = new EtatPrincipal();
//    protected static final EtatLivrSelectionnee etatLivrSelectionnee = new EtatLivrSelectionnee();
//    protected static final EtatDeuxLivrSelectionnees etatDeuxLivrSelectionnees = new EtatDeuxLivrSelectionnees();
//    protected static final EtatPlusDeDeuxLivrSelectionnees etatPlusDeDeuxLivrSelectionnees = new EtatPlusDeDeuxLivrSelectionnees();
//    protected static final EtatAjoutLivr etatAjoutLivr = new EtatAjoutLivr();
//    protected static final EtatAjoutLivrValidable etatAjoutLivrValidable = new EtatAjoutLivrValidable();


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
        etatCourant.updateVue();
    }

    public void chargerDemandeLivraisons() {
        ordonnanceur.chargerDemandeLivraison();
    }

    public void calculerItineraire() {

    }

    public void undo() {

    }

    public void redo() {

    }

    public void ajouterLivraison() {

    }

    public void echangerLivraisons() {

    }

    public void supprimerLivraison() {

    }

    public void genererFeuilleDeRoute() {

    }

    public void setFenetreControleur(FenetreControleur fenetreControleur) {
        this.fenetreControleur = fenetreControleur;
    }
}
