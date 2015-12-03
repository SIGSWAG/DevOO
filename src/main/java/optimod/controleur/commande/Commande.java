package optimod.controleur.commande;

/**
 * Représente une Commande
 */
public interface Commande {

    /**
     * Execute la commande
     */
    void executer();

    /**
     * Execute la commande inverse
     */
    void annuler();
}
