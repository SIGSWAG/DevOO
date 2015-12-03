package optimod.controleur.commande;

/**
 *
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
