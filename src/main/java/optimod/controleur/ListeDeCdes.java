package optimod.controleur;

import java.util.LinkedList;
/**
 * Created by (PRO) Loïc Touzard on 23/11/2015.
 */
public class ListeDeCdes {
    private LinkedList<Commande> liste;
    private int indiceCrt;

    public ListeDeCdes(){
        indiceCrt = -1;
        liste = new LinkedList<Commande>();
    }

    /**
     * Ajout de la commande c a la liste this
     * @param c
     */
    public void ajoute(Commande c){
        for (int i=indiceCrt+1; i<liste.size(); i++)
            liste.remove(i);
        indiceCrt++;
        liste.add(indiceCrt, c);
        c.doCde();
    }

    /**
     * Annule temporairement la derniere commande ajoutee (cette commande pourra etre remise dans la liste avec redo)
     */
    public void undo(){
        if (onPeutAnnuler()){
            Commande cde = liste.get(indiceCrt);
            indiceCrt--;
            cde.undoCde();
        }
    }

    /**
     * Remet dans la liste la derniere commande annulee avec undo
     */
    public void redo(){
        if (onPeutRejouer()){
            indiceCrt++;
            Commande cde = liste.get(indiceCrt);
            cde.doCde();
        }
    }

    /**
     * Supprime definitivement toutes les commandes de liste
     */
    public void reset(){
        while (indiceCrt >=0){
            liste.remove(indiceCrt);
            indiceCrt--;
        }
    }

    /**
     * @return true si on peut annuler une commande, false sinon
     */
    public boolean onPeutAnnuler(){
        return indiceCrt >= 0;
    }

    /**
     * @return true si on peut rejouer une commande, false sinon
     */
    public boolean onPeutRejouer(){
        return indiceCrt < liste.size()-1;
    }
}
