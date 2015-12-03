package optimod.controleur;

import optimod.controleur.commande.Commande;

import java.util.LinkedList;

/**
 *
 */
public class ListeDeCommandes {

    private LinkedList<Commande> liste;
    private int indiceCourant;

    public ListeDeCommandes() {
        indiceCourant = -1;
        liste = new LinkedList<>();
    }

    /**
     * Ajoute la commande c a la liste this et l'execute
     *
     * @param c La Commande à ajouter et executer
     */
    public void ajoute(Commande c) {
        int i = indiceCourant + 1;
        while (i < liste.size()) {
            liste.remove(i);
        }
        indiceCourant++;
        liste.add(indiceCourant, c);
        c.executer();
    }

    /**
     * Annule temporairement la derniere commande ajoutee (cette commande pourra etre remise dans la liste avec rejouerDerniereAction)
     */
    public void undo() {
        if (onPeutAnnuler()) {
            Commande cde = liste.get(indiceCourant);
            indiceCourant--;
            cde.annuler();
        }
    }

    /**
     * Remet dans la liste la derniere commande annulee avec annulerDerniereAction
     */
    public void redo() {
        if (onPeutRejouer()) {
            indiceCourant++;
            Commande cde = liste.get(indiceCourant);
            cde.executer();
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
