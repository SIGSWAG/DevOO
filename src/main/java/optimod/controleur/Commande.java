package optimod.controleur;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public interface Commande {

    /**
     * Execute la commande this
     */
    void doCde();

    /**
     * Execute la commande inverse a this
     */
    void undoCde();
}
