package optimod.controleur;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class CdeInverse implements Commande{
    private Commande cde;

    /**
     * Cree la commande inverse a la commande cde (de sorte que cde.doCde corresponde a this.undoCde, et vice-versa)
     * @param cde
     */
    public CdeInverse(Commande cde){
        this.cde = cde;
    }

    public void doCde() {
        cde.undoCde();
    }

    public void undoCde() {
        cde.doCde();
    }
}
