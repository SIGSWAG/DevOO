package optimod.controleur.commande;

/**
 *
 */
public interface Commande {

    /**
     * Execute la commande
     */
    void executerCommande();

    /**
     * Execute la commande inverse
     */
    void annulerCommande();
}
