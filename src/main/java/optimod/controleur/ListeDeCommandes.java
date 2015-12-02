package optimod.controleur;

import java.util.LinkedList;

/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class ListeDeCommandes {

    private LinkedList<Commande> liste;
    private int indiceCourant;

    public ListeDeCommandes() {
        indiceCourant = -1;
        liste = new LinkedList<>();
    }

    /**
     * Ajout de la commande c a la liste this
     *
     * @param c
     */
    public void ajoute(Commande c) {
        int i = indiceCourant + 1;
        while (i < liste.size()) {
            liste.remove(i);
        }
        indiceCourant++;
        liste.add(indiceCourant, c);
        c.doCde();
    }

    /**
     * Annule temporairement la derniere commande ajoutee (cette commande pourra etre remise dans la liste avec redo)
     */
    public void undo() {
        if (onPeutAnnuler()) {
            Commande cde = liste.get(indiceCourant);
            indiceCourant--;
            cde.undoCde();
        }
    }

    /**
     * Remet dans la liste la derniere commande annulee avec undo
     */
    public void redo() {
        if (onPeutRejouer()) {
            indiceCourant++;
            Commande cde = liste.get(indiceCourant);
            cde.doCde();
        }
    }

    /**
     * Supprime definitivement toutes les commandes de liste
     */
    public void reset() {
        indiceCourant = -1;
        liste.clear();
    }

    /**
     * @return true si on peut annuler une commande, false sinon
     */
    public boolean onPeutAnnuler() {
        return indiceCourant >= 0;
    }

    /**
     * @return true si on peut rejouer une commande, false sinon
     */
    public boolean onPeutRejouer() {
        return indiceCourant < liste.size() - 1;
    }
}
